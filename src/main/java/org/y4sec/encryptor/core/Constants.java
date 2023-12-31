package org.y4sec.encryptor.core;

/**
 * Constants
 */
public interface Constants {
    String Version = "0.0.1";
    String DecrypterDLL = "decrypter.dll";
    String DecrypterSo = "libdecrypter.so";
    String EncryptorDLL = "encryptor.dll";
    String EncryptorSO = "libencryptor.so";
    String TempDir = "code-encryptor-plus-temp";
    String NewFileSuffix = "encrypted";
    String DllFile = ".dll";
    String SOFile = ".so";
    String ClassFile = ".class";
    String WindowsOS = "windows";
    Integer BufSize = 16384;
    String PatchCMD = "patch";
    String ExportCMD = "export";
}
