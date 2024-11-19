/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sgucore.data;

import parsers.BasicFile;
import parsers.pkg.IISO_Root;

/**
 *
 * @author armax
 */
public class ISO_Root extends IISO_Root {

    public ISO_Root(BasicFile parent)
    {
        super(parent, Flags.SVOF, AFS.class);
    }

}
