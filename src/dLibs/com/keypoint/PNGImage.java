/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dLibs.com.keypoint;

/**
 *
 * @author ARMAX
 */
public class PNGImage {

    public int code;
    public byte[] data;
    public int w;
    public int h;
    public byte[] pal;

    public PNGImage(int width, int height, boolean init)
    {
        this.data = init ? new byte[width * height * 4] : null;
        this.w = width;
        this.h = height;
    }

    public PNGImage(int width, int height, byte[] data)
    {
        this.data = data;
        this.w = width;
        this.h = height;
    }

    public PNGImage()
    {
    }

}
