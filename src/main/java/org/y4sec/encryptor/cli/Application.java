package org.y4sec.encryptor.cli;

import com.beust.jcommander.JCommander;
import org.y4sec.encryptor.core.Constants;


public class Application implements Constants {
    public static void main(String[] args) {
        printLogo();
        // OS
        String os = System.getProperty("os.name").toLowerCase();
        // ARCH
        String bits = System.getProperty("sun.arch.data.model").toLowerCase();
        if (!os.contains("windows") || !bits.equals("64")) {
            System.out.println("ONLY SUPPORT WINDOWS X64");
            System.out.printf("YOUR OS IS: %s - %s%n", os, bits);
            return;
        }
        String javaVersion = System.getProperty("java.version");
        if (!javaVersion.startsWith("1.8")) {
            System.out.println("ONLY SUPPORT JAVA 8");
            return;
        }

        PatchCommand patchCommand = new PatchCommand();
        ExportCommand exportCommand = new ExportCommand();

        JCommander jCommander = JCommander.newBuilder()
                .addCommand(PatchCMD, patchCommand)
                .addCommand(ExportCMD, exportCommand)
                .build();

        jCommander.parse(args);

        String command = jCommander.getParsedCommand();
        if (command == null) {
            jCommander.usage();
            return;
        }

        switch (command) {
            case PatchCMD:
                patchCommand.execute();
                break;
            case ExportCMD:
                exportCommand.execute();
                break;
            default:
                jCommander.usage();
                break;
        }
    }

    private static void printLogo() {
        System.out.println("___________                                   __                \n" +
                "\\_   _____/ ____   ___________ ___.__._______/  |_  ___________ \n" +
                " |    __)_ /    \\_/ ___\\_  __ <   |  |\\____ \\   __\\/  _ \\_  __ \\\n" +
                " |        \\   |  \\  \\___|  | \\/\\___  ||  |_> >  | (  <_> )  | \\/\n" +
                "/_______  /___|  /\\___  >__|   / ____||   __/|__|  \\____/|__|   \n" +
                "        \\/     \\/     \\/       \\/     |__|                      ");
        System.out.printf("Bytecode Encryptor Tool - version: %s\n", Version);
        System.out.println("Powered By Y4Sec-Team");
    }
}
