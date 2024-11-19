/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sgucore;

import com.DeltaSKR.IO.interfce.IOSys;
import java.util.prefs.Preferences;
import main_app.swing.MainGui;

/**
 *
 * @author armax
 */
public class MainApp {

    public static Preferences prefs;

    public static void main(String[] args)
    {
        IOSys.setInternal(null);
        prefs = Preferences.userNodeForPackage(MainApp.class);
        MainGui.main(args);
    }

}
