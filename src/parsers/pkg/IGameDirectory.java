/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.pkg;

import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.interfce.RndAccFile;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.lang.ArrayUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import main_app.IndexHelper;
import parsers.BasicFile;
import parsers.Pkg_box;

/**
 *
 * @author ARMAX
 */
public class IGameDirectory extends Pkg_box {

    ArrayList benk = new ArrayList();
    private static final int[] CODECs = {0x47414D45};

    public IGameDirectory(BasicFile parent)
    {
        super(null, CODECs, parent, Flags.DUNK | Flags.NAME | Flags.DEFS);
    }

    public IGameDirectory(BasicFile parent, Object... parsers)
    {
        super(null, CODECs, parent, Flags.DUNK | Flags.NAME | Flags.DEFS | Flags.SVOF, parsers);
    }

    @Override
    public String getName(int index) throws IOException
    {
//        return ((File) paths.get(index)).getAbsolutePath().substring(subIDX).replace(File.separator, "%2f");
        return ((File) paths.get(index)).getName();
    }

    public File getFile(int index) throws IOException
    {
        return (File) paths.get(index);
    }

    @Override
    protected void printOffsets(IndexHelper.IEntry entry, final int index,
            final long off, final long end, final long siz) throws IOException
    {
        String ac = getName(index);
        RndAccess rs = null;
        try {
            rs = new RndAccFile((File) paths.get(index), "r");
            if (!isCompressed() && off == 0 && end - off < 4) {//fix recursive problems
                unk.setIO(rs);
                unk.isValid(null, 0, 0, 0);
                unk.printOffset(entry, unk.getCType(), ac != null, false);
                IndexHelper.setName(entry.last(), ac);
                countFounds = index + 1;
                super.updateProgress();
                return;
            }
            boolean _unk = true;
            if (end - off >= 4) {
                BasicFile pr;
                for (int j = 0; j < parsers.length; j++) {
                    pr = getParser(j);
                    pr.setIO(rs);
//                System.out.println(off + " " + end+" "+this.getClass());
                    try {
                        if (pr.isValid(useAbsOffsetsB() ? null : this, off, end, siz)) {
//                    if (pr instanceof CMPS) {
//                        printOut(pr);
//                    }
                            pr.printOffset(entry, pr.getCType(), ac != null, false);
                            _unk = false;
                            break;
                        }
                    }
                    finally {
                        pr.endFile();
                    }

                }
            }
            if (_unk) {
                unk.setIO(rs);
                unk.isValid(useAbsOffsetsB() ? null : this, off, end, siz);
                unk.printOffset(entry, unk.getCType(), ac != null, false);
            }
            IndexHelper.setName(entry.last(), ac);
            countFounds = index + 1;
            super.updateProgress();
        }
        finally {
            IOSys.fclose(rs);
        }
    }

    public RndAccess getFileRW(int index) throws IOException
    {
        return getFileRW(index, false);
    }

    public RndAccess getFileRW(int index, boolean delete) throws IOException
    {
        countFounds = index;//fix back get index
        final String coc = IndexHelper.getIfNamed(jindex.get(index));
        final File cop = (File) paths.get(index);
        if (cop.exists() && cop.isFile() && !cop.isDirectory() && cop.canWrite()) {
            if (delete) {
                IOSys.openOutput(cop).close();
            }
            return IOSys.openRandom(cop, false);
        }
        throw new IOException("File not exists \"" + coc + "\"");
    }

    @Override
    public BasicFile readFile(int index) throws IOException
    {
        countFounds = index;//fix back get index
        final String coc = IndexHelper.getIfNamed(jindex.get(index));
        final File cop = (File) paths.get(index);
        if (cop.exists() && cop.isFile() && !cop.isDirectory() && cop.canRead()) {
            long[][] offs0 = (long[][]) offsets;
            RndAccess rs = new RndAccFile(cop, "r");
            unk.setIO(rs);
            unk.isValid(null, offs0[index][0], offs0[index][1], 0);
            return unk;
        }
        throw new IOException("File not exists \"" + coc + "\"");
    }

    @Override
    public BasicFile getEnt(boolean force, final int index, final IndexHelper.IEntry el,
            final long a, final long b, final long c) throws IOException
    {
        countFounds = index;//fix back get index
        final String coc = IndexHelper.getIfNamed(jindex.get(index));
        final File cop = (File) paths.get(index);
        if (((!cop.exists() || !cop.isFile()) || cop.isDirectory()) || !cop.canRead()) {
            throw new IOException("File not exists \"" + coc + "\"");
        }
        IndexHelper.IEntry ofs = el;
        int code = IndexHelper.getCode(el);
//        RndAccess rs = new RndAccFile((File) paths.get(index), "r");
        RndAccess rs = new RndAccFile(cop, "r");
        benk.add(rs);
        BasicFile pr;
        if (IndexHelper.isKNOWN(el)) {
            for (int j = 0; j < parsers.length; j++) {
                pr = getParser(j);
                pr.setIO(rs);
                if (pr.isValidCode(code)) {
                    if (pr instanceof Pkg_box) {
                        ((Pkg_box) pr).setOnlyKnown(onlyKnown);
                    }
                    pr.setJData(ofs);
                    pr.isValid(null, a, b, c);
                    return pr;
                }
                pr.endFile();
            }
        }
        unk.setIO(rs);
        unk.isValidCode(code);
        unk.isValid(null, a, b, c);
        return unk;
    }

