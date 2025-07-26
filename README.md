# ForzaTune.PRO Backend

Spring Boot 后端服务，为 ForzaTune.PRO 前端应用提供完整的 API 支持。

## 技术栈

- **Spring Boot 2.7.18**
- **Spring Security** - 认证和授权
- **MyBatis** - 数据访问层（纯MyBatis，无JPA）
- **MySQL 8.0** - 数据库
- **JWT** - 无状态认证
- **Lombok** - 代码简化
- **Java 8**

## 项目结构

```
src/main/java/com/forzatune/backend/
├── ForzaTuneProBackendApplication.java  # 主启动类
├── entity/                              # 实体类
│   ├── User.java                        # 用户实体
│   ├── Car.java                         # 车辆实体
│   ├── Tune.java                        # 调校实体
│   ├── TuneParameters.java              # 调校参数实体
│   ├── LapTime.java                     # 圈速实体
│   ├── Track.java                       # 赛道实体
│   ├── TuneComment.java                 # 调校评论实体
│   ├── TuneCommentReply.java            # 评论回复实体
│   ├── Team.java                        # 车队实体
│   ├── TeamMember.java                  # 车队成员实体
│   ├── ProCertification.java            # PRO认证实体
│   └── UserActivity.java                # 用户活动实体
├── mapper/                              # MyBatis Mapper接口
│   ├── UserMapper.java
│   ├── CarMapper.java
│   ├── TuneMapper.java
│   ├── CommentMapper.java               # 评论Mapper
│   ├── TeamMapper.java                  # 车队Mapper
│   ├── ProMapper.java                   # PRO认证Mapper
│   └── ActivityMapper.java              # 活动Mapper
├── service/                             # 业务逻辑层
│   ├── UserService.java
│   ├── CarDataService.java              # 车型数据服务
│   ├── CommentService.java              # 评论服务
│   ├── TeamService.java                 # 车队服务
│   ├── ProService.java                  # PRO认证服务
│   ├── ActivityService.java             # 活动服务
│   └── DataCleanupService.java          # 数据清理服务
├── controller/                          # 控制器层
│   ├── CarController.java
│   ├── TuneController.java
│   ├── UserController.java
│   ├── CommentController.java           # 评论控制器
│   ├── TeamController.java              # 车队控制器
│   ├── ProController.java               # PRO认证控制器
│   ├── ActivityController.java          # 活动控制器
│   ├── HomeController.java              # 首页控制器
│   └── AdminController.java             # 运维管理控制器
└── handler/                             # 类型处理器
    └── JsonTypeHandler.java             # JSON字段处理器

src/main/resources/
├── static/                              # 静态资源
│   └── cars.json                        # 车型数据文件
├── mapper/                              # MyBatis XML映射文件
│   ├── UserMapper.xml
│   ├── CarMapper.xml
│   ├── TuneMapper.xml
│   ├── CommentMapper.xml                # 评论映射
│   ├── TeamMapper.xml                   # 车队映射
│   ├── ProMapper.xml                    # PRO认证映射
│   └── ActivityMapper.xml               # 活动映射
├── application.yml                      # 配置文件
└── schema.sql                          # 数据库初始化脚本

scripts/
└── update_cars.py                      # 车型数据更新脚本
```

## 架构设计

### 纯MyBatis架构
本项目采用纯MyBatis架构，不使用JPA，具有以下优势：

1. **更好的SQL控制** - 可以精确控制SQL语句，优化性能
2. **更直接的数据库操作** - 避免JPA的抽象层开销
3. **更灵活的查询** - 支持复杂的动态SQL查询
4. **更好的性能** - 减少ORM框架的额外开销
5. **更简单的学习曲线** - 团队更容易理解和维护

### 分层架构
```
Controller层 (API接口)
    ↓
Service层 (业务逻辑)
    ↓
Mapper层 (MyBatis数据访问)
    ↓
数据库 (MySQL)
```

## 核心功能

