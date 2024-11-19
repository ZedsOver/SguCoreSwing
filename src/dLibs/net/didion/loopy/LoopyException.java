// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29/11/2017 23:42:57
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   LoopyException.java
package dLibs.net.didion.loopy;

import java.io.IOException;

public class LoopyException extends IOException {

    public LoopyException()
    {
    }

    public LoopyException(String message)
    {
        super(message);
    }

    public LoopyException(Throwable cause)
    {
        super(cause);
    }

    public LoopyException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
