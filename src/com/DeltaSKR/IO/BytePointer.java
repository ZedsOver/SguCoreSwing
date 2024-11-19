/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author ARMAX
 */
public final class BytePointer {

    public void copyTo(byte[] dst)
    {
        copyTo(dst, dst.length);
    }

    public void copyTo(byte[] dst, int size)
    {
        System.arraycopy(data, pointer, dst, 0, size);
    }

    public volatile byte[] data;
    public int pointer = 0;

    public BytePointer(int size)
    {
        data = new byte[size];
    }

    public BytePointer(byte[] oca, int p)
    {
        data = oca;
        pointer = p;
    }

    public BytePointer(String src)
    {
        try {
            data = src.getBytes("US-ASCII");
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void add(int b)
    {
        data[pointer++] = (byte) b;
    }

    public void setAll(int i, int v)
    {
        for (int k = pointer + i; k < data.length; k++) {
            data[k] = (byte) v;
        }
    }

    public void set(int i, int v)
    {
        data[pointer + i] = (byte) v;
    }

    public void set(int v)
    {
        data[pointer] = (byte) v;
    }

    public int get(int i)
    {
        if (pointer + i < data.length) {
            return data[pointer + i] & 255;
        }
        return 0;
    }

    public BytePointer copy(int add)
    {
        return new BytePointer(data, pointer + add);
    }

    public BytePointer copy()
    {
        return new BytePointer(data, pointer);
    }

    public int strLen()
    {
        int ptr = this.pointer;
        while (ptr < data.length && data[ptr] != 0) {
            ++ptr;
        }
        return ptr - this.pointer;
    }

    public int strLen2()
    {
        int ptr = this.pointer;
        while (ptr + 1 < data.length && (data[ptr] | data[ptr + 1]) != 0) {
            ptr += 2;
        }
        return ptr - this.pointer;
    }

    public int size()
    {
        return data.length - pointer;
    }

    public int get()
    {
        if (pointer < data.length) {
            return data[pointer] & 255;
        }
        return 0;
    }

    public void setData(byte[] data)
    {
        this.data = data;
        pointer = 0;
    }

    public String toString(String code) throws UnsupportedEncodingException
    {
        return new String(data, pointer, strLen(), code);
    }

    public String toString2(String code) throws UnsupportedEncodingException
    {
        return new String(data, pointer, strLen2(), code);
    }

}
