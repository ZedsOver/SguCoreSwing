/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parsers;

import java.io.IOException;

/**
 *
 * @author armax
 */
public class Pkg_Adapter_testa {

    public static class Pack0 extends Pkg_box {

        public Pack0(BasicFile parent, Object... parsers)
        {
            super(null, null, parent, Flags.DUNK, parsers);
        }

        @Override
        protected boolean isValid() throws IOException
        {
            return true;
        }

    }

    public static class Pack1 extends Pkg_box {

        public Pack1(BasicFile parent, Object... parsers)
        {
            super(null, null, parent, Flags.DUNK, parsers);
        }

        @Override
        protected boolean isValid() throws IOException
        {
            return true;
        }

    }

    public static class Pack2 extends Pack1 {

        public Pack2(BasicFile parent, Object... parsers)
        {
            super(parent, parsers);
        }

    }

    public static class Pack3 extends Pack1 {

        public Pack3(BasicFile parent, Object... parsers)
        {
            super(parent, parsers);
        }

    }

    public static void main(String[] args)
    {
        Pkg_Adapter pack2 = new Pkg_Adapter(Pack2.class, UNK_BIN.class);
        Pkg_Adapter pack3 = new Pkg_Adapter(Pack3.class, Pkg_Adapter.class, UNK_BIN.class, pack2);
        Pkg_Adapter pack1 = new Pkg_Adapter(Pack1.class, Pkg_Adapter.class, pack3, pack2);
        Pack0 rom = new Pack0(null, pack1);
        Pack1 ca = rom.getParser(0);
        System.out.println("REF0: " + ca);
        System.out.println("REF2: " + ca.getParser(2));

    }

}
