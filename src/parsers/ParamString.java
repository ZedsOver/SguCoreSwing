/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

/**
 *
 * @author ARMAX
 */
public class ParamString {

    protected final Object[] params;
    protected int po = 0;
    protected String fns;

    public ParamString(int maz)
    {
        params = new Object[maz];
    }

    public ParamString(String str, int maz)
    {
        params = new Object[maz];
        fns = str;
    }

    public final ParamString format(String s)
    {
        po = 0;
        fns = s;
        return this;
    }

    public ParamString reset()
    {
        po = 0;
        return this;
    }

    public final ParamString add(boolean v)
    {
        params[po++] = v ? Boolean.TRUE : Boolean.FALSE;
        return this;
    }

    public final ParamString add(byte v)
    {
        params[po++] = new Byte(v);
        return this;
    }

    public final ParamString add(short v)
    {
        params[po++] = new Short(v);
        return this;
    }

    public final ParamString add(int v)
    {
        params[po++] = new Integer(v);
        return this;
    }

    public final ParamString add(long v)
    {
        params[po++] = new Long(v);
        return this;
    }

    public final ParamString add(double v)
    {
        params[po++] = new Double(v);
        return this;
    }

    public final ParamString add(float v)
    {
        params[po++] = new Float(v);
        return this;
    }

    public final ParamString add(Object v)
    {
        params[po++] = v;
        return this;
    }

    public String finish(String fmt)
    {
        try {
            return String.format(fmt, params);
        }
        finally {
            po = 0;
        }
    }

    public String finish()
    {
        try {
            return String.format(fns, params);
        }
        finally {
            po = 0;
        }
    }

    @Override
    public String toString()
    {
        return String.format(fns, params);
    }
}
