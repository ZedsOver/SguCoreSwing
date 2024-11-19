/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;

/**
 *
 * @author ARMAX
 */
public final class IOSys {

    public static void main(String[] args)
    {
        System.out.println(File.pathSeparatorChar + " " + File.separatorChar);
    }
    private static final String ERR_END = "Incomplete character at the end";
    private static final String ERR_WRONG = "Wrong code at byte ";
    private static final String ERR_LONG = "Encoded string is too long";

    /**
     * Decodes bytes to String using modified UTF-8 format.
     *
     * @param b byte array
     * @param len
     * @return decoded string
     * @throws UTFDataFormatException if given byte sequence is not valid UTF
     */
    public static String utfDecode(byte[] b, int len) throws UTFDataFormatException
    {
        char[] chars = new char[len];
        int count = 0;                 //count of chars read so far
        int ofs = 0;                  //offset in byte array
        int b1, b2, b3;                //bytes to compound symbols from

        while (ofs < len) {
            b1 = b[ofs] & 0xff;
            ofs++;
            if (b1 < 0x80) {  // 0xxx xxxx
                chars[count] = (char) b1;
                count++;
                continue;
            }
            switch (b1 & 0xf0) {
                case 0xc0:    // 1100 xxxx   10xx xxxx
                case 0xd0:    // 1101 xxxx   10xx xxxx
                    if (ofs + 1 > len) {
                        throw new UTFDataFormatException(ERR_END);
                    }
                    b2 = b[ofs];
                    ofs++;
                    if ((b2 & 0xc0) != 0x80) {
                        throw new UTFDataFormatException(ERR_WRONG + (ofs - 1));
                    }
                    chars[count] = (char) (((b1 & 0x1f) << 6) | (b2 & 0x3f));
                    count++;
                    break;
                case 0xe0:    // 1110 xxxx   10xx xxxx   10xx xxxx
                    if (ofs + 2 > len) {
                        throw new UTFDataFormatException(ERR_END);
                    }
                    b2 = b[ofs];
                    ofs++;
                    b3 = b[ofs];
                    ofs++;
                    if (((b2 & 0xc0) != 0x80)) {
                        throw new UTFDataFormatException(ERR_WRONG + (ofs - 2));
                    }
                    if (((b3 & 0xc0) != 0x80)) {
                        throw new UTFDataFormatException(ERR_WRONG + (ofs - 1));
                    }
                    chars[count] = (char) (((b1 & 0x0f) << 12) | ((b2 & 0x3f) << 6) | (b3 & 0x3f));
                    count++;
                    break;
                default:
                    throw new UTFDataFormatException(ERR_WRONG + (ofs - 1));
            }
        }

        return new String(chars, 0, count);
    }

    /**
     * Encodes string in byte array in modified UTF format.
     *
     * @param str string to encode
     * @return byte array containing encoded string
     * @throws UTFDataFormatException if string is too long to encode
     */
    public static byte[] utfEncode(CharSequence str) throws UTFDataFormatException
    {
        int len = str.length();  //string length, C.O.
        int count = 0;           //a count of bytes
        char ch;                 //current character
        byte[] bytes;            //result of encoding
        int ofs = 0;             //offset in byte array

        //calculating byte count
        for (int i = 0; i < len; i++) {
            ch = str.charAt(i);
            if (ch == 0) {
                count += 2;
            }
            else if (ch < 0x80) {
                count++;
            }
            else if (ch < 0x0800) {
                count += 2;
            }
            else {
                count += 3;
            }
        }

        if (count >= 0xffff) {
            throw new UTFDataFormatException(ERR_LONG);
        }

        bytes = new byte[count];

        //string encoding
        for (int i = 0; i < len; i++) {
            ch = str.charAt(i);
            if (ch == 0) {
                bytes[ofs] = (byte) 0xc0;
                ofs++;
                bytes[ofs] = (byte) 0x80;
                ofs++;
            }
            else if (ch < 0x80) {
                bytes[ofs] = (byte) (ch);
                ofs++;
            }
            else if (ch < 0x800) {
                bytes[ofs] = (byte) ((ch >> 6) | 0xc0);
                ofs++;
                bytes[ofs] = (byte) (ch & 0x3f | 0x80);
                ofs++;
            }
            else {
                bytes[ofs] = (byte) ((ch >> 12) | 0xe0);
                ofs++;
                bytes[ofs] = (byte) ((ch >> 6) & 0x3f | 0x80);
                ofs++;
                bytes[ofs] = (byte) (ch & 0x3f | 0x80);
                ofs++;
            }
        }

        return bytes;
    }

