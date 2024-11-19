/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DeltaSKR.IO.interfce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author ARMAX
 */
public class FileSysPC implements IOSys.AbstractIOSys {

    public FileSysPC()
    {
    }

    @Override
    public OutputStream openOutput(Object o, boolean append) throws IOException
    {
        File f = IOSys.toFile(o);
        if (IOSys.isValidWritable(f)) {
            return new FileOutputStream(f, append);
        }
        else {
            throw new IOException("Invalid write Access: " + f + " " + f.isFile() + " " + f.canWrite());
        }
    }

    @Override
    public RndAccess openRandom(Object o) throws IOException
    {
        File f = IOSys.toFile(o);
        if (IOSys.isValidWritable(f)) {
            createFile(f);
            return new RndAccFile(f, "rw");
        }
        else {
            throw new IOException("Invalid write Access: " + f + " " + f.isFile() + " " + f.canWrite());
        }
    }


    private boolean mkDir(File f)
    {
        if (f.exists()) {
            return true;
        }
        if (mkDir(f.getParentFile())) {
            return f.mkdir();
        }
        return false;
    }

    @Override
    public boolean mkdir(Object o)
    {
        return mkDir(IOSys.toFile(o));
    }

    @Override
    public boolean delete(Object o)
    {
        return IOSys.toFile(o).delete();
    }

    @Override
    public void rmdir(Object o)
    {
        rmdir_impl(IOSys.toFile(o));
    }

    private void rmdir_impl(File dir)
    {
        File s, b[] = dir.listFiles();
        for (int i = 0; i < b.length; i++) {
            s = b[i];
            if (s.isDirectory()) {
                rmdir_impl(s);
            }
            else {
                s.delete();
            }
        }
        dir.delete();
    }

    @Override
    public boolean createFile(Object o)
    {
        try {
            return IOSys.toFile(o).createNewFile();
        }
        catch (IOException ex) {
        }
        return false;
    }

    @Override
    public boolean rename(Object src, Object dst)
    {
        return IOSys.toFile(src).renameTo(IOSys.toFile(dst));
    }

    @Override
    public void setRead(Object o, boolean on) throws IOException
    {
        IOSys.toFile(o).setReadable(on);
    }

    @Override
    public void setWrite(Object o, boolean on) throws IOException
    {
        IOSys.toFile(o).setWritable(on);
    }

    @Override
    public void setExec(Object o, boolean on) throws IOException
    {
        IOSys.toFile(o).setExecutable(on);
    }

}
