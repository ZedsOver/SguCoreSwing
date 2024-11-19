/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.json;

import java.io.Closeable;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Formatter;
import java.util.Locale;

/**
 *
 * @author ARMAX
 */
public class TABPrintStream implements Appendable, Closeable, Flushable {

    final Formatter fmt;
    private final PrintStream po;
    private final Parameter params = new Parameter();
    private boolean lastLN = true;

    public TABPrintStream(OutputStream po)
    {
        this(po, null);
    }

    public TABPrintStream(OutputStream po, Locale loc)
    {
        this(new PrintStream(po), loc);
    }

    public TABPrintStream(PrintStream po)
    {
        this(po, null);
    }

    public TABPrintStream(PrintStream po, Locale loc)
    {
        if (po == null) {
            this.po = System.out;
        }
        else {
            this.po = po;
        }
        fmt = new Formatter(this, loc != null ? loc : Locale.US);
    }

    public void lastln()
    {
        lastLN = true;
    }

    public static void main(String[] args)
    {

        TABPrintStream tab = new TABPrintStream((PrintStream) null, null);

        tab.addTAB(10);
        byte[] pilk = new byte[128];
        pilk[0] = -12;
        tab.printHexF(pilk, 0, 128, 16, 2);
        tab.println();
        float[] pilf = new float[128];
        pilf[0] = -12;
        pilf[1] = -12000000000000f;
        tab.printArrayF(pilf, 0, 128, 16, true);
        tab.println();
        tab.addTAB(3);
        tab.p().add(123);
        tab.printfln("h\nola \n%012X");
        tab.addTAB(-3);
        tab.addTAB(-10);
        tab.println();
        tab.println("pops");
        tab.println();
    }

