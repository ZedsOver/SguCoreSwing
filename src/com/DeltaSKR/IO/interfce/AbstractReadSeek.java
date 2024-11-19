/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DeltaSKR.IO.interfce;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

/**
 *
 * @author armax
 */
public abstract class AbstractReadSeek implements ReadSeek {

    @Override
    public int read(byte[] b) throws IOException
    {
        return read(b, 0, b.length);
    }

    @Override
    public long readU4be() throws IOException
    {
        return read4be() & 0xffffffffL;
    }

    @Override
    public int read4le() throws IOException
    {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch1 + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }

    @Override
    public long readU4le() throws IOException
    {
        return (read4le() & 0xFFFFFFFFL);
    }

    @Override
    public final long read8le() throws IOException
    {
        return (read4le() & 0xFFFFFFFFL) + ((long) (read4le()) << 32);
    }

    @Override
    public short read2le() throws IOException
    {
        return (short) readU2le();
    }

    @Override
    public int readU2le() throws IOException
    {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 + (ch2 << 8));
    }

    @Override
    public final void fully(byte b[]) throws IOException
    {
        fully(b, 0, b.length);
    }

    @Override
    public final void fully(byte b[], int off, int len) throws IOException
    {
        int n = 0;
        do {
            int count = this.read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        }
        while (n < len);
    }

    @Override
    public final boolean readBool() throws IOException
    {
        int ch = read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (ch != 0);
    }

    @Override
    public final byte readS() throws IOException
    {
        int ch = read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (byte) (ch);
    }

    @Override
    public final int readU() throws IOException
    {
        int ch = read();
        if (ch < 0) {
            throw new EOFException();
        }
        return ch;
    }

    @Override
    public final short read2be() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch1 << 8) + (ch2 << 0));
    }

    @Override
    public final int readU2be() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 << 8) + (ch2 << 0);
    }

    @Override
    public final char readCle() throws IOException
    {
        return (char) readU2le();
    }

    @Override
    public final char readCbe() throws IOException
    {
        return (char) readU2be();
    }

    @Override
    public final int read4be() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        int ch4 = read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    private final byte readBuffer[] = new byte[8];

    @Override
    public final long read8be() throws IOException
    {
        fully(readBuffer, 0, 8);
        return (((long) readBuffer[0] << 56)
                + ((long) (readBuffer[1] & 255) << 48)
                + ((long) (readBuffer[2] & 255) << 40)
                + ((long) (readBuffer[3] & 255) << 32)
                + ((long) (readBuffer[4] & 255) << 24)
                + ((readBuffer[5] & 255) << 16)
                + ((readBuffer[6] & 255) << 8)
                + ((readBuffer[7] & 255) << 0));
    }

    @Override
    public final float readFbe() throws IOException
    {
        return Float.intBitsToFloat(read4be());
    }

    @Override
    public final double readDbe() throws IOException
    {
        return Double.longBitsToDouble(read8be());
    }

    @Override
    public final float readFle() throws IOException
    {
        return Float.intBitsToFloat(read4le());
    }

    @Override
    public final double readDle() throws IOException
    {
        return Double.longBitsToDouble(read8le());
    }

    @Override
    public void close() throws IOException
    {

    }

    @Override
    public int read3be() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        if ((ch1 | ch2 | ch3) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 16) + (ch2 << 8) + (ch3 << 0));
    }

    @Override
    public int read3le() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        if ((ch1 | ch2 | ch3) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16));
    }

    @Override
    public byte[] fully(int size) throws IOException
    {
        byte[] tmp = new byte[size];
        fully(tmp);
        return tmp;
    }

    @Override
    public String readStr(int size, String coding) throws IOException
    {
        return new String(fully(size), coding);
    }

    @Override
    public String readCStr(boolean is16, String coding) throws IOException
    {
        long ix = seek();
        int sz = 0, iy = 0;
        final byte[] duf = new byte[16];
        dux:
        while ((sz = read(duf, sz, 16 - sz)) > 0) {
            if (is16) {
                for (int i = 0; i < sz; i += 2) {
                    iy += 2;
                    if ((duf[i] | duf[i + 1]) == 0) {
                        break dux;
                    }
                }
                if (sz % 2 == 1) {
                    duf[0] = duf[sz - 1];
                    sz = 1;
                }
                else {
                    sz = 0;
                }
            }
            else {
                for (byte b : duf) {
                    iy++;
                    if (b == 0) {
                        break dux;
                    }
                }
                sz = 0;
            }
        }
        if (iy == 0) {
            return null;
        }
        seek(ix);
        iy = iy - (is16 ? 2 : 1);
        if (iy > 0) {
            String red = new String(fully(iy), coding);
            skip(is16 ? 2 : 1);
            return red;
        }
        skip(is16 ? 2 : 1);
        return "";
    }

    @Override
    public Object readAbe(Object dst, int off, int sz) throws IOException
    {
        IOSeekUtil.readAbe(this, dst, off, sz);
        return dst;
    }

    @Override
    public Object readAle(Object dst, int off, int sz) throws IOException
    {
        IOSeekUtil.readAle(this, dst, off, sz);
        return dst;
    }

    @Override
    public Object readAbe(Class type, int sz) throws IOException
    {
        Object arr = Array.newInstance(type, sz);
        readAbe(arr, 0, sz);
        return arr;
    }

    @Override
    public Object readAle(Class type, int sz) throws IOException
    {
        Object arr = Array.newInstance(type, sz);
        readAle(arr, 0, sz);
        return arr;
    }

    @Override
    public Object readA(Class type, boolean usig, Object dst, int off, int sz) throws IOException
    {
        if (useLE) {
            return readAle(type, usig, dst, off, sz);
        }
        return readAbe(type, usig, dst, off, sz);
    }

    @Override
    public Object readAbe(Class type, boolean usig, Object dst, int off, int sz) throws IOException
    {
        IOSeekUtil.readAbe(this, type, usig, dst, off, sz);
        return dst;
    }

    @Override
    public Object readAle(Class type, boolean usig, Object dst, int off, int sz) throws IOException
    {
        //fix error LE
        IOSeekUtil.readAle(this, type, usig, dst, off, sz);
        return dst;
    }

    @Override
    public InputStream asInputStream()
    {
        return IOSeekUtil.loadAsIntputStream(this);
    }

    protected boolean useLE;

    @Override
    public void toBE()
    {
        useLE = false;
    }

    @Override
    public void toLE()
    {
        useLE = true;
    }

    @Override
    public boolean isLE()
    {
        return useLE;
    }

    @Override
    public void setEndian(boolean LE)
    {
        useLE = LE;
    }

    @Override
    public short read2() throws IOException
    {
        return useLE ? read2le() : read2be();
    }

    @Override
    public int read4() throws IOException
    {
        return useLE ? read4le() : read4be();
    }

    @Override
    public long read8() throws IOException
    {
        return useLE ? read8le() : read8be();
    }

    @Override
    public int read3() throws IOException
    {
        return useLE ? read3le() : read3be();
    }

    @Override
    public float readF() throws IOException
    {
        return useLE ? readFle() : readFbe();
    }

    @Override
    public double readD() throws IOException
    {
        return useLE ? readDle() : readDbe();
    }

    @Override
    public void readA(Object dst, int off, int sz) throws IOException
    {
        if (useLE) {
            readAle(dst, off, sz);
        }
        else {
            readAbe(dst, off, sz);
        }
    }

    @Override
    public Object readA(Class type, int sz) throws IOException
    {
        if (useLE) {
            return readAle(type, sz);
        }
        return readAbe(type, sz);
    }

    @Override
    public int readU2() throws IOException
    {
        return useLE ? readU2le() : readU2be();
    }

    @Override
    public char readC() throws IOException
    {
        return useLE ? readCle() : readCbe();
    }

    @Override
    public long readU4() throws IOException
    {
        return useLE ? readU4le() : readU4be();
    }

    @Override
    public Object readA(Class fromType, boolean usig, Class toType, int sz) throws IOException
    {
        final Object arr = Array.newInstance(toType, sz);
        if (useLE) {
            IOSeekUtil.readAle(this, fromType, usig, arr, 0, sz);
        }
        else {
            IOSeekUtil.readAbe(this, fromType, usig, arr, 0, sz);
        }
        return arr;
    }

    @Override
    public Object readAbe(Class fromType, boolean usig, Class toType, int sz) throws IOException
    {
        final Object arr = Array.newInstance(toType, sz);
        IOSeekUtil.readAbe(this, fromType, usig, arr, 0, sz);
        return arr;
    }

    @Override
    public Object readAle(Class fromType, boolean usig, Class toType, int sz) throws IOException
    {
        final Object arr = Array.newInstance(toType, sz);
        IOSeekUtil.readAle(this, fromType, usig, arr, 0, sz);
        return arr;
    }

}
