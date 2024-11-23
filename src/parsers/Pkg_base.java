/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.IO.json.TABPrintStream;
import com.DeltaSKR.lang.PrimitiveList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import main_app.IExNameProvider;
import main_app.IndexHelper;
import parsers.pkg.IGameDirectory;

/**
 *
 * @author ARMAX
 */
public abstract class Pkg_base extends BasicFile {

    protected boolean onlyKnown = true;
    protected boolean makeFree = false;

    public boolean isMakeable()
    {
        return false;
    }

    protected void makeData(RndAccess w, Object p, int[] sels, int sz1, int[] path, int p2, int sz2, boolean raw) throws IOException
    {

    }

    protected volatile byte[] tdata;
    protected int cLEN;

    @Override
    public final void endFile()
    {
        currentType = -1;
        jindex = null;
        offsets = null;
        TLen = 0;
        beginOffset = endOffset = -1;
        unlock();
        count = 0;
        TLen = 0;
        cLEN = 0;
        tdata = null;
        if (idata.length > 0) {
            idata = new byte[0];
        }
    }

    @Override
    protected int type(boolean noEmpty)
    {
        return noEmpty ? TYPE_DIR : TYPE_EDIR;
    }

    public final boolean isInvalidPos(long a) throws IOException
    {
        return a < 0 || a > length();
    }

    public final static boolean hasIntersectSZ(long off, long sz, long b) throws IOException
    {
        return hasIntersectMid(off, off + sz, b);
    }

    public final static boolean hasIntersectSZ(long off, long sz, long off2, long sz2) throws IOException
    {
        return hasIntersectMid(off, off + sz, off2, off2 + sz2);
    }

    public final static boolean hasIntersectMid(long off, long end, long off2, long end2) throws IOException
    {
        return hasIntersectMid(off, end, off2)
                || hasIntersectMid(off, end, end2)
                || hasIntersectMid(off2, end2, off)
                || hasIntersectMid(off2, end2, end);
    }

    public final static boolean hasIntersectMid(long off, long end, long b) throws IOException
    {
        if (off > end) {
            return b > end && b < off;
        }
        return b > off && b < end;
    }

    public final boolean isInvalidMid(long a, long b) throws IOException
    {
        return (a < 0 || a > length()) || (b < 0 || b > length()) || a > b;
    }

    public Object offsets;
    public int count = 0;

    protected static final boolean resizeOffets(Pkg_base bs, int sz1, int dz, int msz) throws IOException
    {
        bs.offsets = resizeOffets(bs, bs.count, sz1, dz, msz);
        if (bs.offsets != null) {
            return true;
        }
        bs.count = 0;
        return false;
    }

    public static final Object resizeOffets(BasicFile bs, int count, int sz1, int dz, int msz) throws IOException
    {
        if (count < dz || count + dz > 0xffff || bs.length() < (count + dz) * msz) {
            return null;
        }
        dz = count + dz;
        if (dz < 0) {
            return null;
        }
        if (sz1 < 2) {
            return new long[dz];
        }
        return new long[dz][sz1];
    }

    protected Pkg_base(int[] types, int[] codecs, BasicFile parent)
    {
        super(types, codecs, parent);
    }

    public final int getByOffset(long off, boolean abs, int deep, int[] path, int level) throws IOException
    {
        return getByOffset(off, abs, deep, path, 0, level);
    }

    public int getByOffset(long off, boolean abs, int deep, int[] path, int p2, int sz2) throws IOException
    {
        return -1;
    }

