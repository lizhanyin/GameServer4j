@echo off
chcp 65001

rem ========================================
rem Game Server 4j - 一键启动脚本
rem ========================================

setlocal enabledelayedexpansion

echo ========================================
echo Game Server 4j - 一键启动
echo ========================================
echo.

rem 检查 Docker 是否运行
docker info >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker 未运行，正在尝试启动 Docker Desktop...

    rem 尝试启动 Docker Desktop（Windows）
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"

    echo [INFO] 等待 Docker 启动...
    timeout /t 30 /nobreak >nul

    rem 再次检查
    docker info >nul 2>&1
    if errorlevel 1 (
        echo [ERROR] Docker 启动失败，请手动启动 Docker Desktop
        pause
        exit /b 1
    )
)

echo [OK] Docker 已运行
echo.

rem 切换到项目根目录
cd /d "%~dp0"

rem 检查是否需要构建镜像
echo 检查 Docker 镜像...
docker images | findstr "game-manage.*releases" >nul
if errorlevel 1 (
    echo [WARN] Docker 镜像不存在，正在构建...
    call build-all-docker.bat
    if errorlevel 1 (
        echo [ERROR] 镜像构建失败
        pause
        exit /b 1
    )
    echo.
)

echo ========================================
echo 启动 Docker 服务...
echo ========================================
echo.

rem 停止旧容器
echo 停止旧容器...
docker compose -f game-res/docker/local/docker-compose.yml down

rem 启动新容器
echo 启动服务...
docker compose -f game-res/docker/local/docker-compose.yml up -d

if errorlevel 1 (
    echo [ERROR] 服务启动失败
    pause
    exit /b 1
)

echo.
echo ========================================
echo 服务启动成功
echo ========================================
echo.

rem 等待服务启动
echo 等待服务初始化...
timeout /t 5 /nobreak >nul

rem 显示运行中的服务
echo 运行中的服务:
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | findstr "game-"

echo.
echo ========================================
echo 服务端口映射
echo ========================================
echo   MongoDB:     27017
echo   Redis:       6379
echo   ZooKeeper:   12181
echo   MongoExpress: 27018
echo   Game API:     7000
echo   Game Gate:    7020, 7021
echo   Game Hall:    7030
echo   Game Manage:  7061
echo.
echo ========================================
echo 查看日志命令
echo ========================================
echo   查看所有日志:     docker compose -f game-res/docker/local/docker-compose.yml logs -f
echo   查看特定服务:     docker compose -f game-res/docker/local/docker-compose.yml logs -f game-api1
echo.
pause
endlocal
