FROM ubuntu:22.04

LABEL author="4ra1n"
LABEL github="https://github.com/4ra1n"

ENV CODE_ENC_VER 0.4

WORKDIR /app

RUN apt-get update && apt-get install -y ca-certificates --reinstall

RUN echo "\
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ jammy main restricted universe multiverse\n\
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ jammy-updates main restricted universe multiverse\n\
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ jammy-backports main restricted universe multiverse\n\
deb http://security.ubuntu.com/ubuntu/ jammy-security main restricted universe multiverse" | tee /etc/apt/sources.list \
    && apt-get update && apt-get install -y zip unzip wget ninja-build gcc g++ openjdk-8-jdk nasm python3 execstack

COPY . .

RUN wget https://cmake.org/files/v3.28/cmake-3.28.0-linux-x86_64.tar.gz && \
	 tar -zxvf cmake-3.28.0-linux-x86_64.tar.gz

ENV PATH="${PATH}:/app/cmake-3.28.0-linux-x86_64/bin"

RUN cd native && \
	cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_MAKE_PROGRAM=ninja -G Ninja -S . -B build-release && \
	cmake --build build-release --target all && \
	python3 main.py && \
	zip -r ../build.zip target/*

CMD ["echo", "build code-encryptor ${CODE_ENC_VER} completed - /app/build.zip"]

