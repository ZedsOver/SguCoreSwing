/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.lang;

import java.util.Comparator;

/**
 *
 * @author armax
 */
public class LegacySort {

    private static final int INSERTIONSORT_THRESHOLD = 7;

    private static void swap(Object[] x, int a, int b)
    {
        Object t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    private static void mergeSort(Object[] src,
            Object[] dest,
            int low,
            int high,
            int off)
    {
        int length = high - low;

        // Insertion sort on smallest arrays
        if (length < INSERTIONSORT_THRESHOLD) {
            for (int i = low; i < high; i++) {
                for (int j = i; j > low
                        && ((Comparable) dest[j - 1]).compareTo(dest[j]) > 0; j--) {
                    swap(dest, j, j - 1);
                }
            }
            return;
        }

        // Recursively sort halves of dest into src
        int destLow = low;
        int destHigh = high;
        low += off;
        high += off;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid, -off);
        mergeSort(dest, src, mid, high, -off);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (((Comparable) src[mid - 1]).compareTo(src[mid]) <= 0) {
            System.arraycopy(src, low, dest, destLow, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && ((Comparable) src[p]).compareTo(src[q]) <= 0) {
                dest[i] = src[p++];
            }
            else {
                dest[i] = src[q++];
            }
        }
    }

    private static void mergeSort(Object[] src,
            Object[] dest,
            int low, int high, int off,
            Comparator c)
    {
        int length = high - low;

        // Insertion sort on smallest arrays
        if (length < INSERTIONSORT_THRESHOLD) {
            for (int i = low; i < high; i++) {
                for (int j = i; j > low && c.compare(dest[j - 1], dest[j]) > 0; j--) {
                    swap(dest, j, j - 1);
                }
            }
            return;
        }

        // Recursively sort halves of dest into src
        int destLow = low;
        int destHigh = high;
        low += off;
        high += off;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid, -off, c);
        mergeSort(dest, src, mid, high, -off, c);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (c.compare(src[mid - 1], src[mid]) <= 0) {
            System.arraycopy(src, low, dest, destLow, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0) {
                dest[i] = src[p++];
            }
            else {
                dest[i] = src[q++];
            }
        }
    }

    public static void legacyMergeSort(Object[] a)
    {
        Object[] aux = a.clone();
        mergeSort(aux, a, 0, a.length, 0);
    }

    public static void legacyMergeSort(Object[] a,
            int fromIndex, int toIndex)
    {
        Object[] aux = ArrayUtil.copyOfRange(a, fromIndex, toIndex);
        mergeSort(aux, a, fromIndex, toIndex, -fromIndex);
    }

    public static <T> void legacyMergeSort(T[] a, Comparator<? super T> c)
    {
        T[] aux = a.clone();
        if (c == null) {
            mergeSort(aux, a, 0, a.length, 0);
        }
        else {
            mergeSort(aux, a, 0, a.length, 0, c);
        }
    }

    public static <T> void legacyMergeSort(T[] a, int fromIndex, int toIndex,
            Comparator<? super T> c)
    {
        T[] aux = ArrayUtil.copyOfRange(a, fromIndex, toIndex);
        if (c == null) {
            mergeSort(aux, a, fromIndex, toIndex, -fromIndex);
        }
        else {
            mergeSort(aux, a, fromIndex, toIndex, -fromIndex, c);
        }
    }
}
