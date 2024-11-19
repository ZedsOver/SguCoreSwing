/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author ARMAX
 */
public interface WriteSeek extends Seekable {

    void write(int b) throws IOException;

    void write(byte b[]) throws IOException;

    void write(byte b[], int off, int len) throws IOException;

    void writeBool(boolean v) throws IOException;

    void writeCle(char v) throws IOException;

    void writeCbe(char v) throws IOException;

    void write2be(int v) throws IOException;

    void write4be(int v) throws IOException;

    void write3be(int v) throws IOException;

    void write8be(long v) throws IOException;

    void writeFbe(float v) throws IOException;

    void writeDbe(double v) throws IOException;

    void writeChars(String s) throws IOException;

    void write2le(int v) throws IOException;

    void write4le(int v) throws IOException;

    void write3le(int v) throws IOException;

    void write8le(long v) throws IOException;

    void writeFle(float v) throws IOException;

    void writeDle(double v) throws IOException;

    void setLength(long len) throws IOException;

    void writeAle(Object arr, int off, int size) throws IOException;

    void writeAbe(Object arr, int off, int size) throws IOException;

    void writeAle(Class astype, Object arr, int off, int size) throws IOException;

    void writeAbe(Class astype, Object arr, int off, int size) throws IOException;

    int writeUTFbe(String str) throws IOException;

    int writeUTFle(String str) throws IOException;

    OutputStream asOutputStream();


    void writeC(char v) throws IOException;

    void write2(int v) throws IOException;

    void write4(int v) throws IOException;

    void write3(int v) throws IOException;

    void write8(long v) throws IOException;

    void writeF(float v) throws IOException;

    void writeD(double v) throws IOException;

    void writeA(Object arr, int off, int size) throws IOException;

    void writeA(Class astype, Object arr, int off, int size) throws IOException;

    void writeNull(int bytes) throws IOException;

    void writeStr(String data, String code, int align) throws IOException;

}
