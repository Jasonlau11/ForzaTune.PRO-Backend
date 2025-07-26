# ForzaTune.PRO 数据库表结构设计文档

## 概述

本文档描述了 ForzaTune.PRO 平台的完整数据库表结构设计，使用 MySQL 语法。该设计支持车辆管理、调校分享、用户系统、社区互动等核心功能。

## 数据库表结构

### 1. 用户相关表

#### 1.1 用户表 (users)
```sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    gamertag VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_pro_player BOOLEAN DEFAULT FALSE,
    pro_player_since TIMESTAMP NULL,
    total_tunes INT DEFAULT 0,
    total_likes INT DEFAULT 0,
    bio TEXT,
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_gamertag (gamertag),
    INDEX idx_email (email),
    INDEX idx_pro_player (is_pro_player)
);
```

#### 1.2 专业认证表 (pro_certifications)
```sql
CREATE TABLE pro_certifications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type ENUM('championship', 'world_record', 'achievement', 'expertise') NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    verified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_type (user_id, type)
);
```

### 2. 车辆相关表

#### 2.1 车辆表 (cars)
```sql
CREATE TABLE cars (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    category ENUM('Sports Cars', 'Muscle Cars', 'Supercars', 'Classic Cars', 'Hypercars', 'Track Toys') NOT NULL,
    pi INT NOT NULL,
    drivetrain ENUM('RWD', 'FWD', 'AWD') NOT NULL,
    game_id VARCHAR(20) NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_manufacturer (manufacturer),
    INDEX idx_category (category),
    INDEX idx_pi (pi),
    INDEX idx_drivetrain (drivetrain),
    INDEX idx_game (game_id)
);
```

### 3. 调校相关表

#### 3.1 调校表 (tunes)
```sql
CREATE TABLE tunes (
    id VARCHAR(36) PRIMARY KEY,
    car_id VARCHAR(36) NOT NULL,
    author_id VARCHAR(36) NOT NULL,
    share_code VARCHAR(20) UNIQUE NOT NULL,
    preference ENUM('Power', 'Handling', 'Balance') NOT NULL,
    pi_class ENUM('X', 'S2', 'S1', 'A', 'B', 'C', 'D') NOT NULL,
    final_pi INT NOT NULL,
    drivetrain ENUM('RWD', 'FWD', 'AWD'),
    tire_compound ENUM('Stock', 'Street', 'Sport', 'Semi-Slick', 'Slick', 'Rally', 'Snow', 'Off-Road', 'Drag', 'Drift'),
    race_type ENUM('Road', 'Dirt', 'Cross Country'),
    surface_conditions JSON, -- 存储地面条件数组 ['Dry', 'Wet', 'Snow']
    description TEXT,
    is_pro_tune BOOLEAN DEFAULT FALSE,
    is_parameters_public BOOLEAN DEFAULT FALSE,
    has_detailed_parameters BOOLEAN DEFAULT FALSE,
    screenshot_url VARCHAR(255),
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (car_id) REFERENCES cars(id),
    FOREIGN KEY (author_id) REFERENCES users(id),
    INDEX idx_car_author (car_id, author_id),
    INDEX idx_pi_class (pi_class),
    INDEX idx_preference (preference),
    INDEX idx_created_at (created_at),
    INDEX idx_like_count (like_count),
    INDEX idx_surface_conditions ((CAST(surface_conditions AS CHAR(100))))
);
```

