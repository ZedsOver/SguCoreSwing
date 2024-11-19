/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parsers;

/**
 * http://vgmpf.com/Wiki/index.php?title=List_of_Platform_Abbreviations_(A-N)
 *
 * @author armax
 */
public interface GameTypes {

    /**
     * GTYPE_SEGA_32X
     */
    public static final int GTYPE_32X = 0x000040E2;
    /**
     * GTYPE_3DO_MULTIPLAYER
     */
    public static final int GTYPE_3DO = 0x00004399;
    /**
     * GTYPE_NINTENDO_3DS
     */
    public static final int GTYPE_3DS = 0x0000439D;
    /**
     * GTYPE_APPLE_II
     */
    public static final int GTYPE_A2 = 0x000002C3;
    /**
     * GTYPE_ATARI_2600
     */
    public static final int GTYPE_A26 = 0x0000B0C7;
    /**
     * GTYPE_APPLE_IIGS
     */
    public static final int GTYPE_A2GS = 0x002C345D;
    /**
     * GTYPE_ATARI_5200
     */
    public static final int GTYPE_A52 = 0x0000B183;
    /**
     * GTYPE_ATARI_7800
     */
    public static final int GTYPE_A78 = 0x0000B209;
    /**
     * GTYPE_ATARI_8_BIT_400
     */
    public static final int GTYPE_A8 = 0x000002C9;
    /**
     * GTYPE_ENTEX_ADVENTUREVISION
     */
    public static final int GTYPE_ADV = 0x0000B3A0;
    /**
     * GTYPE_COMMODORE_AMIGA
     */
    public static final int GTYPE_AMI = 0x0000B5D3;
    /**
     * GTYPE_ANDROID_OS
     */
    public static final int GTYPE_AND = 0x0000B60E;
    /**
     * GTYPE_ARCADE
     */
    public static final int GTYPE_ARC = 0x0000B70D;
    /**
     * GTYPE_ACORN_ARCHIMEDES
     */
    public static final int GTYPE_ARCH = 0x002DC352;
    /**
     * GTYPE_ATARI_ST
     */
    public static final int GTYPE_AST = 0x0000B75E;
    /**
     * GTYPE_BALLY_ASTROCADE
     */
    public static final int GTYPE_BA = 0x0000030B;
    /**
     * GTYPE_BLACKBERRY
     */
    public static final int GTYPE_BB = 0x0000030C;
    /**
     * GTYPE_BBC_MICRO
     */
    public static final int GTYPE_BBC = 0x0000C30D;
    /**
     * GTYPE_QUALCOMM_BREW
     */
    public static final int GTYPE_BREW = 0x0031C3E1;
    /**
     * GTYPE_COMMODORE_64
     */
    public static final int GTYPE_C64 = 0x0000D1C5;
    /**
     * GTYPE_COMMODORE_128
     */
    public static final int GTYPE_C128 = 0x003420C9;
    /**
     * GTYPE_AMIGA_CD32
     */
    public static final int GTYPE_CD32 = 0x0034E103;
    /**
     * GTYPE_PHILIPS_CD_I
     */
    public static final int GTYPE_CDI = 0x0000D393;
    /**
     * GTYPE_COMMODORE_CDTV
     */
    public static final int GTYPE_CDTV = 0x0034E7A0;
    /**
     * GTYPE_FAIRCHILD_CHANNEL_F
     */
    public static final int GTYPE_CHF = 0x0000D490;
    /**
     * GTYPE_COMMODORE_116
     */
    public static final int GTYPE_CP4 = 0x0000D685;
    /**
     * GTYPE_AMSTRAD_CPC_464
     */
    public static final int GTYPE_CPC = 0x0000D68D;
    /**
     * GTYPE_COLECOVISION
     */
    public static final int GTYPE_CV = 0x00000360;
    /**
     * GTYPE_SEGA_DREAMCAST
     */
    public static final int GTYPE_DC = 0x0000038D;
    /**
     * GTYPE_MS_DOS
     */
    public static final int GTYPE_DOS = 0x0000E65D;
    /**
     * GTYPE_ACORN_ELECTRON
     */
    public static final int GTYPE_ELEC = 0x003D63CD;
    /**
     * GTYPE_IN_FUSIO_EXEN
     */
    public static final int GTYPE_EXEN = 0x003E23D8;
    /**
     * GTYPE_ATARI_FALCON030
     */
    public static final int GTYPE_FAL = 0x000102D6;
    /**
     * GTYPE_NINTENDO_FAMICOM
     */
    public static final int GTYPE_FC = 0x0000040D;
    /**
     * GTYPE_NINTENDO_FAMICOM_DISK_SYSTEM
     */
    public static final int GTYPE_FDS = 0x0001039D;
    /**
     * GTYPE_AMAZON_FIRE_OS
     */
    public static final int GTYPE_FIRE = 0x0041370F;
    /**
     * GTYPE_FUJITSU_FM_7
     */
    public static final int GTYPE_FM7 = 0x000105C8;
    /**
     * GTYPE_FUJITSU_FM_TOWNS
     */
    public static final int GTYPE_FMT = 0x000105DE;
    /**
     * GTYPE_NINTENDO_GAME_BOY
     */
    public static final int GTYPE_GB = 0x0000044C;
    /**
     * GTYPE_NINTENDO_GAME_BOY_ADVANCE
     */
    public static final int GTYPE_GBA = 0x0001130B;
    /**
     * GTYPE_NINTENDO_GAME_BOY_COLOR
     */
    public static final int GTYPE_GBC = 0x0001130D;
    /**
     * GTYPE_NINTENDO_GAMECUBE
     */
    public static final int GTYPE_GC = 0x0000044D;
    /**
     * GTYPE_TIGER_GAME_COM
     */
    public static final int GTYPE_GCOM = 0x0044D657;
    /**
     * GTYPE_SEGA_GENESIS
     */
    public static final int GTYPE_GEN = 0x000113D8;
    /**
     * GTYPE_SEGA_GAME_GEAR
     */
    public static final int GTYPE_GG = 0x00000451;
    /**
     * GTYPE_TIGER_GIZMONDO
     */
    public static final int GTYPE_GIZ = 0x000114E4;
    /**
     * GTYPE_NINTENDO_GAME_AND_WATCH
     */
    public static final int GTYPE_GW = 0x00000461;
    /**
     * GTYPE_AMSTRAD_GX4000
     */
    public static final int GTYPE_GX4 = 0x00011885;
    /**
     * GTYPE_NTT_DOCOMO_I_MODE
     */
    public static final int GTYPE_IMOD = 0x004D764E;
    /**
     * GTYPE_INTELLIVISION
     */
    public static final int GTYPE_INTV = 0x004D87A0;
    /**
     * GTYPE_APPLE_IOS_IPAD
     */
    public static final int GTYPE_IOS = 0x0001365D;
    /**
     * GTYPE_SUN_JAVA_2_MICRO_EDITION
     */
    public static final int GTYPE_J2ME = 0x005035CF;
    /**
     * GTYPE_ATARI_JAGUAR
     */
    public static final int GTYPE_JAG = 0x000142D1;
    /**
     * GTYPE_ATARI_JAGUAR_CD
     */
    public static final int GTYPE_JCD = 0x0001434E;
    /**
     * GTYPE_PIONEER_LASERACTIVE
     */
    public static final int GTYPE_LASR = 0x0058B75C;
    /**
     * GTYPE_LINUX
     */
    public static final int GTYPE_LIN = 0x000164D8;
    /**
     * GTYPE_ATARI_LYNX
     */
    public static final int GTYPE_LYNX = 0x005A3622;
    /**
     * GTYPE_APPLE_MACINTOSH
     */
    public static final int GTYPE_MAC = 0x000172CD;
    /**
     * GTYPE_FUJITSU_FM_TOWNS_MARTY
     */
    public static final int GTYPE_MART = 0x005CB71E;
    /**
     * GTYPE_SEGA_MEGA_CD
     */
    public static final int GTYPE_MCD = 0x0001734E;
    /**
     * GTYPE_MSX
     */
    public static final int GTYPE_MSX = 0x00017762;
    /**
     * GTYPE_MSX2
     */
    public static final int GTYPE_MSX2 = 0x005DD883;
    /**
     * GTYPE_NINTENDO_64
     */
    public static final int GTYPE_N64 = 0x000181C5;
    /**
     * GTYPE_NINTENDO_DS
     */
    public static final int GTYPE_NDS = 0x0001839D;
    /**
     * GTYPE_NINTENDO_ENTERTAINMENT_SYSTEM
     */
    public static final int GTYPE_NES = 0x000183DD;
    /**
     * GTYPE_SNK_NEO_GEO
     */
    public static final int GTYPE_NG = 0x00000611;
    /**
     * GTYPE_NOKIA_N_GAGE
     */
    public static final int GTYPE_NGAG = 0x006112D1;
    /**
     * GTYPE_SNK_NEO_GEO_CD
     */
    public static final int GTYPE_NGCD = 0x0061134E;
    /**
     * GTYPE_SNK_NEO_GEO_POCKET
     */
    public static final int GTYPE_NGP = 0x0001845A;
    /**
     * GTYPE_SNK_NEO_GEO_POCKET_COLOR
     */
    public static final int GTYPE_NGPC = 0x0061168D;
    /**
     * GTYPE_MAGNAVOX_ODYSSEY_2
     */
    public static final int GTYPE_ODY2 = 0x0064E8C3;
    /**
     * GTYPE_OUYA
     */
    public static final int GTYPE_OUYA = 0x0065F8CB;
    /**
     * GTYPE_PALM_OS
     */
    public static final int GTYPE_PALM = 0x0068B597;
    /**
     * GTYPE_PINBALL
     */
    public static final int GTYPE_PBL = 0x0001A316;
    /**
     * GTYPE_NINTENDO_PLAYCHOICE_10
     */
    public static final int GTYPE_PC10 = 0x0068D081;
    /**
     * GTYPE_NEC_PC_8801
     */
    public static final int GTYPE_PC88 = 0x0068D249;
    /**
     * GTYPE_NEC_PC_9801
     */
    public static final int GTYPE_PC98 = 0x0068D289;
    /**
     * GTYPE_PC_BOOT_LOADER
     */
    public static final int GTYPE_PCB = 0x0001A34C;
    /**
     * GTYPE_NEC_PC_ENGINE_CD_ROM
     */
    public static final int GTYPE_PCCD = 0x0068D34E;
    /**
     * GTYPE_NEC_PC_ENGINE
     */
    public static final int GTYPE_PCE = 0x0001A34F;
    /**
     * GTYPE_NEC_PC_ENGINE_DUO
     */
    public static final int GTYPE_PCED = 0x0068D3CE;
    /**
     * GTYPE_SEGA_PICO_OR_KIDS_COMPUTER_PICO
     */
    public static final int GTYPE_PICO = 0x00693359;
    /**
     * GTYPE_MICROSOFT_POCKET_PC_2000_OR_2002
     */
    public static final int GTYPE_PPC = 0x0001A68D;
    /**
     * GTYPE_SONY_PLAYSTATION_1
     */
    public static final int GTYPE_PS1 = 0x0001A742;
    /**
     * GTYPE_SONY_PLAYSTATION_2
     */
    public static final int GTYPE_PS2 = 0x0001A743;
    /**
     * GTYPE_SONY_PLAYSTATION_3
     */
    public static final int GTYPE_PS3 = 0x0001A744;
    /**
     * GTYPE_SONY_PLAYSTATION_4
     */
    public static final int GTYPE_PS4 = 0x0001A745;
    /**
     * GTYPE_SONY_PLAYSTATION_5
     */
    public static final int GTYPE_PS5 = 0x0001A746;
    /**
     * GTYPE_SONY_PLAYSTATION_PORTABLE
     */
    public static final int GTYPE_PSP = 0x0001A75A;
    /**
     * GTYPE_SINCLAIR_QUANTUM_LEAP
     */
    public static final int GTYPE_QL = 0x000006D6;
    /**
     * GTYPE_SEGA_SATURN
     */
    public static final int GTYPE_SAT = 0x0001D2DE;
    /**
     * GTYPE_SEGA_CD
     */
    public static final int GTYPE_SCD = 0x0001D34E;
    /**
     * GTYPE_SUPER_FAMICOM
     */
    public static final int GTYPE_SFC = 0x0001D40D;
    /**
     * GTYPE_SLOT_MACHINE
     */
    public static final int GTYPE_SLOT = 0x0075665E;
    /**
     * GTYPE_SEGA_MARK_III
     */
    public static final int GTYPE_SM3 = 0x0001D5C4;
    /**
     * GTYPE_SEGA_MEGA_DRIVE
     */
    public static final int GTYPE_SMD = 0x0001D5CE;
    /**
     * GTYPE_SEGA_MASTER_SYSTEM
     */
    public static final int GTYPE_SMS = 0x0001D5DD;
    /**
     * GTYPE_SUPER_NINTENDO_ENTERTAINMENT_SYSTEM
     */
    public static final int GTYPE_SNES = 0x007583DD;
    /**
     * GTYPE_NINTENDO_SWITCH
     */
    public static final int GTYPE_SW = 0x00000761;
    /**
     * GTYPE_SYMBIAN
     */
    public static final int GTYPE_SYM = 0x0001D8D7;
    /**
     * GTYPE_NEC_TURBODUO
     */
    public static final int GTYPE_TD = 0x0000078E;
    /**
     * GTYPE_NEC_TURBOGRAFX_16
     */
    public static final int GTYPE_TG16 = 0x00791087;
    /**
     * GTYPE_NEC_TURBOGRAFX_CD_ROM
     */
    public static final int GTYPE_TGCD = 0x0079134E;
    /**
     * GTYPE_THOMSON_MO
     */
    public static final int GTYPE_TMO = 0x0001E5D9;
    /**
     * GTYPE_RADIO_SHACK_TRS_80
     */
    public static final int GTYPE_TRS = 0x0001E71D;
    /**
     * GTYPE_RADIO_SHACK_TRS_80_CO_CO
     */
    public static final int GTYPE_TRSC = 0x0079C74D;
    /**
     * GTYPE_THOMSON_TO
     */
    public static final int GTYPE_TTO = 0x0001E799;
    /**
     * GTYPE_NINTENDO_VIRTUAL_BOY
     */
    public static final int GTYPE_VB = 0x0000080C;
    /**
     * GTYPE_GENERAL_CONSUMER_ELECTRIC_VECTREX
     */
    public static final int GTYPE_VECT = 0x0080F35E;
    /**
     * GTYPE_COMMODORE_VIC_20
     */
    public static final int GTYPE_VIC = 0x000204CD;
    /**
     * GTYPE_SONY_PLAYSTATION_VITA
     */
    public static final int GTYPE_VITA = 0x0081378B;
    /**
     * GTYPE_NINTENDO_VS_SYSTEM
     */
    public static final int GTYPE_VS = 0x0000081D;
    /**
     * GTYPE_V_TECH_V_SMILE
     */
    public static final int GTYPE_VVS = 0x0002081D;
    /**
     * GTYPE_V_TECH_V_SMILE_POCKET
     */
    public static final int GTYPE_VSP = 0x0002075A;
    /**
     * GTYPE_16_BIT_WINDOWS_1
     */
    public static final int GTYPE_W16 = 0x00021087;
    /**
     * GTYPE_32_BIT_WINDOWS_95
     */
    public static final int GTYPE_W32 = 0x00021103;
    /**
     * GTYPE_64_BIT_WINDOWS_NT64
     */
    public static final int GTYPE_W64 = 0x000211C5;
    /**
     * GTYPE_MICROSOFT_WINDOWS_CE_2_0
     */
    public static final int GTYPE_WCE = 0x0002134F;
    /**
     * GTYPE_WEB_BROWSER
     */
    public static final int GTYPE_WEB = 0x000213CC;
    /**
     * GTYPE_NINTENDO_WII
     */
    public static final int GTYPE_WII = 0x000214D3;
    /**
     * GTYPE_NINTENDO_WII_U
     */
    public static final int GTYPE_WIIU = 0x008534DF;
    /**
     * GTYPE_WINDOWS_MOBILE_2003
     */
    public static final int GTYPE_WMOB = 0x0085764C;
    /**
     * GTYPE_WINDOWS_PHONE
     */
    public static final int GTYPE_WPH = 0x00021692;
    /**
     * GTYPE_BANDAI_WONDERSWAN
     */
    public static final int GTYPE_WS = 0x0000085D;
    /**
     * GTYPE_BANDAI_WONDERSWAN_COLOR
     */
    public static final int GTYPE_WSC = 0x0002174D;
    /**
     * GTYPE_SHARP_X1
     */
    public static final int GTYPE_X1 = 0x00000882;
    /**
     * GTYPE_MICROSOFT_XBOX_360
     */
    public static final int GTYPE_X360 = 0x008841C1;
    /**
     * GTYPE_SHARP_X68000
     */
    public static final int GTYPE_X68 = 0x000221C9;
    /**
     * GTYPE_MICROSOFT_XBOX
     */
    public static final int GTYPE_XBOX = 0x0088C662;
    /**
     * GTYPE_ATARI_XEGS
     */
    public static final int GTYPE_XEGS = 0x0088F45D;
    /**
     * GTYPE_MICROSOFT_XBOX_ONE
     */
    public static final int GTYPE_XONE = 0x0089960F;
    /**
     * GTYPE_MICROSOFT_XBOX_SERIES_X
     */
    public static final int GTYPE_XSX = 0x00022762;
    /**
     * GTYPE_ZEEBO_ZEEBO
     */
    public static final int GTYPE_ZEBO = 0x0090F319;
    /**
     * GTYPE_TAPWAVE_ZODIAC
     */
    public static final int GTYPE_ZOD = 0x0002464E;
    /**
     * GTYPE_SINCLAIR_ZX_SPECTRUM
     */
    public static final int GTYPE_ZXS = 0x0002489D;

}
