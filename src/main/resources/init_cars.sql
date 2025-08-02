-- 初始化车辆数据
USE forzatune_pro;

-- 清空现有数据
DELETE FROM cars;

-- 插入车辆数据
INSERT INTO cars (id, name, manufacturer, year, category, pi, drivetrain, game_category, image_url) VALUES
-- Forza Horizon 5 车辆
('car1', 'RS6 Avant', 'Audi', 2020, 'Sports Cars', 860, 'AWD', 'fh5', '/images/audi-rs6.jpg'),
('car2', 'M4 Competition', 'BMW', 2021, 'Sports Cars', 890, 'RWD', 'fh5', '/images/bmw-m4.jpg'),
('car3', 'Chiron', 'Bugatti', 2019, 'Hypercars', 999, 'AWD', 'fh5', '/images/bugatti-chiron.jpg'),
('car4', 'Corvette C8', 'Chevrolet', 2020, 'Supercars', 880, 'RWD', 'fh5', '/images/chevrolet-corvette.jpg'),
('car5', 'Mustang GT', 'Ford', 2021, 'Muscle Cars', 820, 'RWD', 'fh5', '/images/ford-mustang.jpg'),
('car6', 'GT-R', 'Nissan', 2020, 'Supercars', 900, 'AWD', 'fh5', '/images/nissan-gtr.jpg'),
('car7', '911 GT3 RS', 'Porsche', 2021, 'Track Toys', 920, 'RWD', 'fh5', '/images/porsche-911.jpg'),
('car8', 'Huracán', 'Lamborghini', 2020, 'Supercars', 910, 'AWD', 'fh5', '/images/lamborghini-huracan.jpg'),
('car9', 'Aventador SVJ', 'Lamborghini', 2019, 'Hypercars', 950, 'AWD', 'fh5', '/images/lamborghini-aventador.jpg'),
('car10', 'F8 Tributo', 'Ferrari', 2020, 'Supercars', 930, 'RWD', 'fh5', '/images/ferrari-f8.jpg'),
('car11', '720S', 'McLaren', 2019, 'Supercars', 940, 'RWD', 'fh5', '/images/mclaren-720s.jpg'),
('car12', 'Vulcan', 'Aston Martin', 2016, 'Track Toys', 925, 'RWD', 'fh5', '/images/aston-martin-vulcan.jpg'),

-- Forza Horizon 4 车辆
('car1', 'RS6 Avant', 'Audi', 2020, 'Sports Cars', 860, 'AWD', 'fh4', '/images/audi-rs6.jpg'),
('car2', 'M4 Competition', 'BMW', 2021, 'Sports Cars', 890, 'RWD', 'fh4', '/images/bmw-m4.jpg'),
('car3', 'Chiron', 'Bugatti', 2019, 'Hypercars', 999, 'AWD', 'fh4', '/images/bugatti-chiron.jpg'),
('car4', 'Corvette C8', 'Chevrolet', 2020, 'Supercars', 880, 'RWD', 'fh4', '/images/chevrolet-corvette.jpg'),
('car5', 'Mustang GT', 'Ford', 2021, 'Muscle Cars', 820, 'RWD', 'fh4', '/images/ford-mustang.jpg'),
('car6', 'GT-R', 'Nissan', 2020, 'Supercars', 900, 'AWD', 'fh4', '/images/nissan-gtr.jpg'),
('car7', '911 GT3 RS', 'Porsche', 2021, 'Track Toys', 920, 'RWD', 'fh4', '/images/porsche-911.jpg'),
('car8', 'Huracán', 'Lamborghini', 2020, 'Supercars', 910, 'AWD', 'fh4', '/images/lamborghini-huracan.jpg');

-- 验证数据插入
SELECT COUNT(*) as total_cars FROM cars;
SELECT game_category, COUNT(*) as count FROM cars GROUP BY game_category; 