# ForzaTune.PRO API 接口文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **认证**: 暂未实现，生产环境需要添加认证

## 接口列表

### 1. 车辆相关接口

#### 1.1 获取所有车辆
**接口**: `GET /cars`

**描述**: 获取所有车辆，支持搜索和筛选

**请求参数**:
- `search` (string, 可选): 搜索关键词
- `manufacturer` (string, 可选): 制造商筛选
- `category` (string, 可选): 分类筛选
- `drivetrain` (string, 可选): 驱动方式筛选
- `minPI` (int, 可选): 最小PI等级
- `maxPI` (int, 可选): 最大PI等级

**响应示例**:
```json
[
  {
    "id": "car1",
    "name": "RS6 Avant",
    "manufacturer": "Audi",
    "year": 2020,
    "category": "SPORTS_CARS",
    "pi": 860,
    "drivetrain": "AWD",
    "imageUrl": "/images/audi-rs6.jpg"
  }
]
```

**前端对应**: `getAllCars()`

#### 1.2 根据ID获取车辆
**接口**: `GET /cars/{id}`

**描述**: 获取指定车辆详情

**响应示例**:
```json
{
  "id": "car1",
  "name": "RS6 Avant",
  "manufacturer": "Audi",
  "year": 2020,
  "category": "SPORTS_CARS",
  "pi": 860,
  "drivetrain": "AWD",
  "imageUrl": "/images/audi-rs6.jpg"
}
```

**前端对应**: `getCarById(carId)`

#### 1.3 获取热门车辆
**接口**: `GET /cars/popular`

**描述**: 获取热门车辆（按调校数量排序）

**请求参数**:
- `limit` (int, 可选): 返回数量，默认4

**响应示例**:
```json
[
  {
    "car": {
      "id": "car1",
      "name": "RS6 Avant",
      "manufacturer": "Audi",
      "year": 2020,
      "category": "SPORTS_CARS",
      "pi": 860,
      "drivetrain": "AWD"
    },
    "tuneCount": 15
  }
]
```

**前端对应**: 首页热门车辆展示

#### 1.4 搜索车辆
**接口**: `GET /cars/search`

**描述**: 根据关键词搜索车辆

**请求参数**:
- `keyword` (string, 必需): 搜索关键词

**前端对应**: 车辆搜索功能

#### 1.5 获取所有制造商
**接口**: `GET /cars/manufacturers`

**描述**: 获取所有制造商列表

**前端对应**: 车辆筛选功能

### 2. 调校相关接口

#### 2.1 获取所有调校
**接口**: `GET /tunes`

**描述**: 获取所有调校，支持多种筛选条件

**请求参数**:
- `carId` (string, 可选): 车辆ID筛选
- `authorId` (string, 可选): 作者ID筛选
- `proOnly` (boolean, 可选): 仅PRO调校
- `preference` (string, 可选): 偏好筛选
- `piClass` (string, 可选): PI等级筛选
- `raceType` (string, 可选): 比赛类型筛选
- `sortBy` (string, 可选): 排序方式
- `limit` (int, 可选): 返回数量限制

**响应示例**:
```json
[
  {
    "id": "tune1",
    "tuneCode": "123 456 789",
    "carId": "car1",
    "authorId": "user1",
    "authorGamertag": "Alex R.",
    "isProTune": true,
    "title": "RS6 Power Tune",
    "description": "High power setup for RS6",
    "preference": "POWER",
    "piClass": "S1",
    "raceType": "Road",
    "likeCount": 45,
    "createdAt": "2024-01-15T10:30:00Z"
  }
]
```

**前端对应**: `getAllTunes()`

#### 2.2 根据ID获取调校详情
**接口**: `GET /tunes/{id}`

**描述**: 获取调校详细信息

**响应示例**:
```json
{
  "id": "tune1",
  "tuneCode": "123 456 789",
  "carId": "car1",
  "authorId": "user1",
  "authorGamertag": "Alex R.",
  "isProTune": true,
  "title": "RS6 Power Tune",
  "description": "High power setup for RS6",
  "preference": "POWER",
  "piClass": "S1",
  "raceType": "Road",
  "likeCount": 45,
  "createdAt": "2024-01-15T10:30:00Z",
  "parameters": {
    "tirePressure": "2.0",
    "gearRatios": "3.2,2.1,1.5,1.2,1.0,0.8",
    "alignment": "0.5,-0.3",
    "antiRollBars": "25,30",
    "rideHeight": "120,125",
    "damping": "8.5,9.0",
    "springs": "650,700",
    "aero": "0,0",
    "brakes": "85,80",
    "differential": "75,25,50"
  }
}
```