    public static void writeNewFile(Object string, byte[] lit, int i, int i0) throws IOException
    {
        OutputStream os = openOutput(string);
        try {
            os.write(lit, i, i0 > -1 ? i0 : lit.length);
        }
        finally {
            fclose(os);
        }
    }

    public static byte[] readAllBytes(Object string) throws IOException
    {
        ReadSeek os = openRandom(string, true);
        try {
            return os.fully((int) os.length());
        }
        finally {
            fclose(os);
        }
    }

    public static int readAllBytes(Object string, byte[] dst, int off) throws IOException
    {
        ReadSeek os = openRandom(string, true);
        try {
            os.fully(dst, off, (int) os.length());
            return (int) os.length();
        }
        finally {
            fclose(os);
        }
    }

    public static void copyToBak(Object fin, String ext) throws IOException
    {
        File fus = IOSys.toFile(fin);
        File bak = new File(fus.getParentFile(), fus.getName() + "." + ext);
        InputStream dut = openInput(fus);
        try {
            OutputStream ou = openOutput(bak);
            try {
                writeAll(dut, ou);
            }
            finally {
                fclose(ou);
            }
        }
        finally {
            fclose(dut);
        }
    }

    public static boolean isValidChar(int aa)
    {
        return !(aa < 0x20 || aa == '\\' || aa == '/'
                || aa == ':' || aa == '*'
                || aa == '?' || aa == '\"'
                || aa == '<' || aa == '>'
                || aa == '|');

    }

