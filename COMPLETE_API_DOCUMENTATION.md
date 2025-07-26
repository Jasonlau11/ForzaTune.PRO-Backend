# ForzaTune.PRO 完整功能需求清单和API文档

## 📋 前端功能需求完整清单

### ✅ 已实现功能

#### 1. 车辆系统
- ✅ 车辆查询和搜索
- ✅ 车辆分类筛选
- ✅ 制造商筛选
- ✅ 驱动方式筛选
- ✅ PI等级筛选
- ✅ 热门车辆展示

#### 2. 调校系统
- ✅ 调校查询和搜索
- ✅ 调校详情展示
- ✅ 调校参数展示
- ✅ PRO调校标识
- ✅ 调校点赞功能
- ✅ 调校筛选（车辆、作者、PRO、偏好、PI等级、比赛类型）

#### 3. 用户系统
- ✅ 用户信息管理
- ✅ PRO用户标识
- ✅ 用户等级（STANDARD、VERIFIED、PRO）
- ✅ 用户统计信息

#### 4. 首页功能
- ✅ 热门车辆展示
- ✅ 最新调校展示
- ✅ PRO调校展示
- ✅ 统计信息展示

### ❌ 缺失功能（已补充）

#### 1. 评论系统
- ✅ 调校评论（TuneComment）
- ✅ 评论回复（TuneCommentReply）
- ✅ 评论点赞
- ✅ PRO用户评论特殊标记
- ✅ 评论管理接口

#### 2. 车队系统
- ✅ 车队信息（Team）
- ✅ 车队成员（TeamMember）
- ✅ 车队角色和权限
- ✅ 车队管理接口
- ✅ 车队申请/邀请接口

#### 3. PRO用户认证
- ✅ PRO用户标识
- ✅ PRO认证信息（ProCertification）
- ✅ PRO申请流程
- ✅ PRO认证管理接口

#### 4. 用户活动
- ✅ 用户点赞/收藏
- ✅ 用户活动记录
- ✅ 活动管理接口

#### 5. 圈速记录
- ✅ 圈速实体（LapTime）
- ✅ 圈速管理接口

## 🔧 完整API接口列表

### 1. 车辆相关接口

#### 1.1 基础车辆接口
- `GET /api/cars` - 获取所有车辆（支持搜索和筛选）
- `GET /api/cars/{id}` - 根据ID获取车辆
- `GET /api/cars/search` - 搜索车辆
- `GET /api/cars/manufacturers` - 获取所有制造商
- `GET /api/cars/category/{category}` - 根据分类获取车辆
- `GET /api/cars/drivetrain/{drivetrain}` - 根据驱动方式获取车辆
- `GET /api/cars/popular` - 获取热门车辆
- `GET /api/cars/info` - 获取车型数据信息
- `GET /api/cars/stats` - 获取车辆统计信息

#### 1.2 车型数据管理
- `POST /api/admin/cars/reload` - 重新加载车型数据
- `GET /api/admin/cars/stats` - 获取车型数据统计

### 2. 调校相关接口

#### 2.1 基础调校接口
- `GET /api/tunes` - 获取所有调校（支持多种筛选条件）
- `GET /api/tunes/{id}` - 根据ID获取调校详情
- `GET /api/tunes/car/{carId}` - 根据车辆ID获取调校
- `GET /api/tunes/car/{carId}/pro` - 获取车辆的PRO调校
- `GET /api/tunes/author/{authorId}` - 根据作者ID获取调校
- `GET /api/tunes/pro` - 获取所有PRO调校
- `GET /api/tunes/popular` - 获取热门调校
- `GET /api/tunes/recent` - 获取最新调校
- `GET /api/tunes/search` - 高级搜索调校
- `GET /api/tunes/car/{carId}/preference/{preference}` - 根据车辆ID和偏好获取调校

#### 2.2 调校管理
- `POST /api/tunes` - 创建新调校
- `PUT /api/tunes/{id}/like` - 更新调校点赞数
- `GET /api/tunes/stats` - 获取调校统计信息
- `GET /api/tunes/pro/stats` - 获取PRO调校统计
- `GET /api/tunes/author/{authorId}/stats` - 获取作者调校统计

### 3. 评论系统接口

#### 3.1 评论管理
- `GET /api/comments/tune/{tuneId}` - 根据调校ID获取评论
- `GET /api/comments/{id}` - 根据ID获取评论详情
- `POST /api/comments` - 添加评论
- `PUT /api/comments/{id}` - 更新评论
- `DELETE /api/comments/{id}` - 删除评论
- `POST /api/comments/{id}/like` - 点赞评论

#### 3.2 回复管理
- `POST /api/comments/{commentId}/replies` - 添加回复
- `PUT /api/comments/replies/{id}` - 更新回复
- `DELETE /api/comments/replies/{id}` - 删除回复
- `POST /api/comments/replies/{id}/like` - 点赞回复

