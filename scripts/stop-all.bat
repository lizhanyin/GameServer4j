@echo off
chcp 65001

rem ========================================
rem Game Server 4j - 停止并清理脚本
rem ========================================

setlocal enabledelayedexpansion

echo ========================================
echo Game Server 4j - 停止并清理
echo ========================================
echo.

cd /d "%~dp0"

set /p CHOICE="请选择操作:
1. 停止容器 (保留数据)
2. 停止容器并删除网络
3. 停止容器并删除卷 (清空数据)
4. 退出
请输入选项 (1-4): "

if "%CHOICE%"=="1" goto STOP_ONLY
if "%CHOICE%"=="2" goto STOP_WITH_NETWORK
if "%CHOICE%"=="3" goto STOP_WITH_VOLUMES
if "%CHOICE%"=="4" goto END

echo 无效选项
pause
goto END

:STOP_ONLY
echo.
echo 停止容器...
docker compose -f game-res/docker/local/docker-compose.yml stop
echo [OK] 容器已停止
goto END

:STOP_WITH_NETWORK
echo.
echo 停止并删除容器...
docker compose -f game-res/docker/local/docker-compose.yml down
echo [OK] 容器和网络已删除
goto END

:STOP_WITH_VOLUMES
echo.
echo [WARN] 此操作将删除所有数据 (MongoDB、Redis、Kafka 等)
set /p CONFIRM="确认删除数据? (YES/NO): "
if /i not "!CONFIRM!"=="YES" (
    echo 操作已取消
    goto END
)
echo.
echo 停止并删除容器和数据卷...
docker compose -f game-res/docker/local/docker-compose.yml down -v
echo [OK] 容器、网络和数据卷已删除
goto END

:END
echo.
pause
endlocal
