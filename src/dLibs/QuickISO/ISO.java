package dLibs.QuickISO;

import com.DeltaSKR.IO.BytePointer;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.IO.interfce.SeekByteArray;
import com.DeltaSKR.lang.DByteArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/*
QuickISO library 0.1
by Luigi Auriemma
e-mail: me@aluigi.org
web:    aluigi.org

Usage:
    quickiso_ctx_t ctx;
    quickiso_open(&ctx, "output.iso");

    quickiso_add_entry(&ctx, "file.txt", "this is a content", 17);
    quickiso_add_entry(&ctx, "sub_folder/file.txt", "this is a content", 17);

    quickiso_close(&ctx);
 */
public class ISO {

    static final int QUICKISO_MAXNAMELEN = (0xff - 0x22);

    static int quickiso_isfolder(int X)
    {
        return (X & 2);
    }

    public static class FileSize implements Qms_FileWriter {

        private final long sz;
        private final long pos;

        public FileSize(long pos, long sz)
        {
            this.pos = pos;
            this.sz = sz;
        }

        @Override
        public long size()
        {
            return sz;
        }

        @Override
        public boolean isInputStream()
        {
            return true;
        }

        @Override
        public InputStream createInput() throws IOException
        {
            return null;
        }

        @Override
        public void writeInput(OutputStream out) throws IOException
        {
        }

    }

    public static class FileItem implements Qms_FileWriter {

        File fi;

        public FileItem(File fi)
        {
            this.fi = fi;
        }

        @Override
        public long size()
        {
            return (int) fi.length();
        }

        @Override
        public boolean isInputStream()
        {
            return true;
        }

        @Override
        public InputStream createInput() throws IOException
        {
            return new FileInputStream(fi);
        }

        @Override
        public void writeInput(OutputStream out) throws IOException
        {
        }

    }

    public static interface Qms_FileWriter {

        long size();

        boolean isInputStream();

        InputStream createInput() throws IOException;

        void writeInput(OutputStream out) throws IOException;
    }

    public static class quickiso_entry_t {

        final BytePointer name = new BytePointer(QUICKISO_MAXNAMELEN + 1);
        long sector;
        long size;
        short id;
        byte flags;
        int childs;
        Qms_FileWriter file = null;
        quickiso_entry_t[] child = {};
        private int offTL;
        private int offTB;

        public void reset()
        {
            name.pointer = 0;
            memset(name, 0, name.size());
            sector = 0;
            size = 0;
            id = 0;
            flags = 0;
            childs = 0;
            for (int i = 0; i < child.length; i++) {
                child[i] = null;
            }
        }
    }

    private static final int QMS_BLOCK_SIZE = 0x22;

    public static class quickiso_ctx_t {

        public RndAccess fd;
        public final BytePointer buff = new BytePointer(2048);
        public final BytePointer root_record = new BytePointer(QMS_BLOCK_SIZE);
        public quickiso_entry_t root;
        public int virtAlign = 0x800;
        public short current_id;
        public long currentFilePosition;
        private OutputStream out;
        public DByteArray ram;
        public String Volume, System_ID, Volume_set_ID,
                Publisher, Date_preparer, Application,
                Copyright, Abstract, Bibliographic;

        public final void reset()
        {
            fd = null;
            buff.pointer = root_record.pointer = 0;
            memset(buff, 0, buff.size());
            memset(root_record, 0, root_record.size());
            root = null;
            current_id = 0;
            currentFilePosition = 0;
            out = null;
            ram = null;
            Volume = System_ID = Volume_set_ID
                    = Publisher = Date_preparer = Application
                    = Copyright = Abstract = Bibliographic = "";
        }
    }

    private static int strnicmp(BytePointer a, BytePointer b, int size)
    {
        int len1 = Math.min(a.strLen(), size);
        int len2 = Math.min(b.strLen(), size);
        int lim = Math.min(len1, len2);
        int k = 0;
        while (k < lim) {
            int c1 = a.get(k) & 255;
            int c2 = b.get(k) & 255;
            if (c1 != c2) {
                c1 = Character.toUpperCase(c1);
                c2 = Character.toUpperCase(c2);
                if (c1 != c2) {
                    c1 = Character.toLowerCase(c1);
                    c2 = Character.toLowerCase(c2);
                    if (c1 != c2) {
                        // No overflow because of numeric promotion
                        c1 = c1 - c2;
                        return c1 == 0 ? 0 : c1 < 0 ? -1 : 1;
                    }
                }
            }
            k++;
        }
        len1 = len1 - len2;
        return len1 == 0 ? 0 : len1 < 0 ? -1 : 1;
    }

