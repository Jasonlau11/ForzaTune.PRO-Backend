-- 检查现有数据
-- 查看cars表中现有的game_category值
SELECT DISTINCT game_category, COUNT(*) as count 
FROM cars 
GROUP BY game_category;

-- 查看具体的数据样例
SELECT id, name, manufacturer, game_category 
FROM cars 
LIMIT 10;
