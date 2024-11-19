/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parsers;

import com.DeltaSKR.IO.interfce.AbstractRndAccess;
import com.DeltaSKR.IO.interfce.IOSeekUtil;
import com.DeltaSKR.IO.interfce.RndAccess;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author armax
 */
public class Subisrad extends AbstractRndAccess {

    protected RndAccess fi;
    protected long beginOffset = -1;
    protected long endOffset = 0;
    private long pointer;

    public final RndAccess getFi()
    {
        return fi;
    }

    public void setIO(RndAccess fi)
    {
        unlock();
        this.fi = fi;
    }

    public final long getBeginOffset()
    {
        return beginOffset;
    }

    public final long getEndOffset()
    {
        return endOffset;
    }

    public void setBeginOffset(long beginOffset)
    {
        this.beginOffset = beginOffset;
    }

    public void setEndOffset(long endOffset)
    {
        this.endOffset = endOffset;
    }

    public void setIOLimits(long beginOffset, long endOffset)
    {
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
    }

    @Override
    public void lock(long pos) throws IOException
    {
        pointer = 0;
        seek(pos);
        lock();
    }

    @Override
    public long lock() throws IOException
    {
        return pointer = fi.seek() - beginOffset;
    }

    @Override
    public void unlock()
    {
        pointer = 0;
    }

    @Override
    public final long seek() throws IOException
    {
        return fi.seek() - beginOffset - pointer;
    }

    @Override
    public final void seek(long pos) throws IOException
    {
        if (beginOffset == -1) {
            throw new RuntimeException("Offset has not setted " + getClass());
        }
        if (pos < 0) {
            throw new EOFException("negative seek");
        }
        if (endOffset - beginOffset < pos) {
            pos = endOffset - beginOffset;
        }
        fi.seek(beginOffset + pointer + pos);
    }

    @Override
    public final long length() throws IOException
    {
        return endOffset - beginOffset - pointer;
    }

    @Override
    public void setLength(long len) throws IOException
    {
        fi.setLength(len);
    }

    @Override
    public OutputStream asOutputStream()
    {
        return fi.asOutputStream();
    }

    @Override
    public InputStream asInputStream()
    {
        return fi.asInputStream();
    }

    @Override
    public void write(int b) throws IOException
    {
        fi.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        fi.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        fi.write(b, off, len);
    }

    @Override
    public void writeNull(int bytes) throws IOException
    {
        IOSeekUtil.writeNull(fi, bytes);
    }

    @Override
    public void writeStr(String data, String code, int align) throws IOException
    {
        IOSeekUtil.writeStr(fi, data, code, align);
    }

    @Override
    public final int read() throws IOException
    {
        if (fi.seek() + 1 > endOffset) {
            return -1;
        }
        return fi.read();
    }

    @Override
    public final int read(byte[] b, int off, int len) throws IOException
    {
        long his = endOffset - fi.seek();
        if (his <= 0) {
            return -1;
        }
        return fi.read(b, off, len < his ? len : (int) his);
    }

    @Override
    public final int skip(int n) throws IOException
    {
        if (beginOffset == -1) {
            throw new RuntimeException("Offset has not setted " + getClass());
        }
        long his;
        if (n < 0) {
            his = beginOffset - fi.seek();
            if (his >= 0) {
                return -1;
            }
            if (his <= n) {
                fi.seek(fi.seek() + n);
                return n;
            }
//            fi.seek(fi.getFilePointer() + his);
//            return (int) his;
            throw new EOFException("BOFException");
        }
        else {
            his = endOffset - fi.seek();
            if (his <= 0) {
                return -1;
            }
            if (n <= his) {
                return fi.skip(n);
            }
//            return fi.skipBytes((int) his);
            throw new EOFException();
        }
    }

}