    public static void strncpy(BytePointer dst, BytePointer src, int size)
    {
        int len1 = src.strLen();
        int len2 = dst.size();
        int lim = Math.min(Math.min(len1, len2), size);
        System.arraycopy(src.data, src.pointer, dst.data, dst.pointer, lim);
        if (dst.get(lim) != 0) {
            dst.set(lim, 0);
        }
    }

    public static void memcpy(BytePointer dst, BytePointer src, int size)
    {
        System.arraycopy(src.data, src.pointer, dst.data, dst.pointer, size);
    }

    public static void memset(BytePointer dst, int val, int size)
    {
        for (int i = 0; i < size; i++) {
            dst.set(i, val);
        }
    }

    public static int quickiso_putxx(BytePointer dst, int num, int bits, int endian)
    {
        if (dst == null) {
            return 0;
        }
        BytePointer p = dst.copy();
        if (endian <= 0) {
            p.add(num);
            if (bits >= 16) {
                p.add(num >> 8);
            }
            if (bits >= 32) {
                p.add(num >> 16);
                p.add(num >> 24);
            }
        }
        if (endian >= 0) {
            if (bits >= 32) {
                p.add(num >> 24);
                p.add(num >> 16);
            }
            if (bits >= 16) {
                p.add(num >> 8);
            }
            p.add(num);
        }
        return p.pointer - dst.pointer;
    }

    public static int quickiso_putss(BytePointer dst, String src, int size)
    {
        return quickiso_putss(dst, new BytePointer(src), size);
    }

    public static int quickiso_putss(BytePointer dst, BytePointer src, int size)
    {
        if (dst == null) {
            return 0;
        }
        if (src == null) {
            src = new BytePointer(0);
        }
        if (size < 0) {
            size = src.strLen();
        }
        strncpy(dst, src, size);
        return size;
    }

    static int sprintf(BytePointer p, String f, Object[] parrams)
    {
        BytePointer po = new BytePointer(String.format(f, parrams));
        int l = po.strLen();
        memcpy(po, p, l);
        return l;
    }

    public static int quickiso_puttime(BytePointer dst, int mode)
    {

        if (dst == null) {
            return 0;
        }
        BytePointer p = dst.copy();
        Calendar tmx = Calendar.getInstance();
        if (mode == 0) {
            p.pointer += sprintf(p, "%04d%02d%02d%02d%02d%02d%02d%c", new Object[]{tmx.get(Calendar.YEAR), tmx.get(Calendar.MONTH) + 1, tmx.get(Calendar.DATE),
                tmx.get(Calendar.HOUR), tmx.get(Calendar.MINUTE), tmx.get(Calendar.SECOND), 0,
                0}
            );
        }
        else {
            p.add(tmx.get(Calendar.YEAR) - 1900);
            p.add(tmx.get(Calendar.MONTH) + 1);
            p.add(tmx.get(Calendar.DATE));
            p.add(tmx.get(Calendar.HOUR));
            p.add(tmx.get(Calendar.MINUTE));
            p.add(tmx.get(Calendar.SECOND));
            p.add(0);
        }
        return p.pointer - dst.pointer;
    }

    public static int quickiso_getnamelen(BytePointer name)
    {
        int len;
        if (name == null) {
            name = new BytePointer(0);    // root
        }
        len = name.strLen();
        if (len == 0) {
            len = 1;       // root
        }
        if (len > QUICKISO_MAXNAMELEN) {
            len = QUICKISO_MAXNAMELEN;
        }
        return len;
    }

    public static int quickiso_padding(quickiso_ctx_t ctx, quickiso_entry_t file) throws IOException
    {
        if (ctx == null || ctx.out == null) {
            return -1;
        }
        int diff = (int) align((file.sector * 0x800 + file.size), ctx.virtAlign);
        if (diff != 0) {
            memset(ctx.buff, 0, Math.min(2048, diff));
            while (diff > 0) {
                ctx.out.write(ctx.buff.data, ctx.buff.pointer, diff < 2048 ? diff : 2048);
                diff -= 2048;
            }
        }
        return 0;
    }