### 1. 车辆系统
- **静态数据化**: 车型数据存储在JSON文件中，内存缓存，查询速度极快
- **多维度筛选**: 支持制造商、分类、驱动方式、PI等级等筛选
- **搜索功能**: 支持车型名称和制造商关键词搜索
- **热门车辆**: 按调校数量排序的热门车辆展示

### 2. 调校系统
- **完整CRUD**: 调校的创建、查询、更新、删除
- **高级搜索**: 支持关键词、车辆、PRO状态、偏好、PI等级、比赛类型等条件
- **PRO标识**: PRO用户调校特殊标记和优先排序
- **参数管理**: 详细的调校参数存储和展示
- **点赞功能**: 调校点赞和统计

### 3. 评论系统
- **嵌套评论**: 支持评论和回复的嵌套结构
- **PRO标识**: PRO用户评论特殊标记
- **点赞功能**: 评论和回复的点赞功能
- **评分系统**: 1-5星评分功能
- **实时更新**: 评论的实时添加和更新

### 4. 车队系统
- **车队管理**: 车队的创建、编辑、删除
- **成员管理**: 车队成员的添加、角色管理、权限控制
- **申请流程**: 用户申请加入车队的完整流程
- **邀请系统**: 车队邀请用户的系统
- **角色权限**: OWNER、ADMIN、MODERATOR、MEMBER四种角色

### 5. PRO认证系统
- **认证管理**: PRO用户认证信息的添加和管理
- **申请流程**: 用户申请PRO认证的完整流程
- **认证类型**: 支持锦标赛、世界纪录、成就、专业认证等类型
- **状态管理**: 申请状态的管理和更新

### 6. 用户活动系统
- **活动追踪**: 记录用户的所有活动（点赞、收藏、评论、上传等）
- **活动统计**: 用户活动的统计和分析
- **实时更新**: 活动的实时记录和更新

### 7. 首页数据系统
- **热门车辆**: 按调校数量排序的热门车辆
- **最新调校**: 最新上传的调校
- **PRO调校**: 精选的PRO调校
- **统计信息**: 网站的整体统计信息

## 车型数据优化

### 静态数据化
车型数据采用静态文件存储，具有以下优势：

1. **性能优化**: 内存缓存，查询速度极快
2. **减少数据库压力**: 车型数据不存储在数据库中
3. **易于维护**: 通过JSON文件管理，支持版本控制
4. **快速更新**: 支持热重载，无需重启服务

### 数据文件结构
```json
{
  "version": "1.0.0",
  "lastUpdated": "2024-01-01",
  "gameVersion": "Forza Horizon 5",
  "cars": [
    {
      "id": "car1",
      "name": "RS6 Avant",
      "manufacturer": "Audi",
      "year": 2020,
      "category": "SPORTS_CARS",
      "pi": 860,
      "drivetrain": "AWD",
      "imageUrl": "/images/audi-rs6.jpg",
      "gameId": "fh5"
    }
  ]
}
```

### 车型数据管理
```bash
# 添加新车型
python scripts/update_cars.py --action add \
  --name "New Car" \
  --manufacturer "Brand" \
  --year 2024 \
  --category "SPORTS_CARS" \
  --pi 850 \
  --drivetrain "AWD"

# 删除车型
python scripts/update_cars.py --action remove \
  --name "Old Car" \
  --manufacturer "Brand" \
  --year 2020

# 更新车型信息
python scripts/update_cars.py --action update \
  --name "Car Name" \
  --manufacturer "Brand" \
  --year 2024 \
  --pi 900

# 重新加载服务器数据
python scripts/update_cars.py --action reload

# 查看服务器统计
python scripts/update_cars.py --action stats
```

## 运维管理接口

### 基础信息
- **Base URL**: `http://localhost:8080/api/admin`
- **文档**: 详见 [ADMIN_API.md](ADMIN_API.md)

### 主要功能

#### 1. 车型数据管理
- `POST /admin/cars/reload` - 重新加载车型数据
- `GET /admin/cars/stats` - 获取车型数据统计

