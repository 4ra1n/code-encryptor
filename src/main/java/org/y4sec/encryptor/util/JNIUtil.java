package org.y4sec.encryptor.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.y4sec.encryptor.core.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JNI Utils
 */
public class JNIUtil implements Constants {
    private static final Logger logger = LogManager.getLogger();
    private static final String lib = "java.library.path";

    /**
     * Make new JNI lib effective
     *
     * @return success or not
     */
    @SuppressWarnings("all")
    private static boolean deleteUrls() {
        try {
            final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
            return true;
        } catch (Exception ex) {
            logger.error("delete classloader sys_paths error: {}", ex.toString());
        }
        return false;
    }

    /**
     * Load JNI lib
     *
     * @param path dll/so path
     * @return success or not
     */
    public static boolean loadLib(String path) {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            logger.error("load lib error: file not found");
            return false;
        }
        if (Files.isDirectory(p)) {
            logger.error("load lib error: input file is a dir");
            return false;
        }
        String os = System.getProperty("os.name").toLowerCase();
        String libDirAbsPath = Paths.get(p.toFile().getParent()).toAbsolutePath().toString();
        String originLib = System.getProperty(lib);
        if (os.contains(WindowsOS)) {
            originLib = originLib + String.format(";%s;", libDirAbsPath);
            System.setProperty(lib, originLib);
            if (!deleteUrls()) {
                return false;
            }
            String dll = p.toFile().getName().toLowerCase();
            if (!dll.endsWith(DllFile)) {
                logger.error("load lib error: must be a dll file");
                return false;
            }
            String file = dll.split("\\.dll")[0].trim();
            logger.info("load library: " + file);
            System.loadLibrary(file);
        } else {
            originLib = originLib + String.format(":%s:", libDirAbsPath);
            System.setProperty(lib, originLib);
            if (!deleteUrls()) {
                return false;
            }
            String so = p.toFile().getName().toLowerCase();
            if (!so.endsWith(SOFile)) {
                logger.error("must be a so file");
                return false;
            }
            String file = so.split("\\.so")[0].trim();
            logger.info("load library: " + file);
            System.loadLibrary(file);
        }
        return true;
    }

    /**
     * Write dll/so file to temp directory and load it
     *
     * @param filename dll/so file name in resources
     */
    public static void extractDllSo(String filename,String dir,boolean load) {
        InputStream is = null;
        try {
            is = JNIUtil.class.getClassLoader().getResourceAsStream(filename);
            if (is == null) {
                logger.error("error dll name");
                return;
            }
            if(dir==null || dir.isEmpty()){
                dir = TempDir;
            }
            Path dirPath = Files.createDirectories(Paths.get(dir));
            Path outputFile = dirPath.resolve(filename);

            if (!Files.exists(outputFile)) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[BufSize];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                Files.write(outputFile, buffer.toByteArray());
                logger.info("write file: " + outputFile.toAbsolutePath());
            }
            if(load) {
                boolean success = loadLib(outputFile.toAbsolutePath().toString());
                if (!success) {
                    logger.error("load lib failed");
                }
            }
        } catch (Exception ex) {
            logger.error("extract file error: {}", ex.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("close stream error: {}", e.toString());
                }
            }
        }
    }
}
