/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_app;

import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.json.TABPrintStream;
import com.DeltaSKR.lang.ArrayUtil;

import com.DeltaSKR.lang.PrimitiveList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import parsers.BasicFile;
import parsers.Pkg_box;
import parsers.UNK_BIN;

/**
 * @author ARMAX
 */
public final class IndexHelper {

    public static final byte TYPEMASK = 0x0F;
    public static final byte NAMED = 0x40;
    public static final short ROOT = 0x80;
    public static final short UNK = 0x100;
    public static final short E_OFFS = 0x200;
    public static final short VRT_DIRS = 0x400;

    public static void trimNulls(IterData vpat)
    {
        if (vpat == null) {
            return;
        }
        int j = 0;
        for (int i = 0; i < vpat.length; i++) {
            Object bup = vpat.subs[i];
            if (bup != null) {
                vpat.subs[j++] = bup;
            }
        }
        final int v = j;
        for (; j < vpat.length; j++) {
            vpat.subs[j++] = null;
        }
        vpat.length = v;
    }

    public static VEntry initVDirIfNot(IEntry es)
    {
        if (es.vdir != null || es.length <= 0) {
            return es.vdir;
        }
        VEntry duska = new VEntry(es.length);
        for (int i = 0; i < es.length; i++) {
            duska.add(i);
        }
        setVDir(es, duska);
        return duska;
    }

    public static VEntry addVOfTo(VEntry ev, String name, boolean trim, int of, int to)
    {
        final VEntry est = __internalNewMove(ev, name, trim, of, to, 0);
        return est == null ? null : (VEntry) (ev.subs[of] = est);
    }

    public static VEntry addVOfTo(VEntry ev, String name, boolean trim, int of, int to, int szref)
    {
        final VEntry est = __internalNewMove(ev, name, trim, of, to, szref);
        return est == null ? null : (VEntry) (ev.subs[of] = est);
    }

    public static VEntry newVOfTo(VEntry ev, String name, boolean trim, int of, int to)
    {
        return __internalNewMove(ev, name, trim, of, to, 0);
    }

    public static VEntry newVOfTo(VEntry ev, String name, boolean trim, int of, int to, int szref)
    {
        return __internalNewMove(ev, name, trim, of, to, szref);
    }

    private static VEntry __internalNewMove(VEntry ev, Object name, boolean trim, int of, int to, int szref)
    {
//        to = to < 0 ? 0 : to > ev.length ? ev.length : to;
//        of = of > to ? to : of;
        if (to == of) {
            return null;
        }
        if (to < of || of < 0 || to > ev.length) {
            return null;
        }
//        if (to < of) {
//            throw new ArrayIndexOutOfBoundsException("Error index 'to' must be greater than of 'of'");
//        }
//        if (of < 0 || to > ev.length) {
//            throw new ArrayIndexOutOfBoundsException("between index is invalid");
//        }
        final VEntry cucas = name instanceof String ? new VEntry((String) name, to - of + szref) : (VEntry) name;
        for (int i = of; i < to; i++) {
            cucas.add(ev.subs[i]);
            if (!trim) {
                ev.subs[i] = null;
            }
        }
        if (trim) {
            System.arraycopy(ev.subs, to, ev.subs, of + 1, ev.length - to);
            for (int i = of + 1 + (ev.length - to); i < ev.length; i++) {
                ev.subs[i] = null;
            }
        }
//        ev.subs[of] = cucas;
        if (trim) {
            ev.length = ev.length - (to - of) + 1;
        }
        return cucas;
    }

    public static VEntry newVNone(VEntry ev, String name, int index)
    {
        ev.subs = ArrayUtil.insert(ev.subs, ev.length++, index, new VEntry(name));
        return (VEntry) ev.subs[index];
    }

    public static boolean moveVOfTo(VEntry src, VEntry dest, boolean trim, int of, int to)
    {
        return null != __internalNewMove(src, dest, trim, of, to, 0);
    }