#### 2. 用户数据管理
- `POST /admin/users/cleanup` - 清理无效用户
- `GET /admin/users/stats` - 获取用户统计

#### 3. 调校数据管理
- `POST /admin/tunes/cleanup` - 清理无效调校
- `GET /admin/tunes/stats` - 获取调校统计

#### 4. 系统监控
- `GET /admin/health` - 系统健康检查
- `GET /admin/stats` - 系统统计信息

#### 5. 数据一致性
- `GET /admin/consistency/check` - 检查数据一致性
- `POST /admin/consistency/fix` - 修复数据问题

### 使用示例

```bash
# 系统健康检查
curl -X GET http://localhost:8080/api/admin/health

# 重新加载车型数据
curl -X POST http://localhost:8080/api/admin/cars/reload

# 清理无效用户（模拟模式）
curl -X POST "http://localhost:8080/api/admin/users/cleanup?dryRun=true"

# 获取系统统计
curl -X GET http://localhost:8080/api/admin/stats
```

## 数据库设计

### 核心表结构

#### 1. 用户表 (users)
- 支持邮箱、Gamertag、Xbox ID 绑定
- 用户等级：STANDARD、VERIFIED、PRO
- PRO 玩家认证功能
- 用户统计信息（调校数量、点赞数等）

#### 2. 调校表 (tunes)
- 调校代码和参数
- 支持 PRO 调校标识
- 点赞计数
- 参数公开设置
- 地面条件支持

#### 3. 调校参数表 (tune_parameters)
- 详细的调校参数
- 轮胎、齿轮、校准、防倾杆等设置

#### 4. 评论表 (tune_comments)
- 调校评论内容
- PRO用户标识
- 评分功能
- 点赞计数

#### 5. 评论回复表 (comment_replies)
- 评论回复内容
- PRO用户标识
- 点赞计数

#### 6. 车队表 (teams)
- 车队基本信息
- 车队统计信息
- 公开/私有设置

#### 7. 车队成员表 (team_members)
- 成员角色管理
- 权限控制
- 成员统计

#### 8. PRO认证表 (pro_certifications)
- 认证类型和内容
- 认证状态管理
- 验证信息

#### 9. 用户活动表 (user_activities)
- 用户活动记录
- 活动类型分类
- 时间戳记录

#### 10. 用户活动表
- user_likes: 用户点赞记录
- user_favorites: 用户收藏记录
- lap_times: 圈速记录

## 环境要求

- Java 8+
- MySQL 8.0+
- Maven 3.6+
- Python 3.6+ (用于车型数据管理脚本)

## 快速开始

### 1. 数据库设置

```sql
-- 创建数据库
CREATE DATABASE forzatune_pro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 运行初始化脚本
source src/main/resources/schema.sql
```

### 2. 配置文件

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/forzatune_pro
    username: your_username
    password: your_password

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.forzatune.backend.entity
  configuration:
    map-underscore-to-camel-case: true
```

### 3. 启动应用

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080/api` 启动。

## API 端点

### 车辆相关
- `GET /api/cars` - 获取所有车辆（支持搜索和筛选）
- `GET /api/cars/{id}` - 获取指定车辆
- `GET /api/cars/search?keyword=xxx` - 搜索车辆
- `GET /api/cars/manufacturers` - 获取所有制造商
- `GET /api/cars/category/{category}` - 根据分类获取车辆
- `GET /api/cars/drivetrain/{drivetrain}` - 根据驱动方式获取车辆
- `GET /api/cars/popular` - 获取热门车辆
- `GET /api/cars/info` - 获取车型数据信息
- `GET /api/cars/stats` - 获取车辆统计信息

