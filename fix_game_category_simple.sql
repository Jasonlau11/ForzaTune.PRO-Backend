-- 简单的游戏分类枚举值修复方案
-- 使用临时列的方式避免数据截断

-- 方案1: 添加临时列
ALTER TABLE cars ADD COLUMN game_category_new 
ENUM('forzahorizon4', 'forzahorizon5', 'forzamotorsport') 
COLLATE utf8mb4_unicode_ci;

-- 方案2: 迁移数据到新列
UPDATE cars SET game_category_new = 'forzahorizon4' WHERE game_category = 'fh4';
UPDATE cars SET game_category_new = 'forzahorizon5' WHERE game_category = 'fh5';

-- 方案3: 删除旧列
ALTER TABLE cars DROP COLUMN game_category;

-- 方案4: 重命名新列
ALTER TABLE cars CHANGE COLUMN game_category_new game_category 
ENUM('forzahorizon4', 'forzahorizon5', 'forzamotorsport') 
COLLATE utf8mb4_unicode_ci NOT NULL;

-- 验证结果
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;