    public static abstract class IterData<T> {

        public int pos = 0;
        public boolean useBuff = false;
        public int length;
        protected volatile T[] subs = (T[]) new Object[0];

        protected IterData()
        {
        }

        public boolean isFull()
        {
            return length == subs.length;
        }

        public T next()
        {
            return pos < length ? subs[pos++] : null;
        }

        public boolean next(T data)
        {
            subs[pos++] = data;
            return pos < length;
        }

        protected final void growBy(int count)
        {
            int minCapacity = subs.length + count;
            int newCapacity = (subs.length * 3) / 2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] data = new Object[newCapacity];
            System.arraycopy(subs, 0, data, 0, length);
            subs = (T[]) data;
        }

        public void trim()
        {
            if (length != subs.length) {
                Object[] data = new Object[length];
                System.arraycopy(subs, 0, data, 0, length);
                subs = (T[]) data;
            }
        }

        public void addLen(int len)
        {
            subs = (T[]) ArrayUtil.increaseArraySize(subs, length, len);
            length += len;
        }

        /**
         *
         * @param e
         */
        public void add(T e)
        {
            if (useBuff) {
                if (length >= subs.length) {
                    growBy(1);
                }
            }
            else {
                subs = (T[]) ArrayUtil.increaseArraySize(subs, length, 1);
            }
            subs[length++] = e;
        }

        public T get(int i)
        {
            return i < 0 || i >= length ? null : subs[i];
        }

        public boolean isNull(int i)
        {
            return i < 0 || subs == null || i >= length || subs[i] == null;
        }

        public int indexOf(T get)
        {
            int cca = 0;
            for (Object cc : subs) {
                if (cc == get) {
                    return cca;
                }
                cca++;
            }
            return -1;
        }

        public void set(int sel, T ee)
        {
            subs[sel] = ee;
        }

        public T last()
        {
            return pos < 1 ? null : subs[pos - 1];
        }

