/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.pkg;

import dLibs.net.didion.loopy.ISO9660FileEntry;
import dLibs.net.didion.loopy.ISO9660FileSystem;
import com.DeltaSKR.IO.interfce.RndAccFile;
import com.DeltaSKR.IO.interfce.RndAccess;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

import main_app.IndexHelper;
import parsers.ANY.PSF;
import parsers.BasicFile;
import parsers.Pkg_box;
import parsers.UNK_BIN;

/**
 * @author ARMAX
 */
public class IISO_Root extends Pkg_box {

    public byte claxType = -1;

    //ISO1 ISO2 GISO XISO UDF, WISO, WBFS
    private static final int[] CODES = {
        0x49534F31, 0x49534F32, 0x4749534F,
        0x5849534F, 0x55444600, 0x5749534F,
        0x57424653
    };

    public IISO_Root(BasicFile parent)
    {
        super(null, CODES, parent, Flags.DUNK | Flags.NAME);
    }

    public IISO_Root(BasicFile parent, int flg, Object... parsers)
    {
        super(null, CODES, parent, Flags.DUNK | Flags.NAME | flg, parsers);
    }

    @Override
    public boolean isValidCode(int code)
    {
        currentType = -1;
        byte cca = 0;
        for (int c : CODES) {
            if (code == c) {
                currentType = cca;
                return true;
            }
            cca++;
        }
        return false;
    }

    @Override
    public int getCType()
    {
        return currentType >= 0 && currentType < CODES.length ? CODES[currentType] : 0x554E4B49;
    }

    @Override
    public String getSType()
    {
        return getSType(getCType(), 4);
    }

    public static byte isValid(File f)
    {
        ISO9660FileSystem usa = null;
        RType = 0;
        try {
            if (f.getName().toUpperCase().endsWith(".ISO") || f.getName().toUpperCase().endsWith(".BIN")) {
                usa = new ISO9660FileSystem(f, true);
                Enumeration ec = usa.getEntries();
                while (ec.hasMoreElements()) {
                    ec.nextElement();
                }
                return 1;
            }
            else {
                return -1;
            }
        }
        catch (Exception ex) {
//            System.err.println(f.getAbsolutePath());
//            ex.printStackTrace(System.out);
//            ex.printStackTrace();
        }
        finally {
            if (usa != null) {
                try {
                    usa.close();
                }
                catch (Exception exception1) {
                }
            }
        }
        return -1;
    }

    public static RndAccess open(File f, int dtp) throws IOException
    {
        if (f.getName().toUpperCase().endsWith(".ISO")) {
            return new RndAccFile(f, "r");
        }
        else {
            return null;
        }
    }

    @Override
    public String getName(int index) throws IOException
    {
        return names == null || names.length == 0 ? null : names[index];//.replace("/", "%2f");
    }

    public String[] names = {};
    public static int RType = 0;//3 PSP, 2 PS2,1 PS1/PSX

    private final static PSF PSF_PSP_GAME = new PSF();
    private final static UNK_BIN UNK_PSF = new UNK_BIN(null);

    protected IndexHelper.VEntry tmpLst;

    @Override
    public IndexHelper.VEntry createVDris(IndexHelper.IEntry curFiles)
    {
        return tmpLst;
    }

