// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29/11/2017 23:43:23
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   ISO9660FileSystem.java
package dLibs.net.didion.loopy;

import com.DeltaSKR.IO.interfce.RndAccFile;
import com.DeltaSKR.IO.interfce.RndAccess;
import java.io.*;
import java.util.Enumeration;

// Referenced classes of package net.didion.loopy.iso9660:
//            ISO9660VolumeDescriptorSet, EntryInputStream, ISO9660FileEntry, EntryEnumeration
public class ISO9660FileSystem {

    private RndAccess channel;

    public synchronized void close() throws IOException
    {
        if (!isClosed()) {
            try {
                RndAccess randomAccessFile = this.channel;
                randomAccessFile.close();
                this.channel = randomAccessFile;
            }
            finally {
                this.channel = null;
            }
        }
    }

    public RndAccess getChannel()
    {
        return channel;
    }

    public boolean isClosed()
    {
        return null == channel;
    }

    protected void ensureOpen()
            throws IllegalStateException
    {
        if (isClosed()) {
            throw new IllegalStateException("File has been closed");
        }
    }

    protected void seek(long pos)
            throws IOException
    {
        ensureOpen();
        channel.seek(pos);
    }

    protected int read(byte buffer[], int offset, int length)
            throws IOException
    {
        ensureOpen();
        return channel.read(buffer, offset, length);
    }

    public Enumeration getEntries()
    {
        ensureOpen();
        if (null == volumeDescriptorSet) {
            try {
                loadVolumeDescriptors();
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return new EntryEnumeration(this, volumeDescriptorSet.rootDirectoryEntry);
    }

    protected void loadVolumeDescriptors()
            throws IOException
    {
        byte buffer[] = new byte[blockSize];
        volumeDescriptorSet = new ISO9660VolumeDescriptorSet(this);
        for (int block = reservedBlocks; readBlock(block, buffer) && !volumeDescriptorSet.deserialize(buffer); block++) {
        }
    }

    protected boolean readBlock(long block, byte buffer[])
            throws IOException
    {
        int bytesRead = readData(block * (long) blockSize, buffer, 0, blockSize);
        if (bytesRead <= 0) {
            return false;
        }
        if (blockSize != bytesRead) {
            throw new IOException("Could not deserialize a complete block");
        }
        else {
            return true;
        }
    }

    public synchronized int readData(long startPos, byte buffer[], int offset, int len)
            throws IOException
    {
//        System.out.printf(">> %08X\n", startPos);
        seek(startPos);
        return read(buffer, offset, len);
    }

    protected final int blockSize;
    protected final int reservedBlocks;
    private ISO9660VolumeDescriptorSet volumeDescriptorSet;

    public ISO9660FileSystem(RndAccess file)
            throws IOException
    {
        channel = file;
        this.blockSize = 2048;
        this.reservedBlocks = 16;
    }

    public ISO9660FileSystem(File file, boolean readOnly)
            throws IOException
    {
        if (!readOnly) {
            throw new IllegalArgumentException("Currrently, only read-only is supported");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file);
        }
        else {
            channel = new RndAccFile(file, "r");
        }
        this.blockSize = 2048;
        this.reservedBlocks = 16;
    }

    public String getEncoding()
    {
        return volumeDescriptorSet.encoding;
    }

    byte[] getBytes(ISO9660FileEntry entry)
            throws IOException
    {
        if (entry.dataLength > Integer.MAX_VALUE) {
            throw new RuntimeException("Data length exceds max int value");
        }

        int size = (int) entry.dataLength;
        byte buf[] = new byte[size];
        readBytes(entry, 0, buf, 0, size);
        return buf;
    }

    int readBytes(ISO9660FileEntry entry, int entryOffset, byte buffer[], int bufferOffset, int len)
            throws IOException
    {
        long startPos = entry.startSector * 2048L + (long) entryOffset;

        return readData(startPos, buffer, bufferOffset, len);
    }

}
