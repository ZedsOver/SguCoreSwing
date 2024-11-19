/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

/**
 *
 * @author ARMAX
 */
public class DByteArray {

    public DByteArray()
    {
    }

    public DByteArray(byte[] da, int len)
    {
        data = da;
        length = len;
    }

    public DByteArray(int capacity)
    {
        data = new byte[capacity < 0 ? 0 : capacity > MAX_ARRAY_SIZE ? MAX_ARRAY_SIZE : capacity];
    }
    public boolean lock = false;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    public volatile byte[] data = {};
    public int length;

    /**
     * comprueba el tamaño actual y se redimensiona manteniendo la memoria
     * antugua de forma interna
     *
     * @param sz
     * @return
     */
    public DByteArray reDim(int sz)
    {
        sz = sz < 0 ? 0 : sz > MAX_ARRAY_SIZE ? MAX_ARRAY_SIZE : sz;
        data = (length != sz && sz > data.length) ? new byte[sz] : data;
        length = sz;
        return this;
    }

    public DByteArray reDimPreserve(int sz)
    {
        sz = sz < 0 ? 0 : sz > MAX_ARRAY_SIZE ? MAX_ARRAY_SIZE : sz;
        byte[] ads = data;
        data = (length != sz && sz > data.length) ? new byte[sz] : data;
        if (ads.length != data.length) {
            System.arraycopy(ads, 0, data, 0, length);
        }
        length = sz;
        return this;
    }

    /**
     * solo se comprueba si tienen el mismo tamaño y se redefine el array
     *
     * @param sz
     * @return
     */
    public DByteArray dim(int sz)
    {
        sz = sz < 0 ? 0 : sz > MAX_ARRAY_SIZE ? MAX_ARRAY_SIZE : sz;
        data = data.length != sz ? new byte[sz] : data;
        length = data.length;
        return this;
    }

    public DByteArray resetAll()
    {
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                data[0] = 0;
            }
        }
        return this;
    }

    public void clear()
    {
        reDim(0);
        System.gc();
        System.gc();
        System.gc();
    }
}
