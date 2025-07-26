# ForzaTune.PRO 后端服务设计文档

## 技术栈选择

### Java 8 + Spring Boot 2.7.18
- **原因**: 广泛使用，稳定性好，兼容性强
- **优势**: 成熟的生态系统，丰富的文档和社区支持

### MyBatis 替代 JPA
- **原因**: 更好的SQL控制，性能更直接
- **优势**: 
  - 复杂查询更容易实现
  - 更好的性能优化控制
  - 团队学习成本更低

### 不使用 Redis
- **原因**: 当前访问量小（并发 < 50）
- **考虑**: 避免增加运维复杂度，MySQL直接查询已足够

## 数据库设计

### 核心表结构

#### 1. 用户表 (users)
```sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    gamertag VARCHAR(100) UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    xbox_id VARCHAR(100) UNIQUE,
    is_pro_player BOOLEAN DEFAULT FALSE,
    pro_player_since VARCHAR(20),
    total_tunes INT DEFAULT 0,
    total_likes INT DEFAULT 0,
    avatar VARCHAR(500),
    created_at VARCHAR(20) NOT NULL,
    bio TEXT,
    user_tier ENUM('STANDARD', 'VERIFIED', 'PRO') DEFAULT 'STANDARD',
    last_login DATETIME,
    is_active BOOLEAN DEFAULT TRUE
);
```

