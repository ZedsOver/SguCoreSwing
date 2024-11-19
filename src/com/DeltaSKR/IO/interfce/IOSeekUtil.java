/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

/**
 *
 * @author armax
 */
public final class IOSeekUtil {

    public static void main(String[] args)
    {
        System.out.println(int[].class.isArray());
    }

    public static void writeAbe(WriteSeek wr, Class astype, Object src, int off, int sz) throws IOException
    {
        if (src == null) {
            throw new NullPointerException();
        }
        if (!src.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        Class s = src.getClass().getComponentType();
        if (s == astype || (s == char.class && src == short.class)
                || (s == short.class && src == char.class)
                || (s == boolean.class && src == byte.class)
                || (s == byte.class && src == boolean.class)) {
            writeAbe(wr, src, off, sz);
            return;
        }
        if (sz == -1) {
            sz = Array.getLength(src);
        }
        else {
            sz += off;
        }
        if (src instanceof byte[]) {
            final byte[] a = (byte[]) src;
            if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2be(a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4be(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8be(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFbe(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDbe(a[i]);
                }
            }
        }
        else if (src instanceof char[]) {
            final char[] a = (char[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write(a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4be(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8be(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFbe(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDbe(a[i]);
                }
            }
        }
        else if (src instanceof boolean[]) {
            final boolean[] a = (boolean[]) src;
            if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2be(a[i] ? 1 : 0);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4be(a[i] ? 1 : 0);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8be(a[i] ? 1 : 0);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFbe(a[i] ? 1 : 0);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDbe(a[i] ? 1 : 0);
                }
            }
        }
        else if (src instanceof short[]) {
            final short[] a = (short[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write(a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4be(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8be(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFbe(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDbe(a[i]);
                }
            }
        }
        else if (src instanceof int[]) {
            final int[] a = (int[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write(a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2be(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8be(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFbe(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDbe(a[i]);
                }
            }
        }
        else if (src instanceof long[]) {
            final long[] a = (long[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write((int) a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2be((int) a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4be((int) a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFbe(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDbe(a[i]);
                }
            }
        }
        else if (src instanceof float[]) {
            final float[] a = (float[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write((int) a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2be((int) a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4be((int) a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8be((long) a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDbe(a[i]);
                }
            }
        }
        else if (src instanceof double[]) {
            final double[] a = (double[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write((int) a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2be((int) a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4be((int) a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8be((long) a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFbe((float) a[i]);
                }
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + src.getClass().getName());
        }
    }

    public static void writeAle(WriteSeek wr, Class astype, Object src, int off, int sz) throws IOException
    {
        if (src == null) {
            throw new NullPointerException();
        }
        if (!src.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        Class s = src.getClass().getComponentType();
        if (s == astype || (s == char.class && src == short.class)
                || (s == short.class && src == char.class)
                || (s == boolean.class && src == byte.class)
                || (s == byte.class && src == boolean.class)) {
            writeAle(wr, src, off, sz);
            return;
        }
        if (sz == -1) {
            sz = Array.getLength(src);
        }
        else {
            sz += off;
        }
        if (src instanceof byte[]) {
            final byte[] a = (byte[]) src;
            if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2le(a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4le(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8le(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFle(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDle(a[i]);
                }
            }
        }
        else if (src instanceof char[]) {
            final char[] a = (char[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write(a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4le(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8le(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFle(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDle(a[i]);
                }
            }
        }
        else if (src instanceof boolean[]) {
            final boolean[] a = (boolean[]) src;
            if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2le(a[i] ? 1 : 0);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4le(a[i] ? 1 : 0);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8le(a[i] ? 1 : 0);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFle(a[i] ? 1 : 0);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDle(a[i] ? 1 : 0);
                }
            }
        }
        else if (src instanceof short[]) {
            final short[] a = (short[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write(a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4le(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8le(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFle(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDle(a[i]);
                }
            }
        }
        else if (src instanceof int[]) {
            final int[] a = (int[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write(a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2le(a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8le(a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFle(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDle(a[i]);
                }
            }
        }
        else if (src instanceof long[]) {
            final long[] a = (long[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write((int) a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2le((int) a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4le((int) a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFle(a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDle(a[i]);
                }
            }
        }
        else if (src instanceof float[]) {
            final float[] a = (float[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write((int) a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2le((int) a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4le((int) a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8le((long) a[i]);
                }
            }
            else if (astype == double.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeDle(a[i]);
                }
            }
        }
        else if (src instanceof double[]) {
            final double[] a = (double[]) src;
            if (astype == byte.class) {
                for (int i = off; i < sz; i++) {
                    wr.write((int) a[i]);
                }
            }
            else if (astype == short.class || astype == char.class) {
                for (int i = off; i < sz; i++) {
                    wr.write2le((int) a[i]);
                }
            }
            else if (astype == int.class) {
                for (int i = off; i < sz; i++) {
                    wr.write4le((int) a[i]);
                }
            }
            else if (astype == long.class) {
                for (int i = off; i < sz; i++) {
                    wr.write8le((long) a[i]);
                }
            }
            else if (astype == float.class) {
                for (int i = off; i < sz; i++) {
                    wr.writeFle((float) a[i]);
                }
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + src.getClass().getName());
        }
    }

    public static void writeAbe(WriteSeek wr, Object src, int off, int sz) throws IOException
    {
        if (src == null) {
            throw new NullPointerException();
        }
        if (!src.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        if (sz == -1) {
            sz = Array.getLength(src);
        }
        else {
            sz += off;
        }
        if (src instanceof char[]) {
            final char[] a = (char[]) src;
            for (int i = off; i < sz; i++) {
                wr.write2be(a[i]);
            }
        }
        else if (src instanceof boolean[]) {
            final boolean[] a = (boolean[]) src;
            for (int i = off; i < sz; i++) {
                wr.write(a[i] ? 1 : 0);
            }
        }
        else if (src instanceof short[]) {
            final short[] a = (short[]) src;
            for (int i = off; i < sz; i++) {
                wr.write2be(a[i]);
            }
        }
        else if (src instanceof int[]) {
            final int[] a = (int[]) src;
            for (int i = off; i < sz; i++) {
                wr.write4be(a[i]);
            }
        }
        else if (src instanceof long[]) {
            final long[] a = (long[]) src;
            for (int i = off; i < sz; i++) {
                wr.write8be(a[i]);
            }
        }
        else if (src instanceof float[]) {
            final float[] a = (float[]) src;
            for (int i = off; i < sz; i++) {
                wr.writeFbe(a[i]);
            }
        }
        else if (src instanceof double[]) {
            final double[] a = (double[]) src;
            for (int i = off; i < sz; i++) {
                wr.writeDbe(a[i]);
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + src.getClass().getName());
        }
    }

    public static void writeAle(WriteSeek wr, Object src, int off, int sz) throws IOException
    {
        if (src == null) {
            throw new NullPointerException();
        }
        if (!src.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        if (sz == -1) {
            sz = Array.getLength(src);
        }
        else {
            sz += off;
        }
        if (src instanceof char[]) {
            final char[] a = (char[]) src;
            for (int i = off; i < sz; i++) {
                wr.write2le(a[i]);
            }
        }
        else if (src instanceof boolean[]) {
            final boolean[] a = (boolean[]) src;
            for (int i = off; i < sz; i++) {
                wr.write(a[i] ? 1 : 0);
            }
        }
        else if (src instanceof short[]) {
            final short[] a = (short[]) src;
            for (int i = off; i < sz; i++) {
                wr.write2le(a[i]);
            }
        }
        else if (src instanceof int[]) {
            final int[] a = (int[]) src;
            for (int i = off; i < sz; i++) {
                wr.write4le(a[i]);
            }
        }
        else if (src instanceof long[]) {
            final long[] a = (long[]) src;
            for (int i = off; i < sz; i++) {
                wr.write8le(a[i]);
            }
        }
        else if (src instanceof float[]) {
            final float[] a = (float[]) src;
            for (int i = off; i < sz; i++) {
                wr.writeFle(a[i]);
            }
        }
        else if (src instanceof double[]) {
            final double[] a = (double[]) src;
            for (int i = off; i < sz; i++) {
                wr.writeDle(a[i]);
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + src.getClass().getName());
        }
    }

    public static int sizeOf(Object dst)
    {
        if (dst == null) {
            throw new NullPointerException();
        }
        if (!dst.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        if (dst instanceof char[] || dst instanceof short[]) {
            return 2;
        }
        else if (dst instanceof boolean[]) {
            return 1;
        }
        else if (dst instanceof int[] || dst instanceof float[]) {
            return 4;
        }
        else if (dst instanceof long[] || dst instanceof double[]) {
            return 8;
        }
        else {
            throw new ClassCastException("Unsoppurted type " + dst.getClass().getName());
        }
    }

    public static void readAbe(ReadSeek re, Object dst, int off, int sz) throws IOException
    {
        if (dst == null) {
            throw new NullPointerException();
        }
        if (!dst.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        if (sz == -1) {
            sz = Array.getLength(dst);
        }
        else {
            sz += off;
        }
        if (dst instanceof char[]) {
            final char[] a = (char[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = (char) re.read2be();
            }
        }
        else if (dst instanceof boolean[]) {
            final boolean[] a = (boolean[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.readU() != 0;
            }
        }
        else if (dst instanceof short[]) {
            final short[] a = (short[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.read2be();
            }
        }
        else if (dst instanceof int[]) {
            final int[] a = (int[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.read4be();
            }
        }
        else if (dst instanceof long[]) {
            final long[] a = (long[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.read8be();
            }
        }
        else if (dst instanceof float[]) {
            final float[] a = (float[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.readFbe();
            }
        }
        else if (dst instanceof double[]) {
            final double[] a = (double[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.readDbe();
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + dst.getClass().getName());
        }
    }

    public static void readAle(ReadSeek re, Object dst, int off, int sz) throws IOException
    {
        if (dst == null) {
            throw new NullPointerException();
        }
        if (!dst.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        if (sz == -1) {
            sz = Array.getLength(dst);
        }
        else {
            sz += off;
        }
        if (dst instanceof char[]) {
            final char[] a = (char[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = (char) re.read2le();
            }
        }
        else if (dst instanceof boolean[]) {
            final boolean[] a = (boolean[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.readU() != 0;
            }
        }
        else if (dst instanceof short[]) {
            final short[] a = (short[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.read2le();
            }
        }
        else if (dst instanceof int[]) {
            final int[] a = (int[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.read4le();
            }
        }
        else if (dst instanceof long[]) {
            final long[] a = (long[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.read8le();
            }
        }
        else if (dst instanceof float[]) {
            final float[] a = (float[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.readFle();
            }
        }
        else if (dst instanceof double[]) {
            final double[] a = (double[]) dst;
            for (int i = off; i < sz; i++) {
                a[i] = re.readDle();
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + dst.getClass().getName());
        }
    }

    public static void readAle(ReadSeek re, Class fromType, boolean unsigned, Object dst, int off, int sz) throws IOException
    {
        if (dst == null) {
            throw new NullPointerException();
        }
        if (!dst.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        Class s = dst.getClass().getComponentType();
        if (s == fromType || (s == char.class && dst == short.class)
                || (s == short.class && dst == char.class)
                || (s == boolean.class && dst == byte.class)
                || (s == byte.class && dst == boolean.class)) {
            readAle(re, dst, off, sz);
            return;
        }
        if (sz == -1) {
            sz = Array.getLength(dst);
        }
        else {
            sz += off;
        }
        if (dst instanceof byte[]) {
            final byte[] a = (byte[]) dst;
            if (fromType == short.class || fromType == char.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.read2le();
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.read4le();
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.read8le();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.readFle();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.readDle();
                }
            }
        }
        else if (dst instanceof char[]) {
            final char[] a = (char[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = (char) re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = (char) re.read();
                    }
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.read4le();
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.read8le();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.readFle();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.readDle();
                }
            }
        }
        else if (dst instanceof boolean[]) {
            final boolean[] a = (boolean[]) dst;
            if (fromType == short.class || fromType == char.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read2le() != 0;
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read4le() != 0;
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read8le() != 0;
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.readFle() != 0;
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.readDle() != 0;
                }
            }
        }
        else if (dst instanceof short[]) {
            final short[] a = (short[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = (short) re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = (short) re.read();
                    }
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.read4le();
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.read8le();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.readFle();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.readDle();
                }
            }
        }
        else if (dst instanceof int[]) {
            final int[] a = (int[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2le();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2le();
                    }
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (int) re.read8le();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (int) re.readFle();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (int) re.readDle();
                }
            }
        }
        else if (dst instanceof long[]) {
            final long[] a = (long[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2le();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2le();
                    }
                }
            }
            else if (fromType == int.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU4le();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read4le();
                    }
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (long) re.readFle();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (long) re.readDle();
                }
            }
        }
        else if (dst instanceof float[]) {
            final float[] a = (float[]) dst;

            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2le();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2le();
                    }
                }
            }
            else if (fromType == int.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU4le();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read4le();
                    }
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read8le();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (float) re.readDle();
                }
            }
        }
        else if (dst instanceof double[]) {
            final double[] a = (double[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2le();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2le();
                    }
                }
            }
            else if (fromType == int.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU4le();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read4le();
                    }
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read8le();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.readFle();
                }
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + dst.getClass().getName());
        }
    }

    public static void readAbe(ReadSeek re, Class fromType, boolean unsigned, Object dst, int off, int sz) throws IOException
    {
        if (dst == null) {
            throw new NullPointerException();
        }
        if (!dst.getClass().isArray()) {
            throw new IllegalArgumentException("ism't an array");
        }
        Class s = dst.getClass().getComponentType();
        if (s == fromType || (s == char.class && dst == short.class)
                || (s == short.class && dst == char.class)
                || (s == boolean.class && dst == byte.class)
                || (s == byte.class && dst == boolean.class)) {
            readAbe(re, dst, off, sz);
            return;
        }
        if (sz == -1) {
            sz = Array.getLength(dst);
        }
        else {
            sz += off;
        }
        if (dst instanceof byte[]) {
            final byte[] a = (byte[]) dst;
            if (fromType == short.class || fromType == char.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.read2be();
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.read4be();
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.read8be();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.readFbe();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (byte) re.readDbe();
                }
            }
        }
        else if (dst instanceof char[]) {
            final char[] a = (char[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = (char) re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = (char) re.read();
                    }
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.read4be();
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.read8be();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.readFbe();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (char) re.readDbe();
                }
            }
        }
        else if (dst instanceof boolean[]) {
            final boolean[] a = (boolean[]) dst;
            if (fromType == short.class || fromType == char.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read2be() != 0;
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read4be() != 0;
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read8be() != 0;
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.readFbe() != 0;
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.readDbe() != 0;
                }
            }
        }
        else if (dst instanceof short[]) {
            final short[] a = (short[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = (short) re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = (short) re.read();
                    }
                }
            }
            else if (fromType == int.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.read4be();
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.read8be();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.readFbe();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (short) re.readDbe();
                }
            }
        }
        else if (dst instanceof int[]) {
            final int[] a = (int[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2be();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2be();
                    }
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (int) re.read8be();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (int) re.readFbe();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (int) re.readDbe();
                }
            }
        }
        else if (dst instanceof long[]) {
            final long[] a = (long[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2be();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2be();
                    }
                }
            }
            else if (fromType == int.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU4be();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read4be();
                    }
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (long) re.readFbe();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (long) re.readDbe();
                }
            }
        }
        else if (dst instanceof float[]) {
            final float[] a = (float[]) dst;

            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2be();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2be();
                    }
                }
            }
            else if (fromType == int.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU4be();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read4be();
                    }
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read8be();
                }
            }
            else if (fromType == double.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = (float) re.readDbe();
                }
            }
        }
        else if (dst instanceof double[]) {
            final double[] a = (double[]) dst;
            if (fromType == byte.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read();
                    }
                }
            }
            else if (fromType == short.class || fromType == char.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU2be();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read2be();
                    }
                }
            }
            else if (fromType == int.class) {
                if (unsigned) {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.readU4be();
                    }
                }
                else {
                    for (int i = off; i < sz; i++) {
                        a[i] = re.read4be();
                    }
                }
            }
            else if (fromType == long.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.read8be();
                }
            }
            else if (fromType == float.class) {
                for (int i = off; i < sz; i++) {
                    a[i] = re.readFbe();
                }
            }
        }
        else {
            throw new ClassCastException("Unsoppurted type " + dst.getClass().getName());
        }
    }

    private static class InputStreamWraper extends InputStream {

        private final ReadSeek priv;
        private final boolean os;

        public InputStreamWraper(ReadSeek priv)
        {
            this.priv = priv;
            boolean os = false;
            try {
                os = priv.seek() >= 0;
            }
            catch (IOException ex) {
            }
            this.os = os;
        }

        @Override
        public int read() throws IOException
        {
            return priv.read();
        }

        @Override
        public long skip(long n) throws IOException
        {
            return priv.skip((int) n);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException
        {
            return priv.read(b, off, len);
        }

        @Override
        public int read(byte[] b) throws IOException
        {
            return priv.read(b);
        }

        @Override
        public boolean markSupported()
        {
            return os;
        }

        @Override
        public void reset() throws IOException
        {
            if (!os) {
                super.reset();
            }
            priv.seek(0);
        }

        @Override
        public void close() throws IOException
        {
        }

    }

    public static InputStream loadAsIntputStream(ReadSeek s)
    {
        if (s == null) {
            return null;
        }
        return s instanceof InputStream ? (InputStream) s : new InputStreamWraper(s);
    }

    private static class OutputStreamWraper extends OutputStream {

        private final WriteSeek priv;

        public OutputStreamWraper(WriteSeek priv)
        {
            this.priv = priv;
        }

        @Override
        public void close() throws IOException
        {
        }

        @Override
        public void flush() throws IOException
        {

        }

        @Override
        public void write(int b) throws IOException
        {
            priv.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException
        {
            priv.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            priv.write(b, off, len);
        }

    }

    public static final OutputStream loadAsOutputStream(WriteSeek s)
    {
        if (s == null) {
            return null;
        }
        return s instanceof OutputStream ? (OutputStream) s : new OutputStreamWraper(s);
    }

    public static long alignMod(long cup, long nAlign)
    {
        return align(cup, nAlign) - cup;
    }

    public static long align(long cup, long nAlign)
    {
        if (nAlign != 0 && cup % nAlign != 0) {
            return (cup - cup % nAlign) + nAlign;
        }
        return cup;
    }

    public static final void writeNull(WriteSeek out, int bytes) throws IOException
    {
        writeNull(out, bytes, false);
    }

    public static final void writeNull(WriteSeek out, int bytes, boolean re) throws IOException
    {
        if (bytes < 1) {
            return;
        }
        if (!re && out.length() > -1 && out.seek() != -1) {
            int suck = (int) (out.length() - out.seek());
            if (suck <= bytes) {
                bytes -= suck;
            }
            else {
                out.skip(bytes);
                return;
            }
        }
        byte[] buff = new byte[0x1000];
        while (bytes >= buff.length) {
            out.write(buff, 0, buff.length);
            bytes -= buff.length;
        }
        if (bytes > 0) {
            out.write(buff, 0, bytes);
        }
    }

    public static final void writeStr(WriteSeek out, String data, String code, int align) throws IOException
    {
        byte[] dat = data.getBytes(code);
        out.write(dat);
        out.writeNull((int) alignMod(dat.length, align));
    }
}
