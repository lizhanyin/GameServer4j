@echo off
chcp 65001

rem ========================================
rem Game Server 4j - 查看日志脚本
rem ========================================

setlocal enabledelayedexpansion

echo ========================================
echo Game Server 4j - 查看日志
echo ========================================
echo.

rem 检查容器是否运行
docker ps | findstr "game-" >nul
if errorlevel 1 (
    echo [ERROR] 没有运行中的游戏服务容器
    pause
    exit /b 1
)

echo 选择要查看日志的服务:
echo   1. game-api1    (API 服务)
echo   2. game-gate1   (网关服务)
echo   3. game-hall1   (游戏大厅)
echo   4. game-manage1 (管理后台)
echo   5. mongodb      (数据库)
echo   6. zookeeper1  (注册中心)
echo   7. redis        (缓存)
echo   8. 全部服务
echo   9. 退出
echo.

set /p SERVICE="请选择 (1-9): "

if "%SERVICE%"=="1" set CONTAINER=game-api1
if "%SERVICE%"=="2" set CONTAINER=game-gate1
if "%SERVICE%"=="3" set CONTAINER=game-hall1
if "%SERVICE%"=="4" set CONTAINER=game-manage1
if "%SERVICE%"=="5" set CONTAINER=mongodb
if "%SERVICE%"=="6" set CONTAINER=zookeeper1
if "%SERVICE%"=="7" set CONTAINER=redis
if "%SERVICE%"=="8" (
    docker compose -f game-res/docker/local/docker-compose.yml logs -f
    goto END
)
if "%SERVICE%"=="9" goto END

if not defined CONTAINER (
    echo 无效选项
    pause
    goto END
)

echo.
echo ========================================
echo 查看 %CONTAINER% 日志...
echo ========================================
echo 按 Ctrl+C 退出日志查看
echo.

docker logs -f %CONTAINER%

:END
echo.
echo 日志查看已结束
pause
endlocal
