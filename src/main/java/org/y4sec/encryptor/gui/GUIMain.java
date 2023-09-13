package org.y4sec.encryptor.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import org.y4sec.encryptor.util.OSUtil;

public class GUIMain {
    public static void main(String[] args) {
        if (OSUtil.isWin() && OSUtil.isArch64()) {
            FlatDarkLaf.setup();
            MainForm.start();
        } else {
            System.out.println("GUI ONLY SUPPORT WINDOWS 64");
        }
    }
}
