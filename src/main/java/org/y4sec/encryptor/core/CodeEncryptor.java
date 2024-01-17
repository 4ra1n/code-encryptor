package org.y4sec.encryptor.core;

/**
 * Code Encryptor use JNI
 */
public class CodeEncryptor {
    /**
     * Native Code Encrypt Method
     *
     * @param text ByteCode
     * @return Encrypted BytesCode
     */
    public native static byte[] encrypt(byte[] text, int length, byte[] key);
}
