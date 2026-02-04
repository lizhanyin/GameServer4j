@echo off
echo ============================================
echo Kafka 初始化脚本
echo ============================================
echo.

if exist uuid.txt (
    echo 发现已存在的 UUID:
    set /p UUID=<uuid.txt
    echo %UUID%
    echo.
    set /p REUSE="是否使用现有 UUID? (Y/N): "
    if /i "%REUSE%"=="Y" goto format
)

echo [1/2] 生成随机 UUID...
.\bin\windows\kafka-storage.bat random-uuid 2>&1 | findstr /v "ERROR Reconfiguration" > uuid.txt
set /p UUID=<uuid.txt
echo UUID: %UUID%
echo.

:format
echo [2/2] 格式化 Kafka 存储...
.\bin\windows\kafka-storage.bat format --standalone -t %UUID% -c .\config\server.properties
echo.

echo ============================================
echo 初始化完成!
echo ============================================
pause
