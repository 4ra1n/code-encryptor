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

## 0.3

修改`JVM`阻止通过`HSDB`以`dump`字节码，支持`linux`和`windows`

更新日志：
- [FEATURE] 参考`beichen`师傅议题阻止dump字节码的方法 #6
- [FEATURE] 应该对完整的所有的字节码进行加密 #3
  [FEATURE] GUI 加入密钥部分 #2
- [FEATURE] GUI 和命令行都应该加入自动生成运行命令部分 #7
- [FEATURE] 优化打印的日志以及调试信息 #4
- [FEATURE] ENGLISH DOC #1
- 代码和文档的优化

提供 GUI 和 CLI 两个版本，CLI 版本分为 patch 和 export 两个命令
- patch 命令用于加密输入的 jar 包
- export 命令用于导出对应的 dll 解密库

GUI 版本的使用和 CLI 版本基本一致，具体参考 README 文件

## 0.4

更新日志：
- 优化`native`层的代码
- 优化`README`说明文档

提供 GUI 和 CLI 两个版本，CLI 版本分为 patch 和 export 两个命令
- patch 命令用于加密输入的 jar 包
- export 命令用于导出对应的 dll/so 解密库

GUI 版本的使用和 CLI 版本基本一致，具体参考 README 文件