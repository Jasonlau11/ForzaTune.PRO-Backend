-- 修复游戏分类枚举值 - 安全迁移脚本
-- 将数据库中的game_category枚举值更新为与前端一致

-- 步骤1: 检查现有数据
SELECT '=== 现有数据检查 ===' as step;
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;

-- 步骤2: 先扩展枚举值，包含新旧值
SELECT '=== 扩展枚举值 ===' as step;
ALTER TABLE cars MODIFY COLUMN game_category 
ENUM('fh4', 'fh5', 'forzahorizon4', 'forzahorizon5', 'forzamotorsport') 
COLLATE utf8mb4_unicode_ci NOT NULL;

-- 步骤3: 迁移现有数据
SELECT '=== 迁移数据 ===' as step;
UPDATE cars SET game_category = 'forzahorizon4' WHERE game_category = 'fh4';
UPDATE cars SET game_category = 'forzahorizon5' WHERE game_category = 'fh5';

-- 步骤4: 验证迁移结果
SELECT '=== 验证迁移结果 ===' as step;
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;

-- 步骤5: 移除旧的枚举值
SELECT '=== 移除旧枚举值 ===' as step;
ALTER TABLE cars MODIFY COLUMN game_category 
ENUM('forzahorizon4', 'forzahorizon5', 'forzamotorsport') 
COLLATE utf8mb4_unicode_ci NOT NULL;

-- 步骤6: 最终验证
SELECT '=== 最终验证 ===' as step;
SHOW COLUMNS FROM cars LIKE 'game_category';
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;