    /*
     * The following private methods on the text- and character-output streams
     * always flush the stream buffers, so that writes to the underlying byte
     * stream occur as promptly as with the original PrintStream.
     */
    private void write(char buf[])
    {
        int ini = 0;
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == '\n') {
                po.print(new String(buf, ini, i));
                println();
                ini = i + 1;
            }
        }
        if (ini != buf.length) {
            if (ini == 0) {
                po.print(buf);
            }
            else {
                po.print(new String(buf, ini, buf.length));
            }
        }
    }

    private void printTabs(int size)
    {
        if (useindent && size > 0 && lastLN) {
            if (tab > 0) {
                po.printf("%" + tab + "s", cup);
            }
            lastLN = false;
        }
    }

    private void write(String s)
    {
        int ulf;
        int ini = 0;
        do {
            ulf = s.indexOf('\n', ini);
            if (ulf > -1) {
                printTabs(ulf - ini);
                po.print(s.substring(ini, ulf));
                println();
            }
            else {
                if (ini == 0) {
                    printTabs(s.length());
                    po.print(s);
                }
                else {
                    printTabs(s.length() - ini);
                    po.print(s.substring(ini, s.length()));
                }
            }
            ini = ulf + 1;
        }
        while (ulf > -1);
    }

    /**
     * Prints a boolean value. The string produced by <code>{@link
     * java.lang.String#valueOf(boolean)}</code> is translated into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param b The <code>boolean</code> to be printed
     */
    public void print(boolean b)
    {
        write(b ? "true" : "false");
    }

    /**
     * Prints a character. The character is translated into one or more bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param c The <code>char</code> to be printed
     */
    public void print(char c)
    {
        if (c == '\r') {
            println();
        }
        else {
            write(String.valueOf(c));
        }
    }

    /**
     * Prints an integer. The string produced by <code>{@link
     * java.lang.String#valueOf(int)}</code> is translated into bytes according
     * to the platform's default character encoding, and these bytes are written
     * in exactly the manner of the <code>{@link #write(int)}</code> method.
     *
     * @param i The <code>int</code> to be printed
     * @see java.lang.Integer#toString(int)
     */
    public void print(int i)
    {
        write(String.valueOf(i));
    }

    /**
     * Prints a long integer. The string produced by <code>{@link
     * java.lang.String#valueOf(long)}</code> is translated into bytes according
     * to the platform's default character encoding, and these bytes are written
     * in exactly the manner of the <code>{@link #write(int)}</code> method.
     *
     * @param l The <code>long</code> to be printed
     * @see java.lang.Long#toString(long)
     */
    public void print(long l)
    {
        write(String.valueOf(l));
    }

    /**
     * Prints a floating-point number. The string produced by <code>{@link
     * java.lang.String#valueOf(float)}</code> is translated into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param f The <code>float</code> to be printed
     * @see java.lang.Float#toString(float)
     */
    public void print(float f)
    {
        write(String.valueOf(f));
    }

    /**
     * Prints a double-precision floating-point number. The string produced by
     * <code>{@link java.lang.String#valueOf(double)}</code> is translated into
     * bytes according to the platform's default character encoding, and these
     * bytes are written in exactly the manner of the <code>{@link
     * #write(int)}</code> method.
     *
     * @param d The <code>double</code> to be printed
     * @see java.lang.Double#toString(double)
     */
    public void print(double d)
    {
        write(String.valueOf(d));
    }

    /**
     * Prints an array of characters. The characters are converted into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param s The array of chars to be printed
     *
     * @throws NullPointerException If <code>s</code> is <code>null</code>
     */
    public void print(char s[])
    {
        write(s);
    }

    /**
     * Prints a string. If the argument is <code>null</code> then the string
     * <code>"null"</code> is printed. Otherwise, the string's characters are
     * converted into bytes according to the platform's default character
     * encoding, and these bytes are written in exactly the manner of the
     * <code>{@link #write(int)}</code> method.
     *
     * @param s The <code>String</code> to be printed
     */
    public void print(String s)
    {
        if (s == null) {
            s = "null";
        }
        write(s);
    }

    /**
     * Prints an object. The string produced by the <code>{@link
     * java.lang.String#valueOf(Object)}</code> method is translated into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param obj The <code>Object</code> to be printed
     * @see java.lang.Object#toString()
     */
    public void print(Object obj)
    {
        write(String.valueOf(obj));
    }

    public void println()
    {
        po.println();
        lastLN = true;
    }

    /**
     * Prints a boolean and then terminate the line. This method behaves as
     * though it invokes <code>{@link #print(boolean)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>boolean</code> to be printed
     */
    public void println(boolean x)
    {
        print(x);
        println();
    }

    /**
     * Prints a character and then terminate the line. This method behaves as
     * though it invokes <code>{@link #print(char)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>char</code> to be printed.
     */
    public void println(char x)
    {
        print(x);
        println();
    }

    /**
     * Prints an integer and then terminate the line. This method behaves as
     * though it invokes <code>{@link #print(int)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>int</code> to be printed.
     */
    public void println(int x)
    {
        print(x);
        println();
    }

    /**
     * Prints a long and then terminate the line. This method behaves as though
     * it invokes <code>{@link #print(long)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x a The <code>long</code> to be printed.
     */
    public void println(long x)
    {
        print(x);
        println();
    }

    /**
     * Prints a float and then terminate the line. This method behaves as though
     * it invokes <code>{@link #print(float)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>float</code> to be printed.
     */
    public void println(float x)
    {
        print(x);
        println();
    }

    /**
     * Prints a double and then terminate the line. This method behaves as
     * though it invokes <code>{@link #print(double)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>double</code> to be printed.
     */
    public void println(double x)
    {
        print(x);
        println();
    }

    /**
     * Prints an array of characters and then terminate the line. This method
     * behaves as though it invokes <code>{@link #print(char[])}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x an array of chars to print.
     */
    public void println(char x[])
    {
        print(x);
        println();
    }

    /**
     * Prints a String and then terminate the line. This method behaves as
     * though it invokes <code>{@link #print(String)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>String</code> to be printed.
     */
    public void println(String x)
    {
        print(x);
        println();
    }

    /**
     * Prints an Object and then terminate the line. This method calls at first
     * String.valueOf(x) to get the printed object's string value, then behaves
     * as though it invokes <code>{@link #print(String)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>Object</code> to be printed.
     */
    public void println(Object x)
    {
        String s = String.valueOf(x);
        print(s);
        println();
    }

    @Override
    public void flush()
    {
        po.flush();
    }

    @Override
    public void close()
    {
        po.close();
    }
    private int tab = 0;

    public final void addTAB(int i)
    {
        if (tab + i < 0) {
            tab = 0;
        }
        else {
            tab += i;
        }
    }
    private final Object[] cup = {" "};

    public void printTab()
    {
        if (useindent && tab > 0) {
            po.printf("%" + tab + "s", cup);
        }
    }

    
    public Parameter p()
    {
        return params.init();
    }

    public void printHex(Object data, int off, int sz)
    {
        printHexF(data, off, sz, 0, 0);
    }

    public void printHex(Object data, int off, int sz, int lsz)
    {
        printHexF(data, off, sz, lsz, 0);
    }

    public void printHexF(Object data, int off, int sz, int fmtSZ)
    {
        printHexF(data, off, sz, 0, fmtSZ);
    }

    public void printHexF(Object data, int off, int sz, int lsz, int fmtSZ)
    {
        if (sz < 1) {
            sz = Array.getLength(data) - off;
        }
        String it = fmtSZ > 1 ? "%0" + fmtSZ + "X" : "%0"
                + (data instanceof byte[] ? 2
                        : data instanceof short[] ? 4
                                : data instanceof int[] ? 8
                                        : 16) + "X";
        if (data instanceof byte[]) {
            for (int j = 0; j < sz; j++) {
//                if(((byte[]) data)[j + off]==0){
//                    print("·· ");
//                    continue;
//                }
                fmt.format(it, ((byte[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
        else if (data instanceof short[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((short[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
        else if (data instanceof int[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((int[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
        else if (data instanceof long[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((long[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
    }

    public void printArrayI(Object data, int off, int sz)
    {
        printArrayIF(data, off, sz, 0, 0);
    }

    public void printArrayI(Object data, int off, int sz, int lsz)
    {
        printArrayIF(data, off, sz, lsz, 0);
    }

    public void printArrayIF(Object data, int off, int sz, int fmtSZ)
    {
        printArrayIF(data, off, sz, 0, fmtSZ);
    }

    public void printArrayIF(Object data, int off, int sz, int lsz, int fmtSZ)
    {
        if (sz < 1) {
            sz = Array.getLength(data) - off;
        }
        String it = fmtSZ > 1 ? "%" + fmtSZ + "d" : "%d";
        if (data instanceof byte[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((byte[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
        else if (data instanceof short[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((short[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
        else if (data instanceof int[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((int[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
        else if (data instanceof long[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((long[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
    }

    public void printArrayF(Object data, int off, int sz, boolean e)
    {
        printArrayFF(data, off, sz, 0, e, 0, 0);
    }

    public void printArrayF(Object data, int off, int sz, int lsz, boolean e)
    {
        printArrayFF(data, off, sz, lsz, e, 0, 0);
    }

    public void printArrayFF(Object data, int off, int sz, boolean e, int fmtSZ, int decSZ)
    {
        printArrayFF(data, off, sz, 0, e, fmtSZ, decSZ);
    }

    public String intFmt(int fmtSZ)
    {
        return fmtSZ > 1 ? "%" + fmtSZ + "d" : "%d";
    }

    public String fltFmt(boolean e, int fmtSZ, int decSZ)
    {
        return fmtSZ > 1 ? "%" + fmtSZ + (decSZ > 0 ? "." + decSZ : "") + (e ? 'e' : 'f') : (e ? "%e" : "%f");
    }

    public String hexFmt(int fmtSZ, int defsz)
    {
        return fmtSZ > 1 ? "%0" + fmtSZ + "X" : "%0" + defsz + "X";
    }

    public void printArrayFF(Object data, int off, int sz, int lsz, boolean e, int fmtSZ, int decSZ)
    {
        if (sz < 1) {
            sz = Array.getLength(data) - off;
        }
        String it = fmtSZ > 1 ? "%" + fmtSZ + (decSZ > 0 ? "." + decSZ : "") + (e ? 'e' : 'f') : (e ? "%e" : "%f");
        if (data instanceof float[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((float[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
        else if (data instanceof double[]) {
            for (int j = 0; j < sz; j++) {
                fmt.format(it, ((double[]) data)[j + off]);
                printNext(j, sz, lsz);
            }
        }
    }

    public void printStrArrayFF(String[] data, int off, int sz, int lsz, boolean line, int fmtSZ)
    {
        if (sz < 1) {
            sz = data.length - off;
        }

        String it = line ? "$%s" : "\"%s\"";
        for (int j = 0; j < sz; j++) {
            fmt.format(it, data[j + off]);
            if (lsz > 0 && sz > 1 && fmtSZ > 1 && data[j + off].length() < fmtSZ) {
                for (int u = fmtSZ - data[j + off].length(); u > 0; u--) {
                    append(' ');
                }
            }
            printNext(j, sz, lsz);
        }
    }

    public void printNext(int j, int sz, int lsz)
    {
        if (lsz > 0 && ((j + 1) % lsz) == 0) {
            if (j < sz - 1) {
                append('\n');
            }
        }
        else if (j + 1 < sz) {
            append(' ');
        }
    }

    public TABPrintStream printf(String format)
    {
        fmt.format(format, params.data);
        return this;
    }

    public TABPrintStream printf(String format, Object... args)
    {
        fmt.format(format, args);
        return this;
    }

    public void printfln(String format)
    {
        fmt.format(format, params.data);
        println();
    }

    public void printfln(String format, Object... args)
    {
        fmt.format(format, args);
        println();
    }

    /**
     * Appends the specified character sequence to this output stream.
     *
     * <p>
     * An invocation of this method of the form <tt>out.append(csq)</tt>
     * behaves in exactly the same way as the invocation
     *
     * <pre>
     *     out.print(csq.toString()) </pre>
     *
     * <p>
     * Depending on the specification of <tt>toString</tt> for the character
     * sequence <tt>csq</tt>, the entire sequence may not be appended. For
     * instance, invoking then <tt>toString</tt> method of a character buffer
     * will return a subsequence whose content depends upon the buffer's
     * position and limit.
     *
     * @param csq The character sequence to append. If <tt>csq</tt> is
     * <tt>null</tt>, then the four characters <tt>"null"</tt> are appended to
     * this output stream.
     *
     * @return This output stream
     *
     * @since 1.5
     */
    @Override
    public TABPrintStream append(CharSequence csq)
    {
        if (csq == null) {
            print("null");
        }
        else {
            print(csq.toString());
        }
        return this;
    }

    /**
     * Appends a subsequence of the specified character sequence to this output
     * stream.
     *
     * <p>
     * An invocation of this method of the form <tt>out.append(csq, start,
     * end)</tt> when <tt>csq</tt> is not <tt>null</tt>, behaves in exactly the
     * same way as the invocation
     *
     * <pre>
     *     out.print(csq.subSequence(start, end).toString()) </pre>
     *
     * @param csq The character sequence from which a subsequence will be
     * appended. If <tt>csq</tt> is <tt>null</tt>, then characters will be
     * appended as if <tt>csq</tt> contained the four characters
     * <tt>"null"</tt>.
     *
     * @param start The index of the first character in the subsequence
     *
     * @param end The index of the character following the last character in the
     * subsequence
     *
     * @return This output stream
     *
     * @throws IndexOutOfBoundsException If <tt>start</tt> or <tt>end</tt> are
     * negative, <tt>start</tt>
     * is greater than <tt>end</tt>, or <tt>end</tt> is greater than
     * <tt>csq.length()</tt>
     *
     * @since 1.5
     */
    @Override
    public TABPrintStream append(CharSequence csq, int start, int end)
    {
        CharSequence cs = (csq == null ? "null" : csq);
        write(cs.subSequence(start, end).toString());
        return this;
    }

    /**
     * Appends the specified character to this output stream.
     *
     * <p>
     * An invocation of this method of the form <tt>out.append(c)</tt>
     * behaves in exactly the same way as the invocation
     *
     * <pre>
     *     out.print(c) </pre>
     *
     * @param c The 16-bit character to append
     *
     * @return This output stream
     *
     * @since 1.5
     */
    @Override
    public TABPrintStream append(char c)
    {
        print(c);
        return this;
    }

    public int getTab()
    {
        return tab;
    }

    public void setTAB(int i)
    {
        tab = i;
    }

    public PrintStream getPo()
    {
        return po;
    }
    private boolean useindent = true;

    public void setIndent(boolean b)
    {
        useindent = b;
    }

    public static final class Parameter {

        private final Object[] data = new Object[255];
        private int pos = 0;

        public Object[] getData()
        {
            return data;
        }

        private Parameter()
        {

        }

        public Parameter init()
        {
            pos = 0;
            return this;
        }

        public Parameter addA(Object arr, int off, int size)
        {
            Class cla = arr.getClass();
            if (cla.isArray() && cla.getComponentType().isPrimitive()) {
                for (int i = 0; i < size; i++) {
                    data[pos++] = Array.get(arr, off + i);
                }
            }
            else {
                System.arraycopy(arr, off, data, pos, size);
                pos += size;
            }
            return this;
        }

        public Parameter add(byte p)
        {
            data[pos++] = new Byte(p);
            return this;
        }

        public Parameter add(short p)
        {
            data[pos++] = new Short(p);
            return this;
        }

        public Parameter add(long p)
        {
            data[pos++] = new Long(p);
            return this;
        }

        public Parameter add(char p)
        {
            data[pos++] = new Character(p);
            return this;
        }

        public Parameter add(int p)
        {
            data[pos++] = new Integer(p);
            return this;
        }

        public Parameter add(float p)
        {
            data[pos++] = new Float(p);
            return this;
        }

        public Parameter add(double p)
        {
            data[pos++] = new Double(p);
            return this;
        }

        public Parameter add(Object p, int off, int size)
        {
            size += off;
            if (p instanceof byte[]) {
                byte[] dox = (byte[]) p;
                for (int i = off; i < size; i++) {
                    add(dox[i]);
                }
            }
            else if (p instanceof short[]) {
                short[] dox = (short[]) p;
                for (int i = off; i < size; i++) {
                    add(dox[i]);
                }
            }
            else if (p instanceof int[]) {
                int[] dox = (int[]) p;
                for (int i = off; i < size; i++) {
                    add(dox[i]);
                }
            }
            else if (p instanceof long[]) {
                long[] dox = (long[]) p;
                for (int i = off; i < size; i++) {
                    add(dox[i]);
                }
            }
            else if (p instanceof float[]) {
                float[] dox = (float[]) p;
                for (int i = off; i < size; i++) {
                    add(dox[i]);
                }
            }
            else if (p instanceof double[]) {
                double[] dox = (double[]) p;
                for (int i = off; i < size; i++) {
                    add(dox[i]);
                }
            }
            else if (p instanceof String[]) {
                String[] dox = (String[]) p;
                for (int i = off; i < size; i++) {
                    add(dox[i]);
                }
            }

            return this;
        }

        public Parameter add(Object p)
        {
            if (p != null && p.getClass().isArray() && (p.getClass().getComponentType().isPrimitive() || p instanceof String[])) {
                return add(p, 0, Array.getLength(p));
            }
            data[pos++] = p;
            return this;
        }

    }
}