    private static final ArrayList paths = new ArrayList(255);
    public static File root;
    private static int subIDX;

    private final static Crups flt = new Crups();
    private final static FileFilter dirs = new FileFilter() {
        @Override
        public boolean accept(File pathname)
        {
            return pathname.isDirectory();
        }
    };

    @Override
    public IndexHelper.VEntry createVDris(IndexHelper.IEntry curFiles)
    {
        try {
            return tempList;
        }
        finally {
            tempList = null;
        }
    }
    IndexHelper.VEntry tempList;

    @Override
    public boolean isValid() throws IOException
    {
        tempList = null;
        beginOffset = 0;
        endOffset = 0;
        if (!root.isAbsolute()) {
            root = root.getAbsoluteFile();
        }
        offsets = new long[0][];
        subIDX = root.getAbsolutePath().length() + 1;
        if (fixFiles()) {
            return true;
        }
        paths.clear();
        System.gc();
        IndexHelper.VEntry tic = new IndexHelper.VEntry();
        getAllFiles(root, tic);
        if (paths.size() < 1) {
            System.err.println("Empty Directory");
            return false;
        }
        count = paths.size();
        offsets = ArrayUtil.increaseArraySize(offsets, 0, count);
        long[][] offs0 = (long[][]) offsets;
        File cu;
        for (int u = 0; u < count; u++) {
            cu = (File) paths.get(u);
            if (offs0[u] == null) {
                offs0[u] = new long[2];
            }
            offs0[u][0] = 0;
            offs0[u][1] = cu.length();
        }
        tempList = tic;
        return true;
    }
    private static boolean casedet;

    private static void getAllFiles(File dir, IndexHelper.VEntry tic)
    {
        File d[] = dir.listFiles(dirs);
        for (int u = 0; u < d.length; u++) {
            IndexHelper.VEntry pas = new IndexHelper.VEntry(d[u].getName());
            pas.length = 1;
            tic.add(pas);
            pas.length = 0;
            getAllFiles(d[u], pas);
        }
        casedet = dir == root;
        flt.pat = tic;
        dir.listFiles(flt);
        flt.pat = null;
    }

    public boolean fixFiles()
    {
        if (jindex != null) {
            count = jindex.length;
            offsets = ArrayUtil.increaseArraySize(offsets, 0, count);
            paths.clear();
            for (int i = 0; i < count; i++) {
                paths.add(null);
            }
            IsoCode = null;
            IsoTitle = "";
            __fixFiles(jindex.vdir, root);
            return true;
        }
        return false;
    }

    private void __fixFiles(IndexHelper.VEntry vk, File fus)
    {
        if (jindex != null) {
            for (int i = 0; i < vk.length; i++) {
                Object ee = vk.get(i);
                if (ee instanceof Integer) {
                    long[][] offs0 = (long[][]) offsets;
                    int u = (Integer) ee;
                    final String nam = IndexHelper.getIfNamed(jindex.get(u));
                    final File cu = new File(fus, nam);
                    paths.set(u, cu);
                    if (nam.startsWith("GAME_ID=")) {
                        IsoCode = nam.substring(8);
                        IsoTitle = "";
                    }
                    if (offs0[u] == null) {
                        offs0[u] = new long[2];
                    }
                    offs0[u][0] = 0;
                    if (cu.exists() && cu.isFile() && !cu.isDirectory() && cu.canRead()) {
                        offs0[u][1] = cu.length();
                    }
                    else {
                        offs0[u][1] = 0;
                    }
                }
                else {
                    __fixFiles((IndexHelper.VEntry) ee, new File(fus, ((IndexHelper.VEntry) ee).desc));
                }
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        while (benk.size() > 0) {
            IOSys.fclose(benk.remove(0));
        }
    }

    private static class Crups implements FileFilter {

        public Crups()
        {
        }
        private IndexHelper.VEntry pat;

        @Override
        public boolean accept(File pathname)
        {
            if (pathname.isFile()) {
                if (casedet && pathname.getName().startsWith("GAME_ID=")) {
                    IsoCode = pathname.getName().substring(8);
                    IsoTitle = "";
                }
                pat.add(paths.size());
                paths.add(pathname);
            }
            return false;
        }
    }

}
