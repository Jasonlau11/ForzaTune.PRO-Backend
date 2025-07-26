-- 创建数据库
CREATE DATABASE IF NOT EXISTS forzatune_pro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE forzatune_pro;

-- 用户表 - 增强唯一性约束
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    gamertag VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    xbox_id VARCHAR(100) UNIQUE,
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
    -- 添加复合唯一索引，确保软删除后不会影响唯一性
    UNIQUE KEY uk_email_active (email, is_active),
    UNIQUE KEY uk_gamertag_active (gamertag, is_active),
    UNIQUE KEY uk_xbox_id_active (xbox_id, is_active),
    INDEX idx_gamertag (gamertag),
    INDEX idx_email (email),
    INDEX idx_pro_player (is_pro_player)
);

-- 车辆表
CREATE TABLE IF NOT EXISTS cars (
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

-- 调校表
CREATE TABLE IF NOT EXISTS tunes (
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

-- 调校参数表
CREATE TABLE IF NOT EXISTS tune_parameters (
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

-- 赛道表
CREATE TABLE IF NOT EXISTS tracks (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    game_id VARCHAR(20) NOT NULL,
    category ENUM('Circuit', 'Sprint', 'Drift', 'Drag', 'Rally') NOT NULL,
    length DECIMAL(5,2), -- 赛道长度（公里）
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_game_category (game_id, category)
);

-- 圈速记录表
CREATE TABLE IF NOT EXISTS lap_times (
    id VARCHAR(36) PRIMARY KEY,
    tune_id VARCHAR(36) NOT NULL,
    track_id VARCHAR(36) NOT NULL,
    time VARCHAR(20) NOT NULL, -- 格式: "1:55.234"
    pro_player_id VARCHAR(36),
    video_url VARCHAR(500),
    is_verified BOOLEAN DEFAULT FALSE,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    FOREIGN KEY (track_id) REFERENCES tracks(id),
    FOREIGN KEY (pro_player_id) REFERENCES users(id),
    INDEX idx_tune_track (tune_id, track_id),
    INDEX idx_pro_player (pro_player_id),
    INDEX idx_verified (is_verified)
);

-- 调校评论表
CREATE TABLE IF NOT EXISTS tune_comments (
    id VARCHAR(36) PRIMARY KEY,
    tune_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_gamertag VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_tune_created (tune_id, created_at),
    INDEX idx_user (user_id)
);

-- 评论回复表
CREATE TABLE IF NOT EXISTS comment_replies (
    id VARCHAR(36) PRIMARY KEY,
    comment_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_gamertag VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES tune_comments(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_comment_created (comment_id, created_at),
    INDEX idx_user (user_id)
);

-- 车队表
CREATE TABLE IF NOT EXISTS teams (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    founder_id VARCHAR(36) NOT NULL,
    founder_gamertag VARCHAR(100) NOT NULL,
    is_public BOOLEAN DEFAULT TRUE,
    tags JSON,
    total_members INT DEFAULT 1,
    total_tunes INT DEFAULT 0,
    total_downloads INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (founder_id) REFERENCES users(id)
);

-- 车队成员表
CREATE TABLE IF NOT EXISTS team_members (
    id VARCHAR(36) PRIMARY KEY,
    team_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_gamertag VARCHAR(100) NOT NULL,
    role ENUM('OWNER', 'ADMIN', 'MODERATOR', 'MEMBER') DEFAULT 'MEMBER',
    permissions JSON,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_contributions INT DEFAULT 0,
    total_tunes INT DEFAULT 0,
    UNIQUE KEY uk_team_user (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_team_members_team_id (team_id),
    INDEX idx_team_members_user_id (user_id)
);

-- 车队申请表
CREATE TABLE IF NOT EXISTS team_applications (
    id VARCHAR(36) PRIMARY KEY,
    team_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_gamertag VARCHAR(100) NOT NULL,
    message TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by VARCHAR(36),
    reviewed_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_team_user_application (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (reviewed_by) REFERENCES users(id)
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
    UNIQUE KEY uk_team_user_invitation (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (invited_by) REFERENCES users(id)
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_type (user_id, type)
);

-- PRO申请表
CREATE TABLE IF NOT EXISTS pro_applications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    gamertag VARCHAR(100) NOT NULL,
    experience TEXT NOT NULL,
    achievements JSON,
    sample_tunes JSON,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at DATETIME,
    reviewed_by VARCHAR(36),
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(id)
);

-- 用户活动表
CREATE TABLE IF NOT EXISTS user_activities (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    user_gamertag VARCHAR(100) NOT NULL,
    type ENUM('LIKE', 'FAVORITE', 'COMMENT', 'UPLOAD', 'JOIN_TEAM', 'PRO_APPLICATION') NOT NULL,
    target_id VARCHAR(36),
    target_name VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_created (user_id, created_at),
    INDEX idx_activity_type (type)
);

-- 用户点赞表
CREATE TABLE IF NOT EXISTS user_likes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    tune_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_tune_like (user_id, tune_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_tune (tune_id)
);

-- 用户收藏表
CREATE TABLE IF NOT EXISTS user_favorites (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    tune_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_tune_favorite (user_id, tune_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tune_id) REFERENCES tunes(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_tune (tune_id)
);

-- 评论点赞表
CREATE TABLE IF NOT EXISTS comment_likes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    comment_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_comment_like (user_id, comment_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES tune_comments(id) ON DELETE CASCADE
);

-- 回复点赞表
CREATE TABLE IF NOT EXISTS reply_likes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    reply_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_reply_like (user_id, reply_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reply_id) REFERENCES comment_replies(id) ON DELETE CASCADE
);

-- 系统设置表
CREATE TABLE IF NOT EXISTS system_settings (
    id VARCHAR(36) PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    description TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_setting_key (setting_key)
); 