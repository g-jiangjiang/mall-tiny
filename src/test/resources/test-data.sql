-- 测试数据SQL脚本
-- 商品配置模块测试数据

-- 类目测试数据
INSERT INTO pms_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description, create_time, update_time) VALUES
(1, 0, '电子产品', 0, 1000, '件', 1, 1, 1, 'electronic.png', '电子,数码,3C', '电子产品类目', NOW(), NOW()),
(2, 1, '手机', 1, 500, '台', 1, 1, 1, 'phone.png', '手机,智能手机', '手机类目', NOW(), NOW()),
(3, 1, '电脑', 1, 300, '台', 1, 1, 2, 'computer.png', '电脑,笔记本', '电脑类目', NOW(), NOW()),
(4, 1, '平板', 1, 200, '台', 1, 1, 3, 'tablet.png', '平板,ipad', '平板类目', NOW(), NOW());

-- SPU测试数据
INSERT INTO pms_spu (id, brand_id, category_id, name, pic, product_sn, publish_status, new_status, recommand_status, verify_status, sort, sale, price, promotion_price, gift_growth, gift_point, use_point_limit, sub_title, description, original_price, stock, low_stock, unit, weight, preview_status, service_ids, keywords, note, album_pics, detail_title, detail_desc, detail_html, detail_mobile_html, promotion_start_time, promotion_end_time, promotion_per_limit, promotion_type, brand_name, product_category_name, brand_logo, create_time, update_time) VALUES
(1, 1, 2, 'iPhone 15 Pro', 'iphone15.jpg', 'IP15PRO', 1, 1, 1, 1, 1, 5000, 8999.00, 7999.00, 100, 50, 1000, '最新款苹果手机', '苹果年度旗舰手机', 9999.00, 1000, 50, '台', 0.21, 1, '1,2,3', 'iphone,apple,phone', '高端机型', 'pic1.jpg,pic2.jpg', 'iPhone 15 Pro 详细介绍', '采用最新A17 Pro芯片', '<h1>iPhone 15 Pro</h1>', '<h1>iPhone 15 Pro</h1>', NULL, NULL, 0, 0, '苹果', '手机', 'apple_logo.png', NOW(), NOW()),
(2, 1, 2, 'iPhone 15 Pro Max', 'iphone15max.jpg', 'IP15PROMAX', 1, 1, 1, 1, 2, 3000, 9999.00, 8999.00, 150, 80, 1500, '最新款苹果大屏手机', '苹果年度旗舰大屏手机', 10999.00, 800, 30, '台', 0.25, 1, '1,2,3', 'iphone,apple,phone,max', '大屏高端机型', 'pic1.jpg,pic2.jpg', 'iPhone 15 Pro Max 详细介绍', '采用最新A17 Pro芯片，大屏体验', '<h1>iPhone 15 Pro Max</h1>', '<h1>iPhone 15 Pro Max</h1>', NULL, NULL, 0, 0, '苹果', '手机', 'apple_logo.png', NOW(), NOW());

-- SKU测试数据
INSERT INTO pms_sku (id, product_id, sku_code, price, stock, low_stock, sp1, sp2, sp3, pic, sale, promotion_price, lock_stock, sp_data, create_time, update_time) VALUES
(1, 1, 'IP15PRO-RED-256', 8999.00, 500, 50, '红色', '256GB', '中国大陆', 'sku_red.jpg', 1000, 8499.00, 10, '{"color":"红色","storage":"256GB"}', NOW(), NOW()),
(2, 1, 'IP15PRO-BLUE-256', 8999.00, 400, 50, '蓝色', '256GB', '中国大陆', 'sku_blue.jpg', 800, 8499.00, 8, '{"color":"蓝色","storage":"256GB"}', NOW(), NOW()),
(3, 1, 'IP15PRO-RED-512', 10999.00, 300, 30, '红色', '512GB', '中国大陆', 'sku_red.jpg', 600, 10499.00, 5, '{"color":"红色","storage":"512GB"}', NOW(), NOW()),
(4, 2, 'IP15MAX-BLACK-256', 9999.00, 400, 40, '黑色', '256GB', '中国大陆', 'sku_black.jpg', 700, 9499.00, 7, '{"color":"黑色","storage":"256GB"}', NOW(), NOW());

-- 规格类型测试数据
INSERT INTO pms_spec_type (id, category_id, name, sort, create_time, update_time) VALUES
(1, 2, '颜色', 1, NOW(), NOW()),
(2, 2, '存储容量', 2, NOW(), NOW()),
(3, 2, '版本', 3, NOW(), NOW()),
(4, 3, '颜色', 1, NOW(), NOW()),
(5, 3, '内存容量', 2, NOW(), NOW());

-- 规格值测试数据
INSERT INTO pms_spec_value (id, spec_type_id, value, sort, create_time, update_time) VALUES
(1, 1, '红色', 1, NOW(), NOW()),
(2, 1, '蓝色', 2, NOW(), NOW()),
(3, 1, '黑色', 3, NOW(), NOW()),
(4, 1, '白色', 4, NOW(), NOW()),
(5, 2, '128GB', 1, NOW(), NOW()),
(6, 2, '256GB', 2, NOW(), NOW()),
(7, 2, '512GB', 3, NOW(), NOW()),
(8, 2, '1TB', 4, NOW(), NOW()),
(9, 3, '中国大陆', 1, NOW(), NOW()),
(10, 3, '中国香港', 2, NOW(), NOW());
