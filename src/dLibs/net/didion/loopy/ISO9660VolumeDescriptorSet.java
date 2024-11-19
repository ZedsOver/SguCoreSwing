// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29/11/2017 23:43:23
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   ISO9660VolumeDescriptorSet.java
package dLibs.net.didion.loopy;

import java.io.IOException;

// Referenced classes of package net.didion.loopy.iso9660:
//            ISO9660FileEntry, Util, ISO9660FileSystem
public class ISO9660VolumeDescriptorSet {

    public ISO9660VolumeDescriptorSet(ISO9660FileSystem fileSystem)
    {
        encoding = "US-ASCII";
        hasPrimary = false;
        hasSupplementary = false;
        isoFile = fileSystem;
    }

    public boolean deserialize(byte descriptor[])
            throws IOException
    {

        int type = Util.getUInt8(descriptor, 1);
        boolean terminator = false;
        switch (type) {
            case 255:
                if (!hasPrimary) {
                    throw new LoopyException("No primary volume descriptor found");
                }
                terminator = true;
                break;

            case 0: // '\0'
//                System.out.println("Found boot record");
                break;

            case 1: // '\001'
//                System.out.println("Found primary descriptor");
                deserializePrimary(descriptor);
                break;

            case 2: // '\002'
//                System.out.println("Found supplementatory descriptor");
                deserializeSupplementary(descriptor);
                break;

            case 3: // '\003'
//                System.out.println("Found partition descriptor");
                break;

            default:
//                System.out.println("Found unknown descriptor with type " + type);
                break;
        }
//        pom.printHex(descriptor, 0, descriptor.length);
//        pom.println();
        return terminator;
    }

    private void deserializePrimary(byte descriptor[])
            throws IOException
    {
        if (hasPrimary) {
            return;
        }
        validateBlockSize(descriptor);
        if (!hasSupplementary) {
            deserializeCommon(descriptor);
        }
        standardIdentifier = Util.getDChars(descriptor, 2, 5);
        volumeSetSize = Util.getUInt16Both(descriptor, 121);
        volumeSequenceNumber = Util.getUInt16Both(descriptor, 125);
        totalBlocks = Util.getUInt32Both(descriptor, 81);
        publisher = Util.getDChars(descriptor, 319, 128);
        preparer = Util.getDChars(descriptor, 447, 128);
        application = Util.getDChars(descriptor, 575, 128);
        creationTime = Util.getStringDate(descriptor, 814);
        mostRecentModificationTime = Util.getStringDate(descriptor, 831);
        expirationTime = Util.getStringDate(descriptor, 848);
        effectiveTime = Util.getStringDate(descriptor, 865);
        pathTableSize = Util.getUInt32Both(descriptor, 133);
        locationOfLittleEndianPathTable = Util.getUInt32LE(descriptor, 141);
        locationOfOptionalLittleEndianPathTable = Util.getUInt32LE(descriptor, 145);
        locationOfBigEndianPathTable = Util.getUInt32BE(descriptor, 149);
        locationOfOptionalBigEndianPathTable = Util.getUInt32BE(descriptor, 153);
        hasPrimary = true;
    }

    private void deserializeSupplementary(byte descriptor[])
            throws IOException
    {
        if (hasSupplementary) {
            return;
        }
        validateBlockSize(descriptor);
        String escapeSequences = Util.getDChars(descriptor, 89, 32);
        String enc = getEncoding(escapeSequences);
        if (null != enc) {
            encoding = enc;
            this.escapeSequences = escapeSequences;
            deserializeCommon(descriptor);
            hasSupplementary = true;
        }
        else {
            System.out.println("W: Unsupported encoding, escapeSequences: '" + this.escapeSequences + "'");
        }
    }

    private void deserializeCommon(byte descriptor[])
            throws IOException
    {
        systemIdentifier = Util.getAChars(descriptor, 9, 32, encoding);
        volumeIdentifier = Util.getDChars(descriptor, 41, 32, encoding);
        volumeSetIdentifier = Util.getDChars(descriptor, 191, 128, encoding);
        rootDirectoryEntry = new ISO9660FileEntry(isoFile, descriptor, 157);
    }

    private void validateBlockSize(byte descriptor[])
            throws IOException
    {
        int blockSize = Util.getUInt16Both(descriptor, 129);
        if (blockSize != 2048) {
            throw new LoopyException("Invalid block size: " + blockSize);
        }
    }

    private String getEncoding(String escapeSequences)
    {
        String encoding = null;
        if (escapeSequences.equals("%/@")) {
            encoding = "UTF-16BE";
        }
        else if (escapeSequences.equals("%/C")) {
            encoding = "UTF-16BE";
        }
        else if (escapeSequences.equals("%/E")) {
            encoding = "UTF-16BE";
        }
        return encoding;
    }

    static Class class$(String x0)
    {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static final int TYPE_BOOTRECORD = 0;
    public static final int TYPE_PRIMARY_DESCRIPTOR = 1;
    public static final int TYPE_SUPPLEMENTARY_DESCRIPTOR = 2;
    public static final int TYPE_PARTITION_DESCRIPTOR = 3;
    public static final int TYPE_TERMINATOR = 255;

    private final ISO9660FileSystem isoFile;
    public String systemIdentifier;
    public String volumeSetIdentifier;
    public String volumeIdentifier;
    public String publisher;
    public String preparer;
    public String application;
    public ISO9660FileEntry rootDirectoryEntry;
    public String standardIdentifier;
    public long totalBlocks;
    public int volumeSetSize;
    public int volumeSequenceNumber;
    public long creationTime;
    public long mostRecentModificationTime;
    public long expirationTime;
    public long effectiveTime;
    public long pathTableSize;
    public long locationOfLittleEndianPathTable;
    public long locationOfOptionalLittleEndianPathTable;
    public long locationOfBigEndianPathTable;
    public long locationOfOptionalBigEndianPathTable;
    public String encoding;
    public String escapeSequences;
    private boolean hasPrimary;
    public boolean hasSupplementary;

}