    public final void printInfo(final TABPrintStream p, boolean useVdir, int[] sels, int[] path, int level) throws IOException
    {
        execute(this, EXEC_FETCH_FIRST | (useVdir ? EXEC_USEVDIR : 0), new Eter() {
            @Override
            public void onPath(Pkg_base thiz, int ix, BasicFile cur, Object il) throws IOException
            {
                if (ix == -1) {
                    p.addTAB(-1);
                    return;
                }
                if (cur != null) {
                    if (IndexHelper.isNamed((IndexHelper.IEntry) il)) {
                        p.printfln("= %s", IndexHelper.getName((IndexHelper.IEntry) il));
                    }
                    cur.printOffset(p, ix + 1, cur.getSType(), DEFAULT_BYTES, 0);
                }
                else {
                    p.printfln("%04d> %s", ix + 1, ((IndexHelper.VEntry) il).desc);
                }
                p.addTAB(1);
            }

            @Override
            public void onFile(Pkg_base thiz, int ix, long beff, long eff, long coff, IndexHelper.IEntry ixdata) throws IOException
            {
                if (IndexHelper.isNamed(ixdata)) {
                    p.printfln("= %s", IndexHelper.getName(ixdata));
                }
                final BasicFile cur = thiz.getEnt(ix, ixdata, beff, eff, coff);
                if (cur == null) {
                    p.p().add(ix + 1)
                            .add(getSType(IndexHelper.getCode(ixdata), 4))
                            .add(beff)
                            .add(eff);
                    p.printfln("%04X %4s %010X %010X | <Undefined data bytes>");
                    return;
                }
                try {
                    cur.printOffset(p, ix + 1, cur.getSType(), DEFAULT_BYTES, 0);
                }
                finally {
                    cur.endFile();
                }
            }

            @Override
            public void onPost(Pkg_base thiz) throws IOException
            {
            }
        }, sels, sels.length, path, 0, level);
    }

    public final void importData(RndAccess w, Object p, int[] sels, int[] path, int level, boolean raw) throws IOException
    {
        importData(w, p, sels, sels.length, path, 0, level, raw);
    }

    protected void importData(RndAccess w, Object p, int[] sels, int sz1, int[] path, int p2, int sz2, boolean raw) throws IOException
    {
        throw new RuntimeException("Import data operation not support for this FILE");
    }

    public final Pkg_base getFromPath(int[] path, int level) throws IOException
    {
        return getFromPath(path, 0, level);
    }

