/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author armax
 */
public abstract class BufferedData<A, T> implements Iterable<T> {

    public int length;
    public A data;

    public BufferedData(Class c)
    {
        data = (A) Array.newInstance(c, 0);
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>() {
            int pits = 0;

            @Override
            public boolean hasNext()
            {
                return pits < length;
            }

            @Override
            public T next()
            {
                return (T) Array.get(data, pits++);
            }

            @Override
            public void remove()
            {
            }
        };
    }

    public final void init(int len)
    {
        length = 0;
        if (Array.getLength(data) < len) {
            data = (A) Array.newInstance(data.getClass().getComponentType(), len);
        }
    }

    public void addI(int e, T... v)
    {
        Object datac = this.data;
        System.arraycopy(datac, e, datac, e + v.length, length - e);
        for (T b : v) {
            Array.set(datac, e++, b);
        }
        length += v.length;
    }

    public void add(T... v)
    {
        for (T b : v) {
            Array.set(this.data, length++, b);
        }
    }

    public abstract boolean exists(T... v);

    public abstract void add(T v);

    public abstract int addIfNot(T... v);

    public static class BufferedDouble extends BufferedData<double[], Double> {

        public BufferedDouble()
        {
            super(double.class);
        }

        public BufferedDouble(int sz)
        {
            super(double.class);
            init(sz);
        }

        @Override
        public boolean exists(Double... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void add(Double v)
        {
            data[length++] = v;
        }

        @Override
        public int addIfNot(Double... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

    }

    public static class BufferedFloat extends BufferedData<float[], Float> {

        public BufferedFloat()
        {
            super(float.class);
        }

        public BufferedFloat(int sz)
        {
            super(float.class);
            init(sz);
        }

        @Override
        public boolean exists(Float... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void add(Float v)
        {
            data[length++] = v;
        }

        @Override
        public int addIfNot(Float... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

    }

    public static class BufferedObject<T> extends BufferedData<T[], T> {

        public BufferedObject(Class c)
        {
            super(c);
        }

        public BufferedObject(Class c, int sz)
        {
            super(c);
            init(sz);
        }

        @Override
        public boolean exists(T... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void add(T v)
        {
            data[length++] = v;
        }

        public int addIfNotE(T... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (!v[k].equals(data[j])) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

        @Override
        public int addIfNot(T... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

        public int addIfNot(Comparator<T> c, T... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (c.compare(v[k], data[j]) != 0) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

    }

    public static class BufferedByte extends BufferedData<byte[], Byte> {

        public BufferedByte()
        {
            super(byte.class);
        }

        public BufferedByte(int sz)
        {
            super(byte.class);
            init(sz);
        }

        @Override
        public boolean exists(Byte... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void add(Byte v)
        {
            data[length++] = v;
        }

        @Override
        public int addIfNot(Byte... v)
        {

            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

    }

    public static class BufferedLong extends BufferedData<long[], Long> {

        public BufferedLong()
        {
            super(long.class);
        }

        public BufferedLong(int sz)
        {
            super(long.class);
            init(sz);
        }

        @Override
        public boolean exists(Long... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0; k < v.length; k++) {
                    if (v[k] != data[i + k]) {
                        continue dd;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void add(Long v)
        {
            data[length++] = v;
        }

        @Override
        public int addIfNot(Long... v)
        {

            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

    }

    public static class BufferedInt extends BufferedData<int[], Integer> {

        public BufferedInt()
        {
            super(int.class);
        }

        public BufferedInt(int sz)
        {
            super(int.class);
            init(sz);
        }

        @Override
        public boolean exists(Integer... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0; k < v.length; k++) {
                    if (v[k] != data[i + k]) {
                        continue dd;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void add(Integer v)
        {
            data[length++] = v;
        }

        @Override
        public int addIfNot(Integer... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

    }

    public static class BufferedShort extends BufferedData<short[], Short> {

        public BufferedShort()
        {
            super(int.class);
        }

        public BufferedShort(int sz)
        {
            super(short.class);
            init(sz);
        }

        @Override
        public boolean exists(Short... v)
        {
            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0; k < v.length; k++) {
                    if (v[k] != data[i + k]) {
                        continue dd;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void add(Short v)
        {
            data[length++] = v;
        }

        @Override
        public int addIfNot(Short... v)
        {

            dd:
            for (int i = 0; i < length; i += v.length) {
                for (int k = 0, j = i; k < v.length; j++, k++) {
                    if (v[k] != data[j]) {
                        continue dd;
                    }
                }
                return i / v.length;
            }
            for (int k = 0; k < v.length; k++) {
                add(v[k]);
            }
            return length / v.length - 1;
        }

    }

}