### 调校相关
- `GET /api/tunes` - 获取调校列表（支持多种筛选）
- `GET /api/tunes/{id}` - 获取调校详情
- `GET /api/tunes/car/{carId}` - 获取车辆的调校
- `GET /api/tunes/car/{carId}/pro` - 获取车辆的PRO调校
- `GET /api/tunes/author/{authorId}` - 获取作者的调校
- `GET /api/tunes/pro` - 获取所有PRO调校
- `GET /api/tunes/popular` - 获取热门调校
- `GET /api/tunes/recent` - 获取最新调校
- `GET /api/tunes/search` - 高级搜索调校
- `POST /api/tunes` - 创建新调校
- `PUT /api/tunes/{id}/like` - 更新调校点赞数
- `GET /api/tunes/stats` - 获取调校统计信息
- `GET /api/tunes/pro/stats` - 获取PRO调校统计
- `GET /api/tunes/author/{authorId}/stats` - 获取作者调校统计

### 评论相关
- `GET /api/comments/tune/{tuneId}` - 根据调校ID获取评论
- `GET /api/comments/{id}` - 根据ID获取评论详情
- `POST /api/comments` - 添加评论
- `PUT /api/comments/{id}` - 更新评论
- `DELETE /api/comments/{id}` - 删除评论
- `POST /api/comments/{id}/like` - 点赞评论
- `POST /api/comments/{commentId}/replies` - 添加回复
- `PUT /api/comments/replies/{id}` - 更新回复
- `DELETE /api/comments/replies/{id}` - 删除回复
- `POST /api/comments/replies/{id}/like` - 点赞回复
- `GET /api/comments/stats` - 获取评论统计

### 车队相关
- `GET /api/teams` - 获取所有车队
- `GET /api/teams/{id}` - 根据ID获取车队详情
- `GET /api/teams/name/{name}` - 根据名称获取车队
- `POST /api/teams` - 创建车队
- `PUT /api/teams/{id}` - 更新车队信息
- `DELETE /api/teams/{id}` - 删除车队
- `GET /api/teams/{teamId}/members` - 获取车队成员
- `POST /api/teams/{teamId}/members` - 添加车队成员
- `PUT /api/teams/{teamId}/members/{userId}` - 更新成员角色
- `DELETE /api/teams/{teamId}/members/{userId}` - 移除车队成员
- `POST /api/teams/{teamId}/apply` - 申请加入车队
- `PUT /api/teams/{teamId}/applications/{userId}` - 处理车队申请
- `POST /api/teams/{teamId}/invite` - 邀请用户加入车队
- `PUT /api/teams/{teamId}/invitations/{userId}` - 处理车队邀请

### PRO认证相关
- `GET /api/pro/certifications/{userId}` - 获取用户的PRO认证信息
- `POST /api/pro/certifications` - 添加PRO认证
- `PUT /api/pro/certifications/{id}` - 更新PRO认证状态
- `DELETE /api/pro/certifications/{id}` - 删除PRO认证
- `POST /api/pro/applications` - 提交PRO申请
- `GET /api/pro/applications` - 获取PRO申请列表
- `GET /api/pro/applications/user/{userId}` - 获取用户的PRO申请
- `PUT /api/pro/applications/{id}` - 处理PRO申请
- `GET /api/pro/stats` - 获取PRO用户统计

### 用户活动相关
- `GET /api/activities/user/{userId}` - 获取用户活动列表
- `POST /api/activities` - 添加用户活动
- `GET /api/activities/stats/user/{userId}` - 获取用户活动统计

### 用户相关
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 获取用户详情
- `GET /api/users/gamertag/{gamertag}` - 根据Gamertag获取用户
- `GET /api/users/pro` - 获取所有PRO玩家
- `GET /api/users/pro/count` - 获取PRO玩家数量
- `POST /api/users` - 创建新用户
- `PUT /api/users/{id}` - 更新用户信息
- `GET /api/users/check/email/{email}` - 检查邮箱是否存在
- `GET /api/users/check/gamertag/{gamertag}` - 检查Gamertag是否存在

### 首页相关
- `GET /api/home/data` - 获取首页所有数据
- `GET /api/home/popular-cars` - 获取热门车辆
- `GET /api/home/recent-tunes` - 获取最新调校
- `GET /api/home/pro-tunes` - 获取PRO调校
- `GET /api/home/stats` - 获取首页统计信息

## 前端API对应关系

