/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 *
 * @author ARMAX
 */
public class SeekByteArray implements RndAccess {

    private final DByteArray arr;
    protected int pos;
    protected int mark = 0;

    public SeekByteArray()
    {
        arr = new DByteArray();
    }

    public SeekByteArray(DByteArray arr)
    {
        this.arr = arr;
    }

    public SeekByteArray(byte[] arr)
    {
        this(arr, 0, arr.length);
    }

    public SeekByteArray(byte[] arr, int off, int len)
    {
        this.arr = new DByteArray();
        setBuffer(arr, off, len);
    }

    public SeekByteArray(int size)
    {
        this(new byte[size]);
        arr.length = 0;
    }

    public void setBuffer(byte[] buf)
    {
        setBuffer(buf, 0, buf.length);
    }

    public final void setBuffer(byte[] buf, int offset, int length)
    {
        this.arr.data = buf;
        this.mark = this.pos = offset;
        this.arr.length = Math.min(offset + length, buf.length);
    }

    public final void setBuffer(SeekByteArray buf, int offset, int length)
    {
        this.arr.data = buf.arr.data;
        this.arr.length = Math.min(offset + length, buf.arr.length - buf.mark) + buf.mark;
        this.mark = this.pos = offset + buf.mark;
    }

    public final void reset(int off, int count)
    {
        mark = pos = off;
        arr.length = mark + count;
    }

    public final DByteArray getPointer()
    {
        return arr;
    }

    public final byte[] getData()
    {
        return arr.data;
    }

    public final int getDataSize()
    {
        return arr.length;
    }

    @Override
    public synchronized int read()
    {
        return (pos < arr.length) ? (arr.data[pos++] & 0xff) : -1;
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        return read(b, 0, b.length);
    }

    @Override
    public synchronized int read(byte b[], int off, int len)
    {
        if (b == null) {
            throw new NullPointerException();
        }
        else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        else if (len == 0) {
            return 0;
        }

        if (pos >= arr.length) {
            return -1;
        }

        int avail = arr.length - pos;
        if (len > avail) {
            len = avail;
        }
        if (len <= 0) {
            return 0;
        }
        System.arraycopy(arr.data, pos, b, off, len);
        pos += len;
        return len;
    }

    @Override
    public void lock(long pos) throws IOException
    {
        mark = 0;
        seek(pos);
        lock();
    }

    @Override
    public long lock() throws IOException
    {
        return mark = pos;
    }

    @Override
    public void unlock()
    {
        mark = 0;
    }

    public void unlock(long i) throws IOException
    {
        seek(i);
        mark = 0;
    }

    @Override
    public long seek() throws IOException
    {
        return pos - mark;
    }

    @Override
    public void seek(long pos) throws IOException
    {
        if (pos < 0) {
            throw new IOException("Negative seek offset");
        }
        else if (pos > arr.length - mark) {
            this.ensureCapacity((int) pos + mark);
        }
        this.pos = (int) pos + mark;
    }

    @Override
    public long length() throws IOException
    {
        return arr.length - mark;
    }

    @Override
    public int skip(int n) throws IOException
    {
        if (n <= 0) {
            return 0;
        }
        int usk = this.pos;
        this.pos = Math.min(this.pos + n, arr.length);
        /* return the actual number of bytes skipped */
        return (int) (this.pos - usk);
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
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private void ensureCapacity(int minCapacity) throws IOException
    {
        // overflow-conscious code
        if (minCapacity - arr.data.length > 0) {
            if (arr.lock) {
                throw new IOException(String.format("Pointer capacity it's lock to 0x%08X", arr.data.length));
            }
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity)
    {
        // overflow-conscious code
        int oldCapacity = arr.data.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        arr.data = Arrays.copyOf(arr.data, newCapacity);
    }

    private static int hugeCapacity(int minCapacity)
    {
        if (minCapacity < 0) // overflow
        {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE)
                ? Integer.MAX_VALUE
                : MAX_ARRAY_SIZE;
    }

    @Override
    public synchronized void write(int b) throws IOException
    {
        if (pos + 1 > arr.length) {
            ensureCapacity(pos + 1);
            arr.length = pos + 1;
        }
        arr.data[pos++] = (byte) b;
    }

    @Override
    public synchronized void write(byte b[], int off, int len) throws IOException
    {
        if ((off < 0) || (off > b.length) || (len < 0)
                || ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }
        if (pos + len >= arr.length) {
            ensureCapacity(pos + len);
            arr.length = pos + len;
        }
        System.arraycopy(b, off, arr.data, pos, len);
        pos += len;
    }

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
    public void close() throws IOException
    {

    }

    @Override
    public void setLength(long len) throws IOException
    {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        pos = 0;
        ensureCapacity((int) len);
        arr.length = (int) len;
    }

    public void writeTo(OutputStream out) throws IOException
    {
        out.write(arr.data, 0, arr.length);
    }

    public void writeToLims(OutputStream out) throws IOException
    {
        out.write(arr.data, mark, arr.length - mark);
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

    public void addSize(int len) throws IOException
    {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (pos + len >= arr.length) {
            ensureCapacity(pos + len);
            arr.length = pos + len;
        }
        pos += len;
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
        seek(ix);
        String red = new String(fully(sz - (is16 ? 2 : 1)), coding);
        skip(is16 ? 2 : 1);
        return red;
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
    public InputStream asInputStream()
    {
        return IOSeekUtil.loadAsIntputStream(this);
    }

    private boolean useLE;

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
