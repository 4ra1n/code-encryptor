#include <jvmti.h>
#include "xxtea_de.h"
#include "core_de.h"
#include "stdlib.h"
#include "string.h"

// PACKAGE
char *PACKAGE_NAME;

void internal(unsigned char *_data, int start) {
    unsigned char first[4];
    for (int i = start; i < start + 4; i++) {
        first[i - start] = _data[i];
    }
    unsigned char second[4];
    for (int i = start + 4; i < start + 8; i++) {
        second[i - start - 4] = _data[i];
    }
    uint32_t v[2] = {convert(first), convert(second)};
    // key: Y4Sec-Team-4ra1n
    // 59345365 632D5465 616D2D34 7261316E
    uint32_t const k[4] = {
            (unsigned int) 0x65533459, (unsigned int) 0x65542d63,
            (unsigned int) 0X342d6d61, (unsigned int) 0x6e316172,
    };
    tea_decrypt(v, k);
    unsigned char first_arr[4];
    unsigned char second_arr[4];
    revert(v[0], first_arr);
    revert(v[1], second_arr);
    for (int i = start; i < start + 4; i++) {
        _data[i] = first_arr[i - start];
    }
    for (int i = start + 4; i < start + 8; i++) {
        _data[i] = second_arr[i - start - 4];
    }
}

// 1. tea encrypt
// 2. asm encrypt
void JNICALL ClassDecryptHook(
        jvmtiEnv *jvmti_env,
        JNIEnv *jni_env,
        jclass class_being_redefined,
        jobject loader,
        const char *name,
        jobject protection_domain,
        jint class_data_len,
        const unsigned char *class_data,
        jint *new_class_data_len,
        unsigned char **new_class_data) {
    *new_class_data_len = class_data_len;
    (*jvmti_env)->Allocate(jvmti_env, class_data_len, new_class_data);
    unsigned char *_data = *new_class_data;
    if (name && strncmp(name, PACKAGE_NAME, strlen(PACKAGE_NAME)) == 0) {
        for (int i = 0; i < class_data_len; i++) {
            _data[i] = class_data[i];
        }
        if (class_data_len < 34) {
            return;
        }
        // 1. {[10:14],[14:18]}
        internal(_data, 10);
        // 2. {[18:22],[22:26]}
        internal(_data, 18);
        // 3. {[26:30],[30:34]}
        internal(_data, 26);
        // 4. asm encrypt
        decrypt((unsigned char *) _data, class_data_len);
    } else {
        for (int i = 0; i < class_data_len; i++) {
            _data[i] = class_data[i];
        }
    }
}

JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm) {
    LOG("UNLOAD AGENT");
}

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved) {
    printf("PARAMS: %s\n", options);

    const char *key = "PACKAGE_NAME";
    char *value = NULL;

    // REPLACE . -> /
    char modified_str[256];
    size_t modified_str_size = sizeof(modified_str);
    strncpy(modified_str, options, modified_str_size - 1);
    modified_str[modified_str_size - 1] = '\0';

    for (size_t i = 0; modified_str[i] != '\0'; ++i) {
        if (modified_str[i] == '.') {
            modified_str[i] = '/';
        }
    }

    // SPLIT A=B -> B
    char *token = strtok(modified_str, "=");
    while (token != NULL) {
        if (strcmp(token, key) == 0) {
            value = strtok(NULL, "=");
            break;
        }
        token = strtok(NULL, "=");
    }

    if (value == NULL) {
        LOG("NEED PACKAGE_NAME PARAMS\n");
        return 0;
    }

    // SET PACKAGE_NAME
    PACKAGE_NAME = (char *) malloc(strlen(value) + 1);
    strcpy(PACKAGE_NAME, value);

    printf("PACKAGE: %s\n", PACKAGE_NAME);
    printf("PACKAGE-LENGTH: %lu\n", strlen(PACKAGE_NAME));

    jvmtiEnv *jvmti;
    LOG("INIT JVMTI ENVIRONMENT");
    jint ret = (*vm)->GetEnv(vm, (void **) &jvmti, JVMTI_VERSION);
    if (JNI_OK != ret) {
        printf("ERROR: Unable to access JVMTI!\n");
        printf("PLEASE USE JVM VERSION 8");
        return ret;
    }
    LOG("INIT JVMTI CAPABILITIES");
    jvmtiCapabilities capabilities;
    (void) memset(&capabilities, 0, sizeof(capabilities));

    capabilities.can_generate_all_class_hook_events = 1;

    LOG("ADD JVMTI CAPABILITIES");
    jvmtiError error = (*jvmti)->AddCapabilities(jvmti, &capabilities);
    if (JVMTI_ERROR_NONE != error) {
        printf("ERROR: Unable to AddCapabilities JVMTI!\n");
        return error;
    }

    LOG("INIT JVMTI CALLBACKS");
    jvmtiEventCallbacks callbacks;
    (void) memset(&callbacks, 0, sizeof(callbacks));

    LOG("SET JVMTI CLASS FILE LOAD HOOK");
    callbacks.ClassFileLoadHook = &ClassDecryptHook;
    error = (*jvmti)->SetEventCallbacks(jvmti, &callbacks, sizeof(callbacks));
    if (JVMTI_ERROR_NONE != error) {
        printf("ERROR: Unable to SetEventCallbacks JVMTI!\n");
        return error;
    }
    LOG("SET EVENT NOTIFICATION MODE");
    error = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
    if (JVMTI_ERROR_NONE != error) {
        printf("ERROR: Unable to SetEventNotificationMode JVMTI!\n");
        return error;
    }

    LOG("INIT JVMTI SUCCESS");
    return JNI_OK;
}