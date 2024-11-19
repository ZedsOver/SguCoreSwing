/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parsers;

import com.DeltaSKR.lang.ArrayUtil;

/**
 * this class is used for make a class maker specification
 *
 * @author armax
 */
public final class Pkg_Adapter {

    public final Class objCls;
    //this isnt final bc need dinamyc assignation parsers
    public Object[] parsers = {};
    private static final Class params[] = {BasicFile.class, Object[].class};

    public Pkg_Adapter(Class<? extends Pkg_box> objCls)
    {
        this.objCls = objCls;
    }

    public Pkg_Adapter(Class<? extends Pkg_box> objCls, Object... parsers)
    {
        this.objCls = objCls;
        setParsers(parsers);
    }

    public void setParsers(Object... parsers)
    {
        if (parsers == null) {
            return;
        }
        this.parsers = parsers;
        int i = 0;
        for (Object cc : parsers) {
            if (cc == Pkg_Adapter.class) {
                parsers[i] = this;//used on recursive objects
            }
            i++;
        }
    }

    public BasicFile newObject(BasicFile parent)
    {
        try {
            return (BasicFile) objCls.getConstructor(params).newInstance(parent,
                    ArrayUtil.copyOf(parsers, parsers.length)
            );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
