/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DeltaSKR.IO.interfce;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author armax
 */
public abstract class AbstractRndAccess extends AbstractReadSeek implements RndAccess {

    @Override
    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }

    @Override
    public final void writeBool(boolean v) throws IOException
    {
        write(v ? 1 : 0);
        //written++;
    }

    @Override
    public final void write2be(int v) throws IOException
    {
        write((v >>> 8) & 0xFF);
        write((v >>> 0) & 0xFF);
        //written += 2;
    }

    @Override
    public final void writeCle(char v) throws IOException
    {
        write2le(v);
    }

    @Override
    public final void writeCbe(char v) throws IOException
    {
        write2be(v);
    }

    @Override
    public final void write4be(int v) throws IOException
    {
        write((v >>> 24) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 0) & 0xFF);
        //written += 4;
    }

    @Override
    public final void write8be(long v) throws IOException
    {
        write((int) (v >>> 56) & 0xFF);
        write((int) (v >>> 48) & 0xFF);
        write((int) (v >>> 40) & 0xFF);
        write((int) (v >>> 32) & 0xFF);
        write((int) (v >>> 24) & 0xFF);
        write((int) (v >>> 16) & 0xFF);
        write((int) (v >>> 8) & 0xFF);
        write((int) (v >>> 0) & 0xFF);
        //written += 8;
    }

    @Override
    public final void writeFbe(float v) throws IOException
    {
        write4be(Float.floatToIntBits(v));
    }

    @Override
    public final void writeDbe(double v) throws IOException
    {
        write8be(Double.doubleToLongBits(v));
    }

    @Override
    public final void writeChars(String s) throws IOException
    {
        int clen = s.length();
        int blen = 2 * clen;
        byte[] b = new byte[blen];
        char[] c = new char[clen];
        s.getChars(0, clen, c, 0);
        for (int i = 0, j = 0; i < clen; i++) {
            b[j++] = (byte) (c[i] >>> 8);
            b[j++] = (byte) (c[i] >>> 0);
        }
        write(b, 0, blen);
    }

    @Override
    public final void write2le(int v) throws IOException
    {
        write((v) & 0xFF);
        write((v >>> 8) & 0xFF);
        //written += 2;
    }

    @Override
    public final void write4le(int v) throws IOException
    {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 24) & 0xFF);
        //written += 4;
    }

    @Override
    public final void write8le(long v) throws IOException
    {
        write((int) (v >>> 0) & 0xFF);
        write((int) (v >>> 8) & 0xFF);
        write((int) (v >>> 16) & 0xFF);
        write((int) (v >>> 24) & 0xFF);
        write((int) (v >>> 32) & 0xFF);
        write((int) (v >>> 40) & 0xFF);
        write((int) (v >>> 48) & 0xFF);
        write((int) (v >>> 56) & 0xFF);
        //written += 8;
    }

    @Override
    public final void writeFle(float v) throws IOException
    {
        write4le(Float.floatToIntBits(v));
    }

    @Override
    public final void writeDle(double v) throws IOException
    {
        write8le(Double.doubleToLongBits(v));
    }

    @Override
    public void write3be(int v) throws IOException
    {
        write((v >>> 16) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 0) & 0xFF);
    }

    @Override
    public void write3le(int v) throws IOException
    {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 16) & 0xFF);
    }

    @Override
    public int writeUTFbe(String str) throws IOException
    {
        return UtfData.writeUTF(str, this, true);
    }

    @Override
    public int writeUTFle(String str) throws IOException
    {
        return UtfData.writeUTF(str, this, false);
    }

    @Override
    public void writeAbe(Object src, int off, int sz) throws IOException
    {
        IOSeekUtil.writeAbe(this, src, off, sz);
    }

    @Override
    public void writeAle(Object src, int off, int sz) throws IOException
    {
        IOSeekUtil.writeAle(this, src, off, sz);
    }

    @Override
    public OutputStream asOutputStream()
    {
        return IOSeekUtil.loadAsOutputStream(this);
    }

    @Override
    public void writeC(char c) throws IOException
    {
        if (useLE) {
            write2le(c);
        }
        else {
            write2be(c);
        }
    }

    @Override
    public void write2(int i) throws IOException
    {
        if (useLE) {
            write2le(i);
        }
        else {
            write2be(i);
        }
    }

    @Override
    public void write4(int i) throws IOException
    {
        if (useLE) {
            write4le(i);
        }
        else {
            write4be(i);
        }
    }

    @Override
    public void write3(int i) throws IOException
    {
        if (useLE) {
            write3le(i);
        }
        else {
            write3be(i);
        }
    }

    @Override
    public void write8(long l) throws IOException
    {
        if (useLE) {
            write8le(l);
        }
        else {
            write8be(l);
        }
    }

    @Override
    public void writeF(float f) throws IOException
    {
        if (useLE) {
            writeFle(f);
        }
        else {
            writeFbe(f);
        }
    }

    @Override
    public void writeD(double d) throws IOException
    {
        if (useLE) {
            writeDle(d);
        }
        else {
            writeDbe(d);
        }
    }

    @Override
    public void writeA(Object o, int i, int i1) throws IOException
    {
        if (useLE) {
            writeAle(o, i, i1);
        }
        else {
            writeAbe(o, i, i1);
        }
    }

    @Override
    public void writeNull(int bytes) throws IOException
    {
        IOSeekUtil.writeNull(this, bytes);
    }

    @Override
    public void writeStr(String data, String code, int align) throws IOException
    {
        IOSeekUtil.writeStr(this, data, code, align);
    }

    @Override
    public void writeAbe(Class astype, Object src, int off, int sz) throws IOException
    {
        IOSeekUtil.writeAbe(this, astype, src, off, sz);
    }

    @Override
    public void writeAle(Class astype, Object src, int off, int sz) throws IOException
    {
        IOSeekUtil.writeAle(this, astype, src, off, sz);
    }

    @Override
    public void writeA(Class astype, Object o, int i, int i1) throws IOException
    {
        if (useLE) {
            writeAle(astype, o, i, i1);
        }
        else {
            writeAbe(astype, o, i, i1);
        }
    }

}
