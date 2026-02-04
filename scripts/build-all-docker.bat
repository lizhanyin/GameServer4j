@echo off
chcp 65001

rem ========================================
rem Game Server 4j - Docker Build Script
rem 自动构建所有服务的 Docker 镜像
rem ========================================

setlocal enabledelayedexpansion

echo ========================================
echo Game Server 4j - Docker Build Script
echo ========================================
echo.

rem 记录开始时间
set START_TIME=%time%

rem 检查 Docker 是否运行
docker info >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker 未运行，请先启动 Docker Desktop
    pause
    exit /b 1
)

echo [OK] Docker 已运行
echo.

rem 设置项目根目录（scripts 的上一级目录）
cd /d "%~dp0.."
set PROJECT_ROOT=%CD%

rem 检查是否已编译
if not exist "game-api\target\game-api-releases.jar" (
    echo [WARN] 未找到编译后的 JAR 文件
    echo [INFO] 正在执行 Maven 编译...
    call mvn clean package -DskipTests -Pdefault
    if errorlevel 1 (
        echo [ERROR] Maven 编译失败
        pause
        exit /b 1
    )
    echo [OK] Maven 编译完成
    echo.
)

rem 定义要构建的服务列表
set SERVICES=game-manage game-api game-gate game-hall

rem 构建计数器
set BUILD_COUNT=0
set SUCCESS_COUNT=0
set FAIL_COUNT=0

echo ========================================
echo 开始构建 Docker 镜像...
echo ========================================
echo.

rem 遍历构建每个服务
for %%S in (%SERVICES%) do (
    echo ----------------------------------------
    echo [!BUILD_COUNT!] 正在构建: %%S
    echo ----------------------------------------

    if exist "%%S\DockerBuild.bat" (
        pushd "%%S"
        call DockerBuild.bat
        popd

        if errorlevel 1 (
            echo [ERROR] %%S 构建失败
            set /a FAIL_COUNT+=1
        ) else (
            echo [OK] %%S 构建成功
            set /a SUCCESS_COUNT+=1
        )
        set /a BUILD_COUNT+=1
    ) else (
        echo [WARN] %%S\DockerBuild.bat 不存在，跳过
    )
    echo.
)

echo ========================================
echo 构建完成
echo ========================================
echo Total %BUILD_COUNT% Success %SUCCESS_COUNT% Failed %FAIL_COUNT%

rem 显示构建结果
if %FAIL_COUNT% gtr 0 (
    echo [ERROR] 部分服务构建失败
    pause
    exit /b 1
) else (
    echo [OK] 所有服务构建成功

    rem 显示镜像列表
    echo.
    echo ========================================
    echo Docker 镜像列表
    echo ========================================
    docker images | findstr "game-"
    echo.

    rem 询问是否启动容器
    set /p START_CONTAINER="是否启动 Docker 容器? (Y/N): "
    if /i "!START_CONTAINER!"=="Y" (
        echo.
        echo ========================================
        echo 启动 Docker 容器...
        echo ========================================
        docker compose -f game-res/docker/local/docker-compose.yml up -d
        if errorlevel 1 (
            echo [ERROR] 容器启动失败
            pause
            exit /b 1
        )
        echo [OK] 容器启动成功
        echo.
        echo 运行中的容器:
        docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | findstr "game-"
    )
)

echo.
echo 按任意键退出...
pause >nul
endlocal
