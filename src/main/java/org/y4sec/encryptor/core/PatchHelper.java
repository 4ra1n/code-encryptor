package org.y4sec.encryptor.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.y4sec.encryptor.util.JNIUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipOutputStream;

public class PatchHelper implements Constants {
    private static final Logger logger = LogManager.getLogger();

    @SuppressWarnings("unchecked")
    public static void patchJar(Path jarPath, Path libPath, String packageName,byte[] key) {
        logger.info("start patch jar");

        JNIUtil.loadLib(libPath.toAbsolutePath().toString());
        packageName = packageName.replaceAll("\\.", "/");

        try {
            File srcFile = jarPath.toFile();
            String srcName = srcFile.getName();
            String outputFileName = String.format("%s_%s.jar",
                    srcName.substring(0, srcName.lastIndexOf(".")), NewFileSuffix);
            File dstFile = new File(outputFileName);

            FileOutputStream dstFos = new FileOutputStream(dstFile);
            JarOutputStream dstJar = new JarOutputStream(dstFos);

            JarFile srcJar = new JarFile(srcFile);
            byte[] buf = new byte[1024];

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            Enumeration<JarEntry> enumeration = srcJar.entries();

            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                InputStream is = srcJar.getInputStream(entry);
                int len;
                while ((len = is.read(buf, 0, buf.length)) != -1) {
                    bao.write(buf, 0, len);
                }
                byte[] bytes = bao.toByteArray();

                String name = entry.getName();
                if (name.startsWith(packageName)) {
                    if (name.toLowerCase().endsWith(ClassFile)) {
                        try {
                            bytes = CodeEncryptor.encrypt(bytes, bytes.length,key);
                        } catch (Exception e) {
                            logger.error("encrypt error: {}", e.toString());
                            return;
                        }
                    }
                }

                JarEntry ne = new JarEntry(name);

                try {
                    dstJar.putNextEntry(ne);

                    // allow duplicate entry
                    // https://stackoverflow.com/questions/39958486/
                    Field namesField = ZipOutputStream.class.getDeclaredField("names");
                    namesField.setAccessible(true);
                    Object obj = namesField.get(dstJar);
                    HashSet<String> names = (HashSet<String>) obj;
                    names.remove(name);

                    dstJar.write(bytes);
                } catch (Exception z) {
                    logger.warn("put entry: {}", z.toString());
                }
                bao.reset();
            }

            srcJar.close();
            dstJar.close();
            dstFos.close();

            logger.info("encrypt finished");
            logger.info("output file: {}", outputFileName);
        } catch (Exception e) {
            logger.error("patch error: {}", e.toString());
        }
    }
}
