-- 创建数据库
CREATE DATABASE IF NOT EXISTS forzatune_pro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE forzatune_pro;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    xbox_id VARCHAR(50) UNIQUE NOT NULL, -- Xbox Live ID，作为用户显示名称
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- 密码字段
    is_pro_player BOOLEAN DEFAULT FALSE,
    pro_player_since TIMESTAMP NULL,
    total_tunes INT DEFAULT 0,
    total_likes INT DEFAULT 0,
    bio TEXT,
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_tier ENUM('STANDARD', 'VERIFIED', 'PRO') DEFAULT 'STANDARD',
    last_login DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    email_verified_at DATETIME NULL
);

-- 车辆表
CREATE TABLE IF NOT EXISTS cars (
    id VARCHAR(50) NOT NULL, -- 简化的ID，如 'porsche-911-gt2-rs'
    name VARCHAR(200) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    category ENUM('Sports Cars', 'Muscle Cars', 'Supercars', 'Classic Cars', 'Hypercars', 'Track Toys') NOT NULL,
    pi INT NOT NULL,
    drivetrain ENUM('RWD', 'FWD', 'AWD') NOT NULL,
    game_category ENUM('fh4', 'fh5') NOT NULL, -- 游戏分类字段
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- 复合主键：同一车辆在不同游戏中是独立记录
    PRIMARY KEY (id, game_category)
);

