/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.interfce.ReadSeek;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.IO.interfce.SeekByteArray;
import com.DeltaSKR.IO.interfce.WriteSeek;
import com.DeltaSKR.IO.json.TABPrintStream;
import com.DeltaSKR.lang.ArrayUtil;
import com.DeltaSKR.lang.PrimitiveList;

import java.io.*;
import java.util.Arrays;
import main_app.*;
import parsers.pkg.IGameDirectory;

/**
 *
 * @author ARMAX
 */
public abstract class BasicFile extends Subisrad implements GameTypes {

    /**
     * @deprecated use GTYPE && GCODE
     * @see GTYPE
     * @see GCODE
     */
    public static String IsoCode = "";
    public static String IsoTitle = "";
    /**
     * platform code
     */
    public static int GTYPE;
    /**
     * game code or name, can use how to you need.
     */
    public static long GCODE;
    /**
     * Used for DesktopVersion debug purposes
     */
    public static boolean DONT_USE_SETFIS;

    /**
     * get first integer from string
     *
     * @param ss
     * @return
     */
    public static int getFInt(String ss)
    {
        if (ss == null) {
            return -1;
        }
        int s = 0;
        while (s < ss.length()) {
            char a = ss.charAt(s);
            if (a >= '0' && a <= '9') {
                break;
            }
            s++;
        }
        int i = s;
        while (s < ss.length()) {
            char a = ss.charAt(s);
            if (a < '0' || a > '9') {
                break;
            }
            s++;
        }
        if (s - i > 0) {
            return Integer.parseInt(ss.substring(i, s));
        }
        return -1;
    }
    private static boolean printUpdates = true;

    public static void noPrintUpdates()
    {
        printUpdates = false;
    }

    public static void printUpdates()
    {
        printUpdates = true;
    }

    public static int getSInt(String name)
    {
        int max = (name.length() - 1) * 8;
        int lix = 0;
        for (int i = 0; i < name.length(); i++) {
            lix |= name.charAt(i) << max;
            max -= 8;
        }
        return lix;
    }

    private final short level;

    public static int MaxLevel;

    /**
     * only for testing usage
     */
    public final static void setFis()
    {
        if (DONT_USE_SETFIS) {//prevent android IO error,caused by leave setFis on anywere
            return;
        }
        IOSys.setInternal(null);
    }

    public final static <T extends BasicFile> T makebas(Class<T> cl, Object pa, boolean store) throws IOException
    {
        Object k;
        try {
            k = cl.getConstructor(BasicFile.class).newInstance((BasicFile) null);
        }
        catch (Exception ex) {
            return null;
        }
        if (store) {
            byte[] ta = IOSys.readAllBytes(pa);
            pa = new SeekByteArray(ta, 0, ta.length);
        }
        else if (!(pa instanceof RndAccess)) {
            pa = IOSys.openRandom(pa, true);
        }
        T pis = (T) k;
        pis.setIO((RndAccess) pa);
        return pis;
    }

    public static boolean delimiteSize = true;
    public static final boolean NOHEX = true;
    protected static final ParamString pStr = new ParamString(20);

