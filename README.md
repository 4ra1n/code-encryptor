# code-encryptor-plus

## 介绍

使用`JNI`加密字节码，通过`JVMTI`解密字节码以保护代码

提供两份`DLL`文件，一份加密一份解密，实际运行只需使用解密`DLL`文件

加密和解密的过程可以指定具体的包名，只加密核心关键部分

详细文章参考：[JVMTI 加密字节码详解](https://mp.weixin.qq.com/s?__biz=MzkzOTQzOTE1NQ==&mid=2247483823&idx=1&sn=a3ae476ccedd2d7fec96e5887989d1c0&chksm=c2f1a4f3f5862de57ce35ebcbf1c39f231ec282934ae8740654be372b1ca4f712c6c101c91e6#rd)

加密后的`Class`文件变成无法解析的畸形文件

![jd-gui](img/002.png)

除了开头保持了`Magic`部分，后续是无法解析的字节

![hex](img/003.png)

## 特点

本项目相比于网上公开的文章/代码，优势和特点有以下几条：
- 原文章固定了包名，用户想加密自己的包名需要重新编译`DLL`
- 原文章加密和解密`DLL`是同一个，这样只用`JNI`调用下加密即可破解
- 原文章的代码仅是`Demo`级别，无法直接上手测试和使用
- 原文章没有加入具体的加密算法，仅是简单的运算，需要加强
- 原文章的代码存在一些`BUG`和优化空间

目前的加密解密算法：
- 汇编实现的多层位运算，交换字节等
- 三次`XXTEA`算法，抽取`10-34`位字节
- 支持自定义密钥

## 构建

编译环境：
- Windows 11 / Ubuntu 22.04
- JDK 8 / Maven
- MSVC + ml64 (Windows) / gcc + nasm (Linux)
- CMake 3.x
- Python 3.x

`native`目录使用`cmake`构建，生成`dll`和`so`移动到`resources`中使用`Maven`构建

## 快速开始

加密解密部分使用`C`做一层加密，使用`汇编`二层加密，已提供编译好的`Release`版本`DLL/SO`文件嵌入`Jar`包中

内置支持是`JDK-8`，其他版本的`JDK`只需要更换`JNI.h`头文件重新编译，新版本已支持`Windows`和`Linux`

加密你的`Jar`包：（指定`Jar`包和`package`加密包名以及密钥`key`）

```shell
 java -jar code-encryptor-plus.jar patch --jar your-jar.jar --package com.your.pack --key your-key
```

![](img/004.png)

导出解密`DLL/SO`文件：（默认导出到`code-encryptor-plus-temp`目录不建议修改）

```shell
java -jar code-encryptor-plus.jar export
```

![](img/005.png)

使用解密`DLL/SO`启动`Jar`包：（使用`-agentpath`参数）

注意必须有两个参数`PACKAGE_NAME`和`KEY`

```shell
java -agentpath:D:\abs-path\decrypter.dll=PACKAGE_NAME=com.your.pack,KEY=your-key --jar your-jar.jar
```

![](img/006.png)

另外支持了简易的`GUI`版本，选择需要加密的`Jar`文件即可一键加密（仅支持`Windows`版）

![screenshot](img/001.png)

## 更新日志

### 0.2

两个重要功能的更新：
- 支持了`linux`系统
- 支持了自定义加密密钥

更新日志：
- 基于`gcc`和`nasm`支持`linux x86_64 (amd64)`
- 支持`Windows`和`Linux`任意密钥加密解密
- 使用`execstack`为`so`库修改堆栈可执行属性
- 汇编中部分寄存器忘记恢复状态导致某些`JVM`崩溃
- 修复`linux`中字符串处理函数的缓冲区溢出问题
- 修改`README`部分应该使用`-agentpath`启动
- 优化重构`Java`层代码，抽出工具类等
- 避免重复宏定义的问题

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
