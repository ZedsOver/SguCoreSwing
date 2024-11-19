/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgucore.data;

import java.io.IOException;

import com.DeltaSKR.IO.interfce.IOSeekUtil;
import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.IO.interfce.WriteSeek;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import main_app.IndexHelper;

import parsers.BasicFile;
import parsers.Pkg_box;

/**
 *
 * @author ARMAX
 */
public class AFS extends Pkg_box {

    private final static int[] CODEC = new int[]{0x41465300};

    public AFS(BasicFile parent)
    {
        super(CODEC, CODEC, parent, Flags.DUNK | Flags.NAME | Flags.SVOF, AFS.class);
    }

    @Override
    protected boolean isValid() throws IOException
    {
        seek(4);
        count = read4le();
        if (!resizeOffets(this, 2, 1, 4)) {
            return false;
        }
        long[][] offsets = (long[][]) this.offsets;
        long ic[];
        long mo = Integer.MAX_VALUE;
//        System.out.println(length());
        for (int i = 0; i < count; i++) {
            ic = offsets[i];
            ic[0] = readU4le();
            ic[1] = readU4le() + ic[0];
            if (isInvalidPos(ic[0]) || isInvalidPos(ic[1])) {
                endFile();
//                System.out.println(isInvalidPos(ic[0]));
//                System.out.println(isInvalidPos(ic[1]));
                return false;
            }
            if (ic[0] > 0 && ic[0] < mo) {
                mo = ic[0];
            }
        }
        long ie[] = offsets[count];
        ie[0] = readU4le();
        ie[1] = readU4le() + ie[0];
        if (ie[0] == 0 && count > 0) {
            seek(mo - 8);
            ie[0] = readU4le();
            ie[1] = readU4le() + ie[0];
            if (isInvalidPos(ie[0])
                    || isInvalidPos(ie[1])) {
//                count = 0;
//                System.out.println("POP: 2");
                ie[1] = ie[0] = 0;
//                return false;
            }
        }
        //testing if name base is invalid
        for (int i = 0; i < count; i++) {
            ic = offsets[i];
            if (hasIntersectMid(ie[0], ie[1], ic[0], ic[1])) {
                ie[1] = ie[0] = 0;
                break;
            }
        }
//        if (count > 0x25F) {
//            count = 0x25F;//testa
//        }
        return true;
    }

    @Override
    public void writeIDX(RndAccess w, int index, long newSize, long newCmpSize) throws IOException
    {
        seek((index + 1) * 8 + 4);
        w.seek(fi.seek());
        long[] ac = ((long[][]) offsets)[index];
        newSize = IOSeekUtil.align(ac[1] - ac[0], 0x800);// fix gettering max size of slot
        ac[1] = ac[0] + newSize;

        w.write4le((int) newSize);
//        ac[1] = ac[0] + newSize;

    }

    @Override
    public String getName(int index) throws IOException
    {
        long ic[] = ((long[][]) offsets)[count];
        if (ic[1] - ic[0] <= 0) {
            return null;
        }
        seek(index * 0x30 + ic[0]);
        fully(po.data, 0, 0x30);
        return new String(po.data, 0, po.strLen(), "windows-1252");
//        return new String(po.data, 0, po.strLen(), "shift-jis");
    }

    protected long genMax(int ac)
    {
        return IOSeekUtil.align(ac, 0x800);
    }

    @Override
    protected long genMaxID(int ac) throws IOException
    {
        long b = getNextPosition(ac);
        long ic[] = ((long[][]) offsets)[count];
        if (ic[1] - ic[0] <= 0) {
            return b;
        }
        if (b > ic[0]) {
            b = ic[0];
        }
        return b - ((long[][]) offsets)[ac][0];
    }

    public void writeCopy(WriteSeek outa, boolean names) throws IOException
    {
        if (jindex == null) {
            throw new RuntimeException("Error jindex mustbe assigned");
        }
        jindex.pos = 0;

        final long[][] ofas = new long[jindex.length][2];
        final long[][] refs = (long[][]) jindex.offs;
        outa.writeNull((int) IOSeekUtil.align((ofas.length + 1) * 8 + 4, 0x800));
        long pox = outa.seek();
        OutputStream os = outa.asOutputStream();
        while (true) {
            IndexHelper.IEntry ast = jindex.next();
            if (ast == null) {
                break;
            }
            long[] ay = refs == null || jindex.pos > refs.length ? null : refs[jindex.pos - 1];
            long[] ax = ofas[jindex.pos - 1];
            ax[0] = pox;
            if (!(ast instanceof IndexHelper.SEntry)) {
                ax[1] += ax[0] + (ay[1] - ay[0]);
                readFile(jindex.pos - 1).exportRaw(outa);
                outa.writeNull((int) IOSeekUtil.alignMod(outa.seek(), 0x800));
                pox = outa.seek();
                continue;
            }
            IndexHelper.SEntry ap = (IndexHelper.SEntry) ast;
            ax[1] += ax[0] + ap.size;
            File fit = (File) ap.source;
            InputStream from = IOSys.openInput(fit);
            try {
                IOSys.writeAll(from, os);
            }
            finally {
                from.close();
            }
            outa.writeNull((int) IOSeekUtil.alignMod(outa.seek(), 0x800));
            pox = outa.seek();
        }
        if (names) {
            jindex.pos = 0;
            while (true) {
                IndexHelper.IEntry ast = jindex.next();
                if (ast == null) {
                    break;
                }
                if (ast.desc == null || ast.desc.isEmpty()) {
                    outa.writeNull(0x20);
                }
                else {
                    byte[] trimmed = ast.desc.getBytes("windows-1252");
                    outa.write(trimmed, 0, Math.min(trimmed.length, 0x20));
                    outa.writeNull(0x20 - Math.min(trimmed.length, 0x20));
                }
                outa.writeNull(0x10);
            }
            outa.writeNull((int) IOSeekUtil.alignMod(outa.seek(), 0x800));
        }
        outa.seek(0);
        outa.write4be(0x41465300);
        outa.write4le(jindex.length);
        for (long[] x : ofas) {
            outa.write4le((int) x[0]);
            outa.write4le((int) (x[1] - x[0]));
        }
        if (names) {
            outa.write4le((int) pox);
            outa.write4le(ofas.length * 0x30);
        }
        offsets = ofas;
    }
}
