# Game Server 4j - 脚本说明

本目录包含 Game Server 4j 项目的 Docker 管理脚本。

## 脚本列表

| 脚本 | 作用 |
|------|------|
| **build-all-docker.bat** | 构建所有服务的 Docker 镜像 |
| **clear-zk-config.bat** | 清除 ZooKeeper 中的旧配置 |
| **start-all.bat** | 一键启动所有服务 |
| **stop-all.bat** | 停止并清理服务 |
| **logs.bat** | 查看各服务日志 |

---

## 使用指南

### 1. 首次运行

首次运行需要先构建 Docker 镜像：

```cmd
build-all-docker.bat
```

该脚本会：
- 检查 Docker 是否运行
- 自动执行 Maven 编译（如果需要）
- 按顺序构建所有服务镜像
- 询问是否启动容器

### 2. 清除 ZooKeeper 旧配置

如果遇到服务连接到 `192.168.110.2` 等旧 IP 地址的问题：

```cmd
clear-zk-config.bat
```

这会清除 ZooKeeper 中存储的所有旧配置，让服务使用新的容器内部地址。

### 3. 启动所有服务

```cmd
start-all.bat
```

该脚本会：
- 检查并启动 Docker Desktop（如果未运行）
- 检查并构建镜像（如果不存在）
- 启动所有服务容器

**服务端口映射：**
| 服务 | 端口 |
|------|------|
| MongoDB | 27017 |
| Redis | 6379 |
| ZooKeeper | 12181 |
| MongoExpress | 27018 |
| Game API | 7000 |
| Game Gate | 7020, 7021 |
| Game Hall | 7030 |
| Game Manage | 7061 |

### 4. 查看日志

```cmd
logs.bat
```

选择要查看的服务，实时显示日志输出。

### 5. 停止服务

```cmd
stop-all.bat
```

提供三个选项：
- 停止容器（保留数据）
- 停止容器并删除网络
- 停止容器并删除卷（清空所有数据）

---

## 常见问题

### Q: 提示 Docker 未运行
**A:** 请先启动 Docker Desktop

### Q: 服务连接到 192.168.110.2 失败
**A:** 运行 `clear-zk-config.bat` 清除 ZooKeeper 旧配置

### Q: 镜像构建失败
**A:** 确保：
1. Docker Desktop 正在运行
2. 已执行 `mvn clean package -DskipTests` 编译项目
3. Dockerfile 中的基础镜像存在（会自动下载）

### Q: 容器启动后无法访问
**A:** 检查：
1. 端口是否被占用
2. 依赖服务是否已启动（如 MongoDB、ZooKeeper）
3. 查看日志排查问题：`logs.bat`

---

## 服务间通信地址

服务间通信使用 Docker 内部网络地址：

| 服务 | 内部地址 |
|------|---------|
| MongoDB | `mongo:27017` |
| Redis | `redis:6379` |
| ZooKeeper | `zookeeper1:2181` |
| Game API | `game-api1:7000` |
| Game Gate | `game-gate1:7020` |
| Game Hall | `game-hall1:7030` |
| Game Manage | `game-manage1:7061` |

---

## 手动构建单个服务

如果只需要构建特定服务：

```cmd
cd game-api
DockerBuild.bat

cd ..\game-gate
DockerBuild.bat

cd ..\game-hall
DockerBuild.bat

cd ..\game-manage
DockerBuild.bat
```