    protected final boolean isValidISO(RndAccess isox)
    {
        try {
            final ISO9660FileSystem iso = new ISO9660FileSystem(
                    isox != null ? isox
                            : this.parent != null ? this : fi);//fix nested data 
            ArrayList el = new ArrayList();
            Enumeration e = iso.getEntries();
            count = 0;
            long ic[];
            long[][] offsets;
            ISO9660FileEntry f;
            count = 0;
            long off = 0;
            long end = 0;
            RType = 0;

            while (e.hasMoreElements()) {
                f = (ISO9660FileEntry) e.nextElement();
                if (f == null || f.isDirectory()) {
                    continue;
                }
                if (RType == 0 || RType == 4) {
//                System.out.println(f.getPath());
                    if (f.getPath().equals("UMD_DATA.BIN")) {
                        RType = 4;
                        off = f.startSector * 0x800;
                        end = off + f.dataLength;
                    }
                    else if (f.getPath().equals("PSP_GAME/PARAM.SFO")) {
                        iso.getChannel().seek(f.startSector * 0x800);
                        if (iso.getChannel().read4le() == PSF.PSF_IDENT) {
                            RType = 3;
                            off = f.startSector * 0x800;
                            end = off + f.dataLength;
                        }
                    }
                    else if (f.getPath().equalsIgnoreCase("SYSTEM.CNF")) {
                        RType = 1;
                        off = f.startSector * 0x800;
                        end = off + f.dataLength;
                    }
                }
                el.add(f);
            }

            try {
                switch (RType) {
                    case 1:
                        byte[] ac = new byte[255];
                        iso.getChannel().seek(off);
                        iso.getChannel().read(ac, 0, (int) Math.min(255, end - off));
                        String s = new String(ac, "windows-1252").toUpperCase();
                        if (s.contains("BOOT2")) {
                            s = s.substring(s.indexOf("BOOT2") + 7);
                            RType = 2;
                        }
                        else if (s.contains("BOOT")) {
                            s = s.substring(s.indexOf("BOOT") + 6);
                            RType = 1;
                        }
                        else {
                            RType = 0;
                        }
                        if (RType != 0) {
                            s = s.substring(s.indexOf(":") + 2);
                            if (s.length() >= 11 && s.charAt(4) == '_'
                                    && s.charAt(5) >= '0' && s.charAt(5) <= '9'
                                    && s.charAt(6) >= '0' && s.charAt(6) <= '9'
                                    && s.charAt(7) >= '0' && s.charAt(7) <= '9'
                                    && s.charAt(8) == '.'
                                    && s.charAt(9) >= '0' && s.charAt(9) <= '9'
                                    && s.charAt(10) >= '0' && s.charAt(10) <= '9') {
                                IsoCode = s.substring(0, 4)
                                        + "-"
                                        + s.substring(5, 8)
                                        + s.substring(9, 11);
                                IsoTitle = "";
                            }
                            else {
                                RType = 0;
                            }
                        }
                        break;
                    case 3:
                        UNK_PSF.setIO(iso.getChannel());
                        UNK_PSF.isValid(null, off, end, 0);
                        UNK_PSF.seek(0);
                        PSF_PSP_GAME.read(UNK_PSF);
                        IsoCode = PSF_PSP_GAME.getString("DISC_ID");
                        IsoTitle = PSF_PSP_GAME.getString("TITLE");
                        break;
                    case 4:
                        UNK_PSF.setIO(iso.getChannel());
                        UNK_PSF.isValid(null, off, end, 0);
                        UNK_PSF.seek(0);
                        String cop[] = UNK_PSF.readStr(16, "utf-8").split("\\|", 2);
                        if (cop != null && cop.length > 0) {
                            IsoCode = cop[0].replace("-", "");
                        }
                        break;
                }
            }
            catch (IOException ex) {
                RType = 0;
                ex.printStackTrace(System.out);
            }
//        System.out.println(Code);
            System.gc();
            count = el.size();
            if (!resizeOffets(this, 2, 0, 1)) {
                return false;
            }
            offsets = (long[][]) super.offsets;
            if (jindex == null && names.length < count) {
                names = new String[count];
            }
            IndexHelper.VEntry tree = jindex == null ? new IndexHelper.VEntry() : null;

            for (int i = 0; i < count; i++) {
                f = (ISO9660FileEntry) el.get(i);
//            System.out.printf("%s %d %d\n",
//                    f.getPath(),
//                    f.getStartBlock(),
//                    f.getSize()
//            );        
                if (jindex == null) {
                    names[i] = f.identifier;
                    IndexHelper.VEntry cas = tree;
                    if (f.getParentPath() != null && f.getParentPath().length() > 0) {
                        String[] spl = f.getParentPath().split("/");
                        for (String kk : spl) {
                            IndexHelper.VEntry cc = cas.get(kk);
                            if (cc == null) {
                                IndexHelper.VEntry ea = new IndexHelper.VEntry();
                                ea.desc = kk;
                                cas.add(ea);
                                cas = ea;
                            }
                            else {
                                cas = cc;
                            }
                        }
                    }
                    cas.add(i);
                }
                ic = offsets[i];
                ic[0] = f.startSector * 2048;
                ic[1] = ic[0] + f.dataLength;
                if (ic[0] >= length()) {
                    ic[0] = length();
                    ic[1] = length();
                }
                else if (ic[1] > length()) {
                    ic[1] = length();
                }
            }
            el.clear();
            System.gc();
            Runtime.getRuntime().runFinalization();
            System.gc();
            tmpLst = tree;
            return true;
        }
        catch (Exception ex) {
//            ex.printStackTrace(CONSOLE.getPo());
        }
        return false;
    }

    @Override
    protected boolean isValid() throws IOException
    {
        currentType = -1;
        if (isValidISO(null)) {
            currentType = 1;
            return true;
        }
        return false;
    }

    public String curPath() throws Exception
    {
        return IndexHelper.findPath(names, createVDris(null), countFounds);
    }

    public BasicFile getFile(String cu, String name) throws IOException
    {
        int fu = IndexHelper.findIndexFromTree(names, createVDris(null), cu, false);
        if (fu < 0) {
            return null;
        }
        try {
            if (countFounds != fu) {
                return null;
            }
        }
        catch (Exception ex) {
        }
        fu = IndexHelper.findIndexFromTree(names, createVDris(null), name, false);
        if (fu < 0) {
            return null;
        }
        unk.isValid(this,
                ((long[][]) super.offsets)[fu][0],
                ((long[][]) super.offsets)[fu][1],
                -1
        );
        return unk;
    }

}
