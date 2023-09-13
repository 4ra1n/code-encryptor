#include "encryptor.h"
#include "xxtea_en.h"

#include "string.h"
#include "stdlib.h"
#include "core_en.h"

void internal(unsigned char *chars, int start) {
    unsigned char first[4];
    for (int i = start; i < start + 4; i++) {
        first[i - start] = chars[i];
    }
    unsigned char second[4];
    for (int i = start + 4; i < start + 8; i++) {
        second[i - start - 4] = chars[i];
    }
    uint32_t v[2] = {convert(first), convert(second)};
    // key: Y4Sec-Team-4ra1n
    // 59345365 632D5465 616D2D34 7261316E
    uint32_t const k[4] = {
            (unsigned int) 0x65533459, (unsigned int) 0x65542d63,
            (unsigned int) 0X342d6d61, (unsigned int) 0x6e316172,
    };
    tea_encrypt(v, k);
    unsigned char first_arr[4];
    unsigned char second_arr[4];
    revert(v[0], first_arr);
    revert(v[1], second_arr);
    for (int i = start; i < start + 4; i++) {
        chars[i] = first_arr[i - start];
    }
    for (int i = start + 4; i < start + 8; i++) {
        chars[i] = second_arr[i - start - 4];
    }
}

// ClassFile {
//    u4             magic; (ignore)
//    u2             minor_version; (ignore)
//    u2             major_version; (ignore)
//    u2             constant_pool_count; (ignore)
//    cp_info        constant_pool[constant_pool_count-1];
//    ...
// }
// start index: 4+2+2+2=10
// 1. asm encrypt
// 2. tea encrypt: {[10:14],[14:18]} {[18:22],[22:26]} {[26:30],[30:34]}
JNIEXPORT jbyteArray JNICALL Java_org_y4sec_encryptor_core_CodeEncryptor_encrypt
        (JNIEnv *env, jclass cls, jbyteArray text, jint length) {
    jbyte *data = (*env)->GetByteArrayElements(env, text, NULL);
    unsigned char *chars = (unsigned char *) malloc(length);
    memcpy(chars, data, length);
    // 1. asm encrypt
    encrypt(chars, length);
    EN_LOG("ASM ENCRYPT FINISH");
    // 2. tea encrypt
    if (length < 34) {
        EN_LOG("ERROR: BYTE CODE TOO SHORT");
        return text;
    }
    // {[10:14],[14:18]}
    internal(chars, 10);
    EN_LOG("TEA ENCRYPT #1");
    // {[18:22],[22:26]}
    internal(chars, 18);
    EN_LOG("TEA ENCRYPT #2");
    // {[26:30],[30:34]}
    internal(chars, 26);
    EN_LOG("TEA ENCRYPT #3");
    (*env)->SetByteArrayRegion(env, text, 0, length, (jbyte *) chars);
    return text;
}