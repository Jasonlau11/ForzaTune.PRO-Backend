-- 回退游戏分类枚举值到旧标准
-- 将数据库中的game_category枚举值回退为短格式

-- 步骤1: 检查现有数据
SELECT '=== 现有数据检查 ===' as step;
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;

-- 步骤2: 先扩展枚举值，包含新旧值
SELECT '=== 扩展枚举值 ===' as step;
ALTER TABLE cars MODIFY COLUMN game_category 
ENUM('fh4', 'fh5', 'fm', 'forzahorizon4', 'forzahorizon5', 'forzamotorsport') 
COLLATE utf8mb4_unicode_ci NOT NULL;

-- 步骤3: 迁移现有数据回到旧格式
SELECT '=== 迁移数据 ===' as step;
UPDATE cars SET game_category = 'fh4' WHERE game_category = 'forzahorizon4';
UPDATE cars SET game_category = 'fh5' WHERE game_category = 'forzahorizon5';
UPDATE cars SET game_category = 'fm' WHERE game_category = 'forzamotorsport';

-- 步骤4: 验证迁移结果
SELECT '=== 验证迁移结果 ===' as step;
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;

-- 步骤5: 移除新的枚举值，保留旧格式
SELECT '=== 移除新枚举值 ===' as step;
ALTER TABLE cars MODIFY COLUMN game_category 
ENUM('fh4', 'fh5', 'fm') 
COLLATE utf8mb4_unicode_ci NOT NULL;

-- 步骤6: 同时更新调校表（如果存在相关数据）
UPDATE tunes SET game_category = 'fh4' WHERE game_category = 'forzahorizon4';
UPDATE tunes SET game_category = 'fh5' WHERE game_category = 'forzahorizon5';
UPDATE tunes SET game_category = 'fm' WHERE game_category = 'forzamotorsport';

-- 步骤7: 更新调校表枚举（如果调校表也有这个字段）
-- ALTER TABLE tunes MODIFY COLUMN game_category 
-- ENUM('fh4', 'fh5', 'fm') 
-- COLLATE utf8mb4_unicode_ci NOT NULL;

-- 步骤8: 最终验证
SELECT '=== 最终验证 ===' as step;
SHOW COLUMNS FROM cars LIKE 'game_category';
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;

-- 验证调校表（如果有）
-- SELECT DISTINCT game_category, COUNT(*) as count 
-- FROM tunes 
-- GROUP BY game_category;