**前端对应**: `getTuneById(tuneId)`

#### 2.3 根据车辆ID获取调校
**接口**: `GET /tunes/car/{carId}`

**描述**: 获取指定车辆的所有调校

**响应示例**:
```json
[
  {
    "id": "tune1",
    "tuneCode": "123 456 789",
    "carId": "car1",
    "authorId": "user1",
    "authorGamertag": "Alex R.",
    "isProTune": true,
    "title": "RS6 Power Tune",
    "preference": "POWER",
    "likeCount": 45
  }
]
```

**前端对应**: `getTunesByCarId(carId)`

#### 2.4 获取车辆的PRO调校
**接口**: `GET /tunes/car/{carId}/pro`

**描述**: 获取指定车辆的PRO调校

**前端对应**: `getProTunesByCarId(carId)`

#### 2.5 获取PRO调校
**接口**: `GET /tunes/pro`

**描述**: 获取所有PRO调校

**前端对应**: `getProTunes()`

#### 2.6 获取热门调校
**接口**: `GET /tunes/popular`

**描述**: 获取热门调校（按点赞数排序）

**前端对应**: 首页热门调校展示

#### 2.7 获取最新调校
**接口**: `GET /tunes/recent`

**描述**: 获取最新调校

**前端对应**: 首页新调校展示

#### 2.8 高级搜索调校
**接口**: `GET /tunes/search`

**描述**: 高级搜索调校，支持多种条件

**请求参数**:
- `keyword` (string, 可选): 搜索关键词
- `carId` (string, 可选): 车辆ID
- `isProTune` (boolean, 可选): 是否PRO调校
- `preference` (string, 可选): 偏好
- `piClass` (string, 可选): PI等级
- `raceType` (string, 可选): 比赛类型
- `sortBy` (string, 可选): 排序方式 (likes/recent/pro)
- `limit` (int, 可选): 返回数量限制

**前端对应**: 调校搜索和筛选功能

#### 2.9 根据作者ID获取调校
**接口**: `GET /tunes/author/{authorId}`

**描述**: 获取指定作者的调校

**前端对应**: `getTunesByAuthorId(authorId)`

#### 2.10 创建新调校
**接口**: `POST /tunes`

**描述**: 创建新调校

**请求体**:
```json
{
  "tuneCode": "123 456 789",
  "carId": "car1",
  "authorId": "user1",
  "authorGamertag": "Alex R.",
  "isProTune": false,
  "title": "My Tune",
  "description": "My custom tune",
  "preference": "BALANCE",
  "piClass": "A",
  "raceType": "Road",
  "isPublic": true
}
```

#### 2.11 更新调校点赞数
**接口**: `PUT /tunes/{id}/like`

**描述**: 更新调校点赞数

**请求参数**:
- `likeCount` (int, 必需): 新的点赞数

### 3. 首页相关接口

#### 3.1 获取首页数据
**接口**: `GET /home/data`

**描述**: 获取首页需要的所有数据（热门车辆、最新调校、PRO调校）

**响应示例**:
```json
{
  "popularCars": [
    {
      "car": {
        "id": "car1",
        "name": "RS6 Avant",
        "manufacturer": "Audi",
        "year": 2020
      },
      "tuneCount": 15
    }
  ],
  "recentTunes": [
    {
      "tune": {
        "id": "tune1",
        "title": "New Tune",
        "authorGamertag": "Alex R.",
        "likeCount": 10
      },
      "carName": "2020 Audi RS6 Avant"
    }
  ],
  "proTunes": [
    {
      "tune": {
        "id": "tune2",
        "title": "Pro Tune",
        "authorGamertag": "Pro Player",
        "isProTune": true,
        "likeCount": 50
      },
      "carName": "2021 BMW M4 Competition"
    }
  ]
}
```

**前端对应**: 首页数据加载

#### 3.2 获取热门车辆
**接口**: `GET /home/popular-cars`

**描述**: 获取热门车辆

**请求参数**:
- `limit` (int, 可选): 返回数量，默认4

#### 3.3 获取最新调校
**接口**: `GET /home/recent-tunes`

**描述**: 获取最新调校

**请求参数**:
- `limit` (int, 可选): 返回数量，默认3

#### 3.4 获取PRO调校
**接口**: `GET /home/pro-tunes`

**描述**: 获取PRO调校

**请求参数**:
- `limit` (int, 可选): 返回数量，默认3

#### 3.5 获取首页统计信息
**接口**: `GET /home/stats`

**描述**: 获取首页统计信息

**响应示例**:
```json
{
  "totalCars": 8,
  "totalTunes": 45,
  "proTunes": 12,
  "carsWithTunes": 6,
  "manufacturers": 8
}
```