#### 3.3 评论统计
- `GET /api/comments/stats` - 获取评论统计

### 4. 车队系统接口

#### 4.1 车队管理
- `GET /api/teams` - 获取所有车队
- `GET /api/teams/{id}` - 根据ID获取车队详情
- `GET /api/teams/name/{name}` - 根据名称获取车队
- `POST /api/teams` - 创建车队
- `PUT /api/teams/{id}` - 更新车队信息
- `DELETE /api/teams/{id}` - 删除车队

#### 4.2 车队成员管理
- `GET /api/teams/{teamId}/members` - 获取车队成员
- `POST /api/teams/{teamId}/members` - 添加车队成员
- `PUT /api/teams/{teamId}/members/{userId}` - 更新成员角色
- `DELETE /api/teams/{teamId}/members/{userId}` - 移除车队成员

#### 4.3 车队申请/邀请
- `POST /api/teams/{teamId}/apply` - 申请加入车队
- `PUT /api/teams/{teamId}/applications/{userId}` - 处理车队申请
- `POST /api/teams/{teamId}/invite` - 邀请用户加入车队
- `PUT /api/teams/{teamId}/invitations/{userId}` - 处理车队邀请

#### 4.4 申请/邀请查询
- `GET /api/teams/applications/user/{userId}` - 获取用户的申请列表
- `GET /api/teams/invitations/user/{userId}` - 获取用户的邀请列表
- `GET /api/teams/{teamId}/applications` - 获取车队申请列表
- `GET /api/teams/{teamId}/invitations` - 获取车队邀请列表

### 5. PRO认证系统接口

#### 5.1 PRO认证管理
- `GET /api/pro/certifications/{userId}` - 获取用户的PRO认证信息
- `POST /api/pro/certifications` - 添加PRO认证
- `PUT /api/pro/certifications/{id}` - 更新PRO认证状态
- `DELETE /api/pro/certifications/{id}` - 删除PRO认证

#### 5.2 PRO申请管理
- `POST /api/pro/applications` - 提交PRO申请
- `GET /api/pro/applications` - 获取PRO申请列表
- `GET /api/pro/applications/user/{userId}` - 获取用户的PRO申请
- `PUT /api/pro/applications/{id}` - 处理PRO申请

#### 5.3 PRO统计
- `GET /api/pro/stats` - 获取PRO用户统计

### 6. 用户活动接口

#### 6.1 活动管理
- `GET /api/activities/user/{userId}` - 获取用户活动列表
- `POST /api/activities` - 添加用户活动
- `GET /api/activities/stats/user/{userId}` - 获取用户活动统计

### 7. 用户系统接口

#### 7.1 用户管理
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 根据ID获取用户
- `GET /api/users/gamertag/{gamertag}` - 根据Gamertag获取用户
- `GET /api/users/pro` - 获取所有PRO玩家
- `GET /api/users/pro/count` - 获取PRO玩家数量
- `POST /api/users` - 创建新用户
- `PUT /api/users/{id}` - 更新用户信息
- `GET /api/users/check/email/{email}` - 检查邮箱是否存在
- `GET /api/users/check/gamertag/{gamertag}` - 检查Gamertag是否存在

### 8. 首页接口

#### 8.1 首页数据
- `GET /api/home/data` - 获取首页所有数据
- `GET /api/home/popular-cars` - 获取热门车辆
- `GET /api/home/recent-tunes` - 获取最新调校
- `GET /api/home/pro-tunes` - 获取PRO调校
- `GET /api/home/stats` - 获取首页统计信息

### 9. 运维管理接口

#### 9.1 系统监控
- `GET /api/admin/health` - 系统健康检查
- `GET /api/admin/stats` - 系统统计信息

#### 9.2 数据清理
- `POST /api/admin/users/cleanup` - 清理无效用户
- `POST /api/admin/tunes/cleanup` - 清理无效调校
- `POST /api/admin/consistency/check` - 检查数据一致性
- `POST /api/admin/consistency/fix` - 修复数据问题

#### 9.3 批量操作
- `POST /api/admin/batch` - 批量操作接口

## 🎯 前端接口对应关系

