/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sgucore.data;

import parsers.BasicFile;
import parsers.pkg.IGameDirectory;

/**
 *
 * @author armax
 */
public class GameDirectory extends IGameDirectory {

    public GameDirectory(BasicFile parent)
    {
        super(parent, AFS.class, ISO_Root.class);
    }

}