#### 3.2 调校参数表 (tune_parameters)
```sql
CREATE TABLE tune_parameters (
    id VARCHAR(36) PRIMARY KEY,
    tune_id VARCHAR(36) NOT NULL,
    
    -- 轮胎
    front_tire_pressure DECIMAL(4,1),
    rear_tire_pressure DECIMAL(4,1),
    
    -- 变速箱
    transmission_speeds INT, -- 变速箱速别 (6, 7, 8, 9)
    final_drive DECIMAL(4,3), -- 终传比
    gear_1_ratio DECIMAL(4,3),
    gear_2_ratio DECIMAL(4,3),
    gear_3_ratio DECIMAL(4,3),
    gear_4_ratio DECIMAL(4,3),
    gear_5_ratio DECIMAL(4,3),
    gear_6_ratio DECIMAL(4,3),
    gear_7_ratio DECIMAL(4,3),
    gear_8_ratio DECIMAL(4,3),
    gear_9_ratio DECIMAL(4,3),
    
    -- 校准
    front_camber DECIMAL(3,1),
    rear_camber DECIMAL(3,1),
    front_toe DECIMAL(3,2),
    rear_toe DECIMAL(3,2),
    front_caster DECIMAL(3,1),
    
    -- 防倾杆
    front_anti_roll_bar DECIMAL(4,1),
    rear_anti_roll_bar DECIMAL(4,1),
    
    -- 弹簧
    front_springs DECIMAL(5,1),
    rear_springs DECIMAL(5,1),
    front_ride_height DECIMAL(4,1),
    rear_ride_height DECIMAL(4,1),
    
    -- 阻尼
    front_rebound DECIMAL(3,1),
    rear_rebound DECIMAL(3,1),
    front_bump DECIMAL(3,1),
    rear_bump DECIMAL(3,1),
    
    -- 差速器
    differential_type ENUM('Stock', 'Street', 'Sport', 'Off-Road', 'Rally', 'Drift'), -- 差速器类型
    front_acceleration DECIMAL(4,1), -- 前差速器加速比
    front_deceleration DECIMAL(4,1), -- 前差速器减速比
    rear_acceleration DECIMAL(4,1),  -- 后差速器加速比
    rear_deceleration DECIMAL(4,1),  -- 后差速器减速比
    center_balance DECIMAL(4,1),     -- 中央差速器动力分配比例 (仅AWD)
    
    -- 制动
    brake_pressure INT,
    front_brake_balance INT,
    
    -- 空气动力学
    front_downforce DECIMAL(4,1),
    rear_downforce DECIMAL(4,1),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE
);
```

### 4. 赛道相关表

#### 4.1 赛道表 (tracks)
```sql
CREATE TABLE tracks (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    game_id VARCHAR(20) NOT NULL,
    category ENUM('Circuit', 'Sprint', 'Drift', 'Drag', 'Rally') NOT NULL,
    length DECIMAL(5,2), -- 赛道长度（公里）
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_game_category (game_id, category)
);
```

#### 4.2 圈速记录表 (lap_times)
```sql
CREATE TABLE lap_times (
    id VARCHAR(36) PRIMARY KEY,
    tune_id VARCHAR(36) NOT NULL,
    track_id VARCHAR(36) NOT NULL,
    time VARCHAR(20) NOT NULL, -- 格式: "1:55.234"
    pro_player_id VARCHAR(36),
    is_verified BOOLEAN DEFAULT FALSE,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    FOREIGN KEY (track_id) REFERENCES tracks(id),
    FOREIGN KEY (pro_player_id) REFERENCES users(id),
    INDEX idx_tune_track (tune_id, track_id),
    INDEX idx_pro_player (pro_player_id),
    INDEX idx_verified (is_verified)
);
```

### 5. 社区互动表

#### 5.1 评论表 (comments)
```sql
CREATE TABLE comments (
    id VARCHAR(36) PRIMARY KEY,
    tune_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_tune_created (tune_id, created_at),
    INDEX idx_user (user_id)
);
```

#### 5.2 评论回复表 (comment_replies)
```sql
CREATE TABLE comment_replies (
    id VARCHAR(36) PRIMARY KEY,
    comment_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_comment_created (comment_id, created_at),
    INDEX idx_user (user_id)
);
```

#### 5.3 用户点赞表 (user_likes)
```sql
CREATE TABLE user_likes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    tune_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_tune (user_id, tune_id),
    INDEX idx_user (user_id),
    INDEX idx_tune (tune_id)
);
```

#### 5.4 用户收藏表 (user_favorites)
```sql
CREATE TABLE user_favorites (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    tune_id VARCHAR(36) NOT NULL,
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_tune (user_id, tune_id),
    INDEX idx_user (user_id),
    INDEX idx_tune (tune_id)
);
```

#### 5.5 用户活动表 (user_activities)
```sql
CREATE TABLE user_activities (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    activity_type ENUM('upload_tune', 'like_tune', 'favorite_tune', 'comment_tune', 'achieve_pro') NOT NULL,
    target_id VARCHAR(36), -- 相关调校ID或其他目标ID
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_created (user_id, created_at),
    INDEX idx_activity_type (activity_type)
);
```

