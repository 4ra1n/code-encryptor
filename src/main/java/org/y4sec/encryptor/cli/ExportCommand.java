package org.y4sec.encryptor.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.y4sec.encryptor.core.Constants;
import org.y4sec.encryptor.util.JNIUtil;

@Parameters(commandDescription = "Export Command")
class ExportCommand implements Command, Constants {
    private static final Logger logger = LogManager.getLogger();
    @Parameter(names = "--output", description = "Output File Path")
    @SuppressWarnings("unused")
    private String outputPath;

    public void execute() {
        logger.info("execute export command");
        JNIUtil.extractDllSo(DecrypterDLL, outputPath, false);
        System.out.println("----------- ADD VM OPTIONS -----------");
        System.out.println("java -agentlib:path-to-file=PACKAGE_NAME=xxx [other-params]");
        System.out.println("java -agentlib:encryptor=PACKAGE_NAME=org.y4sec -jar test.jar");
    }
}