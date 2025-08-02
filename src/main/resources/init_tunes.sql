-- 初始化调校数据
USE forzatune_pro;

-- 清空现有数据
DELETE FROM tunes;

-- 插入调校数据
INSERT INTO tunes (id, car_id, author_id, author_xbox_id, share_code, preference, pi_class, final_pi, drivetrain, tire_compound, race_type, surface_conditions, description, is_pro_tune, is_parameters_public, has_detailed_parameters, like_count, game_category, created_at) VALUES
-- Forza Horizon 5 调校
('tune1', 'car1', 'user1', 'ProDriver1', '123-456-789', 'Power', 'S1', 850, 'AWD', 'Sport', 'Road', '["Dry"]', '高性能调校，适合直线加速', true, true, true, 25, 'fh5', NOW()),
('tune2', 'car1', 'user2', 'HandlingMaster', '234-567-890', 'Handling', 'S1', 845, 'AWD', 'Semi-Slick', 'Road', '["Dry", "Wet"]', '操控优先调校，适合弯道', false, true, true, 18, 'fh5', NOW()),
('tune3', 'car2', 'user1', 'ProDriver1', '345-678-901', 'Balance', 'S2', 890, 'RWD', 'Sport', 'Road', '["Dry"]', '平衡调校，适合综合性能', true, false, false, 32, 'fh5', NOW()),
('tune4', 'car3', 'user3', 'HyperCarGuru', '456-789-012', 'Power', 'X', 999, 'AWD', 'Slick', 'Road', '["Dry"]', '极限性能调校', true, true, true, 45, 'fh5', NOW()),
('tune5', 'car4', 'user2', 'HandlingMaster', '567-890-123', 'Handling', 'S1', 880, 'RWD', 'Semi-Slick', 'Road', '["Dry"]', '赛道专用调校', false, true, true, 22, 'fh5', NOW()),
('tune6', 'car5', 'user4', 'MuscleCarFan', '678-901-234', 'Power', 'A', 820, 'RWD', 'Street', 'Road', '["Dry"]', '肌肉车调校', false, false, false, 15, 'fh5', NOW()),
('tune7', 'car6', 'user1', 'ProDriver1', '789-012-345', 'Balance', 'S2', 900, 'AWD', 'Sport', 'Road', '["Dry", "Wet"]', '全能调校', true, true, true, 28, 'fh5', NOW()),
('tune8', 'car7', 'user5', 'TrackToyExpert', '890-123-456', 'Handling', 'S1', 920, 'RWD', 'Semi-Slick', 'Road', '["Dry"]', '赛道玩具调校', false, true, true, 35, 'fh5', NOW()),

-- Forza Horizon 4 调校
('tune9', 'car1', 'user1', 'ProDriver1', '111-222-333', 'Power', 'S1', 860, 'AWD', 'Sport', 'Road', '["Dry"]', 'FH4高性能调校', true, true, true, 20, 'fh4', NOW()),
('tune10', 'car2', 'user2', 'HandlingMaster', '222-333-444', 'Handling', 'S1', 890, 'RWD', 'Semi-Slick', 'Road', '["Dry", "Wet"]', 'FH4操控调校', false, true, true, 16, 'fh4', NOW()),
('tune11', 'car3', 'user3', 'HyperCarGuru', '333-444-555', 'Power', 'X', 999, 'AWD', 'Slick', 'Road', '["Dry"]', 'FH4极限调校', true, true, true, 40, 'fh4', NOW()),
('tune12', 'car4', 'user2', 'HandlingMaster', '444-555-666', 'Balance', 'S1', 880, 'RWD', 'Sport', 'Road', '["Dry"]', 'FH4平衡调校', false, true, true, 19, 'fh4', NOW());

-- 验证数据插入
SELECT COUNT(*) as total_tunes FROM tunes;
SELECT game_category, COUNT(*) as count FROM tunes GROUP BY game_category;
SELECT car_id, COUNT(*) as tune_count FROM tunes GROUP BY car_id; 