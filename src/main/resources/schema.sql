-- forzatune_pro.cars definition

CREATE TABLE `cars` (
  `id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `manufacturer` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `year` int NOT NULL,
  `category` enum('Sports Cars','Muscle Cars','Supercars','Classic Cars','Hypercars','Track Toys') COLLATE utf8mb4_unicode_ci NOT NULL,
  `pi` int NOT NULL,
  `drivetrain` enum('RWD','FWD','AWD') COLLATE utf8mb4_unicode_ci NOT NULL,
  `game_category` enum('fh4','fh5','fm') COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`game_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.comment_likes definition

CREATE TABLE `comment_likes` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_comment_like` (`user_id`,`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.pro_applications definition

CREATE TABLE `pro_applications` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gamertag` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `experience` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `achievements` json DEFAULT NULL,
  `sample_tunes` json DEFAULT NULL,
  `status` enum('PENDING','APPROVED','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `submitted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reviewed_at` datetime DEFAULT NULL,
  `reviewed_by` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.pro_certifications definition

CREATE TABLE `pro_certifications` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('championship','world_record','achievement','expertise') COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `verified_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `verified_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.reply_likes definition

CREATE TABLE `reply_likes` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reply_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_reply_like` (`user_id`,`reply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.system_settings definition

CREATE TABLE `system_settings` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `setting_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `setting_value` text COLLATE utf8mb4_unicode_ci,
  `description` text COLLATE utf8mb4_unicode_ci,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `setting_key` (`setting_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.team_applications definition

CREATE TABLE `team_applications` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `team_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_gamertag` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci,
  `status` enum('PENDING','APPROVED','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `reviewed_by` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reviewed_at` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_user_application` (`team_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.team_invitations definition

CREATE TABLE `team_invitations` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `team_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `invited_by` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci,
  `status` enum('PENDING','ACCEPTED','DECLINED','EXPIRED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `expires_at` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_user_invitation` (`team_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.team_members definition

CREATE TABLE `team_members` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `team_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_gamertag` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('OWNER','ADMIN','MODERATOR','MEMBER') COLLATE utf8mb4_unicode_ci DEFAULT 'MEMBER',
  `permissions` json DEFAULT NULL,
  `joined_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total_contributions` int DEFAULT '0',
  `total_tunes` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_user` (`team_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.teams definition

CREATE TABLE `teams` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `founder_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `founder_gamertag` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_public` tinyint(1) DEFAULT '1',
  `tags` json DEFAULT NULL,
  `total_members` int DEFAULT '1',
  `total_tunes` int DEFAULT '0',
  `total_downloads` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.tunes definition

CREATE TABLE `tunes` (
  `id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `car_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `author_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `author_xbox_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `share_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `preference` enum('Power','Handling','Balance') COLLATE utf8mb4_unicode_ci NOT NULL,
  `pi_class` enum('X','S2','S1','A','B','C','D') COLLATE utf8mb4_unicode_ci NOT NULL,
  `final_pi` int NOT NULL,
  `drivetrain` enum('RWD','FWD','AWD') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tire_compound` enum('Stock','Street','Sport','Semi-Slick','Slick','Rally','Snow','Off-Road','Drag','Drift') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `race_type` enum('Road','Dirt','Cross Country') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `surface_conditions` json DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `is_pro_tune` tinyint(1) DEFAULT '0',
  `is_parameters_public` tinyint(1) DEFAULT '0',
  `has_detailed_parameters` tinyint(1) DEFAULT '0',
  `screenshot_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `like_count` int DEFAULT '0',
  `game_category` enum('fh4','fh5','fm') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parameters` json DEFAULT NULL,
  `owner_user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '归属者站内用户ID（若归属给站内用户）',
  `owner_xbox_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '归属Xbox ID（可为他人，未必在站内）',
  `ownership_status` enum('unverified','pending','verified') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'unverified' COMMENT '归属状态：未验证/审核中/已验证',
  `owner_verified_at` datetime DEFAULT NULL COMMENT '归属被验证或认领通过的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `share_code` (`share_code`),
  KEY `idx_tunes_owner_user_id` (`owner_user_id`),
  KEY `idx_tunes_owner_xbox_id` (`owner_xbox_id`),
  KEY `idx_tunes_ownership_status` (`ownership_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.user_activities definition

CREATE TABLE `user_activities` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_xbox_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('LIKE','FAVORITE','COMMENT','UPLOAD','JOIN_TEAM','PRO_APPLICATION') COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `notifications_sent` int DEFAULT '0' COMMENT '发送的通知数量',
  `notifications_received` int DEFAULT '0' COMMENT '收到的通知数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.user_favorites definition

CREATE TABLE `user_favorites` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tune_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_tune_favorite` (`user_id`,`tune_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.user_likes definition

CREATE TABLE `user_likes` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tune_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_tune_like` (`user_id`,`tune_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.users definition

CREATE TABLE `users` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `xbox_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_pro_player` tinyint(1) DEFAULT '0',
  `pro_player_since` timestamp NULL DEFAULT NULL,
  `total_tunes` int DEFAULT '0',
  `total_likes` int DEFAULT '0',
  `bio` text COLLATE utf8mb4_unicode_ci,
  `avatar_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_tier` enum('STANDARD','VERIFIED','PRO') COLLATE utf8mb4_unicode_ci DEFAULT 'STANDARD',
  `last_login` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `email_verified_at` datetime DEFAULT NULL COMMENT '邮箱验证通过时间',
  `xuid` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Xbox 用户XUID，人工核验后回填（可空）',
  `xbox_verification_status` enum('pending','approved','denied') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT 'Xbox ID站内验证状态',
  `xbox_evidence` json DEFAULT NULL COMMENT '注册或重新验证时提交的证据URL数组、备注等',
  `xbox_verified_at` datetime DEFAULT NULL COMMENT 'Xbox ID站内通过时间',
  `xbox_denied_reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拒绝原因，供前端展示',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `xbox_id` (`xbox_id`),
  UNIQUE KEY `xuid` (`xuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.comment_replies definition

CREATE TABLE `comment_replies` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_xbox_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reply_to_user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '回复的目标用户ID',
  `reply_to_xbox_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '回复的目标用户Xbox ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `like_count` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_tune_replies_reply_to` (`reply_to_user_id`),
  CONSTRAINT `fk_reply_reply_to_user` FOREIGN KEY (`reply_to_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.notifications definition

CREATE TABLE `notifications` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT (uuid()),
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接收通知的用户ID',
  `type` enum('tune_like','tune_favorite','tune_comment','comment_reply') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知类型',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '通知内容',
  `related_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '相关对象ID（调校ID、评论ID等）',
  `sender_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送通知的用户ID',
  `sender_xbox_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送者Xbox ID',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_created_at` (`created_at`),
  KEY `sender_id` (`sender_id`),
  KEY `idx_notifications_user_read_time` (`user_id`,`is_read`,`created_at` DESC),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户通知表';


-- forzatune_pro.tune_claims definition

CREATE TABLE `tune_claims` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tune_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `claimer_user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `claimer_xbox_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `evidence` json NOT NULL COMMENT '证据URL数组、备注文本等',
  `status` enum('pending','approved','rejected','cancelled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `decision_text` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `decided_by` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `decided_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_claims_tune` (`tune_id`),
  KEY `idx_claims_status` (`status`),
  KEY `idx_claims_claimer` (`claimer_user_id`),
  CONSTRAINT `fk_claim_tune` FOREIGN KEY (`tune_id`) REFERENCES `tunes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- forzatune_pro.tune_comments definition

CREATE TABLE `tune_comments` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tune_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_xbox_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reply_to_user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '回复的目标用户ID',
  `reply_to_xbox_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '回复的目标用户Xbox ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `rating` int DEFAULT NULL,
  `like_count` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_tune_comments_reply_to` (`reply_to_user_id`),
  CONSTRAINT `fk_reply_to_user` FOREIGN KEY (`reply_to_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  CONSTRAINT `tune_comments_chk_1` CHECK (((`rating` >= 1) and (`rating` <= 5)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;