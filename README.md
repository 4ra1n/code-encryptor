# code-encryptor-plus

## 介绍

使用`JNI`加密字节码，通过`JVMTI`解密字节码以保护代码

提供两份`DLL`文件，一份加密一份解密，实际运行只需使用解密`DLL`文件

加密和解密的过程可以指定具体的包名，只加密核心关键部分

详细文章参考：[JVMTI 加密字节码详解]([http://127.0.0.1:8080](https://mp.weixin.qq.com/s?__biz=MzkzOTQzOTE1NQ==&mid=2247483823&idx=1&sn=a3ae476ccedd2d7fec96e5887989d1c0&chksm=c2f1a4f3f5862de57ce35ebcbf1c39f231ec282934ae8740654be372b1ca4f712c6c101c91e6#rd))

加密后的`Class`文件变成无法解析的畸形文件

![jd-gui](img/002.png)

除了开头保持了`Magic`部分，后续是无法解析的字节

![hex](img/003.png)

## 快速开始

加密解密部分使用`C`做一层加密，使用`汇编`二层加密，已提供编译好的`Release`版本`DLL`文件嵌入`Jar`包中

仅支持`Windows 64位`/`JDK-8`环境，其他版本的`JDK`只需要更换`JNI.h`头文件重新编译，其他操作系统可能需要重写

加密你的`Jar`包：（指定`Jar`包和`package`加密包名）

```shell
 java -jar code-encryptor-plus.jar patch --jar your-jar.jar --package com.your.pack
```

导出解密`DLL`文件：（默认导出到`code-encryptor-plus-temp`目录）

```shell
java -jar code-encryptor-plus.jar export
```

使用解密`DLL`启动`Jar`包：（使用`-agentlib`参数）

```shell
java -agentlib:D:\abs-path\decrypter=PACKAGE_NAME=com.your.pack --jar your-jar.jar
```

另外支持了简易的`GUI`版本，选择需要加密的`Jar`文件即可一键加密

![screenshot](img/001.png)

## 其他

不适用于`SpringBoot`场景，存在两个问题：
- `SpringBoot`不允许压缩`lib`依赖（这个有解决办法）
- `SpringBoot`启动扫描会分析`class`由于加密报错

网上提供了两种办法，可以参考

参考：https://zhuanlan.zhihu.com/p/545268749

类似地，启动扫描`class`的代码是无法使用这种加密的

## 致谢

感谢以下项目或文章提供的思路：
- https://juejin.cn/post/6844903487784894477
- https://github.com/sea-boat/ByteCodeEncrypt
