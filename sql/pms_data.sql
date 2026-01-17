-- 类目数据
INSERT INTO pms_category (name, parent_id, level, description) VALUES ('电子产品', 0, 1, '电子产品类目');
INSERT INTO pms_category (name, parent_id, level, description) VALUES ('手机', 1, 2, '手机类目');
INSERT INTO pms_category (name, parent_id, level, description) VALUES ('电脑', 1, 2, '电脑类目');

-- 规格类型数据
INSERT INTO pms_spec_type (name, description) VALUES ('颜色', '商品颜色规格');
INSERT INTO pms_spec_type (name, description) VALUES ('尺寸', '商品尺寸规格');

-- 规格值数据
INSERT INTO pms_spec_value (spec_type_id, value, description) VALUES (1, '黑色', '黑色');
INSERT INTO pms_spec_value (spec_type_id, value, description) VALUES (1, '白色', '白色');
INSERT INTO pms_spec_value (spec_type_id, value, description) VALUES (2, '6.1英寸', '6.1英寸');
INSERT INTO pms_spec_value (spec_type_id, value, description) VALUES (2, '6.7英寸', '6.7英寸');

-- SPU数据
INSERT INTO pms_spu (name, brand, category_id, detail, pic) VALUES ('iPhone 15', 'Apple', 2, '最新款苹果手机', '/images/iphone15.jpg');
INSERT INTO pms_spu (name, brand, category_id, detail, pic) VALUES ('MacBook Pro', 'Apple', 3, '苹果笔记本电脑', '/images/macbook.jpg');

-- SKU数据
INSERT INTO pms_sku (code, spu_id, price, stock, specs, pic) VALUES ('IPH15-BLK-61', 1, 5999.00, 100, '{"颜色":"黑色","尺寸":"6.1英寸"}', '/images/iphone15-black.jpg');
INSERT INTO pms_sku (code, spu_id, price, stock, specs, pic) VALUES ('IPH15-WHT-61', 1, 5999.00, 50, '{"颜色":"白色","尺寸":"6.1英寸"}', '/images/iphone15-white.jpg');
INSERT INTO pms_sku (code, spu_id, price, stock, specs, pic) VALUES ('IPH15-BLK-67', 1, 6999.00, 80, '{"颜色":"黑色","尺寸":"6.7英寸"}', '/images/iphone15-black-67.jpg');
INSERT INTO pms_sku (code, spu_id, price, stock, specs, pic) VALUES ('MAC-PRO-M1', 2, 9999.00, 30, '{"颜色":"银色"}', '/images/macbook-silver.jpg');