### 车辆API
- `getAllCars()` → `GET /api/cars`
- `getCarById(carId)` → `GET /api/cars/{id}`

### 调校API
- `getAllTunes()` → `GET /api/tunes`
- `getTuneById(tuneId)` → `GET /api/tunes/{id}`
- `getTunesByCarId(carId)` → `GET /api/tunes/car/{carId}`
- `getProTunes()` → `GET /api/tunes/pro`
- `getProTunesByCarId(carId)` → `GET /api/tunes/car/{carId}/pro`

### 评论API
- `getCommentsByTuneId(tuneId)` → `GET /api/comments/tune/{tuneId}`
- `addComment(tuneId, commentData)` → `POST /api/comments`
- `addReply(commentId, replyData)` → `POST /api/comments/{commentId}/replies`
- `updateCommentLikes(commentId)` → `POST /api/comments/{id}/like`
- `updateReplyLikes(replyId)` → `POST /api/comments/replies/{id}/like`

### 车队API
- 车队信息 → `GET /api/teams/{id}`
- 车队成员 → `GET /api/teams/{teamId}/members`
- 申请加入车队 → `POST /api/teams/{teamId}/apply`
- 处理申请 → `PUT /api/teams/{teamId}/applications/{userId}`

### PRO认证API
- PRO认证信息 → `GET /api/pro/certifications/{userId}`
- 提交PRO申请 → `POST /api/pro/applications`
- 处理PRO申请 → `PUT /api/pro/applications/{id}`

### 用户API
- `getAllUsers()` → `GET /api/users`
- `getUserById(userId)` → `GET /api/users/{id}`
- `getUserByGamertag(gamertag)` → `GET /api/users/gamertag/{gamertag}`

### 首页API
- 首页热门车辆 → `GET /api/home/popular-cars`
- 首页最新调校 → `GET /api/home/recent-tunes`
- 首页PRO调校 → `GET /api/home/pro-tunes`

## 开发说明

### 车型数据优化策略

1. **静态文件存储**: 车型数据存储在JSON文件中，减少数据库压力
2. **内存缓存**: 启动时加载到内存，查询速度极快
3. **索引优化**: 构建制造商、分类、驱动方式等索引
4. **热重载**: 支持运行时重新加载数据，无需重启服务

### 为什么不使用 Redis？

对于当前访问量（并发 < 50）的情况，选择不使用 Redis 的原因：

1. **复杂度控制** - 避免额外的运维复杂度
2. **资源效率** - MySQL 直接查询性能已足够
3. **开发效率** - 避免缓存一致性问题
4. **成本效益** - 小并发下收益不明显

### 为什么选择 MyBatis 而不是 JPA？

1. **SQL控制** - 更好的SQL优化控制
2. **性能** - 更直接的数据库操作
3. **灵活性** - 复杂查询更容易实现
4. **学习成本** - 团队更熟悉MyBatis

### 性能优化策略

1. **数据库索引** - 为常用查询字段创建索引
2. **连接池** - 使用 HikariCP 连接池
3. **查询优化** - 使用 MyBatis 动态SQL
4. **分页查询** - 大数据量使用分页
5. **静态数据缓存** - 车型数据内存缓存

### 后续扩展

当访问量增长时，可以考虑：
1. 引入 Redis 缓存热点数据
2. 使用 CDN 加速静态资源
3. 数据库读写分离
4. 微服务架构拆分

## 部署

### Docker 部署

```dockerfile
FROM openjdk:8-jdk-slim
COPY target/forzatune-pro-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 生产环境配置

1. 修改数据库连接为生产环境
2. 配置 JWT 密钥
3. 设置 CORS 允许的域名
4. 配置日志级别
5. 添加运维接口认证

## 文档

- [完整API文档](COMPLETE_API_DOCUMENTATION.md) - 详细的功能需求清单和API说明
- [运维API文档](ADMIN_API.md) - 运维管理接口文档

## 贡献

欢迎提交 Issue 和 Pull Request！

## 许可证

MIT License 