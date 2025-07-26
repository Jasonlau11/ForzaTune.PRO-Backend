# ForzaTune.PRO 运维管理API文档

## 概述

本文档描述了ForzaTune.PRO后端服务的运维管理接口，主要用于网站管理员的日常维护工作。所有接口都可以通过Postman等工具调用。

## 基础信息

- **Base URL**: `http://localhost:8080/api/admin`
- **Content-Type**: `application/json`
- **认证**: 暂未实现，生产环境需要添加认证

## 车型数据管理

### 1. 重新加载车型数据

**接口**: `POST /admin/cars/reload`

**描述**: 重新从静态文件加载车型数据，用于游戏版本更新后更新车型信息

**请求**:
```http
POST /api/admin/cars/reload
```

**响应**:
```json
{
  "message": "车型数据重新加载成功",
  "status": "success"
}
```

### 2. 获取车型数据统计

**接口**: `GET /admin/cars/stats`

**描述**: 获取车型数据的统计信息

**请求**:
```http
GET /api/admin/cars/stats
```

**响应**:
```json
{
  "dataInfo": {
    "version": "1.0.0",
    "lastUpdated": "2024-01-01",
    "gameVersion": "Forza Horizon 5",
    "totalCars": "8"
  },
  "totalCars": 8,
  "manufacturers": 8
}
```

## 用户数据管理

### 3. 清理无效用户

**接口**: `POST /admin/users/cleanup`

**描述**: 清理长期未登录或无效的用户账户

**请求**:
```http
POST /api/admin/users/cleanup?dryRun=false
```

**参数**:
- `dryRun` (boolean, 可选): 是否模拟执行，默认false

**响应**:
```json
{
  "message": "用户清理完成",
  "dryRun": false,
  "cleanedCount": 5,
  "status": "success"
}
```

## 调校数据管理

### 4. 清理无效调校

**接口**: `POST /admin/tunes/cleanup`

**描述**: 清理长期无人使用或无效的调校

**请求**:
```http
POST /api/admin/tunes/cleanup?dryRun=false&daysThreshold=30
```

**参数**:
- `dryRun` (boolean, 可选): 是否模拟执行，默认false
- `daysThreshold` (int, 可选): 清理阈值天数，默认0

**响应**:
```json
{
  "message": "调校清理完成",
  "dryRun": false,
  "daysThreshold": 30,
  "cleanedCount": 10,
  "status": "success"
}
```

## 系统监控

### 5. 系统健康检查

**接口**: `GET /admin/health`

**描述**: 检查系统各组件的健康状态

**请求**:
```http
GET /api/admin/health
```

**响应**:
```json
{
  "carData": {
    "status": "healthy",
    "count": 8,
    "version": "1.0.0"
  },
  "userData": {
    "status": "healthy",
    "proPlayerCount": 15
  },
  "overall": "healthy",
  "timestamp": 1703123456789
}
```

### 6. 获取系统统计信息

**接口**: `GET /admin/stats`

**描述**: 获取系统的详细统计信息

**请求**:
```http
GET /api/admin/stats
```

**响应**:
```json
{
  "cars": {
    "total": 8,
    "manufacturers": 8,
    "dataVersion": "1.0.0"
  },
  "users": {
    "proPlayers": 15
  },
  "system": {
    "javaVersion": "1.8.0_301",
    "osName": "Linux",
    "memoryUsage": 1073741824,
    "totalMemory": 2147483648
  }
}
```

## 批量操作

### 7. 批量操作接口

**接口**: `POST /admin/batch`

**描述**: 执行批量操作，支持多种操作类型

**请求**:
```http
POST /api/admin/batch?operation=reload_cars&dryRun=false
Content-Type: application/json

{
  "param1": "value1",
  "param2": "value2"
}
```

**参数**:
- `operation` (string, 必需): 操作类型
  - `reload_cars`: 重新加载车型数据
  - `cleanup_users`: 清理用户数据
  - `cleanup_tunes`: 清理调校数据
- `dryRun` (boolean, 可选): 是否模拟执行，默认false
- `params` (object, 可选): 操作参数

**响应**:
```json
{
  "message": "车型数据重新加载完成",
  "operation": "reload_cars",
  "dryRun": false,
  "params": {
    "param1": "value1",
    "param2": "value2"
  },
  "status": "success"
}
```

## 数据一致性管理

### 8. 数据一致性检查

**接口**: `GET /admin/consistency/check`

**描述**: 检查数据一致性，发现潜在的数据问题

**请求**:
```http
GET /api/admin/consistency/check
```

**响应**:
```json
{
  "message": "数据一致性检查完成",
  "issues": 3,
  "details": [
    {
      "type": "orphaned_tune",
      "count": 2,
      "description": "发现2个孤立调校"
    },
    {
      "type": "invalid_user",
      "count": 1,
      "description": "发现1个无效用户"
    }
  ],
  "status": "success"
}
```

### 9. 修复数据一致性问题

**接口**: `POST /admin/consistency/fix`

**描述**: 自动修复发现的数据一致性问题

**请求**:
```http
POST /api/admin/consistency/fix?dryRun=false
```

**参数**:
- `dryRun` (boolean, 可选): 是否模拟执行，默认false

**响应**:
```json
{
  "message": "数据一致性修复完成",
  "fixedCount": 3,
  "dryRun": false,
  "status": "success"
}
```

## 使用示例

### Postman 集合示例

```json
{
  "info": {
    "name": "ForzaTune.PRO Admin API",
    "description": "运维管理接口集合"
  },
  "item": [
    {
      "name": "重新加载车型数据",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/admin/cars/reload"
      }
    },
    {
      "name": "系统健康检查",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/admin/health"
      }
    },
    {
      "name": "清理无效用户",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/admin/users/cleanup?dryRun=false"
      }
    }
  ]
}
```

### 常用运维流程

1. **日常监控**
   ```bash
   # 检查系统健康状态
   curl -X GET http://localhost:8080/api/admin/health
   
   # 获取系统统计
   curl -X GET http://localhost:8080/api/admin/stats
   ```

2. **数据清理**
   ```bash
   # 先模拟清理用户数据
   curl -X POST "http://localhost:8080/api/admin/users/cleanup?dryRun=true"
   
   # 确认无误后执行实际清理
   curl -X POST "http://localhost:8080/api/admin/users/cleanup?dryRun=false"
   ```

3. **游戏版本更新**
   ```bash
   # 更新车型数据文件后重新加载
   curl -X POST http://localhost:8080/api/admin/cars/reload
   
   # 检查数据一致性
   curl -X GET http://localhost:8080/api/admin/consistency/check
   ```

## 注意事项

1. **生产环境安全**: 所有运维接口在生产环境中都应该添加适当的认证和授权
2. **数据备份**: 执行清理操作前建议先备份数据库
3. **dryRun模式**: 建议先使用dryRun=true模式测试操作效果
4. **监控告警**: 建议对关键指标设置监控告警
5. **日志记录**: 所有运维操作都会记录详细日志

## 错误处理

所有接口在发生错误时会返回相应的HTTP状态码和错误信息：

```json
{
  "message": "操作失败的具体原因",
  "status": "error",
  "timestamp": 1703123456789
}
```

常见HTTP状态码：
- `200`: 操作成功
- `400`: 请求参数错误
- `500`: 服务器内部错误
- `503`: 服务不可用 