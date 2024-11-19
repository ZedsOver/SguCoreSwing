/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author ARMAX
 */
public interface ReadSeek extends Seekable {

    void fully(byte b[]) throws IOException;

    void fully(byte b[], int off, int len) throws IOException;

    byte readS() throws IOException;

    int readU() throws IOException;

    boolean readBool() throws IOException;

    public InputStream asInputStream();

    //<editor-fold defaultstate="collapsed" desc="units">
    short read2be() throws IOException;

    int readU2be() throws IOException;

    char readCle() throws IOException;

    char readCbe() throws IOException;

    int read4be() throws IOException;

    long read8be() throws IOException;

    float readFbe() throws IOException;

    double readDbe() throws IOException;

    long readU4be() throws IOException;

    int read4le() throws IOException;

    int read3be() throws IOException;

    int read3le() throws IOException;

    long readU4le() throws IOException;

    long read8le() throws IOException;

    short read2le() throws IOException;

    int readU2le() throws IOException;

    float readFle() throws IOException;

    double readDle() throws IOException;

    short read2() throws IOException;

    int readU2() throws IOException;

    char readC() throws IOException;

    int read3() throws IOException;

    int read4() throws IOException;

    long readU4() throws IOException;

    long read8() throws IOException;

    float readF() throws IOException;

    double readD() throws IOException;

    void readA(Object dst, int off, int sz) throws IOException;

    Object readA(Class type, int sz) throws IOException;

    Object readA(Class fromtype, boolean unsigned, Object dst, int off, int sz) throws IOException;

    Object readA(Class fromtype, boolean unsigned, Class totype, int sz) throws IOException;
    //</editor-fold>

    Object readAbe(Class fromtype, boolean unsigned, Object dst, int off, int sz) throws IOException;

    Object readAle(Class fromtype, boolean unsigned, Object dst, int off, int sz) throws IOException;

    Object readAbe(Class fromtype, boolean unsigned, Class totype, int sz) throws IOException;

    Object readAle(Class fromtype, boolean unsigned, Class totype, int sz) throws IOException;

    Object readAbe(Object dst, int off, int sz) throws IOException;

    Object readAle(Object dst, int off, int sz) throws IOException;

    Object readAbe(Class type, int sz) throws IOException;

    Object readAle(Class type, int sz) throws IOException;

    int read() throws IOException;

    int read(byte[] b) throws IOException;

    int read(byte[] b, int off, int len) throws IOException;

    //microsoft impls
    byte[] fully(int size) throws IOException;

    String readStr(int size, String coding) throws IOException;

    String readCStr(boolean is16, String coding) throws IOException;

}