-- 调校表
CREATE TABLE IF NOT EXISTS tunes (
    id VARCHAR(50) PRIMARY KEY, -- 简化的ID，如 'tune-001'
    car_id VARCHAR(50) NOT NULL,
    author_id VARCHAR(36) NOT NULL,
    author_xbox_id VARCHAR(50) NOT NULL, -- 作者Xbox ID，冗余存储便于查询
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
    game_category ENUM('fh4', 'fh5') NOT NULL, -- 游戏分类字段
    parameters JSON, -- 调校参数JSON字段，支持不同游戏格式
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 调校参数表
CREATE TABLE IF NOT EXISTS tune_parameters (
    id VARCHAR(36) PRIMARY KEY,
    tune_id VARCHAR(50) NOT NULL, -- 修正数据类型，与tunes表保持一致
    
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 赛道表和圈速记录表已移除：地平线系列不使用传统赛道概念

-- 调校评论表
CREATE TABLE IF NOT EXISTS tune_comments (
    id VARCHAR(36) PRIMARY KEY,
    tune_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_xbox_id VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 评论回复表
CREATE TABLE IF NOT EXISTS comment_replies (
    id VARCHAR(36) PRIMARY KEY,
    comment_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_xbox_id VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 车队表
CREATE TABLE IF NOT EXISTS teams (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    founder_id VARCHAR(36) NOT NULL,
    founder_xbox_id VARCHAR(100) NOT NULL,
    is_public BOOLEAN DEFAULT TRUE,
    tags JSON,
    total_members INT DEFAULT 1,
    total_tunes INT DEFAULT 0,
    total_downloads INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 车队成员表
CREATE TABLE IF NOT EXISTS team_members (
    id VARCHAR(36) PRIMARY KEY,
    team_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_xbox_id VARCHAR(100) NOT NULL,
    role ENUM('OWNER', 'ADMIN', 'MODERATOR', 'MEMBER') DEFAULT 'MEMBER',
    permissions JSON,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_contributions INT DEFAULT 0,
    total_tunes INT DEFAULT 0,
    UNIQUE KEY uk_team_user (team_id, user_id)
);

-- 车队申请表
CREATE TABLE IF NOT EXISTS team_applications (
    id VARCHAR(36) PRIMARY KEY,
    team_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_xbox_id VARCHAR(100) NOT NULL,
    message TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by VARCHAR(36),
    reviewed_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_team_user_application (team_id, user_id)
);

-- 车队邀请表
CREATE TABLE IF NOT EXISTS team_invitations (
    id VARCHAR(36) PRIMARY KEY,
    team_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    invited_by VARCHAR(36) NOT NULL,
    message TEXT,
    status ENUM('PENDING', 'ACCEPTED', 'DECLINED', 'EXPIRED') DEFAULT 'PENDING',
    expires_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_team_user_invitation (team_id, user_id)
);

-- PRO认证表
CREATE TABLE IF NOT EXISTS pro_certifications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type ENUM('championship', 'world_record', 'achievement', 'expertise') NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    verified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PRO申请表
CREATE TABLE IF NOT EXISTS pro_applications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    xbox_id VARCHAR(100) NOT NULL,
    experience TEXT NOT NULL,
    achievements JSON,
    sample_tunes JSON,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at DATETIME,
    reviewed_by VARCHAR(36),
    notes TEXT
);

-- 用户活动表
CREATE TABLE IF NOT EXISTS user_activities (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    user_xbox_id VARCHAR(100) NOT NULL,
    type ENUM('LIKE', 'FAVORITE', 'COMMENT', 'UPLOAD', 'JOIN_TEAM', 'PRO_APPLICATION') NOT NULL,
    target_id VARCHAR(36),
    target_name VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户点赞表
CREATE TABLE IF NOT EXISTS user_likes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    tune_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_tune_like (user_id, tune_id)
);

-- 用户收藏表
CREATE TABLE IF NOT EXISTS user_favorites (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    tune_id VARCHAR(36) NOT NULL,
    note TEXT, -- 收藏备注
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_tune_favorite (user_id, tune_id)
);

-- 评论点赞表
CREATE TABLE IF NOT EXISTS comment_likes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    comment_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_comment_like (user_id, comment_id)
);

-- 回复点赞表
CREATE TABLE IF NOT EXISTS reply_likes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    reply_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_reply_like (user_id, reply_id)
);

-- 系统设置表
CREATE TABLE IF NOT EXISTS system_settings (
    id VARCHAR(36) PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    description TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================================
-- 示例数据插入（用于测试游戏分类功能）
-- =====================================

-- 插入示例用户
INSERT IGNORE INTO users (id, xbox_id, email, password, is_pro_player, user_tier) VALUES
('user-001', 'ProTuner1', 'protuner1@example.com', '$2a$10$example.hash.1', TRUE, 'PRO'),
('user-002', 'SpeedMaster', 'speedmaster@example.com', '$2a$10$example.hash.2', FALSE, 'VERIFIED'),
('user-003', 'TuneExpert', 'tuneexpert@example.com', '$2a$10$example.hash.3', TRUE, 'PRO'),
('user-004', 'RacingFan', 'racingfan@example.com', '$2a$10$example.hash.4', FALSE, 'STANDARD');

-- 插入示例车辆 - FH5
INSERT IGNORE INTO cars (id, name, manufacturer, year, category, pi, drivetrain, game_category, image_url) VALUES
('1', '911 GT2 RS', 'Porsche', 2018, 'Supercars', 920, 'RWD', 'fh5', 'https://example.com/porsche-911-gt2-rs.jpg'),
('2', 'Senna', 'McLaren', 2019, 'Hypercars', 999, 'RWD', 'fh5', 'https://example.com/mclaren-senna.jpg'),
('3', 'Corvette C7 Z06', 'Chevrolet', 2015, 'Sports Cars', 875, 'RWD', 'fh5', 'https://example.com/corvette-c7.jpg'),
('4', 'Mustang RTR Spec 5', 'Ford', 2018, 'Muscle Cars', 850, 'RWD', 'fh5', 'https://example.com/mustang-rtr.jpg'),
('5', 'Huracán Performante', 'Lamborghini', 2018, 'Supercars', 920, 'AWD', 'fh5', 'https://example.com/huracan.jpg');

-- 插入示例车辆 - FH4  
INSERT IGNORE INTO cars (id, name, manufacturer, year, category, pi, drivetrain, game_category, image_url) VALUES
('6', '911 GT2 RS', 'Porsche', 2018, 'Supercars', 920, 'RWD', 'fh4', 'https://example.com/porsche-911-gt2-rs-fh4.jpg'),
('7', 'Senna', 'McLaren', 2019, 'Hypercars', 999, 'RWD', 'fh4', 'https://example.com/mclaren-senna-fh4.jpg'),
('8', 'RS6 Avant', 'Audi', 2020, 'Sports Cars', 825, 'AWD', 'fh4', 'https://example.com/audi-rs6.jpg'),
('9', 'M5 Competition', 'BMW', 2019, 'Sports Cars', 875, 'AWD', 'fh4', 'https://example.com/bmw-m5.jpg');

-- 插入示例调校 - FH5
INSERT IGNORE INTO tunes (id, car_id, author_id, author_xbox_id, share_code, preference, pi_class, final_pi, drivetrain, tire_compound, race_type, surface_conditions, description, is_pro_tune, like_count, game_category) VALUES
('tune-fh5-001', 'porsche-911-gt2-rs', 'user-001', 'ProTuner1', 'FH5-001-ABC', 'Power', 'S2', 920, 'RWD', 'Semi-Slick', 'Road', '["Dry"]', 'FH5专用保时捷GT2 RS暴力调校', TRUE, 156, 'fh5'),
('tune-fh5-002', 'mclaren-senna', 'user-002', 'SpeedMaster', 'FH5-002-DEF', 'Handling', 'X', 999, 'RWD', 'Slick', 'Road', '["Dry", "Wet"]', 'FH5迈凯伦Senna赛道调校', FALSE, 89, 'fh5'),
('tune-fh5-003', 'chevrolet-corvette-c7', 'user-003', 'TuneExpert', 'FH5-003-GHI', 'Balance', 'S1', 875, 'RWD', 'Sport', 'Road', '["Dry"]', 'FH5科尔维特平衡调校', TRUE, 234, 'fh5'),
('tune-fh5-004', 'ford-mustang-rtr', 'user-001', 'ProTuner1', 'FH5-004-JKL', 'Power', 'A', 850, 'RWD', 'Street', 'Road', '["Dry", "Wet"]', 'FH5野马街道调校', FALSE, 67, 'fh5'),
('tune-fh5-005', 'lamborghini-huracan', 'user-003', 'TuneExpert', 'FH5-005-MNO', 'Handling', 'S2', 920, 'AWD', 'Semi-Slick', 'Road', '["Dry"]', 'FH5兰博基尼操控调校', TRUE, 198, 'fh5');

-- 插入示例调校 - FH4
INSERT IGNORE INTO tunes (id, car_id, author_id, author_xbox_id, share_code, preference, pi_class, final_pi, drivetrain, tire_compound, race_type, surface_conditions, description, is_pro_tune, like_count, game_category) VALUES
('tune-fh4-001', 'porsche-911-gt2-rs', 'user-002', 'SpeedMaster', 'FH4-001-PQR', 'Power', 'S2', 920, 'RWD', 'Semi-Slick', 'Road', '["Dry"]', 'FH4保时捷GT2 RS英国调校', FALSE, 123, 'fh4'),
('tune-fh4-002', 'mclaren-senna', 'user-003', 'TuneExpert', 'FH4-002-STU', 'Handling', 'X', 999, 'RWD', 'Slick', 'Road', '["Dry", "Wet"]', 'FH4迈凯伦Senna英国赛道版', TRUE, 176, 'fh4'),
('tune-fh4-003', 'audi-rs6-avant', 'user-001', 'ProTuner1', 'FH4-003-VWX', 'Balance', 'S1', 825, 'AWD', 'Sport', 'Road', '["Dry", "Wet", "Snow"]', 'FH4奥迪RS6全天候调校', TRUE, 145, 'fh4'),
('tune-fh4-004', 'bmw-m5-competition', 'user-004', 'RacingFan', 'FH4-004-YZA', 'Power', 'S1', 875, 'AWD', 'Street', 'Road', '["Dry"]', 'FH4宝马M5街道版', FALSE, 98, 'fh4');

-- 插入一些用户活动记录
INSERT IGNORE INTO user_activities (id, user_id, user_xbox_id, type, target_id, target_name, description) VALUES
('activity-001', 'user-001', 'ProTuner1', 'UPLOAD', 'tune-fh5-001', 'FH5 保时捷GT2 RS调校', '上传了新的FH5调校'),
('activity-002', 'user-002', 'SpeedMaster', 'LIKE', 'tune-fh5-003', 'FH5 科尔维特调校', '点赞了调校'),
('activity-003', 'user-003', 'TuneExpert', 'UPLOAD', 'tune-fh4-002', 'FH4 迈凯伦Senna调校', '上传了新的FH4调校'),
('activity-004', 'user-004', 'RacingFan', 'FAVORITE', 'tune-fh5-005', 'FH5 兰博基尼调校', '收藏了调校'); 