/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import com.DeltaSKR.IO.BytePointer;
import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.IO.interfce.SeekByteArray;
import com.DeltaSKR.IO.interfce.WriteSeek;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import main_app.IExNameProvider;
import main_app.IndexHelper;
import parsers.pkg.IGameDirectory;
import parsers.pkg.IISO_Root;

/**
 *
 * @author ARMAX
 */
public abstract class Pkg_box extends Pkg_base {

    protected volatile Object parsers[];
    protected static final BytePointer po = new BytePointer(0x800);

    @Override
    protected int type(boolean noEmpty)
    {
        return isCompressed() ? (noEmpty ? TYPE_ZIP : TYPE_EZIP) : (noEmpty ? TYPE_DIR : TYPE_EDIR);
    }

    public Iterable<BasicFile> forParsers()
    {
        return new Iterable<BasicFile>() {
            @Override
            public Iterator<BasicFile> iterator()
            {
                return new Iterator<BasicFile>() {
                    int u = 0;

                    @Override
                    public boolean hasNext()
                    {
                        return u < parsers.length;
                    }

                    @Override
                    public BasicFile next()
                    {
                        return getParser(u++);
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                };
            }
        };
    }

    public void writeCopy(Pkg_box src, WriteSeek outa, boolean names) throws IOException
    {
        throw new RuntimeException("Error unsupported WriteCopy yet");
    }

    public final static class Flags {

        /**
         * support names
         */
        public static final byte NAME = 1;
        /**
         * is compressed
         */
        public static final byte CMPR = 2;
        /**
         * need CSize
         */
        public static final byte NCSZ = 4;
        /**
         * is Default System
         */
        public static final byte DEFS = 8;
        /**
         * support remake index
         */
        public static final byte RIDX = 0x10;
        /**
         * embed offsets
         */
        public static final byte SVOF = 0x20;

        public static final byte ABS_OFF = 0x40;
        /**
         * use default unk
         */
        public static final byte DUNK = (byte) 0x80;

    }

    private final byte flags;

    public final boolean supportNames()
    {
        return (flags & Flags.NAME) != 0;
    }

    public final boolean hasSaveOffsets()
    {
        return (flags & Flags.SVOF) != 0;
    }

    public final boolean isCompressed()
    {
        return (flags & Flags.CMPR) != 0;
    }

    public final boolean needCSize()
    {
        return (flags & Flags.NCSZ) != 0;
    }

    public final boolean isDefaultOrCompressed()
    {
        return (flags & (Flags.DEFS | Flags.CMPR)) != 0;
    }

    public final boolean isDefaultSystem()
    {
        return (flags & Flags.DEFS) != 0;
    }

    public final boolean canReIndex()
    {
        return (flags & Flags.RIDX) != 0;
    }

    public final boolean useAbsOffsets()
    {
        return (flags & Flags.ABS_OFF) != 0;
    }

    public final boolean useAbsOffsetsB()
    {
        return (flags & (Flags.ABS_OFF | Flags.CMPR)) != 0;
    }

    public final boolean useAbsOffsetsC()
    {
        return (flags & (Flags.ABS_OFF | Flags.CMPR | Flags.DEFS)) != 0;
    }

    protected boolean isRepDetailed = true;
    protected boolean useAsRaw = false;

    private boolean isRefsLoaded = false;

    private final SeekByteArray byt;//used by compressed pack

    protected BasicFile unk;

    public BasicFile getUnk()
    {
        return unk;
    }

    protected Pkg_box(int[] types, int[] codecs, BasicFile parent, int flags, Object... parsers)
    {
        super(types, codecs, parent);
        this.parsers = parsers;
        this.flags = (byte) (flags & 0x7F);
        if ((flags & Flags.DUNK) != 0) {
            unk = new UNK_BIN(useAbsOffsetsB() ? null : this);
        }
        byt = isCompressed() ? new SeekByteArray(0) : null;
    }

    /**
     * this is method for initialize parser objects
     *
     * @deprecated please dont use this
     */
    public final void initializeParsers()
    {
        if (!isRefsLoaded) {
            for (int i = 0; i < parsers.length; i++) {
                if (!(parsers[i] instanceof BasicFile)) {
                    parsers[i] = newParser(parsers[i], this);
                }
            }
            isRefsLoaded = true;
        }
    }
//    private static final Class params[] = {BasicFile.class};

    private static Object newParser(Object c, Pkg_box parent)
    {
        if ((c instanceof Class)) {
            System.gc();
            try {
                if (Modifier.isStatic(((Class) c).getModifiers()) || !((Class) c).isMemberClass()) {//fix  error when initalize  dynamic classes
                    c = ((Class) c).getConstructor(new Class[]{BasicFile.class}).newInstance(new Object[]{parent});
                }
                else {
//                    CONSOLE.println(((Class) c).getName());
//                    Constructor ee = ((Class) c).getConstructors()[0];
                    c = ((Class) c).getConstructor(new Class[]{parent.getClass(), BasicFile.class}).newInstance(new Object[]{parent, parent});
                }
            }
            catch (Exception ex) {
                ex.printStackTrace(CONSOLE.getPo());
            }
        }
        else if (c instanceof Pkg_Adapter) {//pseudo class data
            c = ((Pkg_Adapter) c).newObject(parent);
        }
        if (!parent.isDefaultSystem()) {
            ((BasicFile) c).setIO(parent.fi);
        }
        return c;
    }

    public final <T> T getParser(Class<? extends T> s)
    {
        for (int i = 0; i < parsers.length; i++) {
            if (!(parsers[i] instanceof BasicFile)) {
                parsers[i] = newParser(parsers[i], this);
            }
            if (parsers[i].getClass() == s) {
                return (T) parsers[i];
            }
        }
        return null;
    }

    public final <T> T getParser(int i)
    {
        if (parsers[i] instanceof BasicFile) {
            return (T) parsers[i];
        }
        parsers[i] = newParser(parsers[i], this);
        return (T) parsers[i];
    }

    public final void setClsParsr(Class from, Class to)
    {
        if (isRefsLoaded) {
            throw new RuntimeException("Error  must be call before to initialize parsers");
        }
        for (int i = 0; i < parsers.length; i++) {
            if (parsers[i] == from) {
                parsers[i] = to;
            }
        }
    }

    public int parsersCount()
    {
        return parsers.length;
    }

    @Override
    protected abstract boolean isValid() throws IOException;

    public final void setIsRepDetailed(boolean isRepDetailed)
    {
        this.isRepDetailed = isRepDetailed;
    }

    public void setUseAsRaw(boolean useAsRaw)
    {
        this.useAsRaw = useAsRaw;
    }

    public final void setOnlyKnown(boolean onlyKnown)
    {
        this.onlyKnown = onlyKnown;
    }

    @Override
    public void setIO(RndAccess fi)
    {
        super.setIO(fi);
        if (!this.isCompressed()) {
            setIOb(fi);
        }
    }

    protected final void setIOb(RndAccess fi)
    {
        if (unk != null) {
            unk.setIO(fi);
        }
        if (parsers != null) {
//            System.out.println("thus " + this);
            for (int i = 0; i < parsers.length; i++) {
                if (parsers[i] != null && parsers[i] instanceof BasicFile) {
                    ((BasicFile) parsers[i]).setIO(fi);
                }
            }
        }
    }

    //only used by compressor
    public byte[] getData()
    {
        return null;
    }

    public final RndAccess getIOb()
    {
        return isCompressed() ? byt : fi;
    }

    @Override
    protected final Object printOffsets(IndexHelper.IEntry entry) throws IOException
    {
        if (!isDefaultSystem()) {
            seek(0);
        }
        if (this instanceof IISO_Root) {
            this.jindex = entry;
        }
        countFounds = 0;
        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
        }

        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
//            if (this.getClass().getName().endsWith("WiiVol")) {
//                System.out.println("parsers.Pkg_box.printOffsets()");
//            }
//            try {
            for (int i = 0; i < count; i++) {
//                if (this instanceof IISO_Root) {
//                    System.out.println(getName(i));
//                }
                printOffsets(entry,
                        i,
                        off_siz_real[i][0],
                        off_siz_real[i][1],
                        off_siz_real[0].length > 2 ? off_siz_real[i][2] : -1
                );
            }
//            }
//            catch (Exception ex) {
//                ex.printStackTrace(CONSOLE.getPo());
//                System.out.println("parsers.Pkg_box.printOffsets()");
//            }
//            if (this.getClass().getName().endsWith("WiiVol")) {
//                System.out.println("parsers.Pkg_box.printOffsets()");
//            }
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
            for (int i = 0; i < count; i++) {
                printOffsets(entry,
                        i,
                        index[i],
                        index[i + 1],
                        -1
                );
            }
        }
        else {
            throw new RuntimeException("Object offsets not valid " + offsets);
        }
        if (this instanceof IISO_Root) {
            jindex = null;
        }
        return hasSaveOffsets() ? offsets : null;
    }

    protected void printOffsets(IndexHelper.IEntry entry, final int index,
            final long off, final long end, final long siz) throws IOException
    {
        try {
            String ac = supportNames() ? getName(index) : null;
            if (!isCompressed() && off == 0 && end - off < 4) {//fix recursive problems
                try {
                    unk.isValid(useAbsOffsetsB() ? null : this, 0, 0, 0);
                    unk.printOffset(entry, unk.getCType(), supportNames() && ac != null, false);
                    if (supportNames() && ac != null) {
                        IndexHelper.setName(entry.get(index), ac);
                    }
                }
                finally {
                    unk.endFile();
                }
                countFounds = index + 1;
                super.updateProgress();
                return;
            }
            boolean _unk = true;
            if (end - off >= 4) {
                BasicFile pr;
                for (int j = 0; j < parsers.length; j++) {
                    pr = getParser(j);
//                System.out.println(off + " " + end+" "+this.getClass());
                    try {
                        if (pr.isValid(useAbsOffsetsB() ? null : this, off, end, siz)) {
//                    if (pr instanceof CMPS) {
//                        printOut(pr);
//                    }
                            pr.printOffset(entry, pr.getCType(), supportNames() && ac != null, false);
                            _unk = false;
                            break;
                        }
                    }
                    catch (EOFException ex) {//ignorig this exception only for security
                    }
                    finally {
                        pr.endFile();
                    }
                }
            }
            if (_unk) {

//            try {
//                unk.isValid(useAbsOffsetsB() ? null : this, off, end, siz);
//                unk.printOffset(entry, unk.getCType(), supportNames() && ac != null, false);
//            }
//            catch (Exception ex) {
//                if (this.getClass().getName().endsWith("WiiVol")) {
//                    System.out.println("parsers.Pkg_box.printOffsets()");
//                }
//            }
                try {
//                if (this.getClass().getName().endsWith("WiiVol")) {
//                    System.out.println("parsers.Pkg_box.printOffsets()");
//                }
                    unk.isValid(useAbsOffsetsB() ? null : this, off, end, siz);
                    unk.printOffset(entry, unk.getCType(), supportNames() && ac != null, false);
                }
                finally {
                    unk.endFile();
                }
            }
            if (supportNames() && ac != null) {
                IndexHelper.setName(entry.get(index), ac);
            }
            countFounds = index + 1;
            super.updateProgress();
        }
        catch (Exception ex) {
            if (unk != null) {//error persistence
                if (entry.pos > index) {
                    entry.pos--;
                }
                entry.next(IndexHelper.newEntry(new IndexHelper.IEntry(),
                        0x21455252, false,
                        false, UNK_BIN.class, TYPE_EMP, null, null
                ));
                countFounds = index + 1;
                super.updateProgress();
//                CONSOLE.println("ERR found");
                return;
            }
            if (ex instanceof IOException) {
                throw (IOException) ex;
            }
        }
    }

    public BasicFile loadFile(String name) throws IOException
    {
        if (name == null) {
            return null;
        }
        if (jindex == null) {
            if (count <= 0) {
                return null;
            }
            throw new RuntimeException("Index not loaded");
        }
        IndexHelper.IEntry en = jindex;
        int sel = -1;
        for (int u = 0; u < en.length; u++) {
            if (name.equals(IndexHelper.getName(en.get(u)))) {
                sel = u;
                break;
            }
        }
        if (sel == -1) {
            throw new RuntimeException("File not found: " + name);
        }
        return loadFile(sel);
    }

    @Override
    public final BasicFile loadFile(int i) throws IOException
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
        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
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

    @Override
    protected void export(String f) throws IOException
    {
        if (jindex == null) {
            if (count <= 0) {
                return;
            }
            if (isCompressed()) {//compressed  packs
                if (TLen <= 0) {
                    return;
                }
                OutputStream o = IOSys.openOutput(new File(f));
                o.write(getData(), 0, (int) TLen);
                o.close();
                if (jindex == null) {
                    return;
                }
            }
            else {
                throw new RuntimeException("Index not loaded");
            }
        }
        if (jindex.length < 1) {
            return;
        }
        int validFiles = 0;
        for (int i = 0; i < count; i++) {
            if (!IndexHelper.isZero(jindex.get(i))) {
                ++validFiles;
            }
        }
        if (validFiles < 1) {
            return;
        }
        File fic = new File(f);
        if (!fic.exists()) {
            IOSys.mkdir(fic);
        }
        countFounds = 0;
        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
        }
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
            for (int i = 0; i < count; i++) {
                write(f, i, jindex.get(i),
                        off_siz_real[i][0],
                        off_siz_real[i][1],
                        off_siz_real[0].length > 2 ? off_siz_real[i][2] : -1
                );
            }
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
            for (int i = 0; i < count; i++) {
                write(f, i, jindex.get(i),
                        index[i],
                        index[i + 1],
                        -1
                );
            }
        }
        else {
            throw new RuntimeException("Object offsets not valid " + offsets);
        }
    }

    protected final void write(String f,
            final int index,
            final IndexHelper.IEntry el,
            final long a, final long b, final long c) throws IOException
    {
        if ((!isCompressed() && a == 0)//fix recursive problems
                || IndexHelper.isZero(el)) {
            return;
        }
        int code = IndexHelper.getCode(el);
        String hos = IExNameProvider.def.getDetail(getCType(), el, index);
        BasicFile pr;
        boolean _unk = true;
        if (IndexHelper.isKNOWN(el)) {
            for (int j = 0; j < parsers.length; j++) {
                pr = getParser(j);
                if (pr instanceof Pkg_box && pr.isValidCode(code)) {
                    ((Pkg_box) pr).setUseAsRaw(useAsRaw);
                    ((Pkg_box) pr).setOnlyKnown(onlyKnown);
                    pr.setJData(el);
                    if (pr.isValid(useAbsOffsetsC() ? null : this, a, b, c)) {
                        pr.export(BasicFile.fileFormat(f, 1 + index, hos, pr.getSType()));
                        _unk = false;
                        pr.endFile();
                    }
                    break;
                }
                else if (!useAsRaw && pr.isValidCode(code)) {
                    if (pr.isValid(useAbsOffsetsC() ? null : this, a, b, c)) {
                        pr.export(BasicFile.fileFormat(f, 1 + index, hos, pr.getSType()));
                        _unk = false;
                        pr.endFile();
                    }
                    break;
                }
            }
        }
        if (_unk) {
            if (!onlyKnown) {
                unk.isValidCode(code);
                unk.isValid(useAbsOffsetsC() ? null : this, a, b, -1);
                unk.export(BasicFile.fileFormat(f, 1 + index, hos, unk.getSType()));
            }
        }
        countFounds = index + 1;
        super.updateProgress();
    }

    public final void reset()
    {
        jindex = null;
        offsets = null;
        if (parsers != null) {
            for (int i = 0; i < parsers.length; i++) {
                if (parsers[i] instanceof Pkg_box) {
                    ((Pkg_box) parsers[i]).reset();
                    ((Pkg_box) parsers[i]).endFile();
                }
                else if (parsers[i] instanceof BasicFile) {
                    ((BasicFile) parsers[i]).endFile();
                }
            }
        }
    }

    @Override
    public int getByOffset(long off, boolean abs, int deep, int[] path, int p2, int sz2) throws IOException
    {
        //verify data bounds
        if ((abs && off >= beginOffset && off < endOffset) || (!abs && off >= 0 && off < length())) {
        }
        else {
            return -1;
        }
        if (jindex == null) {
            if (count <= 0) {
                return -1;
            }
            throw new RuntimeException("Index not loaded");
        }
        if (jindex.length < 1) {
            return -1;
        }

        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
        }
        BasicFile fb = null;
        try {
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
                    return ((Pkg_base) fb).getByOffset(off, abs, deep, path, p2 + 1, sz2);
                }
                else {
                    throw new RuntimeException("Object " + fb + " not is an Pkg_box");
                }
            }
            else if (offsets instanceof long[][]) {
                long off_siz_real[][] = (long[][]) this.offsets;
                for (int i = 0; i < count; i++) {
                    ace = i;
                    if ((abs && off >= beginOffset + off_siz_real[ace][0] && off < beginOffset + off_siz_real[ace][1]) || (!abs && off >= 0 && off < (off_siz_real[ace][1] - off_siz_real[ace][0]))) {
                    }
                    else {
                        continue;
                    }
                    System.out.printf("%08X - %08X %08X\n", off, beginOffset + off_siz_real[ace][0], beginOffset + off_siz_real[ace][1]);
                    if (deep > 1) {
                        fb = getEnt(
                                ace,
                                jindex.get(ace),
                                off_siz_real[ace][0],
                                off_siz_real[ace][1],
                                off_siz_real[0].length > 2 ? off_siz_real[ace][2] : -1
                        );
                        if (fb instanceof Pkg_box) {

                        }
                    }
                    return i;
                }
            }
            else if (offsets instanceof long[]) {
                long index[] = (long[]) this.offsets;
                for (int i = 0; i < count; i++) {
                    ace = i;
                    if ((!abs || off < beginOffset + index[ace]) || off >= beginOffset + index[ace + 1]
                            && ((abs || off < 0) || off >= (index[ace + 1] - index[ace]))) {
                        continue;
                    }
                    if (deep > 1) {
                        fb = getEnt(
                                ace,
                                jindex.get(ace),
                                index[ace],
                                index[ace + 1],
                                -1
                        );
                        if (fb instanceof Pkg_box) {

                        }
                    }
                    return i;
                }
            }
            else {
                throw new RuntimeException("Object offsets not valid " + this + " " + offsets);
            }
        }
        finally {
            if (isDefaultSystem()) {
                if (fb != null && fb.fi != null) {
                    fb.fi.close();
                }
            }
        }
        return -1;
    }

    /**
     * set especial compressed data
     *
     * @throws IOException
     */
    public final void setCmpData() throws IOException
    {
        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
        }
    }

    protected final void writeIDX89(RndAccess w, Object p, int[] sels, int sz1, int p2, int sz2, boolean raw) throws IOException
    {
        if (raw && sz2 - p2 == 0 && offsets instanceof long[][]) {
            long[][] offsets = (long[][]) this.offsets;
            long ic[];

            if (p instanceof Long) {//size
                ic = offsets[sels[0]];
                if (getNextPosition(ic[0], sels[0]) - ic[0] >= (Long) p) {
                    writeIDX(w, sels[0], (Long) p, 0);
                }
            }
            else if (p instanceof long[]) {//size+cmp
                ic = offsets[sels[0]];
                if (getNextPosition(ic[0], sels[0]) - ic[0] >= ((long[]) p)[0]) {
                    writeIDX(w, sels[0], ((long[]) p)[0], ((long[]) p)[1]);
                }
            }
            else if (p instanceof RndAccess) {
                ic = offsets[sels[0]];
                if (getNextPosition(ic[0], sels[0]) - ic[0] >= ((RndAccess) p).length()) {
                    writeIDX(w, sels[0], ((RndAccess) p).length(), 0);
                }
            }
            else {
                File cus = p instanceof File ? (File) p : new File((String) p);
                if (cus.exists()) {
                    if (cus.isFile()) {
                        ic = offsets[sels[0]];
                        if (getNextPosition(ic[0], sels[0]) - ic[0] >= cus.length()) {
                            writeIDX(w, sels[0], cus.length(), 0);
                        }
                    }
                    else if (cus.isDirectory()) {
                        throw new RuntimeException("Directory function dont implemented");
                    }
                }
            }
        }
    }

    //note this only accept flat paths without vdir
    @Override
    protected void importData(RndAccess w, Object p, int[] sels, int sz1, int[] path, int p2, int sz2, boolean raw) throws IOException
    {
        if (jindex == null) {
            if (count <= 0) {
                return;
            }
            throw new RuntimeException("Index not loaded");
        }
        if (jindex.length < 1) {
            return;
        }
        if (isCompressed() && !isMakeable()) {
            throw new IOException("Insertion not supported in Compressed TYPES=" + super.getSType());
        }
        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
        }
        BasicFile fb = null;
        try {
            int ace;
            final boolean cc = offsets instanceof long[][];
            if (!cc && !(offsets instanceof long[])) {
                throw new RuntimeException("Object offsets not valid " + this + " " + this.offsets);
            }
            final long[] a1 = cc ? null : (long[]) this.offsets;
            final long[][] a2 = cc ? (long[][]) this.offsets : null;
//            atler:
            if (p2 < sz2) {
                ace = path[p2];
                final long a = cc ? a2[ace][0] : a1[ace];
                final long b = cc ? a2[ace][1] : a1[ace + 1];
                final long c = cc && a2[0].length > 2 ? a2[ace][2] : -1;
                fb = getEnt(ace, jindex.get(ace), a, b, c);
                if (fb instanceof Pkg_base) {
//                    System.out.println("TYPE PASS: " + p2 + " " + fb.getSType());
                    if (this instanceof IGameDirectory) {
                        //fix import into game directory
                        if (((Pkg_box) fb).isCompressed()) {
//                            System.out.println("TYPE: " + fb.getSType());
                            if (!((Pkg_box) fb).isMakeable()) {
                                throw new IOException("Insertion not supported in Compressed TYPES=" + fb.getSType());
                            }
//                                System.out.println("ACCEPTED: " + fb.getSType());
                            SeekByteArray ota;
                            if (p2 + 1 < sz2) {//re-use current data
                                ota = new SeekByteArray(((Pkg_box) fb).getData(), 0, (int) ((Pkg_box) fb).TLen);
//                                    ota.setLength(0);
                            }
                            else {//make wiped data
                                ota = new SeekByteArray((int) ((Pkg_box) fb).TLen);
                            }
                            ((Pkg_box) fb).makeFree = true;
                            try {
                                ((Pkg_base) fb).importData(ota, p, sels, sz1, path, p2 + 1, sz2, raw);
                            }
                            finally {
                                ((Pkg_box) fb).makeFree = makeFree;
                            }
                            w = ((IGameDirectory) this).getFileRW(ace, true);//trimmed data
                            try {
                                ((Pkg_box) fb).makeFree = true;
                                ((Pkg_base) fb).endOffset = w.length();//some data lenght  error
                                fb.importData(w, ota, ace, false, true);//generating compressed data
                            }
                            finally {
                                ((Pkg_box) fb).makeFree = makeFree;
                                fb.endFile();
                                IOSys.fclose(w);
                            }
                        }
                        else {
                            w = ((IGameDirectory) this).getFileRW(ace);
                            try {
                                ((Pkg_base) fb).importData(w, p, sels, sz1, path, p2 + 1, sz2, raw);
                            }
                            finally {
                                IOSys.fclose(w);
                            }
                        }
                        return;
                    }
                    else if (fb instanceof Pkg_box) {
                        if (((Pkg_box) fb).isCompressed()) {
//                            System.out.println("TYPE: " + fb.getSType());
                            if (!((Pkg_box) fb).isMakeable()) {
                                throw new IOException("Insertion not supported in Compressed TYPES=" + fb.getSType());
                            }
//                                System.out.println("ACCEPTED: " + fb.getSType());
                            SeekByteArray ota;
                            if (p2 + 1 < sz2) {//re-use current data
                                ota = new SeekByteArray(((Pkg_box) fb).getData(), 0, (int) ((Pkg_box) fb).TLen);
//                                    ota.setLength(0);
                            }
                            else {//make wiped data
                                ota = new SeekByteArray((int) ((Pkg_box) fb).TLen);
                            }
                            ((Pkg_box) fb).makeFree = true;
                            try {
                                ((Pkg_base) fb).importData(ota, p, sels, sz1, path, p2 + 1, sz2, raw);
                            }
                            finally {
                                ((Pkg_box) fb).makeFree = makeFree;
                            }

                            BasicFile par = ((Pkg_box) fb).parent;
                            long ac = fb.length();
                            if (par instanceof Pkg_box) {//try rewrite  index
                                ((Pkg_box) fb).endOffset = ((Pkg_box) fb).beginOffset + ((Pkg_box) par).genMax(ac, path[p2]);
                            }
                            fb.importData(w, ota, ace, false, makeFree);//generating compressed data
                            long eso = w.seek() - ((Pkg_box) fb).beginOffset;
                            if (par instanceof Pkg_box) {//try rewrite  index
                                ((Pkg_box) par).writeIDX89(w,
                                        ((Pkg_box) par).needCSize() ? new long[]{eso, ota.length()}
                                        : eso,
                                        new int[]{path[p2]}, 1, 0, 0, true);
                            }
                            fb.endFile();
                            return;
                        }
                    }

                    BasicFile fis = fb;
                    boolean press = false;
                    if (((Pkg_base) fb).isMakeable()) {
                        do {
                            fis = fis.getAbsParent();
                            if (fis instanceof Pkg_box && ((Pkg_box) fis).isCompressed()) {
                                press = true;
                                break;
                            }
                        }
                        while (fis != null);
                    }
                    if (press) {
                        long es = w.seek();
                        w.seek(0);
                        w.unlock();
                        long old = w.seek();
                        w.lock(old + es);//lock sub position
                        ((Pkg_base) fb).makeFree = true;
                        int pis = sz2 > 0 ? path[0] : -1;
                        try {
                            ((Pkg_base) fb).makeData(w, p, sels, sz1, path, p2 + 1, sz2, raw);
                        }
                        finally {
                            if (sz2 > 0) {
                                path[0] = pis;
                            }
                            ((Pkg_base) fb).makeFree = false;
                            es += w.seek();
                            w.lock(old);//unlock
                            w.seek(es);
                        }
                    }
                    else {
                        ((Pkg_base) fb).importData(w, p, sels, sz1, path, p2 + 1, sz2, raw);
                    }
                    fb.endFile();
                }
                else {
                    if (fb != null) {
                        fb.endFile();
                    }
                    throw new RuntimeException("Object " + fb + " not is an Pkg_box");
                }
                return;
            }
            ace = sels[0];
            final long a = cc ? a2[ace][0] : a1[ace];
            final long b = cc ? a2[ace][1] : a1[ace + 1];
            final long c = cc && a2[0].length > 2 ? a2[ace][2] : -1;
            if (canReIndex()) {
                writeIDX89(w, p, sels, sz1, p2, sz2, raw);
            }
            if (p instanceof RndAccess) {
                fb = getEnt(true, ace, jindex.get(ace), a, b, c);
                //fix error of size
                fb.endOffset = fb.beginOffset + (b - a);
                try {
                    if (this instanceof IGameDirectory) {
                        //fix import into game directory
                        w = ((IGameDirectory) this).getFileRW(ace);
                        try {
                            fb.importData(w, (RndAccess) p, ace, raw, false);
                        }
                        finally {
                            IOSys.fclose(w);
                        }
                    }
                    else {
                        //try rewrite  index
                        long ac = fb.length();
                        fb.endOffset = fb.beginOffset + genMax(ac, ace);
                        fb.importData(w, (RndAccess) p, ace, raw, makeFree);
                    }
                }
                catch (Exception ex) {
                    System.err.println("Warn strem not writed");
                    ex.printStackTrace();
                }
                finally {
                    fb.endFile();
                }
            }
            else {
                File fic = IOSys.toFile(p);
                if (!fic.exists() || !fic.canRead()) {
                    throw new IOException("Unaccessible DIR/FILE " + fic.getPath());
                }
                if (fic.isDirectory()) {
                    throw new RuntimeException("Directory import dont implemented yet");
                }
                else if (fic.isFile()) {
                    fb = getEnt(true, ace, jindex.get(ace), a, b, c);
                    //fix error of size
                    //fixed fatal error caused by dab sum component  
                    //  fb.endOffset = fb.beginOffset + (b - c);//C is read size  and is incorrect
                    fb.endOffset = fb.beginOffset + (b - a);
                    try {
                        if (this instanceof IGameDirectory) {
                            //fix import into game directory
                            w = ((IGameDirectory) this).getFileRW(ace);
                            try {
                                fb.importData(w, fic.getPath(), ace, raw, false);
                            }
                            finally {
                                IOSys.fclose(w);
                            }
                        }
                        else {
                            //try rewrite  index
                            long ac = fb.length();
                            fb.endOffset = fb.beginOffset + genMax(ac, ace);
                            fb.importData(w, fic.getPath(), ace, raw, makeFree);
                        }
                    }
                    catch (Exception ex) {
                        System.err.println("Warn file not writed: " + fic.getPath());
                        ex.printStackTrace();
                    }
                    finally {
                        fb.endFile();
                    }
                }
            }
        }
        finally {
            if (isDefaultSystem()) {
                if (fb != null) {
                    IOSys.fclose(fb.fi);
                }
            }
        }
    }

    @Override
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
        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
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

    @Override
    public BasicFile getEnt(boolean force, final int index, final IndexHelper.IEntry el,
            final long a, final long b, final long c) throws IOException
    {
        countFounds = index;//fix back get index
        IndexHelper.IEntry ofs = el;
        int code = IndexHelper.getCode(el);
//        CONSOLE.println(getSType(code, 4) + " " + Integer.toHexString(el.get(1).toByte()));
        BasicFile pr;
        if (IndexHelper.isKNOWN(el)) {
            for (int j = 0; j < parsers.length; j++) {
                pr = getParser(j);
                if (pr.isValidCode(code)) {
                    if (pr instanceof Pkg_box) {
                        ((Pkg_box) pr).setOnlyKnown(onlyKnown);
                    }
                    pr.setJData(ofs);
                    if (pr.isValid(useAbsOffsetsC() ? null : this, a, b, c)) {
                        return pr;
                    }
                    else if (force && pr.forceable) {
                        pr.beginOffset = (useAbsOffsetsC() ? 0 : this.beginOffset);
                        pr.endOffset = pr.beginOffset + b;
                        pr.beginOffset += a;
                        pr.seek(0);
                        return pr;
                    }
                    else {
                        pr.endFile();
                    }
                    //throw new IOException("Error IDX is different to this file");
                    break;
                }
                pr.endFile();
            }
        }
        unk.isValidCode(code);
        unk.isValid(useAbsOffsetsC() ? null : this, a, b, c);
        return unk;
    }

    public BasicFile readFile(int code, int nim, int max) throws IOException
    {
        for (int i = nim + countFounds; i <= countFounds + max; i++) {
            if (i != countFounds && i > -1 && i < count) {
                if (!jindex.isNull(i) && code == IndexHelper.getCode(jindex.get(i))) {
                    return readFile(i);
                }
            }
        }
        return null;
    }

    @Override
    public BasicFile readFile(int i) throws IOException
    {
        return readFile(i, null);
    }

    public <T extends BasicFile> T readFile(int i, Object type) throws IOException
    {
        return (T) readFile(i, (Class) type);
    }

    public <T extends BasicFile> T readFile(int i, Class<T> type) throws IOException
    {
        if (isCompressed()) {
            byt.getPointer().data = getData();
            byt.setLength(TLen);
            byt.seek(0);
            setIOb(byt);
        }
        if (i <= -1 || i >= count) {
            return null;
        }
        countFounds = i;
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
            return (T) readFile(
                    off_siz_real[i][0],
                    off_siz_real[i][1],
                    off_siz_real[0].length > 2 ? off_siz_real[i][2] : -1,
                    type);
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
            return (T) readFile(
                    index[i],
                    index[i + 1],
                    -1, type
            );
        }
        else {
            throw new RuntimeException("Object offsets not valid " + offsets);
        }
    }

    public BasicFile readFile(final long a, final long b, final long c, Class type) throws IOException
    {
        if (type == null) {
            unk.isValid(useAbsOffsetsB() ? null : this, a, b, c);
            return unk;
        }
        BasicFile ps = (BasicFile) getParser(type);
        if (ps == null) {
            return null;
        }
        if (!ps.isValid(useAbsOffsetsB() ? null : this, a, b, c)) {
            return null;
        }
        return ps;
    }

    public String getName(int index) throws IOException
    {
        return null;
    }

    public boolean exists(int in)
    {
        return in >= 0 && in < count;
    }

    public long beginOf(int in)
    {
        if (in < 0 || in >= count) {
            return -1;
        }
        if (offsets instanceof long[][]) {
            long off_siz_real[][] = (long[][]) this.offsets;
            return beginOffset + off_siz_real[in][0];
        }
        else if (offsets instanceof long[]) {
            long index[] = (long[]) this.offsets;
            return beginOffset + index[in];
        }
        else {
            throw new RuntimeException("Object offsets not valid " + offsets);
        }
    }


    public int currentIndex()
    {
        return countFounds;
    }

}