    public static int quickiso_padding(quickiso_ctx_t ctx) throws IOException
    {
        int diff;

        if (ctx == null || ctx.fd == null) {
            return -1;
        }

        diff = (int) (ctx.fd.seek() % 2048);
        if (diff != 0) {
            diff = 2048 - diff;
            memset(ctx.buff, 0, diff);
            ctx.fd.write(ctx.buff.data, ctx.buff.pointer, diff);
        }
        return 0;
    }

    public static int quickiso_write_directory_raw(quickiso_ctx_t ctx, quickiso_entry_t entry, String name) throws IOException
    {
        return quickiso_write_directory_raw(ctx, entry, new BytePointer(name));
    }

    public static int quickiso_write_directory_raw(quickiso_ctx_t ctx, quickiso_entry_t entry, BytePointer name) throws IOException
    {
        int len;
        BytePointer p;

        if (ctx == null || ctx.fd == null) {
            return -1;
        }
        if (entry == null) {
            return -1;
        }
        // placeholder fixed by quickiso_fix_directories

        p = ctx.buff.copy();
        p.add(0);   // placeholder
        p.add(0);
        if (entry.flags == 0) {
            entry.sector = (int) (ctx.fd.seek() + p.pointer);
        }
        p.pointer += quickiso_putxx(p, (int) (entry.sector), 32, 0);
        p.pointer += quickiso_putxx(p, (int) entry.size, 32, 0);
        p.pointer += quickiso_puttime(p, 1);
        p.add(entry.flags);
        p.add(0);
        p.add(0);
        p.pointer += quickiso_putxx(p, 1, 16, 0);
        len = quickiso_getnamelen(name);
        p.add(len);
        p.pointer += quickiso_putss(p, name, len);
        if (((p.pointer - ctx.buff.pointer) & 1) != 0) {
            p.add(0);
        }
//        if (entry.flags == 0) {
//            memset(p, 0, 0xE);
//            p.pointer += 0xE;
//        }
        int ma = Math.max(QMS_BLOCK_SIZE, p.pointer - ctx.buff.pointer);
        if (ma > p.pointer) {
            memset(p, 0, ma - p.pointer);
        }
        ctx.buff.set(0, ma);
        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, ma);
        return 0;
    }

    public static int quickiso_write_directory(quickiso_ctx_t ctx, quickiso_entry_t[] entry) throws IOException
    {
//        for (quickiso_entry_t eta : entry) {
//            
//        }
        for (quickiso_entry_t eta : entry) {
            if (quickiso_isfolder(eta.flags) != 0) {
                quickiso_write_directory(ctx, eta.child);
            }
            quickiso_write_directory2(ctx, eta);
        }
        return 0;
    }

    public static int quickiso_write_directory2(quickiso_ctx_t ctx, quickiso_entry_t entry) throws IOException
    {
        int i;
        if (ctx == null || ctx.fd == null) {
            return -1;
        }
        if (entry == null) {
            return -1;
        }
        if (quickiso_isfolder(entry.flags) == 0) {
            return 0;
        }
        quickiso_entry_t[] child = entry.child;
        long backup_offset = ctx.fd.seek();
        entry.sector = (int) (backup_offset / (long) 2048);
        memset(ctx.buff, 0, QMS_BLOCK_SIZE);           // placeholder fixed by quickiso_fix_directories
        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, QMS_BLOCK_SIZE);  // quickiso_write_directory_raw(ctx, entry, "");
        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, QMS_BLOCK_SIZE);  // quickiso_write_directory_raw(ctx, parent, "\1");
        for (i = 0; i < entry.childs; i++) {
            quickiso_write_directory_raw(ctx, child[i], child[i].name);
        }
        quickiso_padding(ctx);
        entry.size = (int) (ctx.fd.seek() - backup_offset);
        return 0;
    }

    public static int quickiso_write_directory(quickiso_ctx_t ctx, quickiso_entry_t entry) throws IOException
    {
        quickiso_entry_t[] child;
        long backup_offset;
        int i;

        if (ctx == null || ctx.fd == null) {
            return -1;
        }
        if (entry == null) {
            return -1;
        }
        if (quickiso_isfolder(entry.flags) == 0) {
            return 0;
        }

        child = entry.child;
        for (i = 0; i < entry.childs; i++) {
            if (quickiso_isfolder(child[i].flags) != 0) {
                quickiso_write_directory(ctx, child[i]);
            }
        }
        backup_offset = ctx.fd.seek();
        entry.sector = (int) (backup_offset / (long) 2048);

        memset(ctx.buff, 0, QMS_BLOCK_SIZE);           // placeholder fixed by quickiso_fix_directories

        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, QMS_BLOCK_SIZE);  // quickiso_write_directory_raw(ctx, entry, "");
        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, QMS_BLOCK_SIZE);  // quickiso_write_directory_raw(ctx, parent, "\1");

        for (i = 0; i < entry.childs; i++) {
            quickiso_write_directory_raw(ctx, child[i], child[i].name);
        }

        quickiso_padding(ctx);
        entry.size = (int) (ctx.fd.seek() - backup_offset);

        return 0;
    }

    public static int quickiso_fix_directories(quickiso_ctx_t ctx, quickiso_entry_t entry, quickiso_entry_t parent) throws IOException
    {
        quickiso_entry_t[] child;
        int i;

        if (ctx == null || ctx.fd == null || entry == null) {
            return -1;
        }
        if (quickiso_isfolder(entry.flags) == 0) {
            return 0;
        }
        ctx.fd.seek(entry.offTL + 2);
        ctx.fd.write4le((int) entry.sector);
        ctx.fd.seek(entry.offTB + 2);
        ctx.fd.write4be((int) entry.sector);

        ctx.fd.seek(entry.sector * 2048);//    fseek(ctx.fd, (long)entry.sector * (long)2048, SEEK_SET);
        quickiso_write_directory_raw(ctx, entry, "");
        if (entry == ctx.root) {
            memcpy(ctx.root_record, ctx.buff, QMS_BLOCK_SIZE);
        }
        quickiso_write_directory_raw(ctx, parent, "\u0001");

        child = entry.child;

        for (i = 0; i < entry.childs; i++) {
            if (quickiso_isfolder(child[i].flags) != 0) {
                quickiso_fix_directories(ctx, child[i], entry);
            }
            else {
                fix_file(ctx, child[i]);
            }
        }
        return 0;
    }
    static BytePointer so = new BytePointer(8);

    public static void fix_file(quickiso_ctx_t ctx, quickiso_entry_t e) throws IOException
    {
        //fix file position
//        long aco = ctx.fd.getFilePointer();
        ctx.fd.seek(e.sector);
        if (e.file instanceof FileSize) {
            quickiso_putxx(so, (int) ((FileSize) e.file).pos, 32, 0);
        }
        else {
            e.sector = ctx.currentFilePosition / 2048;
            quickiso_putxx(so, (int) e.sector, 32, 0);
            ctx.currentFilePosition
                    = ctx.currentFilePosition + e.size
                    + align(ctx.currentFilePosition + e.size, ctx.virtAlign);
        }

        ctx.fd.write(so.data, so.pointer, 8);
//        ctx.fd.seek(aco);
    }

    static int quickiso_write_table_raw(quickiso_ctx_t ctx, quickiso_entry_t entry, short id, int endian) throws IOException
    {
        int len;
        BytePointer p;
        if (ctx == null || ctx.fd == null || entry == null) {
            return -1;
        }
        p = ctx.buff.copy();
        len = quickiso_getnamelen(entry.name);
        p.add(len);
        p.add(0);
        p.pointer += quickiso_putxx(p, (int) (entry.sector), 32, endian);
        p.pointer += quickiso_putxx(p, id, 16, endian);
        p.pointer += quickiso_putss(p, entry.name, len);
        if (((p.pointer - ctx.buff.pointer) & 1) != 0) {
            p.add(0);
        }
        if (endian < 0) {
            entry.offTL = (int) ctx.fd.seek();
        }
        else {
            entry.offTB = (int) ctx.fd.seek();
        }
        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, p.pointer - ctx.buff.pointer);
        return 0;
    }

    static long align(long size, final int align)
    {
        size = size % align;
        if (size <= 0) {
            return 0;
        }
        return align - size;
    }

    public static int quickiso_write_table(quickiso_ctx_t ctx, quickiso_entry_t entry, short id, int endian) throws IOException
    {
        quickiso_entry_t[] child;
        int i;

        if (ctx == null || ctx.fd == null) {
            return -1;
        }

        if (entry == null) {    // root
            quickiso_write_table_raw(ctx, ctx.root, (short) 1, endian);
            quickiso_write_table(ctx, ctx.root, (short) 1, endian);
            return 0;
        }

        child = entry.child;

        for (i = 0; i < entry.childs; i++) {
            if (quickiso_isfolder(child[i].flags) != 0) {
                quickiso_write_table_raw(ctx, child[i], entry.id, endian);
            }
        }

        for (i = 0; i < entry.childs; i++) {
            if (quickiso_isfolder(child[i].flags) != 0) {
                quickiso_write_table(ctx, child[i], entry.id, endian);
            }
        }

        return 0;
    }

    public static quickiso_entry_t quickiso_add_entry_raw(quickiso_ctx_t ctx, quickiso_entry_t parent, BytePointer name, int name_len, int flags, long size) throws IOException
    {
        quickiso_entry_t entry;
        quickiso_entry_t[] child;

        if (ctx == null || ctx.fd == null) {
            return null;
        }

        if (parent != null) {
            child = parent.child;
            parent.child = new quickiso_entry_t[parent.childs + 1];
            System.arraycopy(child, 0, parent.child, 0, parent.childs);

//            parent.child = real_realloc(parent.child, (parent.childs + 1) *);
//            if (parent.child == null) {
//                return null;
//            }
            child = parent.child;
            child[parent.childs] = new quickiso_entry_t();
            entry = child[parent.childs];
        }
        else {
            entry = new quickiso_entry_t();
        }
        if (entry == null) {
            return null;
        }
        entry.reset();
        if (name == null) {
            name = new BytePointer(0);
        }
        if (name_len < 0) {
            name_len = name.strLen();
        }
        if (name_len > QUICKISO_MAXNAMELEN) {
            name_len = QUICKISO_MAXNAMELEN;
        }
        strncpy(entry.name, name, name_len);
        entry.name.set(name_len, 0);
        if (quickiso_isfolder(flags) != 0) {
            entry.id = ++ctx.current_id;
        }
        else {
            //fix sequence of files
            entry.sector = ctx.currentFilePosition / 2048;// (int) (ctx.fd.getFilePointer() / 2048);
            entry.size = size;
            ctx.currentFilePosition = (ctx.currentFilePosition + size) + align(ctx.currentFilePosition + size, ctx.virtAlign);
        }
        entry.flags = (byte) flags;

        if (parent != null) {
            parent.childs++;
        }
        return entry;
    }

    public static int quickiso_free_entry(quickiso_entry_t entry)
    {
        quickiso_entry_t[] child;
        int i;

        if (entry == null) {
            return -1;
        }

        child = entry.child;
        if (child != null) {
            for (i = 0; i < entry.childs; i++) {
                if (child[i] != null) {
                    quickiso_free_entry(child[i]);
//                real_free(child[i]);
                    child[i] = null;
                }
            }
//        real_free(entry.child);
            entry.child = null;
        }
        entry.childs = 0;
        //real_free(entry); // NO!
        return 0;
    }

    // public API
    public int quickiso_add_entry(quickiso_ctx_t ctx, String name, String file) throws IOException
    {
        return quickiso_add_entry(ctx, new BytePointer(name), new FileItem(new File(file)));
    }

    public int quickiso_add_entry(quickiso_ctx_t ctx, String path, long pos, long len) throws IOException
    {
        return quickiso_add_entry(ctx, new BytePointer(path), new FileSize(pos, len));
    }

    public int quickiso_add_entry(quickiso_ctx_t ctx, BytePointer name, Qms_FileWriter file) throws IOException
    {
        quickiso_entry_t entry;
        quickiso_entry_t[] child;
        int i;
        BytePointer p, l;

        if (ctx == null || ctx.fd == null) {
            return -1;
        }

        if (ctx.root == null) {
            ctx.root = quickiso_add_entry_raw(ctx, null, new BytePointer(0), -1, 2, 0);
        }
        if (name == null) {
            return 0; // root
        }
        entry = ctx.root;
        l = name.copy();
        p = name.copy();
        for (; p.get() != 0; p.pointer = l.pointer + 1) {
            while (p.get() == 0 && ((p.get() == '/') || (p.get() == '\\'))) {
                p.pointer++;
            }
            if (p.get() == 0) {
                break;  // invalid
            }
            l = p.copy();
            while (l.get() != 0 && ((l.get() != '/') && (l.get() != '\\'))) {
                l.pointer++;
            }
            if (l.get() == 0) {
                break;  // this is a file
            }
            // this is a folder
            child = entry.child;
            for (i = 0; i < entry.childs;) {
                if ((child[i].name.strLen() == (l.pointer - p.pointer)) && strnicmp(child[i].name, p, l.pointer - p.pointer) == 0) {
//                    System.out.println("gotcha");
                    break;
                }
                i++;
            }
            if (i < entry.childs) {
                entry = child[i];
            }
            else {
                entry = quickiso_add_entry_raw(ctx, entry, p, l.pointer - p.pointer, 2, 0);
            }
        }

        if (p.get() != 0) {
            entry = quickiso_add_entry_raw(ctx, entry, p, l.pointer - p.pointer, 0, file == null ? 0 : file.size());
//            System.out.println("add " + new String(p.data, p.pointer, p.strLen(), "US-ASCII"));

            entry.file = file;
//            if (buff != null && size != 0) {
//                ctx.fd.write(buff.data, buff.pointer, size);
//                quickiso_padding(ctx);
//            }
        }

        return 0;
    }

    public int quickiso_open(quickiso_ctx_t ctx, OutputStream file) throws IOException
    {
        int i;

        if (ctx == null) {
            return -1;
        }
        ctx.reset();
        if (file == null) {
            return -1;
        }
        ctx.ram = new DByteArray();
        ctx.fd = new SeekByteArray(ctx.ram);
        if (ctx.fd == null) {
            return -1;
        }
        ctx.out = file;
        quickiso_add_entry(ctx, (BytePointer) null, (Qms_FileWriter) null);

        memset(ctx.buff, 0, 2048);
        for (i = 0; i < 16; i++) {
            ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);
        }

        // placeholders
        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);  // primary
        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);  // terminator
