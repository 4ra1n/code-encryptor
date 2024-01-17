package org.y4sec.encryptor.core;

/**
 * Constants
 */
public interface Constants {
    String Version = "0.4";
    String DecrypterDLL = "libdecrypter.dll";
    String DecrypterSo = "libdecrypter.so";
    String EncryptorDLL = "libencryptor.dll";
    String EncryptorSO = "libencryptor.so";
    String TempDir = "code-encryptor-temp";
    String NewFileSuffix = "encrypted";
    String DllFile = ".dll";
    String SOFile = ".so";
    String ClassFile = ".class";
    String WindowsOS = "windows";
    Integer BufSize = 16384;
    String PatchCMD = "patch";
    String ExportCMD = "export";
}