    public static OutputStream openNullOut()
    {
        return new OutputStream() {
            @Override
            public void write(byte[] b) throws IOException
            {
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException
            {
            }

            @Override
            public void write(int b) throws IOException
            {
            }
        };
    }

    public static void fetch(File file, boolean recursive, final OnFetch onFetch) throws Exception
    {
        final class aa extends RuntimeException {

            public aa(Throwable thrwbl)
            {
                super(thrwbl);
            }

        }
        try {
            file.listFiles(new FileFilter() {
                public boolean accept(File pathname)
                {
                    if (pathname.isDirectory()) {
                        pathname.listFiles(this);
                    }
                    else {
                        try {
                            onFetch.onFile(pathname);
                        }
                        catch (Exception ex) {
                            throw new aa(ex);
                        }
                    }
                    return false;
                }
            });
        }
        catch (aa ex) {
            throw (Exception) ex.getCause();
        }
    }

    private IOSys()
    {
    }

    public static interface OnFetch {

        public void onFile(File pathname) throws Exception;

    }

    public interface AbstractIOSys {

        RndAccess openRandom(Object f) throws IOException;

        OutputStream openOutput(Object f, boolean append) throws IOException;

        boolean mkdir(Object f);

        boolean delete(Object dir);

        void rmdir(Object dir);

        boolean rename(Object src, Object dst);

        boolean createFile(Object f);

        void setRead(Object file, boolean on) throws IOException;

        void setWrite(Object file, boolean on) throws IOException;

        void setExec(Object file, boolean on) throws IOException;

    }

    public static String getPaths(Object content)
    {
        if (content instanceof String[]) {
            StringBuffer ss = new StringBuffer();
            for (String k : (String[]) content) {
                if (ss.length() > 0) {
                    ss.append(File.pathSeparatorChar);
                }
                ss.append(k);
            }
            return ss.toString();
        }
        else if (content instanceof File[]) {
            StringBuffer ss = new StringBuffer();
            for (File k : (File[]) content) {
                if (ss.length() > 0) {
                    ss.append(File.pathSeparatorChar);
                }
                ss.append(k.getPath());
            }
            return ss.toString();
        }
        else if (content instanceof String) {
            return (String) content;
        }
        else if (content instanceof File) {
            return ((File) content).getPath();
        }
        return null;
    }

    public static Object getOneFile(Object content, boolean asString)
    {
        if (content instanceof String[]) {
            if (((String[]) content).length < 1) {
                return null;
            }
            if (asString) {
                return ((String[]) content)[0];
            }
            return new File(((String[]) content)[0]);
        }
        else if (content instanceof File[]) {
            if (((File[]) content).length < 1) {
                return null;
            }
            if (asString) {
                return ((File[]) content)[0].getPath();
            }
            return ((File[]) content)[0];
        }
        else if (content instanceof String) {
            if (asString) {
                return content;
            }
            return new File((String) content);
        }
        else if (content instanceof File) {
            if (asString) {
                return ((File) content).getPath();
            }
            return content;
        }
        return null;
    }

    private static final AbstractIOSys DEFAULT_INTERNAL = new FileSysPC();
    private static AbstractIOSys iternalUse;

    public static void setInternal(AbstractIOSys iternal)
    {
        IOSys.iternalUse = iternal == null ? DEFAULT_INTERNAL : iternal;
    }

//    private static final ArrayList STREAMS = new ArrayList(0x1000);
    public synchronized static final void closeAll()
    {
//        for (Object ac : STREAMS) {
//            try {
//                if (ac instanceof Closeable) {
//                    ((Closeable) ac).close();
//                } else if (ac != null) {
//                    ac.getClass().getMethod("close", new Class[0]).invoke(ac, new Object[0]);
//                }
//            } catch (Exception ex) {
//            }
//        }
//        STREAMS.clear();
    }

    public static final void fclose(Object c)
    {
        try {
            if (c instanceof Closeable) {
                ((Closeable) c).close();
            }
            else if (c != null) {
                c.getClass().getMethod("close", new Class[0]).invoke(c, new Object[0]);
            }
        }
        catch (Exception ex) {
        }
    }

    public static File toCanonicalFile(File o)
    {
        if (o != null && o.exists()) {
            try {
                return o.getCanonicalFile();
            }
            catch (Exception ex) {

            }
            return o.getAbsoluteFile();
        }
        return o;
    }

    public static File toFile(Object o)
    {
        return o instanceof File ? (File) o : new File((String) o);
    }

    public final static boolean isValidWritable(Object o)
    {
        if (o == null) {
            return false;
        }
        File x = toFile(o);
        if (x.exists()) {
            return x.isFile() && x.canWrite();
        }
        else {
            if (!x.isAbsolute()) {
                return new File(".").canWrite();
            }
            x = x.getParentFile();
            return x.isDirectory() && x.canWrite();
        }
    }

    public static RndAccess openRandom(Object f, boolean ro) throws IOException
    {
        if (ro) {
            return new RndAccFile(IOSys.toFile(f), "r");
        }
        RndAccess ros = iternalUse.openRandom(f);
//        STREAMS.add(ros);
        return ros;
    }

    public static InputStream openInput(Object f, int buffer) throws FileNotFoundException
    {
        if (f instanceof InputStream) {
            if (f instanceof BufferedInputStream) {
                return (InputStream) f;
            }
            else {
                return new BufferedInputStream((InputStream) f, buffer);
            }
        }
        return new BufferedInputStream(openInput(f), buffer);
    }

    public static String addExt(String src, String ext, boolean ignorecase)
    {
        if (ignorecase ? src.length() >= ext.length() && src.substring(src.length() - ext.length()).equalsIgnoreCase(ext) : src.endsWith(ext)) {
            if (ext.charAt(0) == '.') {
                return src;
            }
            if (ext.charAt(0) != '.' && src.charAt(src.length() - ext.length() - 1) == '.') {
                return src;
            }
        }
        if (ext.charAt(0) == '.') {
            return src + ext;
        }
        return src + '.' + ext;
    }

    public static String setExt(String src, String ext)
    {
        return setExt(src, ext, true);
    }

    public static String setExt(String src, String ext, boolean add_dot)
    {
        int dx = Math.max(0, Math.max(src.lastIndexOf('/'), src.lastIndexOf('\\')));
        int dy = src.lastIndexOf('.');
        src = (dx > dy ? src : src.substring(0, dy));
        if (!add_dot || ext.charAt(0) == '.') {
            return src + ext;
        }
        return src + '.' + ext;
    }

    public static InputStream openInput(Object f) throws FileNotFoundException
    {
        InputStream ros = new FileInputStream(toFile(f));
//        STREAMS.add(ros);
        return ros;
    }

    public static OutputStream openOutput(Object f, int buffer) throws IOException
    {
        return new BufferedOutputStream(openOutput(f, false), buffer);
    }

    public static OutputStream openOutput(Object f) throws IOException
    {
        return openOutput(f, false);
    }

    public static OutputStream openOutput(Object f, boolean append, int buffer) throws IOException
    {
        return new BufferedOutputStream(openOutput(f, append), buffer);
    }

    public static OutputStream openOutput(Object f, boolean append) throws IOException
    {
        OutputStream ros = iternalUse.openOutput(f, append);
//        STREAMS.add(ros);
        return ros;
    }

    public static boolean mkdir(Object f)
    {
        return iternalUse.mkdir(f);
    }

    public static boolean delete(Object dir)
    {
        return iternalUse.delete(dir);
    }

    public static void rmdir(Object dir)
    {
        iternalUse.rmdir(dir);
    }

    public static boolean rename(Object src, Object dst)
    {
        return iternalUse.rename(src, dst);
    }

    public static boolean createFile(Object f)
    {
        return iternalUse.createFile(f);
    }

    public static void setRead(Object file, boolean on) throws IOException
    {
        iternalUse.setRead(file, on);
    }

    public static void setWrite(Object file, boolean on) throws IOException
    {
        iternalUse.setWrite(file, on);
    }

    public static void setExec(Object file, boolean on) throws IOException
    {
        iternalUse.setExec(file, on);
    }

    public static String CopyLib(Class cls, String wk, String inm, String oum) throws IOException
    {
        File ak = new File(wk, oum);
        if (!ak.exists() || ak.length() == 0) {
            InputStream ss = cls.getResourceAsStream(inm);
            if (ss == null) {
                return null;
            }
            try {
                OutputStream opa = IOSys.openOutput(ak);
                try {
                    writeAll(ss, opa);
                }
                finally {
                    opa.close();
                }
            }
            finally {
                ss.close();
            }
        }
        return ak.getAbsolutePath();
    }

    public static void writeAll(InputStream from, OutputStream to) throws IOException
    {
        writeAll(from, to, new byte[1024]);
    }

    public static void writeAll(InputStream from, OutputStream to, byte[] buf) throws IOException
    {
        int len = from.read(buf);
        while (len > 0) {
            to.write(buf, 0, len);
            len = from.read(buf);
        }
    }

    public static void writeAll(InputStream from, long size, OutputStream to) throws IOException
    {
        writeAll(from, size, to, new byte[1024]);
    }

    public static void writeAll(InputStream from, long size, OutputStream to, byte[] buf) throws IOException
    {
        while (size > 0) {
            int len = from.read(buf, 0, (int) Math.min(size, buf.length));
            if (len <= 0) {
                throw new IOException("Cant get more data from stream");
            }
            to.write(buf, 0, len);
            size -= len;
        }
    }
}