### 6. 系统配置表

#### 6.1 系统设置表 (system_settings)
```sql
CREATE TABLE system_settings (
    id VARCHAR(36) PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    description TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_setting_key (setting_key)
);
```

## 索引设计说明

### 主要索引策略

1. **外键索引**：所有外键字段都创建了索引以提高关联查询性能
2. **复合索引**：针对常用查询场景创建复合索引
3. **唯一索引**：确保数据唯一性的字段（如share_code、gamertag等）
4. **JSON索引**：对surface_conditions字段创建JSON索引以支持条件查询

### 性能优化建议

1. **分区策略**：对于大表（如tunes、comments），可考虑按时间分区
2. **读写分离**：对于高并发场景，建议实施读写分离
3. **缓存策略**：对热点数据（如热门调校、用户信息）实施缓存

## 数据完整性约束

### 外键约束
- 所有关联表都设置了适当的外键约束
- 使用CASCADE删除确保数据一致性

### 检查约束
- 使用ENUM类型限制字段取值范围
- 数值字段设置了合理的精度和范围

### 唯一性约束
- 用户gamertag和email唯一
- 调校share_code唯一
- 用户点赞和收藏记录唯一

## 扩展性考虑

### 水平扩展
- 表结构设计支持水平分片
- 关键字段使用UUID确保分布式环境下的唯一性

### 功能扩展
- 预留了扩展字段（如JSON字段）
- 系统设置表支持动态配置
- 活动类型支持新功能扩展

## 安全考虑

### 数据安全
- 密码使用哈希存储
- 敏感信息脱敏处理
- 支持数据备份和恢复

### 访问控制
- 用户权限通过字段控制（如is_parameters_public）
- 支持细粒度的数据访问控制

## 总结

该数据库设计支持ForzaTune.PRO平台的所有核心功能，包括：
- 用户管理和专业认证
- 车辆信息管理
- 调校分享和参数配置
- 赛道和圈速记录
- 社区互动（评论、点赞、收藏）
- 用户活动追踪
- 系统配置管理

设计充分考虑了性能、扩展性和安全性，为后续的功能扩展提供了良好的基础。 

# ForzaTune.PRO 后端接口设计文档

## 枚举值规范

### 调校倾向 (Preference)
- `Power` - 动力优先
- `Handling` - 操控优先  
- `Balance` - 平衡

### 路面条件 (Surface Condition)
- `Dry` - 干燥
- `Wet` - 潮湿
- `Snow` - 雪地

### 驱动形式 (Drivetrain)
- `FWD` - 前轮驱动
- `RWD` - 后轮驱动
- `AWD` - 全轮驱动

### 轮胎化合物 (Tire Compound)
- `Stock` - 原厂胎
- `Street` - 街道胎
- `Sport` - 运动胎
- `Semi-Slick` - 半光头胎
- `Slick` - 光头胎
- `Rally` - 拉力胎
- `Snow` - 雪地胎
- `Off-Road` - 越野胎
- `Drag` - 直线加速胎
- `Drift` - 漂移胎

### 差速器类型 (Differential Type)
- `Stock` - 原厂差速器
- `Street` - 街道差速器
- `Sport` - 运动差速器
- `Off-Road` - 越野差速器
- `Rally` - 拉力差速器
- `Drift` - 漂移差速器

### 变速箱档位 (Transmission Speeds)
- `6` - 6速
- `7` - 7速
- `8` - 8速
- `9` - 9速

### PI等级 (PI Class)
- `X` - X级
- `S2` - S2级
- `S1` - S1级
- `A` - A级
- `B` - B级
- `C` - C级
- `D` - D级

### 比赛类型 (Race Type)
- `Road` - 公路赛
- `Dirt` - 泥地赛
- `Cross Country` - 越野赛

## 接口返回格式规范

所有涉及枚举值的接口返回字段，必须使用上述规范中的英文值，前端通过 i18n 进行本地化显示。

### 示例
```json
{
  "success": true,
  "data": {
    "id": "tune-001",
    "preference": "Power",
    "surfaceConditions": ["Dry", "Wet"],
    "drivetrain": "AWD",
    "tireCompound": "Sport",
    "differentialType": "Sport",
    "transmissionSpeeds": 8
  }
}
``` 