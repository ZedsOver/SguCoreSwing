// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29/11/2017 23:43:23
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Util.java
package dLibs.net.didion.loopy;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;

public final class Util {

    public static int getUInt8(byte block[], int pos)
    {
        return LittleEndian.getUInt8(block, pos - 1);
    }

    public static int getInt8(byte block[], int pos)
    {
        return LittleEndian.getInt8(block, pos - 1);
    }

    public static int getUInt16LE(byte block[], int pos)
    {
        return LittleEndian.getUInt16(block, pos - 1);
    }

    public static int getUInt16BE(byte block[], int pos)
    {
        return BigEndian.getUInt16(block, pos - 1);
    }

    public static int getUInt16Both(byte block[], int pos)
    {
        return LittleEndian.getUInt16(block, pos - 1);
    }

    public static long getUInt32LE(byte block[], int pos)
    {
        return LittleEndian.getUInt32(block, pos - 1);
    }

    public static long getUInt32BE(byte block[], int pos)
    {
        return BigEndian.getUInt32(block, pos - 1);
    }

    public static long getUInt32Both(byte block[], int pos)
    {
        return LittleEndian.getUInt32(block, pos - 1);
    }

    public static String getAChars(byte block[], int pos, int length)
    {
        return (new String(block, pos - 1, length)).trim();
    }

    public static String getDChars(byte block[], int pos, int length)
    {
        return (new String(block, pos - 1, length)).trim();
    }

    public static String getAChars(byte[] block, int pos, int length, String encoding)
    {
        try {
            return new String(block, pos - 1, length, encoding).trim();
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getDChars(byte[] block, int pos, int length, String encoding)
    {
        try {
            return new String(block, pos - 1, length, encoding).trim();
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static long getStringDate(byte block[], int pos)
    {
        int i = pos - 1;
        Calendar cal = Calendar.getInstance();
        cal.set(1, toInt(block, i, 4));
        cal.set(2, toInt(block, i += 4, 2) - 1);
        cal.set(5, toInt(block, i += 2, 2));
        cal.set(11, toInt(block, i += 2, 2));
        cal.set(12, toInt(block, i += 2, 2));
        cal.set(13, toInt(block, i += 2, 2));
        cal.set(14, toInt(block, i += 2, 2) * 10);
        cal.setTimeZone(TimeZone.getTimeZone(getGMTpos(block[i + 2])));
        return cal.getTimeInMillis();
    }

    private static int toInt(byte[] block, int pos, int len)
    {
        try {
            return Integer.parseInt(new String(block, pos, len));
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static long getDateTime(byte sector[], int pos)
    {
        int i = pos - 1;
        Calendar cal = Calendar.getInstance();
        cal.set(1, 1900 + sector[i]);
        cal.set(2, sector[i + 1] - 1);
        cal.set(5, sector[i + 2]);
        cal.set(11, sector[i + 3]);
        cal.set(12, sector[i + 4]);
        cal.set(13, sector[i + 5]);
        cal.set(14, 0);
        cal.setTimeZone(TimeZone.getTimeZone(getGMTpos(sector[i + 6])));
        return cal.getTimeInMillis();
    }

    private static String getGMTpos(byte b)
    {
        if (0 == b) {
            return "GMT";
        }
        else {
            StringBuffer buf = new StringBuffer("GMT");
            buf.append(b >= 0 ? '+' : '-');
            int posMinutes = Math.abs(b) * 15;
            int hours = posMinutes / 60;
            int minutes = posMinutes % 60;
            buf.append(hours).append(':').append(0 != minutes ? String.valueOf(minutes) : "00");
            return buf.toString();
        }
    }

    private Util()
    {
    }
}