    public static byte[] copyRes(String a)
    {
        InputStream ar = BasicFile.class.getResourceAsStream(a);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            int u;
            while ((u = ar.read()) != -1) {
                b.write(u);
            }
            return b.toByteArray();
        }
        catch (IOException e) {
        }
        finally {
            try {
                ar.close();
            }
            catch (Exception e) {
            }
        }
        return null;
    }

    protected boolean forceable = true;

    public void endFile()
    {
        currentType = -1;
        jindex = null;
        this.TLen = 0;
        this.beginOffset = 0;
        this.endOffset = 0;
        this.unlock();
        if (idata.length > 0) {
            idata = new byte[0];
        }
    }
    protected volatile byte[] idata = {};//temporaldata

    protected void resizeTmp(int size)
    {
        if (size > idata.length) {
            idata = new byte[size];
        }
    }

    public static final TABPrintStream CONSOLE = new TABPrintStream(System.out) {
        @Override
        public void close()
        {//dont close  console
        }
    };

    protected static void printOut(BasicFile b) throws IOException
    {
        BasicFile pa = b.getAbsParent();
        int nux = 0;
        if (pa instanceof Pkg_base) {
            nux = pa.countFounds;//probably this used for get current id
        }
        b.printOffset(CONSOLE, nux, b.getSType(), DEFAULT_BYTES, 1);
    }

    public static final byte[] TMP_DATA = new byte[(int) 3e6];
    public final BasicFile auxParent;
    public final BasicFile parent;

    public BasicFile getAbsParent()
    {
        return parent != null ? parent : auxParent;
    }
    protected int countFounds;
    private static TaskInterface progress;
    private static long timeDuration;
    private static long timeCursor;

    public static void initTime()
    {
        timeDuration = System.currentTimeMillis();
        timeCursor = timeDuration;
    }

    public final static String fileFormat(String base, int number, String det, String ext)
    {
        if (det == null) {
            return pStr.format("%s/%08d-%2$08X.%s").add(base).add(number).add(ext).toString();
        }
        else {
            return pStr.format("%s/%08d-%2$08X-%s.%s").add(base).add(number).add(det).add(ext).toString();
        }
    }

    protected void updateProgress(String msg, boolean f) throws IOException
    {
        if (printUpdates && progress != null && (f || System.currentTimeMillis() - timeCursor >= 5000)) {
            timeCursor = System.currentTimeMillis();
            long ec = (timeCursor - timeDuration) / 1000l;
            int s = (int) (ec % 60);
            int m = (int) ((ec / 60) % 60);
            int h = (int) ((ec / 3600) % 60);
            progress.updateProgress(null);
            System.setOut(System.out);
            System.out.printf("Time: %02d:%02d:%02d\n", h, m, s);
            System.out.println(msg);

//            System.gc();
        }
    }

    public static void updateProgress(int p, int total, boolean shouldbe)
    {
        if (!printUpdates || progress == null) {
            return;
        }
        long eac = System.currentTimeMillis();
        if (shouldbe || eac - timeCursor >= 500) {
            timeCursor = eac;
            long ec = (timeCursor - timeDuration) / 1000l;
            int s = (int) (ec % 60);
            int m = (int) ((ec / 60) % 60);
            int h = (int) ((ec / 3600) % 60);
            progress.updateProgress(null);
            double a = p, c = total;
            System.out.printf("Time searching: %02d:%02d:%02d\n", h, m, s);
            System.out.printf("PROGRESS...: %6.02f\n", (a / c) * 100D);
        }
    }

    protected void updateProgress() throws IOException
    {
        if (!printUpdates || progress == null) {
            return;
        }
        long eac = System.currentTimeMillis();
        if (eac - timeCursor >= 500) {
            timeCursor = eac;
            long ec = (timeCursor - timeDuration) / 1000l;
            int s = (int) (ec % 60);
            int m = (int) ((ec / 60) % 60);
            int h = (int) ((ec / 3600) % 60);
            progress.updateProgress(null);
            BasicFile yus = this;
            double a, c;
            System.out.printf("Time searching: %02d:%02d:%02d\n", h, m, s);
            while (yus != null) {
                a = yus instanceof IGameDirectory ? 0 : yus.seek();
                if (a < 0) {
                    yus = yus.getAbsParent();
                    continue;
                }
                c = yus instanceof IGameDirectory ? 1 : yus.length();
                final double fatso = (a / c) * 100D;
                System.out.printf("PROGRESS...: %6.02f %05X\n", fatso < 0 ? Float.NaN : fatso > 100 ? fatso % 100 : fatso, yus.countFounds);
                yus = yus.getAbsParent();
            }
        }
    }

    public static void setProgress(TaskInterface progress)
    {
        BasicFile.progress = progress;
    }

    protected final int[] P_BIN_TYPES;
    public final int[] P_CODEC_IDS;
    private static final int[] EMPTCODES = {};

    protected BasicFile(int[] types, int[] codecs, BasicFile parent)
    {
        if (codecs != null && codecs.length == 0 && codecs != EMPTCODES) {
            throw new RuntimeException("Error empty codes dont success");
        }
        P_CODEC_IDS = codecs == null ? EMPTCODES : codecs;
        P_BIN_TYPES = types == null || types.length == 0 ? EMPTCODES : types;
        boolean cus = false;
        if (parent instanceof Pkg_box) {
            cus = ((Pkg_box) parent).isDefaultOrCompressed();
        }
        this.parent = cus ? null : parent;
        this.auxParent = cus ? parent : null;
        level = (short) (this instanceof UNK_BIN || this instanceof UNK_PCK ? -1 : parent == null ? 0 : cus ? parent.level : parent.level + 1);
    }

    public byte currentType = -1;

    private final static StringBuffer bfo = new StringBuffer(8);

    public final static String getSType(long code, int bytes)
    {
        bfo.delete(0, bfo.length());
        char e;
        for (int i = (bytes - 1) * 8; i >= 0; i -= 8) {
            e = (char) ((code >> i) & 255);
            if ((e >= 'a' && e <= 'z')
                    || (e >= 'A' && e <= 'Z')
                    || (e >= '0' && e <= '9')
                    || e == '_') {
                bfo.append(e);
            }
        }
        return bfo.toString();
    }

    public String getSType()
    {
        if (P_CODEC_IDS != EMPTCODES) {
            if (P_CODEC_IDS.length == 1) {//directly get SType if have a one ID
                return getSType(P_CODEC_IDS[0], 4);
            }
            if (currentType != -1) {
                return getSType(P_CODEC_IDS[currentType], 4);
            }
        }
        return null;
    }

    public int getCType()
    {
        if (P_CODEC_IDS != EMPTCODES) {
            if (P_CODEC_IDS.length == 1) {
                return P_CODEC_IDS[0];
            }
            return currentType != -1 ? P_CODEC_IDS[currentType] : 0;
        }
        return 0;
    }

    public boolean isValidCode(int code)
    {
        if (P_CODEC_IDS != EMPTCODES) {
            for (int i : P_CODEC_IDS) {
                if (i == code) {
                    return true;
                }
            }
        }
        return false;
    }
    protected long TLen;

    public final boolean isValid(long realsize) throws IOException
    {
        return isValid(null, 0, fi.length(), realsize);
    }
    private static final boolean DEKUS = false;
    private static PrimitiveList.ListObject<String> emstack;//dont use static inline initialization on some objects or AIDE can get invalid  class desc "L;"
    private static TABPrintStream tompa;

    public static void closeCrop()
    {
        if (DEKUS) {
            emstack.elements = null;
            tompa.flush();
            IOSys.fclose(tompa);
            tompa = null;
        }
    }

    public static void initDuck(String name)
    {
        if (DEKUS) {
            emstack = PrimitiveList.newFrom(String.class, 256);
            TABPrintStream to = null;
            setFis();
            try {
                to = new TABPrintStream(IOSys.openOutput("com.neon.sgu/xeno/tumba/" + new File(name).getName() + ".txt", (int) 1e6));
            }
            catch (IOException ex) {
                ex.printStackTrace(CONSOLE.getPo());
            }
            tompa = to;
        }
    }

    public final boolean isValid(BasicFile parent, long offset, long endOffset, long realsize) throws IOException
    {
        final boolean cos = _isValid(parent, offset, endOffset, realsize);
        if (DEKUS && cos) {
            if (tompa != null) {
                BasicFile tim = this;
                if (getAbsParent() instanceof Pkg_box) {
                }
                else if (getAbsParent() instanceof Pkg_base) {//ignore Pkg_base element
                    return cos;
                }
//            tompa.print("SUCCESS");
                emstack.setSize(0);
                while (tim != null) {
                    BasicFile la = tim;
                    Class a = tim.getClass();
                    tim = tim.getAbsParent();
                    int ix = tim == null ? 999 : tim instanceof Pkg_box ? ArrayUtil.indexOf(((Pkg_box) tim).parsers, la) : 998;
                    if (ix < 0) {
                        ix = 997;
                    }
                    emstack.addO(String.format("%03d%s", ix, a.getName()));
                }
                while (emstack.size() > 0) {
                    tompa.print(":" + emstack.last());
                    emstack.popLast();
                }
                tompa.println();
            }
        }
        return cos;
    }

    private final boolean _isValid(BasicFile parent, long offset, long endOffset, long realsize) throws IOException
    {
        if (level != -1 && MaxLevel > 0 && level >= MaxLevel) {
            return false;//limit level of analisis
        }
        currentType = -1;
        TLen = realsize;
        if (offset < 0 || endOffset < offset) {
            return false;
        }
        if (parent != null) {
            offset += parent.beginOffset;
            endOffset += parent.beginOffset;
        }
        if (offset > -1) {
            beginOffset = offset;
        }
        else {
            beginOffset = fi.seek();
        }
        this.endOffset = endOffset;
        seek(0);
        boolean clax = false;
        drunk:
        try {
            if (P_BIN_TYPES != EMPTCODES) {
                int tp;
                seek(0);
                tp = fi.length() - fi.seek() > 0//fix fatal error omit EOF when  is Negative or 0
                        && length() >= 4 ? read4be() : -1;
                for (int i = 0; i < P_BIN_TYPES.length; i++) {
                    if (P_BIN_TYPES[i] == tp) {
                        currentType = (byte) i;
                        if (this instanceof UNK_BIN) {
                            return isValid();
                        }
                        clax = isValid();
                        break drunk;
                    }
                }
                seek(0);
                if (this instanceof UNK_BIN) {
                    return isValid();
                }
                endFile();
                return false;
            }
            clax = isValid();
            break drunk;
        }
        catch (EOFException e) {
            BasicFile ao = this;
            while (ao != null) {
                BasicFile.printOut(ao);
                ao = ao.getAbsParent();
            }
            CONSOLE.p()
                    .add(fi.seek())
                    .add(fi.length())
                    .add(endOffset - beginOffset)
                    .add(beginOffset + getBeginOffset())
                    .add(this.getClass().getName());
            CONSOLE.printfln("%08X %08X %08X %08X %s\n");
            throw e;
        }
        if (!clax) {
            endFile();
            return false;
        }
        if (this instanceof Pkg_box) {
            ((Pkg_box) this).initializeParsers();
        }
        return true;
    }

    protected boolean isValid() throws IOException
    {
        return true;
    }

    protected Object printOffsets(IndexHelper.IEntry entry) throws IOException
    {
        return null;
    }

    public static File CUR_FILE;

    public final void importData(RndAccess s, String f, int idx, boolean raw, boolean wFree) throws IOException
    {
        CUR_FILE = new File(f);
        ReadSeek rit = IOSys.openRandom(f, true);
        try {
            importData(s, rit, idx, raw, wFree);
        }
        finally {
            CUR_FILE = null;
            IOSys.fclose(rit);
        }
    }

    protected static void ThrowMEM(boolean wfree, long in, long dst) throws IOException
    {
        if (!wfree && in > dst) {
            throw new IOException(String.format("Not writed file because input.size(%08X) > source.size(%08X).", in, dst));
        }
    }

    private static void printStackTrace()
    {
        RuntimeException em = new RuntimeException();
        em.setStackTrace(Thread.currentThread().getStackTrace());
        em.printStackTrace(CONSOLE.getPo());
    }

    public final void importData(RndAccess w, ReadSeek in, int idx, boolean raw, boolean wfree) throws IOException
    {
//        printStackTrace();
        //sett to minit op file for write
        seek(0);
        if (!wfree) {
            w.seek(this.fi.seek());
        }
        if (raw) {
            if (in == null) {
                in = this;
            }
            ThrowMEM(wfree, in.length(), length());

            long minsize = wfree ? in.length() : Math.min(in.length(), this.length());
            in.seek(0);
            int u;
            do {
                if (minsize <= 0) {
                    break;
                }
                if (minsize < TMP_DATA.length) {
                    u = in.read(TMP_DATA, 0, (int) minsize);
                }
                else {
                    u = in.read(TMP_DATA, 0, TMP_DATA.length);
                }
                if (u == -1) {
                    break;
                }
                w.write(TMP_DATA, 0, u);
                minsize -= u;
            }
            while (true);
            minsize = wfree ? in.length() : Math.min(in.length(), this.length());
            if (!wfree) {// no fill with zeros
                fillZero(w, (int) (this.length() - minsize));
            }
            System.out.println("Write success");
        }
        else {
            importData(w, in, idx, wfree);
        }
    }

    protected static void fillZero(RndAccess w, int sz) throws IOException
    {
        if (sz < 0) {
            return;
        }
        Arrays.fill(TMP_DATA, (byte) 0);
        int u;
        do {
            if (sz <= 0) {
                break;
            }
            if (sz < TMP_DATA.length) {
                u = sz;
            }
            else {
                u = TMP_DATA.length;
            }
            w.write(TMP_DATA, 0, u);
            sz -= u;
        }
        while (true);
    }

    protected void importData(RndAccess w, ReadSeek in, int fidx, boolean wFree) throws IOException
    {
        IOSys.fclose(in);
        throw new RuntimeException("Error IMPORT is't suppurted by this  file type: " + getSType());
    }

    public final void exportRaw(WriteSeek out) throws IOException
    {
        seek(0);
        long ica = length();
        while (ica > 0) {
            if (ica >= TMP_DATA.length) {
                fully(TMP_DATA);
                out.write(TMP_DATA);
            }
            else {
                fully(TMP_DATA, 0, (int) ica);
                out.write(TMP_DATA, 0, (int) ica);
            }
            ica -= TMP_DATA.length;
        }
    }

    public final void exportRaw(OutputStream out) throws IOException
    {
        seek(0);
        long ica = length();
        while (ica > 0) {
            if (ica >= TMP_DATA.length) {
                fully(TMP_DATA);
                out.write(TMP_DATA);
            }
            else {
                fully(TMP_DATA, 0, (int) ica);
                out.write(TMP_DATA, 0, (int) ica);
            }
            ica -= TMP_DATA.length;
        }
    }

    protected OutputStream makeOutput(Object fs, String ext) throws IOException
    {
        if (fs instanceof String) {
            if (ext == null) {
                if (((String) fs).endsWith(".")) {
                    fs = (String) fs + getSType();
                }
            }
            return IOSys.openOutput(new File((String) fs));
        }
        else if (fs instanceof File) {
            return IOSys.openOutput((File) fs);
        }
        else if (fs instanceof WriteSeek) {
            return ((WriteSeek) fs).asOutputStream();
        }
        else if (fs instanceof OutputStream) {
            return (OutputStream) fs;
        }
        else {
            throw new ClassCastException("Output type not found");
        }
    }

    protected void closeIfPath(Object fs, Closeable cl) throws IOException
    {
        if (fs instanceof File || fs instanceof String) {
            cl.close();
        }
    }

    public final void export(Object fs, boolean raw) throws IOException
    {
        if (raw) {
            OutputStream f = makeOutput(fs, null);
            seek(0);
            long ica = length();
            while (ica > 0) {
                if (ica >= TMP_DATA.length) {
                    fully(TMP_DATA);
                    f.write(TMP_DATA);
                }
                else {
                    fully(TMP_DATA, 0, (int) ica);
                    f.write(TMP_DATA, 0, (int) ica);
                }
                ica -= TMP_DATA.length;
            }
            closeIfPath(fs, f);
        }
        else if (fs instanceof String) {
            export((String) fs);
        }
        else {
            throw new ClassCastException("Output type must be String");
        }
    }

    protected void export(String f) throws IOException
    {
        throw new RuntimeException("don't suppurted yet");
    }

    public static final int TYPE_EMP = 0,
            TYPE_BIN = 1,
            TYPE_IMG = 2,
            TYPE_MDL = 3,
            TYPE_PAL = 4,
            TYPE_SND = 5,
            TYPE_TXT = 6,
            TYPE_DIR = 7,
            TYPE_EDIR = 8,
            TYPE_ZIP = 9,
            TYPE_EZIP = 10,
            TYPE_VDIR = 11,
            TYPE_EVDIR = 12;

    protected abstract int type(boolean noEmpty);
    public IndexHelper.IEntry jindex;

    public final void setJData(IndexHelper.IEntry vix)
    {
        jindex = vix;
    }

    public final void printOffset(IndexHelper.IEntry entry, int type, int icoType, boolean useName, boolean root) throws IOException
    {
        printOffset(entry, type, icoType, useName, root, false);
    }

    public final void printOffset(IndexHelper.IEntry entry, int type, int icoType, boolean useName, boolean root, boolean noSubs) throws IOException
    {
        jindex = null;
        final boolean isPkg = this instanceof Pkg_base;
        int uflen = isPkg ? ((Pkg_base) this).count : 0;
        IndexHelper.VEntry vd = null;
        Object offsets = null;
        final IndexHelper.IEntry cc;
        if (!noSubs) {
            cc = new IndexHelper.IEntry(uflen);
            offsets = printOffsets(cc);
            useName = cc.desc != null;//fix naming exception
            vd = createVDris(cc);
        }
        else {
            cc = new IndexHelper.IEntry();
        }
        entry.next(
                IndexHelper.newEntry(cc, type, useName, root, this.getClass(), (length() >= 4//
                        || this instanceof IGameDirectory//fix game directory  datas
                                ? (icoType != -1 ? icoType : type(uflen > 0))
                                : TYPE_EMP), offsets, vd)
        );
    }

    /**
     * generates virtual dir structure, also can rename file if need using the
     * curFiles
     *
     * @param curFiles current list of files
     * @return virtual dir struct
     */
    public IndexHelper.VEntry createVDris(IndexHelper.IEntry curFiles)
    {
        return null;
    }

    public final void printOffset(IndexHelper.IEntry entry, int type, boolean useName, boolean root) throws IOException
    {
        printOffset(entry, type, -1, useName, root);
    }
    public static final int DEFAULT_BYTES = 4;

    public final void printOffset(TABPrintStream out, int num, String type, int bytes, int dat) throws IOException
    {
        printOffset(this, out, num, type, bytes, dat);
    }

    private static final byte[] TDAT = new byte[128];

    protected static void printOffset(
            BasicFile pf,
            TABPrintStream out, int num,
            String type,
            int bytes,
            int data
    ) throws IOException
    {
        if (out == null) {
            return;
        }
        if (type != null) {
            out.p().add(num)
                    .add(type)
                    .add(pf.beginOffset)
                    .add(pf.length());
            out.printf("%04X %4s %010X %010X ");
        }
        else {
            out.p().add(num)
                    .add(pf.beginOffset)
                    .add(pf.length());
            out.printf("%04X %010X %010X ");
        }

        book:
        if (bytes > 0) {
            byte[] arr = TDAT;
            int l = 1 << (bytes < 8 ? bytes : 7);
            int sot = Math.max(0, Math.min((int) (pf.length()), l));
            if (pf.fi == null) {//fix nullpointer exception
                out.print("| NullIO");
                break book;
            }
            pf.seek(0);

//            try {
            pf.read(arr, 0, sot);
//            } catch (Exception exception) {
//                System.out.println();
//            }

            for (int i = sot; i < l; i++) {
                arr[i] = 0;
            }
            out.print('|');
            out.printHex(arr, 0, l);
            switch (data) {
                case 1://ascii
                    out.print("|");
                    int cr;
                    for (int i = 0; i < l; i++) {
                        cr = arr[i] & 255;
                        if (Character.isSpaceChar(cr)
                                || Character.isWhitespace(cr)) {
                            out.print(' ');
                        }
                        else if (!Character.isISOControl(cr)) {
                            out.print((char) cr);
                        }
                        else {
                            out.print('.');
                        }
                    }
                    break;
                case 2://utf-16LE
                    out.print("|");
                    for (int i = 0; i < l; i += 2) {
                        cr = (arr[i] & 255) | ((arr[i + 1] & 255) << 8);
                        if (cr == 0) {
                            break;
                        }
                        if (Character.isSpaceChar(cr)
                                || Character.isWhitespace(cr)) {
                            out.print(' ');
                        }
                        else if (!Character.isISOControl(cr)) {
                            out.print((char) cr);
                        }
                        else {
                            out.print('.');
                        }
                    }
            }
        }
        out.println();
        out.flush();
    }

    public final InputStream getAsStream() throws IOException
    {
        seek(0);
        return new InputStream() {

            @Override
            public int read(byte[] data, int off, int siz) throws IOException
            {
                return BasicFile.this.read(data, off, siz);
            }

            @Override
            public int read() throws IOException
            {
                return BasicFile.this.read();
            }

            @Override
            public boolean markSupported()
            {
                return true;
            }

            @Override
            public synchronized void reset() throws IOException
            {
                seek(0);
            }

        };
    }

    public final InputStream getAsStream(final long a, final long b) throws IOException
    {
        BasicFile bi = new BasicFile(null, null, this) {
            @Override
            protected void export(String f) throws IOException
            {
            }

            @Override
            protected int type(boolean noEmpty)
            {
                return 0;
            }
        };
        bi.setIO(fi);
        bi.isValid(this, a, b, -1);
        return bi.getAsStream();
    }

}
