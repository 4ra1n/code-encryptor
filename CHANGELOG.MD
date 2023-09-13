## 0.1

第一个版本：
- code-encryptor-plus-0.0.1-cli.jar
- code-encryptor-plus-0.0.1-gui.jar

提供 GUI 和 CLI 两个版本，CLI 版本分为 patch 和 export 两个命令
- patch 命令用于加密输入的 jar 包
- export 命令用于导出对应的 dll 解密库

GUI 版本的使用和 CLI 版本基本一致，具体参考 README 文件

目前的加密算法：
- 汇编实现的多层位运算，交换字节等
- 三次XXTEA算法，抽取 10-34 位字节

适用环境：
- Windows 64 位 (Linux环境需要重写)
- JDK 8 (其他版本可以自行编译)

## 0.2

- todo