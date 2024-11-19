/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.ANY;

import com.DeltaSKR.IO.interfce.RndAccFile;
import java.io.IOException;

/**
 *
 * @author ARMAX
 */
public class ELF {

    static final int EI_NIDENT = 16;

    private class Elf32_Ehdr {

        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        byte[] e_ident = new byte[EI_NIDENT];//16
        short e_type;//2
        short e_machine;//2
        int e_version;//4
        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        int e_entry;//4
        int e_phoff;//4
        int e_shoff;//4  32
        int e_flags;//4
        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        short e_ehsize;//2
        short e_phentsize;//2
        short e_phnum;//2
        short e_shentsize;//2 46 
        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        short e_shnum;//2 48
        short e_shstrndx;//2
    }

    private class Elf64_Ehdr {

        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        byte[] e_ident = new byte[EI_NIDENT];//16
        short e_type;//2
        short e_machine;//2
        int e_version;//4
        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        long e_entry;//8
        long e_phoff;//8
        long e_shoff;//8 40
        int e_flags;//4
        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        short e_ehsize;//2
        short e_phentsize;//2
        short e_phnum;//2
        short e_shentsize;//2 58
        /* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
        short e_shnum;//2 60
        short e_shstrndx;//2
    }
    static byte[] ruk = new byte[EI_NIDENT];

    public static long get_elf_size(RndAccFile fi) throws IOException
    {
//        fi.seek(0x53B000L);
        long ro = fi.getFilePointer();
        fi.read(ruk, 0, 16);
        fi.seek(ro);
        long e_shoff, e_shentsize, e_shnum;

        switch (ruk[5]) {//EI_DATA
            case 1:// ELFDATA2LSB 1
                switch (ruk[4]) {//EI_CLASS
                    case 1://ELFCLASS32
                        fi.skipBytes(32);
                        e_shoff = fi.readU4le();
                        fi.skipBytes(10);
                        e_shentsize = fi.readU2le();
                        e_shnum = fi.readU2le();
                        return e_shoff + (e_shentsize * e_shnum);
                    case 2://ELFCLASS64
                        fi.skipBytes(40);
                        e_shoff = fi.read8le();
                        fi.skipBytes(10);
                        e_shentsize = fi.readU2le();
                        e_shnum = fi.readU2le();
                        return e_shoff + (e_shentsize * e_shnum);
                }
                break;
            case 2:// ELFDATA2MSB 2
                switch (ruk[4]) {//EI_CLASS
                    case 1://ELFCLASS32
                        fi.skipBytes(32);
                        e_shoff = fi.readU4be();
                        fi.skipBytes(10);
                        e_shentsize = fi.readU2be();
                        e_shnum = fi.readU2be();
                        return e_shoff + (e_shentsize * e_shnum);
                    case 2://ELFCLASS64
                        fi.skipBytes(40);
                        e_shoff = fi.read8be();
                        fi.skipBytes(10);
                        e_shentsize = fi.readU2be();
                        e_shnum = fi.readU2be();
                        return e_shoff + (e_shentsize * e_shnum);
                }
        }
        return -1;
    }

    public static void main(String args[]) throws Exception
    {
        int k = 0;//0
        k = 0x8000;//1
        k = 0x20000;//4
        k = 0x28000;//5
        k = 0x30000;//6
        k = 0x40000;//8
        k = 0x50000;//A
        k = 0x58000;//B
        k = 0x60000;//C
        k = 0x68000;//D
        RndAccFile ra = new RndAccFile("package.rdp", "r");
        ra.seek(k);
        System.out.printf("%08X\n", get_elf_size(ra) + k);
        ra.close();
    }
}
