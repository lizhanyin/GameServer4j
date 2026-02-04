@echo off
chcp 65001

rem ========================================
rem 清除 ZooKeeper 中的旧配置
rem ========================================

setlocal enabledelayedexpansion

echo ========================================
echo 清除 ZooKeeper 配置脚本
echo ========================================
echo.

rem 检查 Docker 是否运行
docker ps | findstr "zookeeper1" >nul
if errorlevel 1 (
    echo [ERROR] zookeeper1 容器未运行
    echo [INFO] 请先启动 Docker Compose 服务
    pause
    exit /b 1
)

echo [OK] zookeeper1 容器正在运行
echo.

echo 旧的配置路径:
echo   /game/default/service
echo   /game/jzy/service
echo   /game/default/mongo/excel
echo   /game/default/log/kafka/url
echo.

set /p CONFIRM="确认清除这些配置? (Y/N): "
if /i not "!CONFIRM!"=="Y" (
    echo 操作已取消
    pause
    exit /b 0
)

echo.
echo ========================================
echo 清除 ZooKeeper 配置...
echo ========================================

rem 创建临时脚本文件
set TEMP_SCRIPT=%TEMP%\zk-delete-%RANDOM%.sh

echo delete /game/default > "!TEMP_SCRIPT!"
echo delete /game/jzy >> "!TEMP_SCRIPT!"

rem 复制脚本到容器并执行
docker cp "!TEMP_SCRIPT!" zookeeper1:/tmp/zk-delete.sh
docker exec zookeeper1 zkCli.sh -f /tmp/zk-delete.sh

if errorlevel 1 (
    echo.
    echo ========================================
echo 尝试使用替代方法...
    echo ========================================

    rem 逐个删除节点
    docker exec zookeeper1 zkCli.sh delete /game/default/service
    docker exec zookeeper1 zkCli.sh delete /game/jzy/service
    docker exec zookeeper1 zkCli.sh delete /game/default/mongo/excel
    docker exec zookeeper1 zkCli.sh delete /game/default/log/kafka/url
    docker exec zookeeper1 zkCli.sh delete /game/default
    docker exec zookeeper1 zkCli.sh delete /game/jzy
)

rem 清理临时文件
if exist "!TEMP_SCRIPT!" del "!TEMP_SCRIPT!"
docker exec zookeeper1 rm -f /tmp/zk-delete.sh

echo.
echo ========================================
echo 清除完成
echo ========================================
echo.
echo 请重启游戏服务以获取最新配置
echo.
pause
endlocal
