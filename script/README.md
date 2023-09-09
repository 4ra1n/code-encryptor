# script

encrypt

```shell
 java -jar .\code-encryptor-plus-0.0.1-jar-with-dependencies.jar patch --jar .\fake-mysql-gui-0.0.3.jar --package me.n1ar4
```

export

```shell
java -jar .\code-encryptor-plus-0.0.1-jar-with-dependencies.jar export
```

run

```shell
java -agentlib:D:\JavaCode\code-encryptor-plus\target\code-encryptor-plus-temp\decrypter=PACKAGE_NAME=me.n1ar4 -jar .\fake-mysql-gui-0.0.3_encrypted.jar
```