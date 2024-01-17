@echo off
setlocal

set CMAKE_EXECUTABLE="D:\CLion 2023.1.1\bin\cmake\win\x64\bin\cmake.exe"
set CMAKE_BUILD_TYPE=Release
set CMAKE_MAKE_PROGRAM="D:/CLion 2023.1.1/bin/ninja/win/x64/ninja.exe"
set SOURCE_DIR="D:\JavaCode\code-encryptor-plus\native"
set BUILD_DIR="D:\JavaCode\code-encryptor-plus\native\build-release"

echo clean
if exist %BUILD_DIR% (
    echo delete %BUILD_DIR%
    rmdir /s /q %BUILD_DIR%
    echo delete finish
) else (
    echo %BUILD_DIR% not exist
)
echo clean finish

echo cmake load
%CMAKE_EXECUTABLE% -DCMAKE_BUILD_TYPE=%CMAKE_BUILD_TYPE% -DCMAKE_MAKE_PROGRAM=%CMAKE_MAKE_PROGRAM% -G Ninja -S %SOURCE_DIR% -B %BUILD_DIR%
echo cmake load finish

echo cmake build
%CMAKE_EXECUTABLE% --build %BUILD_DIR% --target all
echo cmake build finish

echo final
python main.py
echo run final finish

endlocal
