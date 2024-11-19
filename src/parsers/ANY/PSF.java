/*
This file is part of jpcsp.

Jpcsp is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Jpcsp is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Jpcsp.  If not, see <http://www.gnu.org/licenses/>.
 */
package parsers.ANY;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import parsers.BasicFile;

public class PSF {

    public static final Charset charset = Charset.forName("UTF-8");
    private int psfOffset;
    private int size;

    private boolean sizeDirty;
    private boolean tablesDirty;

    private int ident;
    private int version; // yapspd: 0x1100. actual: 0x0101.
    private int keyTableOffset;
    private int valueTableOffset;
    private int indexEntryCount;

    private LinkedList pairList;//PSFKeyValuePair

    public final static int PSF_IDENT = 0x46535000;

    public final static int PSF_DATA_TYPE_BINARY = 0;
    public final static int PSF_DATA_TYPE_STRING = 2;
    public final static int PSF_DATA_TYPE_INT32 = 4;

    public PSF(int psfOffset)
    {
        this.psfOffset = psfOffset;
        size = 0;

        sizeDirty = true;
        tablesDirty = true;

        ident = PSF_IDENT;
        version = 0x0101;

        pairList = new LinkedList();

    }

    public PSF()
    {
        this(0);
    }

    public static String readStringZ(BasicFile buf) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        byte b;
        for (; buf.seek() < buf.length();) {
            b = buf.readS();
            if (b == 0) {
                break;
            }
            sb.append((char) b);
        }
        return sb.toString();
    }

    /**
     * f.position() is undefined after calling this
     *
     * @param f
     * @throws java.io.IOException
     */
    public void read(BasicFile f) throws IOException
    {
        psfOffset = 0;
        size = 0;
        sizeDirty = true;
        tablesDirty = true;
        ident = PSF_IDENT;
        version = 0x0101;
        pairList.clear();

        psfOffset = (int) f.seek();

        ident = f.read4le();//uword
        if (ident != PSF_IDENT) {
            System.out.println("Not a valid PSF file (ident=" + String.format("%08X", ident) + ")");
            return;
        }

        // header
        version = f.read4le(); //uword 0x0101
        keyTableOffset = f.read4le(); //uword
        valueTableOffset = f.read4le();//uword
        indexEntryCount = f.read4le();//uword

        // index table
        for (int i = 0; i < indexEntryCount; i++) {
            PSFKeyValuePair pair = new PSFKeyValuePair();
            pair.read(f);
            pairList.add(pair);
        }

        // key/pairs
        for (Iterator it = pairList.iterator(); it.hasNext();) {
            PSFKeyValuePair pair = (PSFKeyValuePair) it.next();
            f.seek(psfOffset + keyTableOffset + pair.keyOffset);
            pair.key = readStringZ(f);
            f.seek(psfOffset + valueTableOffset + pair.valueOffset);
            switch (pair.dataType) {
                case PSF_DATA_TYPE_BINARY:
                    byte[] data = new byte[pair.dataSize];
                    f.fully(data);
                    pair.data = data;

                    //System.out.println(String.format("offset=%08X key='%s' binary packed [len=%d]",
                    //    keyTableOffset + pair.keyOffset, pair.key, pair.dataSize));
                    break;

                case PSF_DATA_TYPE_STRING:
                    // String may not be in english!
                    byte[] s = new byte[pair.dataSize];
                    f.fully(s);
                    // Strip trailing null character
                    pair.data = new String(s, 0, s[s.length - 1] == '\0' ? s.length - 1 : s.length, charset);

                    //System.out.println(String.format("offset=%08X key='%s' string '%s' [len=%d]",
                    //    keyTableOffset + pair.keyOffset, pair.key, pair.data, pair.dataSize));
                    break;

                case PSF_DATA_TYPE_INT32:
                    pair.data = new Integer(f.read4le());//uword

                    //System.out.println(String.format("offset=%08X key='%s' int32 %08X %d [len=%d]",
                    //    keyTableOffset + pair.keyOffset, pair.key, pair.data, pair.data, pair.dataSize));
                    break;

                default:
                    System.out.println(String.format("offset=%08X key='%s' unhandled data type %d [len=%d]",
                            keyTableOffset + pair.keyOffset, pair.key, pair.dataType, pair.dataSize));
                    break;
            }
        }

        sizeDirty = true;
        tablesDirty = false;
        calculateSize();
    }

    public Object get(String key)
    {
        for (Iterator it = pairList.iterator(); it.hasNext();) {
            PSFKeyValuePair pair = (PSFKeyValuePair) it.next();
            if (pair.key.equals(key)) {
                return pair.data;
            }
        }
        return null;
    }

    public String getString(String key)
    {
        Object obj = get(key);
        if (obj != null) {
            return (String) obj;
        }
        return null;
    }

    /**
     * kxploit patcher tool adds "\nKXPloit Boot by PSP-DEV Team"
     */
    public String getPrintableString(String key)
    {
        String rawString = getString(key);
        if (rawString == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rawString.length(); i++) {
            char c = rawString.charAt(i);
            if (c == '\0' || c == '\n') {
                break;
            }
            sb.append(rawString.charAt(i));
        }

        return sb.toString();
    }

    public int getNumeric(String key)
    {
        Object obj = get(key);
        if (obj != null) {
            return ((Integer) obj).intValue();
        }
        return 0;
    }

    public void put(String key, byte[] data)
    {
        PSFKeyValuePair pair = new PSFKeyValuePair(key, PSF_DATA_TYPE_BINARY, data.length, data);
        pairList.add(pair);

        sizeDirty = true;
        tablesDirty = true;
        indexEntryCount++;
    }

    public void put(String key, String data, int rawlen)
    {
        byte[] b = (data.getBytes(charset));

        //if (b.length != data.length())
        //    System.out.println("put string '" + data + "' size mismatch. UTF-8=" + b.length + " regular=" + (data.length() + 1));
        //PSFKeyValuePair pair = new PSFKeyValuePair(key, PSF_DATA_TYPE_STRING, data.length() + 1, rawlen, data);
        PSFKeyValuePair pair = new PSFKeyValuePair(key, PSF_DATA_TYPE_STRING, b.length + 1, rawlen, data);
        pairList.add(pair);

        sizeDirty = true;
        tablesDirty = true;
        indexEntryCount++;
    }

    public void put(String key, String data)
    {
        byte[] b = (data.getBytes(charset));
        //int rawlen = data.length() + 1;
        int rawlen = b.length + 1;

        put(key, data, (rawlen + 3) & ~3);
    }

    public void put(String key, int data)
    {
        PSFKeyValuePair pair = new PSFKeyValuePair(key, PSF_DATA_TYPE_INT32, 4, new Integer(data));
        pairList.add(pair);

        sizeDirty = true;
        tablesDirty = true;
        indexEntryCount++;
    }

    private void calculateTables()
    {
        tablesDirty = false;

        // position the key table after the index table and before the value table
        // 20 byte PSF header
        // 16 byte per index entry
        keyTableOffset = 5 * 4 + indexEntryCount * 0x10;

        // position the value table after the key table
        valueTableOffset = keyTableOffset;

        for (Iterator it = pairList.iterator(); it.hasNext();) {
            // keys are not aligned
            valueTableOffset += ((PSFKeyValuePair) it.next()).key.length() + 1;
        }

        // 32-bit align for data start
        valueTableOffset = (valueTableOffset + 3) & ~3;

        // index table
        int keyRunningOffset = 0;
        int valueRunningOffset = 0;

        for (Iterator it = pairList.iterator(); it.hasNext();) {
            PSFKeyValuePair pair = (PSFKeyValuePair) it.next();
            pair.keyOffset = keyRunningOffset;
            keyRunningOffset += pair.key.length() + 1;
            pair.valueOffset = valueRunningOffset;
            valueRunningOffset += pair.dataSizePadded;
        }
    }

    private void calculateSize()
    {
        sizeDirty = false;
        size = 0;

        if (tablesDirty) {
            calculateTables();
        }

        for (Iterator it = pairList.iterator(); it.hasNext();) {
            PSFKeyValuePair pair = (PSFKeyValuePair) it.next();
            int keyHighBound = keyTableOffset + pair.keyOffset + pair.key.length() + 1;
            int valueHighBound = valueTableOffset + pair.valueOffset + pair.dataSizePadded;
            if (keyHighBound > size) {
                size = keyHighBound;
            }
            if (valueHighBound > size) {
                size = valueHighBound;
            }
        }
    }

    public int size()
    {
        if (sizeDirty) {
            calculateSize();
        }

        return size;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (Iterator it = pairList.iterator(); it.hasNext();) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(((PSFKeyValuePair) it.next()).toString());
        }

        if (isLikelyHomebrew()) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append("This is likely a homebrew");
        }

        return sb.toString();
    }

    /**
     * used by isLikelyHomebrew()
     */
    private boolean safeEquals(Object a, Object b)
    {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    public boolean isLikelyHomebrew()
    {
        boolean homebrew = false;

        String disc_version = getString("DISC_VERSION");
        String disc_id = getString("DISC_ID");
        String category = getString("CATEGORY");
        Integer bootable = (Integer) get("BOOTABLE"); // don't use getNumeric, we also want to know if the entry exists or not
        Integer region = (Integer) get("REGION");
        String psp_system_ver = getString("PSP_SYSTEM_VER");
        Integer parental_level = (Integer) get("PARENTAL_LEVEL");

        Integer ref_one = new Integer(1);
        Integer ref_region = new Integer(32768);

        if (safeEquals(disc_version, "1.00")
                && safeEquals(disc_id, "UCJS10041")
                && // loco roco demo, should not false positive since that demo has sys ver 3.40
                safeEquals(category, "MG")
                && safeEquals(bootable, ref_one)
                && safeEquals(region, ref_region)
                && safeEquals(psp_system_ver, "1.00")
                && safeEquals(parental_level, ref_one)) {

            if (indexEntryCount == 8) {
                homebrew = true;
            }
            else if (indexEntryCount == 9
                    && safeEquals(get("MEMSIZE"), ref_one)) {
                // lua player hm 8
                homebrew = true;
            }
        }
        else if (indexEntryCount == 4
                && safeEquals(category, "MG")
                && safeEquals(bootable, ref_one)
                && safeEquals(region, ref_region)) {
            homebrew = true;
        }

        return homebrew;
    }

    public static class PSFKeyValuePair {

        // index table info
        public int keyOffset;
        public int unknown1;
        public int dataType;
        public int dataSize;
        public int dataSizePadded;
        public int valueOffset;

        // key table info
        public String key;

        // data table info
        public Object data;

        public PSFKeyValuePair()
        {
            this(null, 0, 0, null);
        }

        public PSFKeyValuePair(String key, int dataType, int dataSize, Object data)
        {
            this(key, dataType, dataSize, (dataSize + 3) & ~3, data);
        }

        public PSFKeyValuePair(String key, int dataType, int dataSize, int dataSizePadded, Object data)
        {
            this.key = key;
            this.dataType = dataType;
            this.dataSize = dataSize;
            this.dataSizePadded = dataSizePadded;
            this.data = data;

            // yapspd: 4
            // probably alignment of the value data
            unknown1 = 4;
        }

        /**
         * only reads the index entry, since this class has doesn't know about
         * the psf/key/value offsets
         */
        public void read(BasicFile f) throws IOException
        {
            // index table entry
            keyOffset = f.readU2le();
            unknown1 = f.readU();
            dataType = f.readU();
            dataSize = f.read4le();//uword
            dataSizePadded = f.read4le();//uword
            valueOffset = f.read4le();//uword
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(key + " = " + data);
            return sb.toString();
        }
    }
}
