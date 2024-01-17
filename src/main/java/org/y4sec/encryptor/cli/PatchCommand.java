package org.y4sec.encryptor.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.y4sec.encryptor.core.Constants;
import org.y4sec.encryptor.core.PatchHelper;
import org.y4sec.encryptor.util.JNIUtil;
import org.y4sec.encryptor.util.OSUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Core method
 */
@Parameters(commandDescription = "Patch Command")
class PatchCommand implements Constants {
    private static final Logger logger = LogManager.getLogger();
    /**
     * Input jar file
     */
    @Parameter(names = "--jar", description = "JAR File Path", required = true)
    String jarPath;
    /**
     * Encrypt KEY
     */
    @Parameter(names = "--key", description = "KEY", required = true)
    String key;
    /**
     * Encrypt package name (such as a.b.c)
     */
    @Parameter(names = "--package", description = "Encrypt Package Name", required = true)
    String packageName;

    public void execute() {
        logger.info("patch jar: {}", jarPath);
        Path path = Paths.get(jarPath);

        if (!OSUtil.isArch64()) {
            System.out.println("ONLY SUPPORT ARCH 64");
            return;
        }

        if (key.length() != 16) {
            System.out.println("KEY LENGTH MUST BE 16");
            return;
        }

        Path libPath;
        Path tmp = Paths.get(TempDir);
        // extract encrypt dll and load it
        if (OSUtil.isWin()) {
            JNIUtil.extractDllSo(EncryptorDLL, null, false);
            libPath = tmp.resolve(EncryptorDLL);
        } else {
            JNIUtil.extractDllSo(EncryptorSO, null, false);
            libPath = tmp.resolve(EncryptorSO);
        }
        if (packageName == null || packageName.isEmpty()) {
            logger.error("need package name");
            return;
        }

        // do patch
        PatchHelper.patchJar(path, libPath, packageName, key.getBytes());
    }
}
