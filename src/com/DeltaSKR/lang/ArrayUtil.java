package com.DeltaSKR.lang;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author ARMAX
 */
public class ArrayUtil {

    //<editor-fold defaultstate="collapsed" desc="CopyRange">
    public static Object copyOfRange(Object original, int from, int to, Class newType)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        Object copy = (newType == Object[].class)
                ? new Object[newLength]
                : Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                Math.min(Array.getLength(original) - from, newLength));
        return copy;
    }

    public static byte[] copyOfRange(byte[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static short[] copyOfRange(short[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        short[] copy = new short[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static int[] copyOfRange(int[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        int[] copy = new int[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static long[] copyOfRange(long[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        long[] copy = new long[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static char[] copyOfRange(char[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        char[] copy = new char[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static float[] copyOfRange(float[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        float[] copy = new float[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static double[] copyOfRange(double[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        double[] copy = new double[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static boolean[] copyOfRange(boolean[] original, int from, int to)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        boolean[] copy = new boolean[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CopyOf">
    public static byte[] copyOf(byte[] original, int newLength)
    {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static short[] copyOf(short[] original, int newLength)
    {
        short[] copy = new short[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static int[] copyOf(int[] original, int newLength)
    {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static long[] copyOf(long[] original, int newLength)
    {
        long[] copy = new long[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static char[] copyOf(char[] original, int newLength)
    {
        char[] copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static float[] copyOf(float[] original, int newLength)
    {
        float[] copy = new float[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static double[] copyOf(double[] original, int newLength)
    {
        double[] copy = new double[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static boolean[] copyOf(boolean[] original, int newLength)
    {
        boolean[] copy = new boolean[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    //</editor-fold>
    public static <T> T convertToPrimitives(Object ref, List primitives)
    {
        int i = 0;
        if (ref == null) {
            return null;
        }
        final Class primitive = ref instanceof Class ? (Class) ref : ref.getClass().getComponentType();
        if (!primitive.isPrimitive()) {
            return null;
        }
        if (ref instanceof Class) {
            ref = null;
        }
        final Object array;
        if (ref == null || Array.getLength(ref) < primitives.size()) {
            array = Array.newInstance(primitive, primitives.size());
        }
        else {
            array = ref;
        }
        if (primitives.size() == 0) {
            return (T) array;
        }
        if (primitive == byte.class) {
            final byte[] ret = (byte[]) array;
            for (byte v : (ArrayList<Byte>) primitives) {
                ret[i++] = v;
            }
        }
        else if (primitive == short.class) {
            final short[] ret = (short[]) array;
            for (short v : (ArrayList<Short>) primitives) {
                ret[i++] = v;
            }
        }
        else if (primitive == char.class) {
            final char[] ret = (char[]) array;
            for (char v : (ArrayList<Character>) primitives) {
                ret[i++] = v;
            }
        }
        else if (primitive == int.class) {
            final int[] ret = (int[]) array;
            for (int v : (ArrayList<Integer>) primitives) {
                ret[i++] = v;
            }
        }
        else if (primitive == long.class) {
            final long[] ret = (long[]) array;
            for (long v : (ArrayList<Long>) primitives) {
                ret[i++] = v;
            }
        }
        else if (primitive == float.class) {
            final float[] ret = (float[]) array;
            for (float v : (ArrayList<Float>) primitives) {
                ret[i++] = v;
            }
        }
        else if (primitive == double.class) {
            final double[] ret = (double[]) array;
            for (double v : (ArrayList<Double>) primitives) {
                ret[i++] = v;
            }
        }
        return (T) array;
    }

    public static int countIfNotNull(Object[] subs)
    {
        if (subs == null) {
            return 0;
        }
        int c = 0;
        for (Object cc : subs) {
            if (cc != null) {
                c++;
            }
        }
        return c;
    }

    public static void inverse(byte[] pam, int off, int sz)
    {
        int j = off + sz - 1;
        sz = (sz >> 1) + off;
        for (int i = off; i < sz; i++, j--) {
            pam[i] ^= pam[j];
            pam[j] ^= pam[i];
            pam[i] ^= pam[j];
        }
    }

    private static abstract class Foxy implements Iterator, Iterable {

        protected int off, end;

        public Foxy(int len, int off, int end)
        {
            this.off = off < 0 ? len - off : off;
            if (this.off < 0) {
                throw new ArrayIndexOutOfBoundsException("Error negative offset:" + this.off);
            }
            this.end = end < 0 ? len : off + end;
            if (this.off < 0 || this.end > len || this.end < this.off) {
                throw new ArrayIndexOutOfBoundsException(String.format("Error out of bounds offset:%d endOffset:%d len:%d", this.off, this.end, len));
            }
        }

        @Override
        public boolean hasNext()
        {
            return off < end;
        }

        @Override
        public void remove()
        {
        }

        @Override
        public Iterator iterator()
        {
            return this;
        }

    }

    public static Iterable<Byte> forArray(final byte[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static Iterable<Character> forArray(final char[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static Iterable<Short> forArray(final short[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static Iterable<Integer> forArray(final int[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static Iterable<Float> forArray(final float[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static Iterable<Long> forArray(final long[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static Iterable<Double> forArray(final double[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static <T> Iterable<T> forArray(final T[] array, int off, int size)
    {
        return new Foxy(array.length, off, size) {
            public Object next()
            {
                return array[off++];
            }
        };
    }

    public static interface ForDim<T> {

        public void element(T data, int[] idx);
    }

    public static <T> void arrayFor(Object em, ForDim<T> fore)
    {
        if (em == null) {
            return;
        }
        int dim = 0;
        Class pam = em.getClass();
        while (pam.isArray()) {
            dim++;
            pam = pam.getComponentType();
        }
        int[] bi = new int[dim - 1];
        Object[][] disc = new Object[dim - 1][];
        disc[0] = (Object[]) em;
        int deep = 0;
        while (deep > -1 && bi[0] <= disc[0].length) {
            if (bi[deep] < disc[deep].length) {
                if (disc[deep][bi[deep]] == null) {
                    bi[deep]++;
                }
                else if (deep + 1 == bi.length) {
                    fore.element((T) disc[deep][bi[deep]], bi);
                    bi[deep]++;
                }
                else {
                    disc[deep + 1] = (Object[]) disc[deep][bi[deep]++];
                    deep++;
                    if (disc[deep].length == 0) {
                        deep--;
                    }
                }
            }
            else {
                bi[deep] = 0;
                deep--;
            }
        }
    }

    public static void swap(Object array, int len, Object src, int dst)
    {
        int i = indexOf(array, len, src);
        Object a = Array.get(array, dst);
        Array.set(array, i, a);
        Array.set(array, dst, src);
    }

    public static void swap(Object array, int src, int dst)
    {
        Object a = Array.get(array, src);
        Object b = Array.get(array, dst);
        Array.set(array, dst, a);
        Array.set(array, src, b);
    }

    public static void swap(Object array, Object src, int dst)
    {
        swap(array, Array.getLength(array), src, dst);
    }

    public static Object oremove(Object arr, Object o, int length)
    {
        for (int i = 0; i < length; i++) {
            if (Array.get(arr, i) == o) {
                return oremove(arr, i, length);
            }
        }
        return null;
    }

    public static Object oremove(Object arr, int i, int length)
    {
        if (i > -1 && i < length) {
            Object p = Array.get(arr, i);
            int numMoved = length - i - 1;
            if (numMoved > 0) {
                System.arraycopy(arr, i + 1, arr, i, numMoved);
            }
            if (!arr.getClass().getComponentType().isPrimitive()) {//fix store exception
                Array.set(arr, length - 1, null);
            }
            return p;
        }
        return null;
    }

    public static Object delete(Object arr, int i, int delsz, int length)
    {
        if (delsz > 0 && i > -1 && i + delsz <= length) {
            int numMoved = length - i - delsz;
            if (numMoved > 0) {
                System.arraycopy(arr, i + delsz, arr, i, numMoved);
            }
            if (!arr.getClass().getComponentType().isPrimitive()) {//fix store exception
                Array.set(arr, length - delsz, null);
            }
            return arr;
        }
        return null;
    }

    public static Object delete(Object arr, Object o, int length)
    {
        for (int i = 0; i < length; i++) {
            if (Array.get(arr, i) == o) {
                return delete(arr, i, length);
            }
        }
        return null;
    }

    public static Object delete(Object arr, int i, int length)
    {
        if (i > -1 && i < length) {
            Object p = Array.get(arr, i);
            int numMoved = length - i - 1;
            if (numMoved > 0) {
                System.arraycopy(arr, i + 1, arr, i, numMoved);
            }
            Array.set(arr, length - 1, null);
            return arr;
        }
        return null;
    }

    public static Object insert(Object a, int length, Object e)
    {
        return insert(a, length, length, e);
    }

    public static Object insert(Object a, int length, int pos, Object e)
    {
        if (pos > -1 && pos <= length) {
            a = increaseArraySize(a, length, 1);
            System.arraycopy(a, pos, a, pos + 1, length - pos);
            Array.set(a, pos, e);
        }
        return a;
    }

    /**
     *
     * @param a
     * @param fromIndex
     * @param toIndex
     * @param key
     * @param co
     * @return
     */
    public static int addSorted(Object[] a, int fromIndex, int toIndex, Object key, Comparator co)
    {
        if (co == null) {
            return addSorted(a, fromIndex, toIndex, key);
        }
        else {
            int mid = fromIndex;
            if (toIndex - fromIndex > 0) {
                int low = fromIndex;
                int high = toIndex - 1;
                while (low <= high) {
                    mid = (low + high) >>> 1;
                    int cmp = co.compare(a[mid], key);
                    if (cmp < 0) {//menor a key
                        low = ++mid;
                        if (mid == toIndex || (mid < toIndex && co.compare(a[mid], key) > 0)) {
                            break;
                        }
                    }
                    else if (cmp > 0) {//mayor a key
                        if (mid == fromIndex || (mid > fromIndex && co.compare(a[mid - 1], key) < 0)) {
                            break;
                        }
                        high = mid - 1;
                    }
                    else {
                        break;
                    }
                }
            }
            System.arraycopy(a, mid, a, mid + 1, toIndex - mid);
            a[mid] = key;
            return mid;
        }
    }

    /**
     *
     * @param a
     * @param fromIndex
     * @param toIndex
     * @param key
     * @return
     */
    public static int addSorted(Object[] a, int fromIndex, int toIndex, Object key)
    {
        int mid = fromIndex;
        if (toIndex - fromIndex > 0) {
            int low = fromIndex;
            int high = toIndex - 1;
            while (low <= high) {
                mid = (low + high) >>> 1;
                int cmp = ((Comparable) a[mid]).compareTo(key);
                if (cmp < 0) {//menor a key
                    low = ++mid;
                    if (mid == toIndex || (mid < toIndex && ((Comparable) a[mid]).compareTo(key) > 0)) {
                        break;
                    }
                }
                else if (cmp > 0) {//mayor a key
                    if (mid == fromIndex || (mid > fromIndex && ((Comparable) a[mid - 1]).compareTo(key) < 0)) {
                        break;
                    }
                    high = mid - 1;
                }
                else {
                    break;
                }
            }
        }
        System.arraycopy(a, mid, a, mid + 1, toIndex - mid);
        a[mid] = key;
        return mid;
    }

    public static int addSorted(float[] a, int fromIndex, int toIndex, float key)
    {
        int mid = fromIndex;
        if (toIndex - fromIndex > 0) {
            int low = fromIndex;
            int high = toIndex - 1;
            while (low <= high) {
                mid = (low + high) >>> 1;
                int cmp = Float.compare(a[mid], key);
                if (cmp < 0) {//menor a key
                    low = ++mid;
                    if (mid == toIndex || (mid < toIndex && Float.compare(a[mid], key) > 0)) {
                        break;
                    }
                }
                else if (cmp > 0) {//mayor a key
                    if (mid == fromIndex || (mid > fromIndex && Float.compare(a[mid - 1], key) < 0)) {
                        break;
                    }
                    high = mid - 1;
                }
                else {
                    break;
                }
            }
        }
        System.arraycopy(a, mid, a, mid + 1, toIndex - mid);
        a[mid] = key;
        return mid;
    }

    public static int addSorted(int[] a, int fromIndex, int toIndex, int interval, int key)
    {
        int cat = fromIndex;
        fromIndex = 0;
        fromIndex /= interval;
        toIndex -= cat;
        toIndex /= interval;

        int mid = fromIndex;
        if (toIndex - fromIndex > 0) {
            int low = fromIndex;
            int high = toIndex - 1;
            while (low <= high) {
                mid = (low + high) >>> 1;
                int cmp = compareInt(a[cat + mid * interval], key);
                if (cmp < 0) {//menor a key
                    low = ++mid;
                    if (mid == toIndex || (mid < toIndex && compareInt(a[cat + mid * interval], key) > 0)) {
                        break;
                    }
                }
                else if (cmp > 0) {//mayor a key
                    if (mid == fromIndex || (mid > fromIndex && compareInt(a[cat + (mid - 1) * interval], key) < 0)) {
                        break;
                    }
                    high = mid - 1;
                }
                else {
                    break;
                }
            }
        }

        System.arraycopy(a, cat + mid * interval, a, cat + (mid + 1) * interval, (toIndex - mid) * interval);
        a[cat + mid * interval] = key;
        return cat + mid * interval;
    }

    public static int addSorted(int[] a, int fromIndex, int toIndex, int key)
    {

        int mid = fromIndex;
        if (toIndex - fromIndex > 0) {
            int low = fromIndex;
            int high = toIndex - 1;
            while (low <= high) {
                mid = (low + high) >>> 1;
                int cmp = compareInt(a[mid], key);
                if (cmp < 0) {//menor a key
                    low = ++mid;
                    if (mid == toIndex || (mid < toIndex && compareInt(a[mid], key) > 0)) {
                        break;
                    }
                }
                else if (cmp > 0) {//mayor a key
                    if (mid == fromIndex || (mid > fromIndex && compareInt(a[mid - 1], key) < 0)) {
                        break;
                    }
                    high = mid - 1;
                }
                else {
                    break;
                }
            }
        }
        System.arraycopy(a, mid, a, mid + 1, toIndex - mid);
        a[mid] = key;
        return mid;
    }

    /**
     * @param arr
     * @param pointersize
     * @param addsize
     * @return
     */
    public static Object increaseArraySize(Object arr, int pointersize, int addsize)
    {
        //si el excedente es mayor este se ignora
        int ac = Array.getLength(arr) - pointersize;//size of
        if (ac >= addsize) {//fix error resize when haves same lengths
            return arr;
        }
        Object newArr = Array.newInstance(
                arr.getClass().getComponentType(),
                Array.getLength(arr) + addsize - ac
        );
        System.arraycopy(arr, 0, newArr, 0, pointersize);
        return newArr;
    }

    public static int indexOf(long[] arr, int off, int len, long e)
    {
        for (int i = 0; i < len; i++) {
            if (e == arr[off++]) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(int[] arr, int off, int len, int e)
    {
        for (int i = 0; i < len; i++) {
            if (e == arr[off++]) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(byte[] arr, int off, int len, byte e)
    {
        for (int i = 0; i < len; i++) {
            if (e == arr[off++]) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(char[] arr, int off, int len, char e)
    {
        for (int i = 0; i < len; i++) {
            if (e == arr[off++]) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(short[] arr, int off, int len, short e)
    {
        for (int i = 0; i < len; i++) {
            if (e == arr[off++]) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(float[] arr, int off, int len, float e)
    {
        for (int i = 0; i < len; i++) {
            if (Float.compare(e, arr[off++]) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(double[] arr, int off, int len, double e)
    {
        for (int i = 0; i < len; i++) {
            if (Double.compare(e, arr[off++]) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(Object arr, int len, Object e)
    {
        if (e != null) {
            for (int i = 0; i < len; i++) {
                if (e.equals(Array.get(arr, i))) {
                    return i;
                }
            }
        }
        else {
            for (int i = 0; i < len; i++) {
                if (e == Array.get(arr, i)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int indexOf(Object arr, Object e)
    {
        return indexOf(arr, Array.getLength(arr), e);
    }

    static NumberFormatException forInputString(String s)
    {
        return new NumberFormatException("For input string: \"" + s + "\"");
    }

    public static int parseHInteger(String s)
            throws NumberFormatException
    {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        int result = 0;
        boolean negative = false;
        int len = s.length();
        int pit = 0;
        int digit;
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "-"
                if (firstChar == '-') {
                    negative = true;
                }
                else {
                    throw forInputString(s);
                }

                if (len == 1) // Cannot have lone "-"
                {
                    throw forInputString(s);
                }
            }
            while (len-- > (negative ? 1 : 0)) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(len), 16);
                if (digit < 0) {
                    throw forInputString(s);
                }
                if (pit > 28) {
                    if (digit == 0) {
                        continue;
                    }
                    throw forInputString(s);
                }
                result |= digit << pit;
                pit += 4;
            }
        }
        else {
            throw forInputString(s);
        }
        return negative ? -result : result;
    }

    public static long parseHLong(String s)
            throws NumberFormatException
    {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        long result = 0;
        boolean negative = false;
        int len = s.length();
        int pit = 0;
        int digit;
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "-"
                if (firstChar == '-') {
                    negative = true;
                }
                else {
                    throw forInputString(s);
                }

                if (len == 1) // Cannot have lone "-"
                {
                    throw forInputString(s);
                }
            }
            while (len-- > (negative ? 1 : 0)) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(len), 16);
                if (digit < 0) {
                    throw forInputString(s);
                }
                if (pit > 60) {
                    if (digit == 0) {
                        continue;
                    }
                    throw forInputString(s);
                }
                result |= (long) digit << pit;
                pit += 4;
            }
        }
        else {
            throw forInputString(s);
        }
        return negative ? -result : result;
    }

    public static int compareInt(int x, int y)
    {
        return x < y ? -1 : x == y ? 0 : 1;
    }

    public static int compareLong(long x, long y)
    {
        return x < y ? -1 : x == y ? 0 : 1;
    }

    public static int compareShort(short x, short y)
    {
        return x < y ? -1 : x == y ? 0 : 1;
    }

    public static int compareByte(byte x, byte y)
    {
        return x < y ? -1 : x == y ? 0 : 1;
    }

    public static int compareChar(char x, char y)
    {
        return x < y ? -1 : x == y ? 0 : 1;
    }

    /**
     *
     * @param a
     * @param key
     * @param co
     * @return
     */
    public static int binarySearch(Object[] a, Object key, Comparator co)
    {
        return binarySearch(a, 0, a.length, key, co);
    }

    /**
     *
     * @param a
     * @param key
     * @return
     */
    public static int binarySearch(Object[] a, Object key)
    {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     *
     * @param a
     * @param fromIndex
     * @param toIndex
     * @param key
     * @param co
     * @return
     */
    public static int binarySearch(Object[] a, int fromIndex, int toIndex, Object key, Comparator co)
    {
        if (co == null) {
            return binarySearch(a, fromIndex, toIndex, key);
        }
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = co.compare(a[mid], key);
            if (cmp < 0) {
                low = mid + 1;
            }
            else if (cmp > 0) {
                high = mid - 1;
            }
            else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    /**
     *
     * @param a
     * @param fromIndex
     * @param toIndex
     * @param key
     * @return
     */
    public static int binarySearch(Object[] a, int fromIndex, int toIndex, Object key)
    {
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = ((Comparable) a[mid]).compareTo(key);
            if (cmp < 0) {
                low = mid + 1;
            }
            else if (cmp > 0) {
                high = mid - 1;
            }
            else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    public static int binarySearch(byte[] a, byte key)
    {
        return binarySearch(a, 0, a.length, key);
    }

    public static int binarySearch(byte[] a, int fromIndex, int toIndex, byte key)
    {
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compareInt(a[mid], key);
            if (cmp < 0) {
                low = mid + 1;
            }
            else if (cmp > 0) {
                high = mid - 1;
            }
            else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    public static int binarySearch(float[] a, float key)
    {
        return binarySearch(a, 0, a.length, key);
    }

    public static int binarySearch(float[] a, int fromIndex, int toIndex, float key)
    {
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = Float.compare(a[mid], key);
            if (cmp < 0) {
                low = mid + 1;
            }
            else if (cmp > 0) {
                high = mid - 1;
            }
            else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    public static int binarySearch(int[] a, int key)
    {
        return binarySearch(a, 0, a.length, key);
    }

    public static int binarySearch(int[] a, int fromIndex, int toIndex, int interval, int key)
    {
        int cat = fromIndex;
        int low = 0;
        int high = (toIndex - fromIndex) / interval - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compareInt(a[cat + mid * interval], key);
            if (cmp < 0) {
                low = mid + 1;
            }
            else if (cmp > 0) {
                high = mid - 1;
            }
            else {
                return cat + mid * interval; // key found
            }
        }
        return cat - (low + 1) * interval;  // key not found
    }

    public static int binarySearch(int[] a, int fromIndex, int toIndex, int key)
    {
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compareInt(a[mid], key);
            if (cmp < 0) {
                low = mid + 1;
            }
            else if (cmp > 0) {
                high = mid - 1;
            }
            else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    //<editor-fold defaultstate="collapsed" desc="sort">
    /**
     *
     * @param numeros
     * @param co
     * @return
     */
    public static Object[] sort(boolean legacy, Object[] numeros, Comparator co)
    {
        if (co == null) {
            sort(legacy, numeros);
        }
        else if (legacy) {
            LegacySort.legacyMergeSort(numeros, co);
        }
        else {
            TimSort.sort(numeros, 0, numeros.length, co, null, 0, 0);
        }
        return numeros;
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static Object[] sort(boolean legacy, Object[] numeros)
    {
        if (legacy) {
            LegacySort.legacyMergeSort(numeros);
        }
        else {
            ComparableTimSort.sort(numeros, 0, numeros.length, null, 0, 0);
        }
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param unsigned
     * @return
     */
    public static byte[] sort(byte[] numeros, boolean unsigned)
    {
        DualPivotQuicksort.sort(numeros, unsigned);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static byte[] sort(byte[] numeros)
    {
        DualPivotQuicksort.sort(numeros, false);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static short[] sort(short[] numeros)
    {
        DualPivotQuicksort.sort(numeros);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static char[] sort(char[] numeros)
    {
        DualPivotQuicksort.sort(numeros);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static int[] sort(int[] numeros)
    {
        DualPivotQuicksort.sort(numeros);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static long[] sort(long[] numeros)
    {
        DualPivotQuicksort.sort(numeros);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static float[] sort(float[] numeros)
    {
        DualPivotQuicksort.sort(numeros);
        return (numeros);
    }

    /**
     *
     * @param numeros
     * @return
     */
    public static double[] sort(double[] numeros)
    {
        DualPivotQuicksort.sort(numeros);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @param co
     * @return
     */
    public static Object[] sort(boolean legacy, Object numeros[], int izq, int der,
            Comparator co)
    {
        if (co == null) {
            sort(legacy, numeros, izq, der);
        }
        else if (legacy) {
            LegacySort.legacyMergeSort(numeros, izq, ++der, co);
        }
        else {
            TimSort.sort(numeros, izq, ++der, co, null, 0, 0);
        }
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static Object[] sort(boolean legacy, Object numeros[], int izq, int der)
    {
        if (legacy) {
            LegacySort.legacyMergeSort(numeros, izq, ++der);
        }
        else {
            ComparableTimSort.sort(numeros, izq, ++der, null, 0, 0);
        }
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @param unsigned
     * @return
     */
    public static byte[] sort(byte numeros[], int izq, int der, boolean unsigned)
    {
        if (unsigned) {
            DualPivotQuicksort.sortU(numeros, izq, der);
        }
        else {
            DualPivotQuicksort.sort(numeros, izq, der);
        }
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static byte[] sort(byte numeros[], int izq, int der)
    {
        DualPivotQuicksort.sort(numeros, izq, der);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static short[] sort(short numeros[], int izq, int der)
    {
        DualPivotQuicksort.sort(numeros, izq, der);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static char[] sort(char numeros[], int izq, int der)
    {
        DualPivotQuicksort.sort(numeros, izq, der);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static int[] sort(int numeros[], int izq, int der)
    {
        DualPivotQuicksort.sort(numeros, izq, der);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static long[] sort(long numeros[], int izq, int der)
    {
        DualPivotQuicksort.sort(numeros, izq, der);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static float[] sort(float numeros[], int izq, int der)
    {
        DualPivotQuicksort.sort(numeros, izq, der);
        return numeros;
    }

    /**
     *
     * @param numeros
     * @param izq
     * @param der
     * @return
     */
    public static double[] sort(double numeros[], int izq, int der)
    {
        DualPivotQuicksort.sort(numeros, izq, der);
        return numeros;
    }
    //</editor-fold>

    public static void shuffle(int[] cou)
    {
        int o;
        int n;
        for (int k = 0; k < cou.length; k++) {
            o = cou[k];
            n = (int) (Math.random() * (cou.length - k)) + k;
            cou[k] = cou[n];
            cou[n] = o;
        }
    }

    public static void shuffle(byte[] cou)
    {
        byte o;
        int n;
        for (int k = 0; k < cou.length; k++) {
            o = cou[k];
            n = (int) (Math.random() * (cou.length - k)) + k;
            cou[k] = cou[n];
            cou[n] = o;
        }
    }

    public static void shuffle(int[] cou, int off, int sz)
    {
        int o;
        int n;
        sz += off;
        for (int k = off; k < sz; k++) {
            o = cou[k];
            n = (int) (Math.random() * (sz - k)) + k;
            cou[k] = cou[n];
            cou[n] = o;
        }
    }

    public static void shuffle(byte[] cou, int off, int sz)
    {
        byte o;
        int n;
        sz += off;
        for (int k = off; k < sz; k++) {
            o = cou[k];
            n = (int) (Math.random() * (sz - k)) + k;
            cou[k] = cou[n];
            cou[n] = o;
        }
    }

    public static void shuffle(Object[] cou)
    {
        Object o;
        int n;
        for (int k = 0; k < cou.length; k++) {
            o = cou[k];
            n = (int) (Math.random() * (cou.length - k)) + k;
            cou[k] = cou[n];
            cou[n] = o;
        }
    }

    public static void shuffle(Object[] cou, int off, int size)
    {
        Object o;
        int n;
        for (int k = 0; k < size; k++) {
            o = cou[k + off];
            n = (int) (Math.random() * (size - k)) + k;
            cou[k + off] = cou[n + off];
            cou[n + off] = o;
        }
    }

    public static void shuffle(ArrayList cou, int off, int size)
    {
        Object o;
        int n;
        for (int k = 0; k < size; k++) {
            o = cou.get(k + off);
            n = (int) (Math.random() * (size - k)) + k;
            cou.set(k + off, cou.get(n + off));
            cou.set(n + off, o);
        }

    }

    public static void shuffleSZ(int[] cou, int sz)
    {
        int n;
        int size = cou.length / sz;
        int[] value = new int[sz];
        for (int k = 0; k < size; k++) {
            System.arraycopy(cou, k * sz, value, 0, sz);
            n = (int) (Math.random() * (size - k)) + k;
            System.arraycopy(cou, n * sz, cou, k * sz, sz);
            System.arraycopy(value, 0, cou, n * sz, sz);
        }
    }

    public static void shuffleSZ(Object cou, int off, int len, int sz)
    {
        int n;
        int size = len / sz;
        Object tmp = Array.newInstance(cou.getClass().getComponentType(), sz);
        for (int k = 0; k < size; k++) {
            int q = off + k * sz;
            n = off + ((int) (Math.random() * (size - k)) + k) * sz;
            //swap
            System.arraycopy(cou, q, tmp, 0, sz);
            System.arraycopy(cou, n, cou, q, sz);
            System.arraycopy(tmp, 0, cou, n, sz);
        }
    }

    public static <T> void sort(boolean legacy, List<T> list, Comparator<? super T> c)
    {
        Object[] a = list.toArray();
        sort(legacy, a, (Comparator) c);
        ListIterator i = list.listIterator();
        for (int j = 0; j < a.length; j++) {
            i.next();
            i.set(a[j]);
        }
    }

    // Cloning
    /**
     * Copies the specified array, truncating or padding with nulls (if
     * necessary) so the copy has the specified length. For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain {@code null}. Such indices will
     * exist if and only if the specified length is greater than that of the
     * original array. The resulting array is of exactly the same class as the
     * original array.
     *
     * @param <T> the class of the objects in the array
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls to
     * obtain the specified length
     * @throws NegativeArraySizeException if {@code newLength} is negative
     * @throws NullPointerException if {@code original} is null
     * @since 1.6
     */
//    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] original, int newLength)
    {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    /**
     * Copies the specified array, truncating or padding with nulls (if
     * necessary) so the copy has the specified length. For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain {@code null}. Such indices will
     * exist if and only if the specified length is greater than that of the
     * original array. The resulting array is of the class {@code newType}.
     *
     * @param <U> the class of the objects in the original array
     * @param <T> the class of the objects in the returned array
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @param newType the class of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls to
     * obtain the specified length
     * @throws NegativeArraySizeException if {@code newLength} is negative
     * @throws NullPointerException if {@code original} is null
     * @throws ArrayStoreException if an element copied from {@code original} is
     * not of a runtime type that can be stored in an array of class
     * {@code newType}
     * @since 1.6
     */
//    @HotSpotIntrinsicCandidate
    public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType)
    {
        @SuppressWarnings("unchecked")
        T[] copy = ((Object) newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified range of the specified array into a new array. The
     * initial index of the range ({@code from}) must lie between zero and
     * {@code original.length}, inclusive. The value at {@code original[from]}
     * is placed into the initial element of the copy (unless
     * {@code from == original.length} or {@code from == to}). Values from
     * subsequent elements in the original array are placed into subsequent
     * elements in the copy. The final index of the range ({@code to}), which
     * must be greater than or equal to {@code from}, may be greater than
     * {@code original.length}, in which case {@code null} is placed in all
     * elements of the copy whose index is greater than or equal to
     * {@code original.length - from}. The length of the returned array will be
     * {@code to - from}.
     * <p>
     * The resulting array is of exactly the same class as the original array.
     *
     * @param <T> the class of the objects in the array
     * @param original the array from which a range is to be copied
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive. (This
     * index may lie outside the array.)
     * @return a new array containing the specified range from the original
     * array, truncated or padded with nulls to obtain the required length
     * @throws ArrayIndexOutOfBoundsException if {@code from < 0} or
     * {@code from > original.length}
     * @throws IllegalArgumentException if {@code from > to}
     * @throws NullPointerException if {@code original} is null
     * @since 1.6
     */
//    @SuppressWarnings("unchecked")
    public static <T> T[] copyOfRange(T[] original, int from, int to)
    {
        return copyOfRange(original, from, to, (Class<? extends T[]>) original.getClass());
    }

    /**
     * Copies the specified range of the specified array into a new array. The
     * initial index of the range ({@code from}) must lie between zero and
     * {@code original.length}, inclusive. The value at {@code original[from]}
     * is placed into the initial element of the copy (unless
     * {@code from == original.length} or {@code from == to}). Values from
     * subsequent elements in the original array are placed into subsequent
     * elements in the copy. The final index of the range ({@code to}), which
     * must be greater than or equal to {@code from}, may be greater than
     * {@code original.length}, in which case {@code null} is placed in all
     * elements of the copy whose index is greater than or equal to
     * {@code original.length - from}. The length of the returned array will be
     * {@code to - from}. The resulting array is of the class {@code newType}.
     *
     * @param <U> the class of the objects in the original array
     * @param <T> the class of the objects in the returned array
     * @param original the array from which a range is to be copied
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive. (This
     * index may lie outside the array.)
     * @param newType the class of the copy to be returned
     * @return a new array containing the specified range from the original
     * array, truncated or padded with nulls to obtain the required length
     * @throws ArrayIndexOutOfBoundsException if {@code from < 0} or
     * {@code from > original.length}
     * @throws IllegalArgumentException if {@code from > to}
     * @throws NullPointerException if {@code original} is null
     * @throws ArrayStoreException if an element copied from {@code original} is
     * not of a runtime type that can be stored in an array of class
     * {@code newType}.
     * @since 1.6
     */
//    @HotSpotIntrinsicCandidate
    public static <T, U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType)
    {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        @SuppressWarnings("unchecked")
        T[] copy = ((Object) newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    // Equality Testing
    /**
     * Returns <tt>true</tt> if the two specified arrays of longs are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(long[] a, long[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of ints are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(int[] a, int[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of shorts are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(short[] a, short a2[])
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of chars are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(char[] a, char[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of bytes are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(byte[] a, byte[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of booleans are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(boolean[] a, boolean[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of doubles are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * Two doubles <tt>d1</tt> and <tt>d2</tt> are considered equal if:
     * <pre>    <tt>new Double(d1).equals(new Double(d2))</tt></pre> (Unlike the
     * <tt>==</tt> operator, this method considers
     * <tt>NaN</tt> equals to itself, and 0.0d unequal to -0.0d.)
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     * @see Double#equals(Object)
     */
    public static boolean equals(double[] a, double[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(a2[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of floats are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * Two floats <tt>f1</tt> and <tt>f2</tt> are considered equal if:
     * <pre>    <tt>new Float(f1).equals(new Float(f2))</tt></pre> (Unlike the
     * <tt>==</tt> operator, this method considers
     * <tt>NaN</tt> equals to itself, and 0.0f unequal to -0.0f.)
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     * @see Float#equals(Object)
     */
    public static boolean equals(float[] a, float[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (Float.floatToIntBits(a[i]) != Float.floatToIntBits(a2[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of Objects are
     * <i>equal</i> to one another. The two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. Two objects <tt>e1</tt>
     * and <tt>e2</tt> are considered <i>equal</i> if <tt>(e1==null ? e2==null :
     * e1.equals(e2))</tt>. In other words, the two arrays are equal if they
     * contain the same elements in the same order. Also, two array references
     * are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(Object[] a, Object[] a2)
    {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }

        int length = a.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            Object o1 = a[i];
            Object o2 = a2[i];
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }

        return true;
    }

    // Equality Range Testing
    /**
     * Returns <tt>true</tt> if the two specified arrays of longs are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(long[] a, int off, long[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i + off] != a2[i + off2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of ints are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(int[] a, int off, int[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i + off] != a2[i + off2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of shorts are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(short[] a, int off, short a2[], int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i + off] != a2[i + off2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of chars are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(char[] a, int off, char[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i + off] != a2[i + off2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of bytes are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(byte[] a, int off, byte[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i + off] != a2[i + off2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of booleans are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(boolean[] a, int off, boolean[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i + off] != a2[i + off2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of doubles are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * Two doubles <tt>d1</tt> and <tt>d2</tt> are considered equal if:
     * <pre>    <tt>new Double(d1).equals(new Double(d2))</tt></pre> (Unlike the
     * <tt>==</tt> operator, this method considers
     * <tt>NaN</tt> equals to itself, and 0.0d unequal to -0.0d.)
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     * @see Double#equals(Object)
     */
    public static boolean equals(double[] a, int off, double[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (Double.doubleToLongBits(a[i + off]) != Double.doubleToLongBits(a2[i + off2])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of floats are
     * <i>equal</i> to one another. Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. In other words, two arrays are
     * equal if they contain the same elements in the same order. Also, two
     * array references are considered equal if both are <tt>null</tt>.<p>
     *
     * Two floats <tt>f1</tt> and <tt>f2</tt> are considered equal if:
     * <pre>    <tt>new Float(f1).equals(new Float(f2))</tt></pre> (Unlike the
     * <tt>==</tt> operator, this method considers
     * <tt>NaN</tt> equals to itself, and 0.0f unequal to -0.0f.)
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     * @see Float#equals(Object)
     */
    public static boolean equals(float[] a, int off, float[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (Float.floatToIntBits(a[i + off]) != Float.floatToIntBits(a2[i + off2])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of Objects are
     * <i>equal</i> to one another. The two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal. Two objects <tt>e1</tt>
     * and <tt>e2</tt> are considered <i>equal</i> if <tt>(e1==null ? e2==null :
     * e1.equals(e2))</tt>. In other words, the two arrays are equal if they
     * contain the same elements in the same order. Also, two array references
     * are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(Object[] a, int off, Object[] a2, int off2, int length)
    {
        if (a != null && off < 0) {
            off = a.length + off;
        }
        if (a2 != null && off2 < 0) {
            off2 = a2.length + off2;
        }
        if (off < 0 || off2 < 0) {
            return false;
        }
        if (a == a2 && off == off2 && (length <= 0 || (a != null && a.length - off >= length))) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int szm = Math.min(a.length - off, a2.length - off2);
        if (length < 0) {
            length = szm;
        }
        else if (szm < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            Object o1 = a[i + off];
            Object o2 = a2[i + off2];
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(boolean[] array1, boolean[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(byte[] array1, byte[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(char[] array1, char[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(short[] array1, short[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(int[] array1, int[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(long[] array1, long[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(float[] array1, float[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(double[] array1, double[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equal(Object[] array1, Object[] array2, int size)
    {
        for (int index = 0; index < size; index++) {
            if (!array1[index].equals(array2[index])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the elements of the two given arrays are the same, or
     * both arrays are null.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @return whether the elements are the same.
     */
    public static boolean equalOrNull(Object[] array1, Object[] array2)
    {
        return array1 == null ? array2 == null
                : equalOrNull(array1, array2, array1.length);
    }

    /**
     * Returns whether the elements of the two given arrays are the same, or
     * both arrays are null.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @param size the size of the arrays to be checked.
     * @return whether the elements are the same.
     */
    public static boolean equalOrNull(Object[] array1, Object[] array2, int size)
    {
        return array1 == null ? array2 == null
                : array2 != null
                && equal(array1, array2, size);
    }

    public static int compare(int x, int y)
    {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public static int compare(long x, long y)
    {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public static int compare(boolean x, boolean y)
    {
        return (x == y) ? 0 : (x ? 1 : -1);
    }

    public static int compare(char x, char y)
    {
        return x - y;
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(boolean[] array1, int size1,
            boolean[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(byte[] array1, int size1,
            byte[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(char[] array1, int size1,
            char[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(short[] array1, int size1,
            short[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(int[] array1, int size1,
            int[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(long[] array1, int size1,
            long[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(float[] array1, int size1,
            float[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = Float.compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(double[] array1, int size1,
            double[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = Double.compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Compares the elements of the two given arrays.
     *
     * @param array1 the first array.
     * @param size1 the size of the first array.
     * @param array2 the second array.
     * @param size2 the size of the second array.
     * @return 0 if all elements are the same, -1 if the first different element
     * in the first array is smaller than the corresponding element in the
     * second array, or 1 if it is larger.
     */
    public static int compare(Comparable[] array1, int size1,
            Comparable[] array2, int size2)
    {
        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int comparison = ObjectUtil.compare(array1[index], array2[index]);
            if (comparison != 0) {
                return comparison;
            }
        }

        return compare(size1, size2);
    }

    /**
     * Returns a shallow copy of the given array, or null if the input is null.
     *
     * @param array the array.
     * @return a shallow copy of the original array, or null if the array is
     * null.
     */
    public static <T> T[] cloneOrNull(T[] array)
    {
        return array != null ? array.clone() : null;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static boolean[] extendArray(boolean[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        boolean[] newArray = new boolean[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static boolean[] ensureArraySize(boolean[] array,
            int size,
            boolean initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new boolean[size];

            if (initialValue) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static byte[] add(byte[] array, int size, byte element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static byte[] insert(byte[] array, int size, int index, byte element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(byte[] array, int size, int index)
    {
        System.arraycopy(array, index + 1,
                array, index,
                array.length - index - 1);

        array[size - 1] = 0;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static byte[] extendArray(byte[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        byte[] newArray = new byte[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static byte[] ensureArraySize(byte[] array,
            int size,
            byte initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new byte[size];

            if (initialValue != 0) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static char[] add(char[] array, int size, char element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static char[] insert(char[] array, int size, int index, char element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(char[] array, int size, int index)
    {
        System.arraycopy(array, index + 1,
                array, index,
                array.length - index - 1);

        array[size - 1] = 0;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static char[] extendArray(char[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        char[] newArray = new char[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static char[] ensureArraySize(char[] array,
            int size,
            char initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new char[size];

            if (initialValue != 0) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static short[] add(short[] array, int size, short element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static short[] insert(short[] array, int size, int index, short element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(short[] array, int size, int index)
    {
        System.arraycopy(array, index + 1,
                array, index,
                array.length - index - 1);

        array[size - 1] = 0;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static short[] extendArray(short[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        short[] newArray = new short[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static short[] ensureArraySize(short[] array,
            int size,
            short initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new short[size];

            if (initialValue != 0) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static int[] add(int[] array, int size, int element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static int[] insert(int[] array, int size, int index, int element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(int[] array, int size, int index)
    {
        System.arraycopy(array, index + 1,
                array, index,
                array.length - index - 1);

        array[size - 1] = 0;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static int[] extendArray(int[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        int[] newArray = new int[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static int[] ensureArraySize(int[] array,
            int size,
            int initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new int[size];

            if (initialValue != 0) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static long[] add(long[] array, int size, long element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static long[] insert(long[] array, int size, int index, long element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(long[] array, int size, int index)
    {
        System.arraycopy(array, index + 1,
                array, index,
                array.length - index - 1);

        array[size - 1] = 0;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static long[] extendArray(long[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        long[] newArray = new long[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static long[] ensureArraySize(long[] array,
            int size,
            long initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new long[size];

            if (initialValue != 0L) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static float[] add(float[] array, int size, float element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static float[] insert(float[] array, int size, int index, float element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(float[] array, int size, int index)
    {
        System.arraycopy(array, index + 1,
                array, index,
                array.length - index - 1);

        array[size - 1] = 0;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static float[] extendArray(float[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        float[] newArray = new float[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static float[] ensureArraySize(float[] array,
            int size,
            float initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new float[size];

            if (initialValue != 0) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static double[] add(double[] array, int size, double element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static double[] insert(double[] array, int size, int index, double element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(double[] array, int size, int index)
    {
        System.arraycopy(array, index + 1,
                array, index,
                array.length - index - 1);

        array[size - 1] = 0;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static double[] extendArray(double[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        double[] newArray = new double[size];

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static double[] ensureArraySize(double[] array,
            int size,
            double initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = new double[size];

            if (initialValue != 0) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    /**
     * Adds the given element to the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static <T> T[] add(T[] array, int size, T element)
    {
        array = extendArray(array, size + 1);

        array[size] = element;

        return array;
    }

    /**
     * Inserts the given element in the given array. The array is extended if
     * necessary.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index at which the element is to be added.
     * @param element the element to be added.
     * @return the original array, or a copy if it had to be extended.
     */
    public static <T> T[] insert(T[] array, int size, int index, T element)
    {
        array = extendArray(array, size + 1);

        // Move the last part.
        System.arraycopy(array, index,
                array, index + 1,
                size - index);

        array[index] = element;

        return array;
    }

    /**
     * Removes the specified element from the given array.
     *
     * @param array the array.
     * @param size the original size of the array.
     * @param index the index of the element to be removed.
     */
    public static void remove(Object[] array, int size, int index)
    {
        //fixed bad removed item
        System.arraycopy(array, index + 1,
                array, index,
                size - index - 1);
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @return the original array, or a copy if it had to be extended.
     */
    public static <T> T[] extendArray(T[] array, int size)
    {
        // Reuse the existing array if possible.
        if (array.length >= size) {
            return array;
        }

        // Otherwise create and initialize a new array.
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), size);

        System.arraycopy(array, 0,
                newArray, 0,
                array.length);

        return newArray;
    }

    /**
     * Ensures the given array has a given size.
     *
     * @param array the array.
     * @param size the target size of the array.
     * @param initialValue the initial value of the elements.
     * @return the original array, or a copy if it had to be extended.
     */
    public static <T> T[] ensureArraySize(T[] array,
            int size,
            T initialValue)
    {
        // Is the existing array large enough?
        if (array.length >= size) {
            // Reinitialize the existing array.
            Arrays.fill(array, 0, size, initialValue);
        }
        else {
            // Otherwise create and initialize a new array.
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), size);

            if (initialValue != null) {
                Arrays.fill(array, 0, size, initialValue);
            }
        }

        return array;
    }

    public static <T> T redim(Class tp, T arr, int newlen)
    {
        if (arr == null || Array.getLength(arr) != newlen) {
            arr = (T) Array.newInstance(tp, newlen);
        }
        return arr;
    }

    public static <T> T redim(T arr, int newlen)
    {
        if (arr != null && Array.getLength(arr) != newlen) {
            arr = (T) Array.newInstance(arr.getClass().getComponentType(), newlen);
        }
        return arr;
    }

    public static interface EnumIndex<T> extends Iterator<T> {

        public int index();
    }

    public static <T> EnumIndex<T> newIterator(final T[] arr, final int off, final int len)
    {
        final int endpod = len + off;
        return new EnumIndex<T>() {
            private int em = off;

            @Override
            public boolean hasNext()
            {
                return em < endpod;
            }

            @Override
            public T next()
            {
                return arr[em++];
            }

            @Override
            public int index()
            {
                return em - 1;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
    }

}
