package org.y4sec.encryptor.util;

public class OSUtil {
    public static boolean isWin(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
    public static boolean isArch64(){
        return System.getProperty("sun.arch.data.model").equalsIgnoreCase("64");
    }
}