#### 2. 车辆表 (cars)
```sql
CREATE TABLE cars (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    manufacturer VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    category ENUM('SPORTS_CARS', 'MUSCLE_CARS', 'SUPERCARS', 'CLASSIC_CARS', 'HYPERCARS', 'TRACK_TOYS') NOT NULL,
    pi INT NOT NULL,
    drivetrain ENUM('RWD', 'FWD', 'AWD') NOT NULL,
    image_url VARCHAR(500),
    game_id VARCHAR(36) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 3. 调校表 (tunes)
```sql
CREATE TABLE tunes (
    id VARCHAR(36) PRIMARY KEY,
    car_id VARCHAR(36) NOT NULL,
    author_id VARCHAR(36) NOT NULL,
    author_gamertag VARCHAR(100) NOT NULL,
    share_code VARCHAR(20) NOT NULL UNIQUE,
    preference ENUM('POWER', 'HANDLING', 'BALANCE') NOT NULL,
    pi_class ENUM('X', 'S2', 'S1', 'A', 'B', 'C', 'D') NOT NULL,
    final_pi INT NOT NULL,
    race_type ENUM('ROAD', 'DIRT', 'CROSS_COUNTRY'),
    description TEXT,
    is_pro_tune BOOLEAN DEFAULT FALSE,
    created_at VARCHAR(20) NOT NULL,
    updated_at VARCHAR(20) NOT NULL,
    like_count INT DEFAULT 0,
    is_parameters_public BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (car_id) REFERENCES cars(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);
```

#### 4. 调校参数表 (tune_parameters)
```sql
CREATE TABLE tune_parameters (
    tune_id VARCHAR(36) PRIMARY KEY,
    front_tire_pressure INT,
    rear_tire_pressure INT,
    final_drive INT,
    gear_ratios JSON,
    front_camber INT,
    rear_camber INT,
    front_toe INT,
    rear_toe INT,
    front_caster INT,
    front_anti_roll_bar INT,
    rear_anti_roll_bar INT,
    front_springs INT,
    rear_springs INT,
    front_ride_height INT,
    rear_ride_height INT,
    front_rebound INT,
    rear_rebound INT,
    front_bump INT,
    rear_bump INT,
    front_differential INT,
    rear_differential INT,
    center_differential INT,
    brake_pressure INT,
    front_brake_balance INT,
    front_downforce INT,
    rear_downforce INT,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE
);
```

#### 5. 用户活动表
- `user_likes`: 用户点赞记录
- `user_favorites`: 用户收藏记录
- `lap_times`: 圈速记录

## API 设计

### 车辆相关 API
```
GET /api/cars                    # 获取所有车辆（支持搜索和筛选）
GET /api/cars/{id}              # 获取指定车辆
GET /api/cars/search            # 搜索车辆
GET /api/cars/manufacturers     # 获取所有制造商
GET /api/cars/category/{cat}    # 根据分类获取车辆
GET /api/cars/drivetrain/{dr}   # 根据驱动方式获取车辆
```

### 调校相关 API
```
GET /api/tunes                  # 获取调校列表（支持多种筛选）
GET /api/tunes/{id}            # 获取调校详情
GET /api/tunes/car/{carId}     # 获取车辆的调校
GET /api/tunes/car/{carId}/pro # 获取车辆的PRO调校
GET /api/tunes/author/{authId} # 获取作者的调校
GET /api/tunes/pro             # 获取所有PRO调校
POST /api/tunes                # 创建新调校
PUT /api/tunes/{id}/like       # 更新调校点赞数
```

### 用户相关 API
```
GET /api/users                 # 获取所有用户
GET /api/users/{id}           # 获取用户详情
GET /api/users/gamertag/{gt}  # 根据Gamertag获取用户
GET /api/users/pro            # 获取所有PRO玩家
GET /api/users/pro/count      # 获取PRO玩家数量
POST /api/users               # 创建新用户
PUT /api/users/{id}           # 更新用户信息
```

## 前端API对应关系

### 车辆API
- `getAllCars()` → `GET /api/cars`
- `getCarById(carId)` → `GET /api/cars/{id}`

### 调校API
- `getAllTunes()` → `GET /api/tunes`
- `getTuneById(tuneId)` → `GET /api/tunes/{id}`
- `getTunesByCarId(carId)` → `GET /api/tunes/car/{carId}`

### 用户API
- `getAllUsers()` → `GET /api/users`
- `getUserById(userId)` → `GET /api/users/{id}`
- `getUserByGamertag(gamertag)` → `GET /api/users/gamertag/{gamertag}`

### 用户活动API
- `getUserLikes(userId)` → `GET /api/users/{id}/likes`
- `getUserFavorites(userId)` → `GET /api/users/{id}/favorites`
- `toggleTuneLike(userId, tuneId)` → `POST /api/tunes/{tuneId}/like`

## 性能优化策略

### 1. 数据库索引
```sql
-- 用户表索引
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_gamertag ON users(gamertag);
CREATE INDEX idx_users_xbox_id ON users(xbox_id);

-- 车辆表索引
CREATE INDEX idx_cars_manufacturer ON cars(manufacturer);
CREATE INDEX idx_cars_category ON cars(category);
CREATE INDEX idx_cars_drivetrain ON cars(drivetrain);

-- 调校表索引
CREATE INDEX idx_tunes_car_id ON tunes(car_id);
CREATE INDEX idx_tunes_author_id ON tunes(author_id);
CREATE INDEX idx_tunes_share_code ON tunes(share_code);
CREATE INDEX idx_tunes_is_pro_tune ON tunes(is_pro_tune);

-- 用户活动索引
CREATE INDEX idx_user_likes_user_id ON user_likes(user_id);
CREATE INDEX idx_user_likes_tune_id ON user_likes(tune_id);
```

### 2. 连接池配置
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

### 3. MyBatis 优化
- 使用动态SQL减少重复查询
- 合理使用延迟加载
- 批量操作优化

## 安全考虑

### 1. 认证授权
- JWT Token 认证
- 基于角色的权限控制
- 密码加密存储

### 2. 数据验证
- 输入参数验证
- SQL注入防护
- XSS防护

### 3. CORS配置
```yaml
cors:
  allowed-origins: 
    - http://localhost:3000
    - http://localhost:5173
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true
```

## 部署方案

### 开发环境
```bash
# 启动应用
mvn spring-boot:run

# 或者使用脚本
./start.sh
```

### 生产环境
```dockerfile
FROM openjdk:8-jdk-slim
COPY target/forzatune-pro-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 监控和日志

### 日志配置
```yaml
logging:
  level:
    com.forzatune.backend: DEBUG
    org.springframework.security: DEBUG
    com.forzatune.backend.mapper: DEBUG
```

### 健康检查
- Spring Boot Actuator 健康检查端点
- 数据库连接状态监控
- 应用性能监控

## 扩展计划

### 短期扩展
1. 完善用户认证系统
2. 添加评论功能
3. 实现车队功能
4. 添加文件上传功能

### 长期扩展
1. 引入Redis缓存
2. 实现微服务架构
3. 添加消息队列
4. 实现分布式部署

## 总结

这个后端设计充分考虑了前端的需求，提供了完整的API支持。使用Java 8 + MyBatis的技术栈既保证了稳定性，又提供了良好的性能。数据库设计完整，支持所有前端功能。API设计清晰，与前端mockData中的函数对应关系明确。

对于当前的访问量，这个设计是合适的。当访问量增长时，可以通过添加缓存、优化查询、分库分表等方式进行扩展。 