//        ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);  // padding

//        for (i = 0; i < 600; i++) {
//            ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);
//        }
        return 0;
    }

    public int quickiso_close(quickiso_ctx_t ctx) throws IOException
    {
        try {
            long sector1,
                    sector2,
                    sector3,
                    table_size;
            int i;
            BytePointer p;

            if (ctx == null || ctx.fd == null) {
                return -1;
            }

            sector1 = (int) (ctx.fd.seek() / (long) 2048);
            quickiso_write_table(ctx, null, (short) 0, -1);
            table_size = (int) (ctx.fd.seek() - ((long) sector1 * (long) 2048));
            quickiso_padding(ctx);

            sector2 = (int) (ctx.fd.seek() / (long) 2048);
            quickiso_write_table(ctx, null, (short) 0, 1);
            quickiso_padding(ctx);

            quickiso_write_directory(ctx, ctx.root);
            quickiso_padding(ctx);

            // necessary because . and .. don't have the correct sector and size
            ctx.currentFilePosition = ctx.fd.length();
//        System.out.println(Integer.toHexString(ctx.currentFilePosition / 2048));
            quickiso_fix_directories(ctx, ctx.root, ctx.root);

            sector3 = ctx.currentFilePosition / 0x800;// (int) (ctx.fd.seek() / (long) 2048);

            ctx.fd.seek(16 * 2048);//    fseek(ctx.fd,16 * 2048 , SEEK_SET);

            // Primary Volume Descriptor
            memset(ctx.buff, 0, 2048);
            p = ctx.buff.copy();
            p.add(1);
            p.pointer += quickiso_putss(p, "CD001", 5);
            p.add(1);
            p.add(0);
            p.pointer += quickiso_putss(p, ctx.System_ID, 32);
            p.pointer += quickiso_putss(p, ctx.Volume, 32);
            for (i = 0; i < 8; i++) {
                p.add(0);
            }
            p.pointer += quickiso_putxx(p, (int) sector3, 32, 0);
            for (i = 0; i < 32; i++) {
                p.add(0);
            }
            p.pointer += quickiso_putxx(p, 1, 16, 0);
            p.pointer += quickiso_putxx(p, 1, 16, 0);
            p.pointer += quickiso_putxx(p, 2048, 16, 0);
            p.pointer += quickiso_putxx(p, (int) table_size, 32, 0);
            p.pointer += quickiso_putxx(p, (int) sector1, 32, -1);
            p.pointer += quickiso_putxx(p, 0, 32, -1);
            p.pointer += quickiso_putxx(p, (int) sector2, 32, 1);
            p.pointer += quickiso_putxx(p, 0, 32, 1);
            memcpy(p, ctx.root_record, QMS_BLOCK_SIZE);
            p.pointer += QMS_BLOCK_SIZE;
            p.pointer += quickiso_putss(p, ctx.Volume_set_ID, 128);
            p.pointer += quickiso_putss(p, ctx.Publisher, 128);
            p.pointer += quickiso_putss(p, ctx.Date_preparer, 128);
            p.pointer += quickiso_putss(p, ctx.Application, 128);
            p.pointer += quickiso_putss(p, ctx.Copyright, 37);
            p.pointer += quickiso_putss(p, ctx.Abstract, 37);
            p.pointer += quickiso_putss(p, ctx.Bibliographic, 37);
            p.pointer += quickiso_puttime(p, 0);
            p.pointer += quickiso_puttime(p, 0);
            p.pointer += quickiso_puttime(p, 0);
            p.pointer += quickiso_puttime(p, 0);
            p.add(1);
            p.add(0);
            ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);

            // Primary Volume Descriptor Terminator
            memset(ctx.buff, 0, 2048);
            p = ctx.buff.copy();
            p.add(0xff);
            p.pointer += quickiso_putss(p, "CD001", 5);
            p.add(1);
            ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);

            // empty sector
            memset(ctx.buff, 0, 2048);
            ctx.fd.write(ctx.buff.data, ctx.buff.pointer, 2048);
            ctx.out.flush();
            // close & free
            ctx.fd.close();

            qms_write_files(ctx, ctx.root);

            quickiso_free_entry(ctx.root);
            if (ctx.root != null) {
                ctx.root = null;
            }
        }
        finally {
            ctx.out.close();
        }
        return 0;
    }

    private void qms_write_files(quickiso_ctx_t ctx, quickiso_entry_t root) throws IOException
    {
        ctx.out.write(ctx.ram.data, 0, ctx.ram.length);
        if (root == null) {
            return;
        }
        for (int i = 0; i < root.childs; i++) {
            qms_write_file(ctx, root.child[i]);
            ctx.out.flush();
        }
    }

    private void qms_write_file(quickiso_ctx_t ctx, quickiso_entry_t file) throws IOException
    {
        if (file == null) {
            return;
        }
//        System.out.println(new String(file.name.data, file.name.pointer, file.name.strLen(), "US-ASCII"));
        if (quickiso_isfolder(file.flags) != 0) {
            for (int i = 0; i < file.childs; i++) {
                qms_write_file(ctx, file.child[i]);
            }
        }
        else {
            Qms_FileWriter fi = file.file;
            if (fi.isInputStream()) {
                System.out.println("write " + file.name.toString("US-ASCII"));
                InputStream in = null;
                try {
                    in = fi.createInput();
                    int lengthToWrite = (int) fi.size();
                    int bytesToRead, bytesHandled;
                    while (lengthToWrite > 0) {
                        if (lengthToWrite > ctx.buff.size()) {
                            bytesToRead = ctx.buff.size();
                        }
                        else {
                            bytesToRead = (int) lengthToWrite;
                        }

                        bytesHandled = in.read(ctx.buff.data, 0, bytesToRead);
                        if (bytesHandled == -1) {
                            throw new IOException("Cannot read all data from reference.");
                        }

                        ctx.out.write(ctx.buff.data, 0, bytesHandled);

                        lengthToWrite -= bytesHandled;
                        System.out.printf("writed %6.2f%%\r", ((1f - (float) lengthToWrite / fi.size()) * 100));
                    }
                    System.out.print("              \r");
//            dataOutputStream.flush();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    try {
                        if (in != null) {
                            in.close();
                            in = null;
                        }
                    }
                    catch (IOException e) {
                    }
                }
            }
            else {
                fi.writeInput(ctx.out);//handled by user
            }
            quickiso_padding(ctx, file);
        }
    }

}
