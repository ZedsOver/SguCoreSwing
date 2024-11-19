// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29/11/2017 23:43:23
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   EntryEnumeration.java
package dLibs.net.didion.loopy;

import java.io.*;
import java.util.*;

// Referenced classes of package net.didion.loopy.iso9660:
//            ISO9660FileEntry, ISO9660FileSystem
class EntryEnumeration implements Enumeration {

    public EntryEnumeration(ISO9660FileSystem fileSystem, ISO9660FileEntry rootEntry)
    {
        this.fileSystem = fileSystem;
        queue.add(rootEntry);
    }

    @Override
    public boolean hasMoreElements()
    {
        return !queue.isEmpty();
    }

    @Override
    public Object nextElement()
    {
        if (!hasMoreElements()) {
            throw new NoSuchElementException();
        }
        ISO9660FileEntry entry = (ISO9660FileEntry) queue.remove(0);

        if (entry.isDirectory()) {
            byte content[];
            try {
                content = fileSystem.getBytes(entry);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            int offset = 0;
            boolean paddingMode = false;
            do {
                if (offset >= content.length) {
                    break;
                }
                if (LittleEndian.getUInt8(content, offset) <= 0) {
                    paddingMode = true;
                    offset += 2;
                }
                else {
                    ISO9660FileEntry child;
                    try {
                        child = new ISO9660FileEntry(fileSystem, entry.getPath(), content, offset + 1);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        return null;
                    }

                    if (!paddingMode || child.dataLength >= 0) {

//                       if (child.getName().equalsIgnoreCase("packfile.bin")) {
//                           try
//                           {
//                               OutputStream  output=RootFile.fis.openOutput(new File(Main.fPath, "sim.bin"));
//                               System.out.println(content.length);
//                               for (int i = offset - 1; i < offset + child.getEntryLength(); i++)
//                               {
//                                   output.write(String.format("%02X", content[i] & 255).getBytes("utf-8"));
//                               }
//                               output.write(13);
//                               output.write((child.getStartBlock() + " " + child.getSize()).getBytes("utf-8"));
//                               output.close();
//                           }
//                           catch (Exception e)
//                           {}
//                       }
                        offset += child.entryLength;
                        if (!".".equals(child.identifier) && !"..".equals(child.identifier)) {
                            queue.add(child);
                        }
                    }
                }
            }
            while (true);
        }
        return entry;
    }

    private final ISO9660FileSystem fileSystem;
    private final List queue = new LinkedList();
}