    protected Pkg_base getFromPath(int[] path, int p2, int sz2) throws IOException
    {
        if (jindex == null) {
            if (count <= 0) {
                return null;
            }
            throw new RuntimeException("Index not loaded");
        }
        if (jindex.length < 1) {
            return null;
        }
        BasicFile fb = null;
        int ace;
        if (p2 < sz2) {
            ace = path[p2];
            if (offsets instanceof long[][]) {
                long off_siz_real[][] = (long[][]) this.offsets;
                fb = getEnt(
                        ace,
                        jindex.get(ace),
                        off_siz_real[ace][0],
                        off_siz_real[ace][1],
                        off_siz_real[0].length > 2 ? off_siz_real[ace][2] : -1
                );
            }
            else if (offsets instanceof long[]) {
                long index[] = (long[]) this.offsets;
                fb = getEnt(
                        ace,
                        jindex.get(ace),
                        index[ace],
                        index[ace + 1],
                        -1
                );
            }
            else {
                throw new RuntimeException("Object offsets not valid " + offsets);
            }

            if (fb instanceof Pkg_base) {
                if (fb instanceof Pkg_box) {
                    ((Pkg_box) fb).setOnlyKnown(onlyKnown);
                }
                return ((Pkg_base) fb).getFromPath(path, p2 + 1, sz2);
            }
            else {
                if (fb != null) {
                    fb.endFile();
                }
                throw new RuntimeException("Object " + fb + " not is an Pkg_box");
            }
        }
        else {
            return this;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Adavanced fetch code">
    /**
     * used with ehanced fetch pkgs for retrieve data
     */
    protected static interface Eter {

        /**
         * VDir and FDir process
         *
         * @param thiz actual pkg class object, can be null
         * @param ix current index
         * @param cur current file, can be null
         * @param il jentry data for get name and other things
         * @throws java.io.IOException
         */
        public void onPath(Pkg_base thiz, int ix, BasicFile cur, Object il) throws IOException;

        /**
         * similat ro onPath, but this retrieves only final file objects
         *
         * @param thiz actual pkg class object, must be non null
         * @param ix current index
         * @param beff init offset
         * @param eff end offset
         * @param coff compressed offset
         * @param ixdata index entry data
         * @throws java.io.IOException
         */
        public void onFile(Pkg_base thiz, int ix, long beff, long eff, long coff, IndexHelper.IEntry ixdata) throws IOException;

        /**
         * used on last pass for close or flush streams
         *
         * @param thiz
         * @throws java.io.IOException
         */
        public void onPost(Pkg_base thiz) throws IOException;

    }

    protected static final byte EXEC_WRITABLE = 1;
    protected static final byte EXEC_FETCH_FIRST = 2;
    protected static final byte EXEC_USEVDIR = 4;

    /**
     * enhanced fetch pkgs
     *
     * @param thiz
     * @param options EXEC_WRITABLE,EXEC_FETCH_FIRST,EXEC_USEVDIR
     * @param itis
     * @param sels
     * @param sz1
     * @param path
     * @param p2
     * @param sz2
     * @throws IOException
     */
    protected static void execute(Pkg_base thiz,
            final int options, final Eter itis,
            final int[] sels, final int sz1,
            final int[] path, int p2, final int sz2
    ) throws IOException
    {
        final boolean useVDir = (options & EXEC_USEVDIR) != 0;
        final boolean isWritable = (options & EXEC_WRITABLE) != 0;
        final boolean fetchFirst = (options & EXEC_FETCH_FIRST) != 0;
        if (thiz == null) {
            throw new NullPointerException("Cant go with null data");
        }
        if (thiz instanceof Pkg_box) {
            if (isWritable && ((Pkg_box) thiz).isCompressed() && !((Pkg_box) thiz).isMakeable()) {
                throw new IOException("Insertion not supported in Compressed TYPES=" + thiz.getSType());
            }
            ((Pkg_box) thiz).setCmpData();
        }
        int tindex;
        IndexHelper.VEntry lets = null;
        BasicFile fb = null;
        try {
            if (thiz.jindex == null) {
                throw new RuntimeException("Pkg_base index Null");
            }
            if (thiz.count < 1 || thiz.jindex.length < 1) {
                throw new RuntimeException("Pkg_base empty index");
            }
            lets = useVDir ? thiz.jindex.vdir : null;//fix path issue
            while (p2 < sz2) {//fetch pkg_base infos
                tindex = -1;
                if (useVDir && lets != null) {
                    if (lets.length < 1) {
                        throw new RuntimeException("Empty treeNode");
                    }
                    Object it = lets.get(path[p2]);
                    if (!(it instanceof Integer)) {
                        if (fetchFirst) {
                            itis.onPath(null, path[p2], null, it);
                        }
                        lets = (IndexHelper.VEntry) it;
                        p2++;
                        continue;
                    }
                    tindex = (Integer) it;
                    lets = null;
                }
                if (tindex < 0) {
                    tindex = useVDir && lets != null ? (int) (Integer) lets.get(path[p2])//fix error when load index path
                            : path[p2];
                }
                if (thiz.offsets instanceof long[][]) {
                    long off_siz_real[][] = (long[][]) thiz.offsets;
                    fb = thiz.getEnt(
                            tindex,
                            thiz.jindex.get(tindex),
                            off_siz_real[tindex][0],
                            off_siz_real[tindex][1],
                            off_siz_real[0].length > 2 ? off_siz_real[tindex][2] : -1
                    );
                }
                else if (thiz.offsets instanceof long[]) {
                    long index[] = (long[]) thiz.offsets;
                    fb = thiz.getEnt(
                            tindex,
                            thiz.jindex.get(tindex),
                            index[tindex],
                            index[tindex + 1],
                            -1
                    );
                }
                else {
                    throw new RuntimeException("Object offsets not valid " + thiz.offsets);
                }
                if (fb instanceof Pkg_base) {
                    if (fetchFirst) {
                        itis.onPath(thiz, tindex, fb, thiz.jindex.get(tindex));
                    }
                    thiz = (Pkg_base) fb;
                    lets = useVDir ? thiz.jindex.vdir : null;
                    p2++;
                    if (thiz instanceof Pkg_box) {
                        if (isWritable && ((Pkg_box) thiz).isCompressed() && !((Pkg_box) thiz).isMakeable()) {
                            throw new IOException("Insertion not supported in Compressed TYPES=" + thiz.getSType());
                        }
                        ((Pkg_box) thiz).setCmpData();
                    }
                    if (thiz.jindex == null) {
                        throw new RuntimeException("Pkg_base index Null");
                    }
                    if (thiz.count < 1 || thiz.jindex.length < 1) {
                        throw new RuntimeException("Pkg_base empty index");
                    }
                }
                else {
                    throw new RuntimeException("Object " + fb + " not is an Pkg_base");
                }
            }
            final boolean cc = thiz.offsets instanceof long[][];
            if (!cc && !(thiz.offsets instanceof long[])) {
                throw new RuntimeException("Object offsets not valid " + thiz + " " + thiz.offsets);
            }
            final long[] a1 = cc ? null : (long[]) thiz.offsets;
            final long[][] a2 = cc ? (long[][]) thiz.offsets : null;
            ArrayList<IndexHelper.VEntry> scope = new ArrayList();
            for (int e = 0; e < sz1; e++) {
                tindex = sels[e];
                pit:
                if (useVDir && lets != null) {
                    if (lets.get(tindex) instanceof Integer) {
                        tindex = (Integer) lets.get(tindex);
                        break pit;
                    }
                    scope.clear();
                    itis.onPath(null, tindex, null, lets.get(tindex));
                    scope.add((IndexHelper.VEntry) lets.get(tindex));
                    ((IndexHelper.VEntry) lets.get(tindex)).pos = 0;
                    while (scope.size() > 0) {
                        IndexHelper.VEntry me = scope.get(scope.size() - 1);
                        Object el = me.next();
                        if (el == null) {
                            scope.remove(scope.size() - 1);
                            itis.onPath(null, -1, null, null);
                        }
                        else if (el instanceof Integer) {
                            tindex = (Integer) el;
                            itis.onFile(thiz, tindex,
                                    cc ? a2[tindex][0] : a1[tindex],
                                    cc ? a2[tindex][1] : a1[tindex + 1],
                                    cc && a2[0].length > 2 ? a2[tindex][2] : -1,
                                    thiz.jindex.get(tindex));
                        }
                        else {
                            itis.onPath(null, me.pos - 1, null, el);
                            me = (IndexHelper.VEntry) el;
                            scope.add(me);
                            me.pos = 0;
                        }
                    }
                    continue;
                }
                itis.onFile(thiz, tindex,
                        cc ? a2[tindex][0] : a1[tindex],
                        cc ? a2[tindex][1] : a1[tindex + 1],
                        cc && a2[0].length > 2 ? a2[tindex][2] : -1,
                        thiz.jindex.get(tindex));
            }
        }
        finally {
            itis.onPost(thiz);
            if (fb != null) {
                IOSys.fclose(fb.fi);
            }
            while (thiz != null) {
                thiz.endFile();
                IOSys.fclose(thiz.fi);
                thiz = (Pkg_base) thiz.getAbsParent();
            }
        }
    }

    protected static void executeAll(Pkg_base thiz, int options, final int deep, final Eter itis) throws IOException
    {
        final boolean useVDir = (options & EXEC_USEVDIR) != 0;
        final boolean isWritable = (options & EXEC_WRITABLE) != 0;
        final boolean fetchFirst = (options & EXEC_FETCH_FIRST) != 0;
        if (thiz instanceof Pkg_box) {
            ((Pkg_box) thiz).setCmpData();
        }
        int tindex = -1;
        BasicFile fb = null;
        final PrimitiveList.ListObject<Pkg_base> dunk = PrimitiveList.newFrom(Pkg_base.class);
        final PrimitiveList.ListObject<IndexHelper.VEntry> bunk = useVDir ? (PrimitiveList.ListObject) PrimitiveList.newFrom(IndexHelper.VEntry.class) : null;
        thiz.jindex.pos = 0;
        if (useVDir && thiz.jindex.vdir != null) {
            thiz.jindex.vdir.pos = 0;
            bunk.addO(thiz.jindex.vdir);
        }
        dunk.addO(thiz);
        try {
            while (true) {//fetch pkg_base infos
                thiz = dunk.last();
                if (thiz == null) {
                    break;
                }
                tindex = 0;
                vdir:
                {
                    fdir:
                    if (useVDir) {
                        IndexHelper.VEntry lax = bunk.last();
                        if (lax == null) {
                            break fdir;
                        }
                        Object cru = lax.next();
                        if (cru == null) {
                            if (lax == thiz.jindex.vdir) {
                                dunk.popLast();//pop last file dir
                            }
                            bunk.popLast();//pop vdir
                            itis.onPath(null, -1, null, null);
                            continue;
                        }
                        if (cru instanceof Integer) {
                            //do files
                            tindex = (Integer) cru;
                            break vdir;
                        }
                        itis.onPath(null, lax.pos - 1, null, cru);
                        bunk.addO(cru);
                        ((IndexHelper.VEntry) cru).pos = 0;
                        continue;
                    }
                    if (thiz.jindex.pos >= thiz.count) {
                        dunk.popLast();//pop last file dir
                        if (useVDir) {
                            bunk.popLast();//pop null vdir
                        }
                        itis.onPath(null, -1, null, null);
                        continue;
                    }
                    tindex = thiz.jindex.pos++;
                }
                final IndexHelper.IEntry et = thiz.jindex.get(tindex);
                final boolean cc = thiz.offsets instanceof long[][];
                if (!cc && !(thiz.offsets instanceof long[])) {
                    throw new RuntimeException("Object offsets not valid " + thiz + " " + thiz.offsets);
                }
                final long[] a1 = cc ? null : (long[]) thiz.offsets;
                final long[][] a2 = cc ? (long[][]) thiz.offsets : null;
                if (IndexHelper.isCollapse(et)) {
                    fb = thiz.getEnt(
                            tindex,
                            et,
                            cc ? a2[tindex][0] : a1[tindex],
                            cc ? a2[tindex][1] : a1[tindex + 1],
                            cc && a2[0].length > 2 ? a2[tindex][2] : -1
                    );
                    if (fb instanceof Pkg_base) {
                        itis.onPath(thiz, tindex, fb, et);
                        thiz = (Pkg_base) fb;
                        if (thiz instanceof Pkg_box) {
                            ((Pkg_box) thiz).setCmpData();
                        }
                        thiz.setJData(et);
                        dunk.addO(thiz);
                        thiz.jindex.pos = 0;
                        if (useVDir) {
                            if (thiz.jindex.vdir == null) {
                                bunk.addO(null);
                            }
                            else {
                                bunk.addO(thiz.jindex.vdir);
                                thiz.jindex.vdir.pos = 0;
                            }
                        }
                        continue;
                    }
                    else if (fb != null) {
                        fb.endFile();
                    }
                }
                itis.onFile(thiz, tindex,
                        cc ? a2[tindex][0] : a1[tindex],
                        cc ? a2[tindex][1] : a1[tindex + 1],
                        cc && a2[0].length > 2 ? a2[tindex][2] : -1,
                        thiz.jindex.get(tindex));
                thiz.updateProgress(tindex + 1, thiz.count, false);
            }
        }
        finally {
            itis.onPost(thiz);
            if (fb != null) {
                IOSys.fclose(fb.fi);
            }
            while (thiz != null) {
                thiz.endFile();
                IOSys.fclose(thiz.fi);
                thiz = (Pkg_base) thiz.getAbsParent();
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="multi export">
    public final void export(String p, boolean useVdir, int[] sels, int[] path, int level, boolean raw) throws IOException
    {
        export(p, useVdir, sels, sels.length, path, 0, level, raw);
    }

    /**
     *
     * @param p ouput write
     * @param useVdir
     * @param sels selected files
     * @param sz1 size of selecction
     * @param path ruta del archivo
     * @param p2 posicion actual
     * @param sz2 tamaño de la ruta
     * @param raw export as raw
     * @throws java.io.IOException
     */
    protected void export(final String p, boolean useVdir, int[] sels, int sz1, int[] path, int p2, int sz2, final boolean raw) throws IOException
    {
        execute(this, useVdir ? EXEC_USEVDIR : 0, new Eter() {
            File cuts = new File(p);

            @Override
            public void onPath(Pkg_base thiz, int ix, BasicFile cur, Object il) throws IOException
            {
                if (ix == -1) {
                    cuts = cuts.getParentFile();
                    return;
                }
                if (cur != null) {
                    cuts = new File(cuts, BasicFile.fileFormat("", 1 + ix, IExNameProvider.def.getDetail(getCType(), (IndexHelper.IEntry) il, ix), cur.getSType()));
                }
                else {
                    cuts = new File(cuts, String.format("%04X %s", 1 + ix, ((IndexHelper.VEntry) il).desc));
                }
                IOSys.mkdir(cuts);
            }

            @Override
            public void onFile(Pkg_base thiz, int ix, long beff, long eff, long coff, IndexHelper.IEntry ixdata) throws IOException
            {
                final BasicFile cur = thiz.getEnt(ix, ixdata, beff, eff, coff);
                try {
                    cur.export(BasicFile.fileFormat(cuts.getPath(), 1 + ix, IExNameProvider.def.getDetail(getCType(), ixdata, ix), cur.getSType()), raw);
                }
                finally {
                    cur.endFile();
                }
            }

            @Override
            public void onPost(Pkg_base thiz) throws IOException
            {
            }
        }, sels, sz1, path, p2, sz2);
    }

    //</editor-fold>
    public final BasicFile[] readRawFile(boolean useVdir, int[] sels, int[] path, int level) throws IOException
    {
        final PrimitiveList.ListObject<BasicFile> eek = PrimitiveList.newFrom(BasicFile.class);
        execute(this, useVdir ? EXEC_USEVDIR : 0, new Eter() {
            File cuts = new File("");

            @Override
            public void onPath(Pkg_base thiz, int ix, BasicFile cur, Object il) throws IOException
            {
                if (ix == -1) {
                    cuts = cuts.getParentFile();
                    return;
                }
                if (cur != null) {
                    cuts = new File(cuts, BasicFile.fileFormat("", 1 + ix, IExNameProvider.def.getDetail(getCType(), (IndexHelper.IEntry) il, ix), cur.getSType()));
                }
                else {
                    cuts = new File(cuts, String.format("%04X %s", 1 + ix, ((IndexHelper.VEntry) il).desc));
                }
                IOSys.mkdir(cuts);
            }

            @Override
            public void onFile(Pkg_base thiz, int ix, long beff, long eff, long coff, IndexHelper.IEntry ixdata) throws IOException
            {
                if (thiz instanceof IGameDirectory) {
                    UNK_BIN bin = new UNK_BIN(null);//unlinked data
                    final BasicFile cur = thiz.readFile(ix);
                    bin.setIO(cur.fi);
                    bin.beginOffset = cur.beginOffset;
                    bin.endOffset = cur.endOffset;
                    eek.addO(bin);
                    setNullIO(cur, false);
                    cur.endFile();
                    return;
                }
                if (thiz instanceof Pkg_box) {
                    UNK_BIN bin;
//                    if (((Pkg_box) thiz).IS_CRYPT_RAND) {
//                        bin=(UNK_BIN) thiz.readFile(ix);
//                    }
//                    else
                    {
                        bin = new UNK_BIN(null);//unlinked data
                        final BasicFile cur = ((Pkg_box) thiz).readFile(beff, eff, coff, null);
                        bin.setIO(((Pkg_box) thiz).getIOb());
                        bin.beginOffset = cur.beginOffset;
                        bin.endOffset = cur.endOffset;
                    }
                    eek.addO(bin);
                    if (((Pkg_box) thiz).isDefaultOrCompressed()) {
                        return;//fix file close issue
                    }
                }
                else {
                    eek.addO(thiz.readFile(beff, eff, coff));
                }
                setNullIO(thiz, false);
            }

            @Override
            public void onPost(Pkg_base thiz) throws IOException
            {
            }

        }, sels, sels.length, path, 0, level);
        return (BasicFile[]) eek.toArray(true);
    }

    public final void printOffsets(final TABPrintStream p, boolean useVdir) throws IOException
    {
        executeAll(this, useVdir ? EXEC_USEVDIR : 0, 0, new Eter() {
            @Override
            public void onPath(Pkg_base thiz, int ix, BasicFile cur, Object il) throws IOException
            {
                if (ix == -1) {
                    p.addTAB(-1);
                    return;
                }
                if (cur != null) {
                    if (IndexHelper.isNamed((IndexHelper.IEntry) il)) {
                        p.printfln("= %s", IndexHelper.getName((IndexHelper.IEntry) il));
                    }
                    cur.printOffset(p, ix + 1, cur.getSType(), DEFAULT_BYTES, 1);
                }
                else {
                    p.printfln("%04d> %s", ix + 1, ((IndexHelper.VEntry) il).desc);
                }
                p.addTAB(1);
            }

            @Override
            public void onFile(Pkg_base thiz, int ix, long beff, long eff, long coff, IndexHelper.IEntry ixdata) throws IOException
            {
                if (IndexHelper.isNamed(ixdata)) {
                    p.printfln("= %s", IndexHelper.getName(ixdata));
                }
                final BasicFile cur = thiz.getEnt(ix, ixdata, beff, eff, coff);
                if (cur == null) {
                    p.p().add(ix + 1)
                            .add(getSType(IndexHelper.getCode(ixdata), 4))
                            .add(beff)
                            .add(eff);
                    p.printfln("%04X %4s %010X %010X | <Undefined data bytes>");
                    return;
                }
                try {
                    cur.printOffset(p, ix + 1, cur.getSType(), DEFAULT_BYTES, 1);
                }
                finally {
                    cur.endFile();
                }
            }

            @Override
            public void onPost(Pkg_base thiz) throws IOException
            {
                p.flush();
            }
        });
    }

    public final BasicFile getEnt(final int index, final IndexHelper.IEntry el,
            final long a, final long b, final long c) throws IOException
    {
        return getEnt(false, index, el, a, b, c);
    }

    public BasicFile getEnt(boolean force, final int index, final IndexHelper.IEntry el,
            final long a, final long b, final long c) throws IOException
    {
        return null;
    }

    public BasicFile loadFile(int i) throws IOException
    {
        if (jindex == null) {
            if (count <= 0) {
                return null;
            }
            throw new RuntimeException("Index not loaded");
        }
        if (jindex.length < 1) {
            return null;
        }
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
            return getEnt(true, i, jindex.get(i), off_siz_real[i][0],
                    off_siz_real[i][1],
                    off_siz_real[0].length > 2 ? off_siz_real[i][2] : -1);
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
            return getEnt(true, i, jindex.get(i), index[i],
                    index[i + 1],
                    -1);
        }
        return null;
    }

    protected final static void setNullIO(BasicFile p, boolean abs)
    {
        while (p != null) {
            p.setIO(null);
            p = abs ? p.getAbsParent() : p.parent;
        }
    }

    public BasicFile readFile(int i) throws IOException
    {
        if (i <= -1 || i >= count) {
            return null;
        }
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
            return readFile(
                    off_siz_real[i][0],
                    off_siz_real[i][1],
                    off_siz_real[0].length > 2 ? off_siz_real[i][2] : -1
            );
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
            return readFile(
                    index[i],
                    index[i + 1],
                    -1
            );
        }
        else {
            throw new RuntimeException("Object offsets not valid " + offsets);
        }
    }

    public BasicFile readFile(final long a, final long b, final long c) throws IOException
    {
        UNK_BIN unk = new UNK_BIN(this);
        unk.setIO(fi);
        unk.isValid(this, a, b, c);
        return unk;
    }

    public long getPosition(int idx)
    {
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
            return off_siz_real[idx][0];
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
            return index[idx];
        }
        else {
            throw new RuntimeException("Object offsets not valid " + offsets);
        }
    }

    public final long getNextPosition(int idx)
    {
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
            return getNextPosition(off_siz_real[idx][0], idx);
        }
        if (offsets instanceof long[]) {
            long off_siz_real[] = (long[]) this.offsets;
            return getNextPosition(off_siz_real[idx], idx);
        }
        throw new RuntimeException("Object offsets not valid " + offsets);
    }

    public final long getNextPosition(long p, int idx)
    {
        long lis;
        try {
            lis = length();
        }
        catch (IOException ex) {
            return -1;
        }
        if (offsets instanceof long[][]) {
            long[][] offsets = (long[][]) this.offsets;
            for (int i = 0; i < count; i++) {
                if (idx != i && p <= offsets[i][0] && lis > offsets[i][0]) {
                    lis = offsets[i][0];
                }
            }
            return lis;
        }
        if (offsets instanceof long[]) {
            long off_siz_real[] = (long[]) this.offsets;
            for (int i = 0; i < count; i++) {
                if (idx != i && p <= off_siz_real[i] && lis > off_siz_real[i]) {
                    lis = off_siz_real[i];
                }
            }
            return lis;
        }
        return -1;

    }

    protected long genMaxID(int ac) throws IOException
    {
        return -1;
    }

    public void writeIDX(RndAccess w, int index, long newSize, long newCmpSize) throws IOException
    {

    }

    public long genMax(long ac, int idx)
    {
        return ac;
    }

    public long sizeOf(int in)
    {
        if (in < 0 || in >= count) {
            return -1;
        }
//        System.out.println(getClass().getName());
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
//            System.out.printf("SZ1 %10s %10s\n", Long.toHexString(off_siz_real[in][0]), Long.toHexString(off_siz_real[in][1]));
            return off_siz_real[in][1] - off_siz_real[in][0];
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
//            System.out.printf("SZ2 %10s %10s\n", Long.toHexString(index[in]), Long.toHexString(index[in + 1]));
            return index[in + 1] - index[in];
        }
        else {
            throw new RuntimeException("Object offsets not valid " + offsets);
        }
    }

}
