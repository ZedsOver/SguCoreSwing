// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29/11/2017 23:43:23
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   ISO9660FileEntry.java
package dLibs.net.didion.loopy;

// Referenced classes of package net.didion.loopy.iso9660:
public final class ISO9660FileEntry {

    public ISO9660FileEntry(ISO9660FileSystem fileSystem, byte block[], int pos)
    {
        this(fileSystem, null, block, pos);
    }
//  public  static TABPrintStream pom;
//
//    static {
//        try {
//            pom = new TABPrintStream(System.out);
//        } catch (Exception e) {
//        }
//    }

    public ISO9660FileEntry(ISO9660FileSystem fileSystem, String parentPath, byte block[], int startPos)
    {
        this.fileSystem = fileSystem;
        this.parentPath = parentPath;
        int offset = startPos - 1;

        entryLength = Util.getUInt8(block, offset + 1);
        startSector = Util.getUInt32LE(block, offset + 3);
        long startSector2 = Util.getUInt32BE(block, offset + 7);
        if (startSector != startSector2) {
            System.out.println("drup");
        }
        dataLength = Util.getUInt32LE(block, offset + 11);
        long dataLength2 = (int) Util.getUInt32BE(block, offset + 15);
        if (dataLength != dataLength2) {
            System.out.println("drap");
        }
//        String dus = "<none>";
        try {
            lastModifiedTime = Util.getDateTime(block, offset + 19);
            flags = Util.getUInt8(block, offset + 26);
//            dus =
            identifier = getFileIdentifier(block, offset, isDirectory());
        }
        finally {
//            if(dus.equals("PZS3US2.AFS")){
//                System.out.println("DLibs.net.didion.loopy.ISO9660FileEntry.<init>()");
//            }
//            System.out.println("Entry -> " + dus);
//            pom.printHex(block, offset,Math.min(32,block.length-offset ) );
//            pom.println();
        }

//        System.out.printf("---------> %s %d %d %d \n", getName(), entryLength, startSector, dataLength);
    }

    private String getFileIdentifier(byte block[], int offset, boolean isDir)
    {
        int fidLength = Util.getUInt8(block, offset + 33);
        if (isDir) {
            int buff34 = Util.getUInt8(block, offset + 34);
            if (fidLength == 1 && buff34 == 0) {
                return ".";
            }
            if (fidLength == 1 && buff34 == 1) {
                return "..";
            }
        }
        String id = Util.getDChars(block, offset + 34, fidLength, fileSystem.getEncoding());
        int sepIdx = id.indexOf(';');
        if (sepIdx >= 0) {
            return id.substring(0, sepIdx);
        }
        else {
            return id;
        }
    }

    public String getParentPath()
    {
        return parentPath;
    }

    public String getPath()
    {
        if (".".equals(identifier)) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        if (null != parentPath) {
            buf.append(parentPath);
        }
        buf.append(identifier);
        if (isDirectory()) {
            buf.append("/");
        }
        return buf.toString();
    }

    public boolean isDirectory()
    {
        return (flags & 3) != 0;
    }

    public final boolean isLastEntry()
    {
        return (flags & 0x40) == 0;
    }

    public static final char ID_SEPARATOR = 59;
    private ISO9660FileSystem fileSystem;
    private String parentPath;
    public final int entryLength;
    public final long startSector;
    public final long dataLength;
    public final long lastModifiedTime;
    private final int flags;
    public final String identifier;
}
