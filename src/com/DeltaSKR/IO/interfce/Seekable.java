/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author ARMAX
 */
public interface Seekable extends Closeable {

    void toBE();

    void toLE();

    void setEndian(boolean LE);

    boolean isLE();

    void seek(long l) throws IOException;

    long seek() throws IOException;

    long length() throws IOException;

    int skip(int n) throws IOException;

    void lock(long pos) throws IOException;

    long lock() throws IOException;

    void unlock();
}
