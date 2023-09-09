package org.y4sec.encryptor.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.y4sec.encryptor.core.Constants;
import org.y4sec.encryptor.core.PatchHelper;
import org.y4sec.encryptor.util.JNIUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

@Parameters(commandDescription = "Patch Command")
class PatchCommand implements Command, Constants {
    private static final Logger logger = LogManager.getLogger();
    @Parameter(names = "--jar", description = "JAR File Path", required = true)
    String jarPath;
    @Parameter(names = "--package", description = "Encrypt Package Name", required = true)
    String packageName;

    public void execute() {
        logger.info("patch jar: {}",jarPath);
        Path path = Paths.get(jarPath);
        JNIUtil.extractDllSo(EncryptorDLL,null,true);
        Path libPath = Paths.get(TempDir).resolve(EncryptorDLL);
        if(packageName == null || packageName.isEmpty()){
            logger.error("need package name");
            return;
        }
        PatchHelper.patchJar(path,libPath,packageName);
    }
}
