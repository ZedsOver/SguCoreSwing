/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parsers;

import dLibs.com.keypoint.PNGImage;

/**
 *
 * @author armax
 */
public interface IImageData {

    public static final boolean[] DEF_PAL = {true};

    public PNGImage readPngImage(int i);

    public byte[] readImage(int i);

}
