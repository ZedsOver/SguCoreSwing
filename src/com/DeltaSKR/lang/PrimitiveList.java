/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DeltaSKR.lang;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author ARMAX
 * @param <T>
 */
public class PrimitiveList<T> {

    public T elements;
    protected int size;
    protected int capacity;

    public PrimitiveList()
    {
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private static class Itr implements Iterator, Iterable {

        PrimitiveList supa;
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount;

        public Itr(PrimitiveList supa)
        {
            this.supa = supa;
            expectedModCount = supa.size;
        }

        @Override
        public boolean hasNext()
        {
            return cursor != supa.size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object next()
        {
            checkForComodification();
            int i = cursor;
            if (i >= supa.size) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return ((Object[]) supa.elements)[lastRet = i];
        }

        @Override
        public void remove()
        {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                supa.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = supa.size;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification()
        {
            if (supa.size != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public Iterator iterator()
        {
            return this;
        }
    }

    public <E> Iterable<E> forIterator()
    {
        if (elements instanceof Object[]) {
            return (Iterable) new Itr(this);
        }
        throw new RuntimeException("Primitive array dont supported");
    }

    private PrimitiveList(Class type, int cap)
    {
        elements = (T) Array.newInstance(type, capacity = cap < 0 ? 16 : cap);
    }

    public void setInternal(T arr)
    {
        elements = arr;
        capacity = Array.getLength(arr);
    }

    protected final void growBy(int count)
    {
        int minCapacity = capacity + count;
        int newCapacity = (capacity * 3) / 2 + 1;
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        Object data = Array.newInstance(elements.getClass().getComponentType(), newCapacity);
        System.arraycopy(elements, 0, data, 0, size);
        elements = (T) data;
        capacity = newCapacity;
    }

    public int size()
    {
        return size;
    }

    public void setSize(int sz)
    {
        if (sz > capacity) {
            growBy(sz - size);
        }
        size = sz;
    }

    public Object toArray()
    {
        return toArray(false);
    }

    public Object toArray(boolean copy)
    {
//        if(size==0x00000A06){
//            System.out.println("com.DeltaSKR.lang.PrimitiveList.toArray()");
//        }
        try {
            if (!copy && capacity == size) {
                return elements;
            }
            Object data = Array.newInstance(elements.getClass().getComponentType(), size);
            System.arraycopy(elements, 0, data, 0, size);
            return data;
        }
        finally {
            if (!copy) {
                elements = null;
            }
        }
    }

    public static <TL extends PrimitiveList> TL newFrom(Class type)
    {
        return newFrom(type, -1);
    }

    public static <TL extends PrimitiveList> TL newFrom(Class type, int capacity)
    {
        if (type == byte.class) {
            return (TL) new ListByte(capacity);
        }
        else if (type == short.class) {
            return (TL) new ListShort(capacity);
        }
        else if (type == char.class) {
            return (TL) new ListChar(capacity);
        }
        else if (type == int.class) {
            return (TL) new ListInt(capacity);
        }
        else if (type == long.class) {
            return (TL) new ListLong(capacity);
        }
        else if (type == float.class) {
            return (TL) new ListFloat(capacity);
        }
        else if (type == double.class) {
            return (TL) new ListDouble(capacity);
        }
        else if (type == boolean.class) {
            return (TL) new ListBool(capacity);
        }
        return (TL) new ListObject(type, capacity);
    }

    public void addB(byte e)
    {
    }

    public void addS(short e)
    {
    }

    public void addC(char e)
    {
    }

    public void addI(int e)
    {
    }

    public void addL(long e)
    {
    }

    public void addF(float e)
    {
    }

    public void addD(double e)
    {
    }

    public void addO(Object e)
    {
    }

    public void addZ(boolean e)
    {
    }

    public void remove(int ul)
    {
        System.arraycopy(elements, ul + 1, elements, ul, size - ul - 1);
        size--;
    }

    public void clear()
    {
        size = 0;
    }

    public int popLast()
    {
        return --size;
    }

    public static class ListByte extends PrimitiveList<byte[]> {

        public ListByte(int cap)
        {
            super(byte.class, cap);
        }

        @Override
        public void addB(byte e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public byte last()
        {
            return size > 0 ? elements[size - 1] : -1;
        }
    }

    public static class ListShort extends PrimitiveList<short[]> {

        public ListShort(int cap)
        {
            super(short.class, cap);
        }

        @Override
        public void addS(short e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public short last()
        {
            return size > 0 ? elements[size - 1] : -1;
        }
    }

    public static class ListChar extends PrimitiveList<char[]> {

        public ListChar(int cap)
        {
            super(char.class, cap);
        }

        @Override
        public void addC(char e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public char last()
        {
            return size > 0 ? elements[size - 1] : 0xFFFF;
        }
    }

    public static class ListInt extends PrimitiveList<int[]> {

        public ListInt(int cap)
        {
            super(int.class, cap);
        }

        @Override
        public void addI(int e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public int last()
        {
            return size > 0 ? elements[size - 1] : -1;
        }
    }

    public static class ListLong extends PrimitiveList<long[]> {

        public ListLong(int cap)
        {
            super(long.class, cap);
        }

        @Override
        public void addL(long e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public long last()
        {
            return size > 0 ? elements[size - 1] : -1;
        }
    }

    public static class ListFloat extends PrimitiveList<float[]> {

        public ListFloat(int cap)
        {
            super(float.class, cap);
        }

        @Override
        public void addF(float e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public float last()
        {
            return size > 0 ? elements[size - 1] : Float.NaN;
        }
    }

    public static class ListBool extends PrimitiveList<boolean[]> {

        public ListBool(int cap)
        {
            super(boolean.class, cap);
        }

        @Override
        public void addZ(boolean e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public boolean last()
        {
            return size > 0 ? elements[size - 1] : false;
        }
    }

    public static class ListDouble extends PrimitiveList<double[]> {

        public ListDouble(int cap)
        {
            super(double.class, cap);
        }

        @Override
        public void addD(double e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = e;
            size++;
        }

        public double last()
        {
            return size > 0 ? elements[size - 1] : Double.NaN;
        }
    }

    public static class ListObject<V> extends PrimitiveList<V[]> {

        public ListObject(int cap)
        {
            super(Object.class, cap);
        }

        public ListObject(Class cls, int cap)
        {
            super(cls, cap);
        }

        @Override
        public void addO(Object e)
        {
            if (capacity == size) {
                growBy(1);
            }
            elements[size] = (V) e;
            size++;
        }

        public V last(int c)
        {
            return (V) (size > 0 ? elements[size - c - 1] : null);
        }

        public V last()
        {
            return (V) (size > 0 ? elements[size - 1] : null);
        }
    }

}