### 4. 用户相关接口

#### 4.1 获取所有用户
**接口**: `GET /users`

**描述**: 获取所有用户

**前端对应**: `getAllUsers()`

#### 4.2 根据ID获取用户
**接口**: `GET /users/{id}`

**描述**: 获取用户详情

**前端对应**: `getUserById(userId)`

#### 4.3 根据Gamertag获取用户
**接口**: `GET /users/gamertag/{gamertag}`

**描述**: 根据Gamertag获取用户

**前端对应**: `getUserByGamertag(gamertag)`

#### 4.4 获取PRO玩家
**接口**: `GET /users/pro`

**描述**: 获取所有PRO玩家

**前端对应**: `getProPlayers()`

#### 4.5 获取PRO玩家数量
**接口**: `GET /users/pro/count`

**描述**: 获取PRO玩家数量

**前端对应**: `getProPlayerCount()`

## 查询条件支持情况

### 车辆查询条件
✅ **已支持**:
- 关键词搜索（车型名称、制造商）
- 制造商筛选
- 分类筛选（Sports Cars、Muscle Cars等）
- 驱动方式筛选（AWD、RWD、FWD）
- PI等级范围筛选

### 调校查询条件
✅ **已支持**:
- 车辆ID筛选
- 作者ID筛选
- PRO调校筛选
- 偏好筛选（Power、Handling、Balance）
- PI等级筛选
- 比赛类型筛选（Road、Dirt、Cross Country）
- 关键词搜索（标题、描述、作者）
- 排序方式（点赞数、时间、PRO优先）

### 首页展示
✅ **已支持**:
- 热门车辆展示（按调校数量排序）
- 最新调校展示
- PRO调校展示（按点赞数排序）
- 统计信息展示

## 前端接口对应关系

| 前端方法 | 后端接口 | 状态 |
|---------|---------|------|
| `getAllCars()` | `GET /cars` | ✅ 已实现 |
| `getCarById(carId)` | `GET /cars/{id}` | ✅ 已实现 |
| `getTunesByCarId(carId)` | `GET /tunes/car/{carId}` | ✅ 已实现 |
| `getTuneById(tuneId)` | `GET /tunes/{id}` | ✅ 已实现 |
| `getProTunes()` | `GET /tunes/pro` | ✅ 已实现 |
| `getProTunesByCarId(carId)` | `GET /tunes/car/{carId}/pro` | ✅ 已实现 |
| `getTunesByAuthorId(authorId)` | `GET /tunes/author/{authorId}` | ✅ 已实现 |
| `getAllUsers()` | `GET /users` | ✅ 已实现 |
| `getUserById(userId)` | `GET /users/{id}` | ✅ 已实现 |
| `getUserByGamertag(gamertag)` | `GET /users/gamertag/{gamertag}` | ✅ 已实现 |
| 首页热门车辆 | `GET /home/popular-cars` | ✅ 已实现 |
| 首页最新调校 | `GET /home/recent-tunes` | ✅ 已实现 |
| 首页PRO调校 | `GET /home/pro-tunes` | ✅ 已实现 |

## 使用示例

### 获取车辆列表
```bash
# 获取所有车辆
curl -X GET http://localhost:8080/api/cars

# 搜索车辆
curl -X GET "http://localhost:8080/api/cars?search=RS6"

# 按制造商筛选
curl -X GET "http://localhost:8080/api/cars?manufacturer=Audi"

# 按分类筛选
curl -X GET "http://localhost:8080/api/cars?category=SPORTS_CARS"
```

### 获取调校列表
```bash
# 获取所有调校
curl -X GET http://localhost:8080/api/tunes

# 获取指定车辆的调校
curl -X GET http://localhost:8080/api/tunes/car/car1

# 获取PRO调校
curl -X GET http://localhost:8080/api/tunes/pro

# 搜索调校
curl -X GET "http://localhost:8080/api/tunes/search?keyword=power&carId=car1"
```

### 获取首页数据
```bash
# 获取首页所有数据
curl -X GET http://localhost:8080/api/home/data

# 获取热门车辆
curl -X GET http://localhost:8080/api/home/popular-cars

# 获取最新调校
curl -X GET http://localhost:8080/api/home/recent-tunes
```

## 注意事项

1. **分页**: 当前接口未实现分页，大量数据时需要考虑添加分页功能
2. **缓存**: 车型数据已实现内存缓存，调校数据可考虑添加Redis缓存
3. **认证**: 生产环境需要添加JWT认证
4. **限流**: 高并发场景需要添加接口限流
5. **监控**: 建议添加接口调用监控和性能指标 