package org.y4sec.encryptor.cli;

import com.beust.jcommander.JCommander;
import org.y4sec.encryptor.core.Constants;

/**
 * Application
 */
public class Application implements Constants {
    public static void main(String[] args) {
        printLogo();

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

    /**
     * Print LOGO
     */
    private static void printLogo() {
        System.out.println("___________                                   __                \n" +
                "\\_   _____/ ____   ___________ ___.__._______/  |_  ___________ \n" +
                " |    __)_ /    \\_/ ___\\_  __ <   |  |\\____ \\   __\\/  _ \\_  __ \\\n" +
                " |        \\   |  \\  \\___|  | \\/\\___  ||  |_> >  | (  <_> )  | \\/\n" +
                "/_______  /___|  /\\___  >__|   / ____||   __/|__|  \\____/|__|   \n" +
                "        \\/     \\/     \\/       \\/     |__|                      ");
        System.out.printf("Bytecode Encryptor Tool - version: %s\n", Version);
        System.out.println("Powered By 4ra1n (https://github.com/4ra1n)");
    }
}