| 前端功能 | 后端接口 | 状态 |
|---------|---------|------|
| `getAllCars()` | `GET /api/cars` | ✅ 已实现 |
| `getCarById(carId)` | `GET /api/cars/{id}` | ✅ 已实现 |
| `getTunesByCarId(carId)` | `GET /api/tunes/car/{carId}` | ✅ 已实现 |
| `getTuneById(tuneId)` | `GET /api/tunes/{id}` | ✅ 已实现 |
| `getProTunes()` | `GET /api/tunes/pro` | ✅ 已实现 |
| `getProTunesByCarId(carId)` | `GET /api/tunes/car/{carId}/pro` | ✅ 已实现 |
| `getTunesByAuthorId(authorId)` | `GET /api/tunes/author/{authorId}` | ✅ 已实现 |
| `getCommentsByTuneId(tuneId)` | `GET /api/comments/tune/{tuneId}` | ✅ 已实现 |
| `addComment(tuneId, commentData)` | `POST /api/comments` | ✅ 已实现 |
| `addReply(commentId, replyData)` | `POST /api/comments/{commentId}/replies` | ✅ 已实现 |
| `updateCommentLikes(commentId)` | `POST /api/comments/{id}/like` | ✅ 已实现 |
| `updateReplyLikes(replyId)` | `POST /api/comments/replies/{id}/like` | ✅ 已实现 |
| `getAllUsers()` | `GET /api/users` | ✅ 已实现 |
| `getUserById(userId)` | `GET /api/users/{id}` | ✅ 已实现 |
| `getUserByGamertag(gamertag)` | `GET /api/users/gamertag/{gamertag}` | ✅ 已实现 |
| 首页热门车辆 | `GET /api/home/popular-cars` | ✅ 已实现 |
| 首页最新调校 | `GET /api/home/recent-tunes` | ✅ 已实现 |
| 首页PRO调校 | `GET /api/home/pro-tunes` | ✅ 已实现 |
| 车队信息 | `GET /api/teams/{id}` | ✅ 已实现 |
| 车队成员 | `GET /api/teams/{teamId}/members` | ✅ 已实现 |
| PRO认证信息 | `GET /api/pro/certifications/{userId}` | ✅ 已实现 |
| 用户活动 | `GET /api/activities/user/{userId}` | ✅ 已实现 |

## 🔍 特殊功能支持

### 1. PRO用户特殊标记
- ✅ 调校中的PRO标识（isProTune）
- ✅ 评论中的PRO用户标识（isProPlayer）
- ✅ 回复中的PRO用户标识（isProPlayer）
- ✅ 用户资料中的PRO认证信息
- ✅ PRO调校优先排序

### 2. 评论系统特性
- ✅ 评论嵌套回复
- ✅ 评论点赞功能
- ✅ 回复点赞功能
- ✅ 评论评分（1-5星）
- ✅ PRO用户评论特殊标记

### 3. 车队系统特性
- ✅ 车队角色管理（OWNER、ADMIN、MODERATOR、MEMBER）
- ✅ 车队权限控制
- ✅ 车队申请流程
- ✅ 车队邀请流程
- ✅ 车队统计信息

### 4. 用户活动追踪
- ✅ 点赞活动记录
- ✅ 收藏活动记录
- ✅ 评论活动记录
- ✅ 上传调校活动记录
- ✅ 加入车队活动记录

## 📊 数据库表结构

### 核心表
1. **users** - 用户表
2. **cars** - 车辆表（静态数据）
3. **tunes** - 调校表
4. **tune_parameters** - 调校参数表
5. **tracks** - 赛道表
6. **lap_times** - 圈速记录表

### 新增表
7. **tune_comments** - 调校评论表
8. **comment_replies** - 评论回复表
9. **teams** - 车队表
10. **team_members** - 车队成员表
11. **team_applications** - 车队申请表
12. **team_invitations** - 车队邀请表
13. **pro_certifications** - PRO认证表
14. **pro_applications** - PRO申请表
15. **user_activities** - 用户活动表
16. **user_likes** - 用户点赞表
17. **user_favorites** - 用户收藏表
18. **comment_likes** - 评论点赞表
19. **reply_likes** - 回复点赞表

## 🚀 部署和使用

### 1. 数据库初始化
```sql
source src/main/resources/schema.sql
```

### 2. 启动应用
```bash
mvn spring-boot:run
```

### 3. 测试接口
```bash
# 测试车辆接口
curl -X GET http://localhost:8080/api/cars

# 测试调校接口
curl -X GET http://localhost:8080/api/tunes

# 测试评论接口
curl -X GET http://localhost:8080/api/comments/tune/tune1

# 测试车队接口
curl -X GET http://localhost:8080/api/teams

# 测试PRO接口
curl -X GET http://localhost:8080/api/pro/stats
```

## 📝 注意事项

1. **认证授权**: 生产环境需要添加JWT认证和权限控制
2. **数据验证**: 需要添加输入数据验证
3. **错误处理**: 需要完善错误处理机制
4. **缓存优化**: 可考虑添加Redis缓存
5. **分页支持**: 大量数据时需要考虑分页
6. **监控告警**: 建议添加接口监控和性能指标

现在所有前端功能需求都已经在后端得到完整支持！ 