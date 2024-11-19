/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import com.DeltaSKR.IO.interfce.RndAccess;
import java.io.IOException;

/**
 *
 * @author ARMAX
 */
public class UNK_BIN extends BasicFile {

    //<editor-fold defaultstate="collapsed" desc="UNK">
    static final int[] BOOK = {
        0x52484700,//RHG
        0x434D416E,//cman
        0x41475300,//ags
        0x89504E47,//png

        0x50504844,//PPHD
        0x50505047,//PPPG
        0x5050544E,//PPTN
        0x50505641,//PPVA

        0x56303030,//V000
        0x4D415044,//MAPD
        0x4D454630,//MEF0
        0x45565070,//EVPp
        0x7F454C46,//ELF
        0x704D646C,//pmdl
        0x704D6446,//pmdf
        0x47534346,//GSCF
        0x2E424251,//.BBQ
        0x00504744,//.PGD
        0x4C495053,//LIPS
        0x49454353,//IECS
        0x706D646C,//pmdl
        0x23414D54,//#AMT image
        0x23414D45,//#AME
        0x23414D4F,//#AMO model
        0x23414D4D,//#AMM
        0x2342534B,//#BSK
        0x2342434D,//#BCM
        0x23535058,//#SPX
        0x23414D43,//#AMC
        0x23415452,//#ATR
        0x23415354,//#AST
        0x23415345,//#ASE
        0x23525054,//#RPT
        0x234D5347,//#MSG
        0x23435055,//#CPU
        0x23414D41,//#AMA
        0x23534B41,//#SKA
        0x52494646,//RIFF
        0x50534D46,//PSMF
        0x234D4720,//#MG
        0x23414D4B,//#AMK
        0x23415756,//#AWV
        0x234D4444,//#MDD
        0x234D4144,//#MAD
        0x234D4153,//#MAS
        0x2349544D,//#ITM
        0x234D5441,//#MTA
        0x234F484B,//#OHK
        0x23534344,//#SCD
        0x23535246,//#SRF

        0x23414D50,//#AMP
        0x234D4346,//#MCF
        0x23574346,//#WCF
        0x234D5353,//#MSS
        0x234D5346,//#MSF

        0x23414D4C,//#AML
        0x23444D44,//#DMD
        0x23444D4b,//#DMK
        0x234D4946,//#MIF

        0x21464C44,//!FLD
        0x234D4150,//#MAP
        0x23424643,//#BFC
        0x2342504C,//#BPL

        0x234C5354,//#LST
        0x2341504C,//#APL
        0x464F4410,//FOD.

        0x52445354,//RDST

        0x336D6170,//3map
    };
    //</editor-fold>
    private static final UNK_BIN uk = new UNK_BIN(null);

    public static boolean isCoded(BasicFile a) throws IOException
    {
        uk.setIO(a.fi);
        return uk.isValid(null, a.beginOffset, a.endOffset, -1);
    }

    public UNK_BIN(BasicFile parent)
    {
        super(BOOK, BOOK, parent);
//        System.out.println(BOOK.length);
    }

    @Override
    public void setIO(RndAccess fi)
    {
        customType = 0;
        super.setIO(fi);
    }

    private int customType = 0;

    @Override
    public void endFile()
    {
        customType = 0;
        super.endFile();
    }

    @Override
    public boolean isValidCode(int code)
    {
        customType = 0;
        if (!super.isValidCode(code)) {
            customType = code;
        }
        return true;
    }

    @Override
    protected int type(boolean noEmpty)
    {
        switch (getCType()) {
            case 0x89504E47://png
                return TYPE_IMG;
            case 0x704D646C://pmdl
            case 0x704D6446://pmdf
            case 0x706D646C://pmdl
            case 0x23414D4F://#AMO  
                return TYPE_MDL;
            case 0x234D5347://#MSG
                return TYPE_TXT;
            case 0x56414750:
                return TYPE_SND;
        }
        return TYPE_BIN;
    }

    @Override
    protected boolean isValid() throws IOException
    {
        if (customType != 0) {
            return true;
        }
        seek(0);
        if (fi.length() - fi.seek() <= 0) {//fix error  for  bad  limits
            currentType = -1;
//            beginOffset = endOffset = 0;
            return false;
        }
        if (length() >= 0x4 && read4be() == 0x7E534345) {
            currentType = (byte) (BOOK.length + 2);
            return true;
        }
        seek(0);
        if (length() >= 0x30 && (read8be()
                | read8be()) == 0
                && (read8be()
                | read8be()) != 0)//posible vagPack
        {
            currentType = (byte) BOOK.length;
            return true;
        }
        seek(0);
        if (length() >= 0x10
                && read8be() == 0x000001BA44000400L
                && (read8be() & 0xffff000000ffffffL) == 0x0401000000F80000L) {
            currentType = (byte) (BOOK.length + 1);
            return true;
        }
        return false;
    }

    @Override
    public String getSType()
    {
        int k = getCType();
        switch (k) {
            case 0x89504E47:
                return "PNG";
            case 0x7F454C46:
                return "ELF";
            case 0x42494e00:
                return "BIN";
            case 0x2E424251:
                return "BBQ";
            case 0x00504744:
                return "PGD";
            case 0x56414750:
                return "VAGP";
            case 0x50535300:
                return "PSS";
        }
        return BasicFile.getSType(k, 4);
    }

    @Override
    public int getCType()
    {
        if (customType != 0) {
            return customType;
        }
        switch (currentType - BOOK.length) {
            case 0:
                return 0x56414750;//VAGP
            case 1:
                return 0x50535300;//VAGP   
            case 2:
                return 0x7E534345;//VAGP     
        }
        if (currentType == -1) {
            return 0x42494e00;//bin file
        }
        return super.getCType();
    }

//    private void printOffset(JEntryList entry, int type, boolean useName, boolean root) throws IOException {
//        if (type == 0 && !(parent instanceof TXT)) {
//            type = getCType();
//        }
//        super.printOffset(entry, type, useName, root);
//    }
//    public void printOffset(TABPrintStream out, int num, String type, int bytes, int dat) throws IOException {
//        if (type == null && !(parent instanceof TXT)) {
//            type = getSType();
//        }
//        super.printOffset(out, num, type, bytes, dat);
//    }
    @Override
    protected void export(String f) throws IOException
    {
        export(f, true);
    }

}