        public T remove(int vo)
        {
            T la = subs[vo];
            ArrayUtil.remove(subs, length--, vo);
            return la;
        }
    }

    public static final class VEntry extends IterData<Object> {

        public String desc;

        public VEntry()
        {
        }

        public VEntry(String desc)
        {
            this.desc = desc;
        }

        public VEntry(int initlen)
        {
            subs = new Object[initlen];
        }

        public VEntry(String desc, int initlen)
        {
            this.desc = desc;
            subs = new Object[initlen];
        }

        public VEntry get(String name)
        {
            for (Object ss : subs) {
                if (ss instanceof Integer) {
                    continue;
                }
                if (name.equals(((VEntry) ss).desc)) {
                    return (VEntry) ss;
                }
            }
            return null;
        }

        public void setCapacity(int capsz, int len)
        {
            subs = subs.length < capsz ? new Object[capsz] : subs;
            length = len;
        }

    }

    public static class SEntry extends IEntry {

        public Object source;//source data FILE|from other instance
        public long size;

        public SEntry(File its)
        {
            IndexHelper.setName(this, its.getName());
            size = its.length();
            source = its;
        }

        public SEntry(Pkg_box src, IEntry its, int index)
        {
            source = new Object[]{src, its, index};
            if (its.offs instanceof long[]) {
                size = ((long[]) its.offs)[index + 1] - ((long[]) its.offs)[index];
            }
            else {
                size = ((long[][]) its.offs)[index][1] - ((long[][]) its.offs)[index][0];
            }
        }
    }

    public static class IEntry extends IterData<IEntry> {

        public short modifier;
        public int code;
        public String desc;
        public Object offs;
        public VEntry vdir;

        public IEntry()
        {
        }

        public IEntry(int inilen)
        {
            length = inilen;
            subs = new IEntry[inilen];
        }

        public IEntry(IEntry cc)
        {
            length = cc.length;
            modifier = cc.modifier;
            code = cc.code;
            desc = cc.desc;
            offs = cc.offs;
            vdir = cc.vdir;
            subs = cc.subs;
            vdir = cc.vdir;
        }
    }

    public static class IEntryDeep extends IEntry {

        private final int codex;
        private final boolean isUnka;
        private final IEntry cc;

        public IEntryDeep(IEntry cc, int codex, boolean isUnka)
        {
            this.cc = cc;
            super.length = cc.length;
            this.codex = codex;
            this.isUnka = isUnka;
            modifier = cc.modifier;
            code = cc.code;
            desc = cc.desc;
            offs = cc.offs;
            vdir = cc.vdir;
            subs = cc.subs;
        }

        public IEntry get(int i)
        {
            IEntry e = super.get(i);
            if (e.code == codex && IndexHelper.isKNOWN(e) == !isUnka) {
                return e.get(0);
            }
            return e;
        }

        public IEntry finish(boolean flushnames)
        {
            cc.length = super.length;
            cc.modifier = modifier;
            cc.code = code;
            cc.desc = desc;
            cc.offs = offs;
            cc.vdir = vdir;
            cc.subs = subs;
            if (!flushnames) {
                return cc;
            }
            for (int i = 0; i < length; i++) {
                IEntry e = super.get(i);
                if (e.code == codex && IndexHelper.isKNOWN(e) == !isUnka) {
                    IndexHelper.setName(e, e.get(0).desc);;
                }
            }
            return cc;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="enhanced  read write binary">
    public static IEntry readBinary(DataInputStream ee) throws IOException
    {
//        final int[] tos = {0};
//        final InputStream ss = ee;
//        ee = new DataInputStream(new InputStream() {
//            @Override
//            public int read() throws IOException
//            {
//                tos[0] += 1;
//                return ss.read();
//            }
//
//            @Override
//            public int read(byte[] b, int off, int len) throws IOException
//            {
//                int i = ss.read(b, off, len); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
//                tos[0] += i;
//                return i;
//            }
//
//        });

        PrimitiveList.ListObject<IterData> it = PrimitiveList.newFrom(IterData.class);
        final IEntry rex = new IEntry(1);
        it.addO(rex);
//        IEntry dptemp = null;
        kit:
        while (it.size() > 0) {
            final IterData das = it.last();
            final byte cum = (byte) ee.read();
            boolean at = (cum & 0x40) != 0;
//            if (dptemp != null && das == dptemp && das.pos == 42735) {
//                System.out.println("main_app.IndexHelper.readBinary()");
//            }
            switch (cum & 0xF) {
                case 0: {
                    final IEntry dp = new IEntry();
                    if (!das.next(dp)) {
                        it.popLast();
                    }
                    dp.modifier = ee.readShort();
                    dp.code = ee.readInt();
                    if ((dp.modifier & NAMED) != 0) {
                        dp.desc = ee.readUTF();
                    }
                    if (at) {
                        continue;
                    }
                    final int clen = ee.readInt();
                    if (clen > 0 && (dp.modifier & E_OFFS) != 0) {
                        byte dim = ee.readByte();
                        if (dim == 0) {
                            final long[] eek = new long[clen + 1];
                            for (int i = 0; i <= clen; i++) {
                                eek[i] = ee.readLong();
                            }
                            dp.offs = eek;
                        }
                        else {
                            long[][] eek = new long[clen][dim];
                            for (long[] ii : eek) {
                                for (int j = 0; j < dim; j++) {
                                    ii[j] = ee.readLong();
                                }
                            }
                            dp.offs = eek;
                        }
                    }
                    dp.subs = new IEntry[clen];
                    dp.length = clen;
                    it.addO(dp);
//                    if ("zs2us.dat".equals(dp.desc)) {//45556
//                        System.out.println("main_app.IndexHelper.readBinary()");
//                        dptemp = dp;
//                    }
                    if ((dp.modifier & VRT_DIRS) != 0) {
                        final int cl = ee.readInt();
                        if (cl > 0) {
                            dp.vdir = new VEntry(cl);
                            dp.vdir.length = cl;
                            it.addO(dp.vdir);
                        }
                    }
                }
                continue;
                case 1://allways after 0|1
                {
                    final VEntry dp = new VEntry();
                    if (!das.next(dp)) {
                        it.popLast();
                    }
                    dp.desc = ee.readUTF();
                    if (at) {
                        continue;
                    }
                    final int cl = ee.readInt();
                    dp.setCapacity(cl, cl);
                    if (cl > 0) {
                        it.addO(dp);
                    }
                }
                continue;
                case 2://allways after 1|0
                    if (!das.next(ee.readInt())) {
                        it.popLast();
                    }
                    continue;
                default:
                    if (cum == -1) {//end file FIX error when load index
                        break kit;
                    }
                    throw new RuntimeException(String.format("Error invalid object type for index &%02X", cum));
            }
        }
//        printToff(rex.subs[0], "New FIle 2");
        return rex.subs[0];
    }

    public static void printToff(IEntry kentry, String ss) throws IOException
    {
        TABPrintStream cosa = new TABPrintStream(IOSys.openOutput("com.neon.sgu/xeno/tumba/" + ss, (int) 1e6));
        try {
            PrimitiveList.ListObject<IterData> it = PrimitiveList.newFrom(IterData.class);
            final IEntry rex = new IEntry(1);
            rex.subs[0] = kentry;
            kentry.pos = 0;
            it.addO(rex);
            while (it.size() > 0) {
                final Object kk = it.last().next();
                if (kk == null) {//end list
                    cosa.addTAB(-1);
                    it.popLast();
                    continue;
                }
                if (kk instanceof IEntry) {
                    cosa.printf("%08X ", it.last().pos - 1);
                    final IEntry dp = (IEntry) kk;
                    cosa.printf("%02X ", dp.length < 1 ? 0x40 : 0);
                    cosa.printf("%04X ", dp.modifier);
                    cosa.printf("%08X ", dp.code);
                    if ((dp.modifier & NAMED) != 0) {
                        cosa.printf("\"%s\" ", dp.desc);
                    }
                    if (dp.length < 1) {
                        cosa.println();
                        continue;
                    }
                    final int clen = dp.length;
                    cosa.printfln("%08X ", clen);
                    dp.pos = 0;
                    cosa.addTAB(1);
                    it.addO(kk);
                    if ((dp.modifier & VRT_DIRS) != 0) {
                        dp.vdir.pos = 0;
                        it.addO(dp.vdir);
                    }
                }
                else if (kk instanceof VEntry) {
                    cosa.printf("%08X ", it.last().pos - 1);
                    cosa.printf("%02X ", 1 | (((VEntry) kk).length < 1 ? 0x40 : 0));
                    cosa.printf("\"%s\" ", ((VEntry) kk).desc);
                    if (((VEntry) kk).length < 1) {
                        cosa.println();
                        continue;
                    }
                    cosa.printfln("%08X ", ((VEntry) kk).length);
                    cosa.addTAB(1);
                    ((VEntry) kk).pos = 0;
                    it.addO(kk);
                }
                else {
                    cosa.printf("%08X ", it.last().pos - 1);
                    cosa.printf("%02X ", 2);
                    cosa.printfln("%08X ", (Integer) kk);
                }
            }
        }
        finally {
            cosa.flush();
            cosa.close();
        }
    }

    //42735
    public static void writeBinary(DataOutputStream ee, IEntry kentry) throws IOException
    {
//        printToff(kentry, "New FIle");
        PrimitiveList.ListObject<IterData> it = PrimitiveList.newFrom(IterData.class);
        final IEntry rex = new IEntry(1);
        rex.subs[0] = kentry;
        kentry.pos = 0;
        it.addO(rex);
        while (it.size() > 0) {
            final IterData cuz = it.last();
            final int cd = cuz.pos;//get sure last index
            final Object kk = cuz.next();
            if (kk == null) {//end list
//                if(it.last().pos!=it.last().length){
//                    System.out.println("main_app.IndexHelper.writeBinary()");
//                }
                if (cd < cuz.length) {//fix nullpointer data exception
                    if (cuz instanceof VEntry) {
                        //assume as empty node group
                        ee.write(0x41);
                        ee.writeUTF("--ERROR--");
                        continue;
                    }
                    else if (cuz instanceof IEntry) {
                        //assume as error bin file
                        ee.write(0x40);
                        ee.writeShort(UNK);
                        ee.writeInt(0x42494E00);
                        continue;
                    }
                }
                it.popLast();
                continue;
            }
            if (kk instanceof IEntry) {
                final IEntry dp = (IEntry) kk;
                ee.write((dp.length < 1 ? 0x40 : 0));
                ee.writeShort(dp.modifier);
                ee.writeInt(dp.code);
                if ((dp.modifier & NAMED) != 0) {
                    ee.writeUTF(dp.desc);
                }
                if (dp.length < 1) {
                    continue;
                }
                final int clen = dp.length;
                ee.writeInt(clen);
                if (clen > 0 && (dp.modifier & E_OFFS) != 0) {
                    if (dp.offs instanceof long[]) {
                        ee.write(1);
                        long[] a = (long[]) dp.offs;
                        for (int i = 0; i <= clen; i++) {
                            ee.writeLong(a[i]);
                        }
                    }
                    else {
                        long[][] a = (long[][]) dp.offs;
                        ee.write(a[0].length);
                        for (int i = 0; i < clen; i++) {
                            for (long ita : a[i]) {
                                ee.writeLong(ita);
                            }
                        }
                    }
                }
                dp.pos = 0;
                it.addO(kk);
                if ((dp.modifier & VRT_DIRS) != 0) {
                    ee.writeInt(dp.vdir.length);
                    dp.vdir.pos = 0;
                    it.addO(dp.vdir);
                }
            }
            else if (kk instanceof VEntry) {
                ee.write(1 | (((VEntry) kk).length < 1 ? 0x40 : 0));
                ee.writeUTF(((VEntry) kk).desc);
                if (((VEntry) kk).length < 1) {
                    continue;
                }
                ee.writeInt(((VEntry) kk).length);
                ((VEntry) kk).pos = 0;
                it.addO(kk);
            }
            else {
                ee.write(2);
                ee.writeInt((Integer) kk);
            }
        }
    }
    //</editor-fold>

    public final static int getCode(IEntry e)
    {
        return e == null ? -1 : e.code;
    }

    public final static void setCode(IEntry e, int code, boolean unk)
    {
        e.code = code;
        e.modifier = (short) ((e.modifier & ~UNK) | (unk ? UNK : 0));
    }

    /**
     * code to set Icon type of element. NOTE: you need sure of what use some
     * types need specific additional elements
     *
     * @param e
     * @param type
     */
    public final static void setType(IEntry e, int type)
    {
        e.modifier = (short) ((e.modifier & ~TYPEMASK) | (type & TYPEMASK));
    }

    public static boolean isZero(IEntry cus)
    {
        return (cus.modifier & TYPEMASK) == 0;
    }

    public static boolean isKNOWN(IEntry cus)
    {
        return (cus.modifier & UNK) == 0;
    }

    public static boolean isKNOWN(int cus)
    {
        return (cus & UNK) == 0;
    }

    public static String getIfNamed(IEntry cus)
    {
        return isNamed(cus) ? getName(cus) : null;
    }

    public static String getName(IEntry cus)
    {
        return cus.desc;
    }

    public static void setName(IEntry cus, String name)
    {
        if (name == null) {
            return;
        }
        //adding named flag
        if (!isNamed(cus)) {
            cus.modifier = (short) (cus.modifier | NAMED);
        }
        cus.desc = name;
    }

    public static boolean isNamed(IEntry cus)
    {
        return (cus.modifier & NAMED) != 0 && cus.desc != null;
    }

    public static boolean isCollapse(IEntry cus)
    {
        switch (cus.modifier & TYPEMASK) {
            case BasicFile.TYPE_DIR:
            case BasicFile.TYPE_ZIP:
                return cus.subs != null;
        }
        return false;
    }

    public static final IEntry[] EMPTY = {};

    public static boolean isRoot(IEntry cus)
    {
        return (cus.modifier & ROOT) != 0;
    }

    public static short getModifier(IEntry cus)
    {
        return cus.modifier;
    }

    public static int getIndexOfName(IEntry en, String s, int cmptype, boolean ignorecase)
    {
        int sel = -1;
        if (ignorecase) {
            s = s.toUpperCase();
        }
        dol:
        for (int u = 0; u < en.length; u++) {
            String us = getName(en.get(u));
            if (us == null) {
                continue;
            }
            if (ignorecase) {
                us = us.toUpperCase();
            }
            switch (cmptype) {
                case 1:
                    if (us.startsWith(s)) {
                        sel = u;
                        break dol;
                    }
                    break;
                case 2:
                    if (us.endsWith(s)) {
                        sel = u;
                        break dol;
                    }
                    break;
                default:
                    if (us.equals(s)) {
                        sel = u;
                        break dol;
                    }
            }
        }
        if (sel == -1) {
            throw new RuntimeException("File not found: " + s);
        }
        return sel;
    }

    public static void setSubs(IEntry val, IEntry[] subs)
    {
        int elis = getModifier(val);
        switch (elis & TYPEMASK) {
            case BasicFile.TYPE_EDIR:
            case BasicFile.TYPE_DIR:
                elis = (elis & 0xFC0) | (subs.length > 0 ? BasicFile.TYPE_DIR : BasicFile.TYPE_EDIR);
                break;
            case BasicFile.TYPE_EZIP:
            case BasicFile.TYPE_ZIP:
                elis = (elis & 0xFC0) | (subs.length > 0 ? BasicFile.TYPE_ZIP : BasicFile.TYPE_EZIP);
                break;
            default:
                return;
        }
        setModifier(val, elis);
        val.subs = subs;
    }

    public static void setVDir(IEntry val, VEntry tree)
    {
        if (tree != null && val.length > 0 && tree.length > 0) {
            final short aaa = val.modifier;
            if ((aaa & VRT_DIRS) == 0) {
                setModifier(val, aaa | VRT_DIRS);
            }
            val.vdir = tree;
        }
    }

    public static void setModifier(IEntry val, int elis)
    {
        val.modifier = (short) elis;
    }

    public static IEntry newEntry(IEntry de, int type, boolean useName, boolean root, Class<? extends BasicFile> cls, int ico,
            Object offsets, VEntry vdir
    )
    {
        if (de.length > 0) {
            if (offsets == null || Array.getLength(offsets) <= 0) {
                offsets = null;
            }
            if (vdir == null || vdir.length <= 0) {
                vdir = null;
            }
        }
        else {
            de.subs = null;
            offsets = null;
            vdir = null;
        }
        de.code = type;
        de.modifier = ((short) ((useName ? NAMED : 0)//use  name
                | (root ? ROOT : 0)//is  root
                | (offsets != null ? E_OFFS : 0)
                | (vdir != null ? VRT_DIRS : 0)
                | (cls == UNK_BIN.class ? UNK : 0)//is unk types, prevent error for bad IDX        
                | ico));
        de.offs = offsets;
        de.vdir = vdir;
        return de;
    }

    public static Object getEmbedOffset(IEntry val)
    {
        if ((val.modifier & E_OFFS) != 0) {
            return val.offs;
        }
        return null;
    }

    public static VEntry getVDir(IEntry val)
    {
        final short aaa = val.modifier;
        if ((aaa & VRT_DIRS) != 0) {
            return val.vdir;
        }
        return null;
    }

    public static String findPath(final String[] datalist, VEntry tree, final int file) throws Exception
    {
        if (tree == null) {
            return datalist[file];
        }
        final String[] ces = {null};
        forTree(tree, false, new DVFor() {
            @Override
            public boolean onValue(List<VEntry> scope, int index) throws Exception
            {
                if (index != file) {
                    return true;
                }
                ces[0] = getPathFromScope(scope, datalist[file]);
                return false;
            }
        });
        return ces[0];
    }

    public static int findIndexFromTree(String[] datalist, VEntry tree, String path, boolean igcase)
    {
        int goTo = 0;
        String[] pax = null;
        while (true) {
            switch (goTo) {
                case 0:
                    if (tree != null) {
                        goTo = 2;
                        continue;
                    }
                case 1: {
                    int il = 0;
                    for (String nas : datalist) {
                        if (nas != null && (igcase ? nas.equalsIgnoreCase(path) : nas.equals(path))) {
                            return il;
                        }
                        il++;
                    }
                    return -1;
                }
                case 2:
                    pax = path.split("/");
//                    if (pax.length == 1) {
//                        goTo = 1;
//                        continue;
//                    }
                case 3: {
                    try {
                        for (int i = 0; i < pax.length - 1; i++) {
                            tree = tree.get(pax[i]);
                        }
                        path = pax[pax.length - 1];
                        for (int cci = 0; cci < tree.length; cci++) {
                            final Object cc = tree.subs[cci];
                            if (!(cc instanceof Integer)) {
                                continue;
                            }
                            String nas = datalist[(Integer) cc];
                            if (nas != null && (igcase ? nas.equalsIgnoreCase(path) : nas.equals(path))) {
                                return (Integer) cc;
                            }
                        }
                    }
                    catch (NullPointerException e) {
                    }
                }
            }
            break;
        }
        return -1;
    }

    public static int findIndexFromTree(IEntry datalist, VEntry tree, String path, int mode, boolean igcase)
    {
        if (tree == null) {
            return getIndexOfName(datalist, path, mode, igcase);
        }
        String[] pax = path.split("/");
        if (pax.length == 1) {
            return getIndexOfName(datalist, path, mode, igcase);
        }
        try {
            for (int i = 0; i < pax.length - 1; i++) {
                tree = tree.get(pax[i]);
            }
            path = pax[pax.length - 1];
            for (int cci = 0; cci < tree.length; cci++) {
                final Object cc = tree.subs[cci];
                if (!(cc instanceof Integer)) {
                    continue;
                }
                if (getName(datalist.get((Integer) cc)).equals(path)) {
                    return (Integer) cc;
                }
            }
        }
        catch (NullPointerException e) {
        }
        return -1;
    }

    public static void forTree(VEntry tree, final boolean fetchIniEnd, DVFor dvFor) throws Exception
    {
        ArrayList<VEntry> scope = new ArrayList();
        scope.add(tree);
        tree.pos = 0;
        while (scope.size() > 0) {
            VEntry me = scope.get(scope.size() - 1);
            Object mi = me.next();
            if (mi == null) {
                if (fetchIniEnd) {
                    dvFor.onValue(null, -1);
                }
                scope.remove(scope.size() - 1);
            }
            else if (mi instanceof Integer) {
                if (!dvFor.onValue(scope, (Integer) mi)) {
                    scope.clear();
                    break;
                }
            }
            else {
                me = (VEntry) mi;
                me.pos = 0;
                scope.add(me);
                if (fetchIniEnd) {
                    dvFor.onValue(scope, -2);
                }
            }
        }
    }

    public static String getPathFromScope(Object scope, String name)
    {
        List<VEntry> scp = (List) scope;
        StringBuffer buf = new StringBuffer();
        for (int i = 1; i < scp.size(); i++) {
            buf.append(scp.get(i).desc).append('/');
        }
        return buf.append(name).toString();
    }

    public static interface DVFor {

        /**
         * @param scope
         * @param index
         * @return if true continue fetching else break
         * @throws Exception
         */
        public boolean onValue(List<VEntry> scope, int index) throws Exception;

    }

}
