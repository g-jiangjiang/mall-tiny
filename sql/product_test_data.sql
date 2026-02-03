-- 商品模块测试数据

-- 品牌数据
INSERT INTO `pms_brand` (`name`, `logo`, `description`, `sort`, `status`, `create_time`, `update_time`) VALUES
('Apple', 'https://example.com/apple.png', '苹果公司', 100, 1, NOW(), NOW()),
('华为', 'https://example.com/huawei.png', '华为技术有限公司', 90, 1, NOW(), NOW()),
('小米', 'https://example.com/xiaomi.png', '小米科技有限责任公司', 80, 1, NOW(), NOW()),
('Nike', 'https://example.com/nike.png', '耐克', 70, 1, NOW(), NOW()),
('Adidas', 'https://example.com/adidas.png', '阿迪达斯', 60, 1, NOW(), NOW()),
('优衣库', 'https://example.com/uniqlo.png', '优衣库', 50, 1, NOW(), NOW()),
('Zara', 'https://example.com/zara.png', '飒拉', 40, 1, NOW(), NOW()),
('H&M', 'https://example.com/hm.png', 'H&M', 30, 1, NOW(), NOW());

-- 类目数据（三级类目树）
-- 一级类目
INSERT INTO `pms_category` (`parent_id`, `name`, `level`, `sort`, `icon`, `description`, `status`, `create_time`, `update_time`) VALUES
(0, '数码家电', 1, 1, 'icon-digital', '数码产品、家用电器', 1, NOW(), NOW()),
(0, '服装服饰', 1, 2, 'icon-clothing', '男装、女装、童装', 1, NOW(), NOW()),
(0, '食品生鲜', 1, 3, 'icon-food', '食品饮料、生鲜蔬果', 1, NOW(), NOW()),
(0, '美妆个护', 1, 4, 'icon-beauty', '美妆护肤、个人护理', 1, NOW(), NOW());

-- 二级类目（数码家电）
INSERT INTO `pms_category` (`parent_id`, `name`, `level`, `sort`, `icon`, `description`, `status`, `create_time`, `update_time`) VALUES
(1, '手机通讯', 2, 1, 'icon-phone', '智能手机、功能手机', 1, NOW(), NOW()),
(1, '电脑办公', 2, 2, 'icon-computer', '笔记本、台式机、办公设备', 1, NOW(), NOW()),
(1, '摄影摄像', 2, 3, 'icon-camera', '数码相机、摄像机', 1, NOW(), NOW()),
(1, '家用电器', 2, 4, 'icon-appliance', '大家电、生活电器', 1, NOW(), NOW());

-- 二级类目（服装服饰）
INSERT INTO `pms_category` (`parent_id`, `name`, `level`, `sort`, `icon`, `description`, `status`, `create_time`, `update_time`) VALUES
(2, '男装', 2, 1, 'icon-men', '男士服装', 1, NOW(), NOW()),
(2, '女装', 2, 2, 'icon-women', '女士服装', 1, NOW(), NOW()),
(2, '童装', 2, 3, 'icon-kids', '儿童服装', 1, NOW(), NOW()),
(2, '运动户外', 2, 4, 'icon-sports', '运动服装、户外装备', 1, NOW(), NOW());

-- 三级类目（手机通讯）
INSERT INTO `pms_category` (`parent_id`, `name`, `level`, `sort`, `icon`, `description`, `status`, `create_time`, `update_time`) VALUES
(5, '智能手机', 3, 1, 'icon-smartphone', '智能手机', 1, NOW(), NOW()),
(5, '手机配件', 3, 2, 'icon-accessories', '充电器、耳机、保护壳', 1, NOW(), NOW()),
(5, '对讲机', 3, 3, 'icon-walkie-talkie', '对讲机', 1, NOW(), NOW());

-- 三级类目（男装）
INSERT INTO `pms_category` (`parent_id`, `name`, `level`, `sort`, `icon`, `description`, `status`, `create_time`, `update_time`) VALUES
(9, 'T恤', 3, 1, 'icon-tshirt', '男士T恤', 1, NOW(), NOW()),
(9, '衬衫', 3, 2, 'icon-shirt', '男士衬衫', 1, NOW(), NOW()),
(9, '外套', 3, 3, 'icon-jacket', '男士外套', 1, NOW(), NOW()),
(9, '裤子', 3, 4, 'icon-pants', '男士裤子', 1, NOW(), NOW());

-- 规格属性数据
INSERT INTO `pms_attribute` (`name`, `type`, `input_type`, `select_list`, `sort`, `status`, `create_time`, `update_time`) VALUES
('颜色', 0, 1, '红色,蓝色,黑色,白色,金色,银色', 100, 1, NOW(), NOW()),
('尺码', 0, 1, 'S,M,L,XL,XXL,XXXL', 90, 1, NOW(), NOW()),
('内存', 0, 1, '64GB,128GB,256GB,512GB,1TB', 80, 1, NOW(), NOW()),
('版本', 0, 1, '国行,港版,美版,日版', 70, 1, NOW(), NOW()),
('材质', 0, 1, '棉,涤纶,羊毛,真丝,麻', 60, 1, NOW(), NOW()),
('适用季节', 1, 1, '春季,夏季,秋季,冬季,四季通用', 50, 1, NOW(), NOW()),
('产地', 1, 0, NULL, 40, 1, NOW(), NOW()),
('保修期', 1, 1, '1年,2年,3年,5年', 30, 1, NOW(), NOW());

-- 规格属性值数据
-- 颜色
INSERT INTO `pms_attribute_value` (`attribute_id`, `value`, `sort`, `status`, `create_time`) VALUES
(1, '红色', 1, 1, NOW()),
(1, '蓝色', 2, 1, NOW()),
(1, '黑色', 3, 1, NOW()),
(1, '白色', 4, 1, NOW()),
(1, '金色', 5, 1, NOW()),
(1, '银色', 6, 1, NOW());

-- 尺码
INSERT INTO `pms_attribute_value` (`attribute_id`, `value`, `sort`, `status`, `create_time`) VALUES
(2, 'S', 1, 1, NOW()),
(2, 'M', 2, 1, NOW()),
(2, 'L', 3, 1, NOW()),
(2, 'XL', 4, 1, NOW()),
(2, 'XXL', 5, 1, NOW()),
(2, 'XXXL', 6, 1, NOW());

-- 内存
INSERT INTO `pms_attribute_value` (`attribute_id`, `value`, `sort`, `status`, `create_time`) VALUES
(3, '64GB', 1, 1, NOW()),
(3, '128GB', 2, 1, NOW()),
(3, '256GB', 3, 1, NOW()),
(3, '512GB', 4, 1, NOW()),
(3, '1TB', 5, 1, NOW());

-- SPU数据（iPhone 15）
INSERT INTO `pms_spu` (`name`, `brand_id`, `category_id`, `detail`, `main_image`, `sub_images`, `status`, `create_time`, `update_time`) VALUES
('iPhone 15 Pro Max', 1, 13, '<p>iPhone 15 Pro Max 是苹果公司推出的旗舰手机，搭载 A17 Pro 芯片，支持 USB-C 接口。</p>', 
'https://example.com/iphone15-main.jpg', 
'https://example.com/iphone15-1.jpg,https://example.com/iphone15-2.jpg,https://example.com/iphone15-3.jpg', 
1, NOW(), NOW());

-- SPU规格关联（iPhone 15）
INSERT INTO `pms_spu_attribute` (`spu_id`, `attribute_id`) VALUES
(1, 3), -- 内存
(1, 4); -- 版本

-- SKU数据（iPhone 15）
INSERT INTO `pms_sku` (`spu_id`, `sku_code`, `price`, `stock`, `specifications`, `main_image`, `status`, `create_time`, `update_time`) VALUES
(1, 'IP15PM-256-国行', 9999.00, 100, '{"内存":"256GB","版本":"国行"}', 'https://example.com/iphone15-256.jpg', 1, NOW(), NOW()),
(1, 'IP15PM-512-国行', 11999.00, 80, '{"内存":"512GB","版本":"国行"}', 'https://example.com/iphone15-512.jpg', 1, NOW(), NOW()),
(1, 'IP15PM-1TB-国行', 13999.00, 50, '{"内存":"1TB","版本":"国行"}', 'https://example.com/iphone15-1tb.jpg', 1, NOW(), NOW()),
(1, 'IP15PM-256-港版', 8999.00, 60, '{"内存":"256GB","版本":"港版"}', 'https://example.com/iphone15-256.jpg', 1, NOW(), NOW());

-- SPU数据（男士T恤）
INSERT INTO `pms_spu` (`name`, `brand_id`, `category_id`, `detail`, `main_image`, `sub_images`, `status`, `create_time`, `update_time`) VALUES
('纯棉短袖T恤', 6, 14, '<p>100%纯棉材质，舒适透气，多色可选。</p>', 
'https://example.com/tshirt-main.jpg', 
'https://example.com/tshirt-1.jpg,https://example.com/tshirt-2.jpg', 
1, NOW(), NOW());

-- SPU规格关联（T恤）
INSERT INTO `pms_spu_attribute` (`spu_id`, `attribute_id`) VALUES
(2, 1), -- 颜色
(2, 2); -- 尺码

-- SKU数据（T恤）
INSERT INTO `pms_sku` (`spu_id`, `sku_code`, `price`, `stock`, `specifications`, `main_image`, `status`, `create_time`, `update_time`) VALUES
(2, 'TSHIRT-RED-S', 99.00, 200, '{"颜色":"红色","尺码":"S"}', 'https://example.com/tshirt-red.jpg', 1, NOW(), NOW()),
(2, 'TSHIRT-RED-M', 99.00, 300, '{"颜色":"红色","尺码":"M"}', 'https://example.com/tshirt-red.jpg', 1, NOW(), NOW()),
(2, 'TSHIRT-RED-L', 99.00, 250, '{"颜色":"红色","尺码":"L"}', 'https://example.com/tshirt-red.jpg', 1, NOW(), NOW()),
(2, 'TSHIRT-BLUE-M', 99.00, 200, '{"颜色":"蓝色","尺码":"M"}', 'https://example.com/tshirt-blue.jpg', 1, NOW(), NOW()),
(2, 'TSHIRT-BLUE-L', 99.00, 200, '{"颜色":"蓝色","尺码":"L"}', 'https://example.com/tshirt-blue.jpg', 1, NOW(), NOW()),
(2, 'TSHIRT-BLACK-M', 99.00, 150, '{"颜色":"黑色","尺码":"M"}', 'https://example.com/tshirt-black.jpg', 1, NOW(), NOW()),
(2, 'TSHIRT-BLACK-L', 99.00, 150, '{"颜色":"黑色","尺码":"L"}', 'https://example.com/tshirt-black.jpg', 1, NOW(), NOW()),
(2, 'TSHIRT-BLACK-XL', 99.00, 100, '{"颜色":"黑色","尺码":"XL"}', 'https://example.com/tshirt-black.jpg', 1, NOW(), NOW());

-- SPU数据（华为Mate 60）
INSERT INTO `pms_spu` (`name`, `brand_id`, `category_id`, `detail`, `main_image`, `sub_images`, `status`, `create_time`, `update_time`) VALUES
('华为 Mate 60 Pro', 2, 13, '<p>华为 Mate 60 Pro，支持卫星通话，超可靠玄武架构。</p>', 
'https://example.com/mate60-main.jpg', 
'https://example.com/mate60-1.jpg,https://example.com/mate60-2.jpg', 
1, NOW(), NOW());

-- SPU规格关联（Mate 60）
INSERT INTO `pms_spu_attribute` (`spu_id`, `attribute_id`) VALUES
(3, 3), -- 内存
(3, 4); -- 版本

-- SKU数据（Mate 60）
INSERT INTO `pms_sku` (`spu_id`, `sku_code`, `price`, `stock`, `specifications`, `main_image`, `status`, `create_time`, `update_time`) VALUES
(3, 'MATE60-256-国行', 6999.00, 150, '{"内存":"256GB","版本":"国行"}', 'https://example.com/mate60-256.jpg', 1, NOW(), NOW()),
(3, 'MATE60-512-国行', 7999.00, 100, '{"内存":"512GB","版本":"国行"}', 'https://example.com/mate60-512.jpg', 1, NOW(), NOW()),
(3, 'MATE60-1TB-国行', 8999.00, 80, '{"内存":"1TB","版本":"国行"}', 'https://example.com/mate60-1tb.jpg', 1, NOW(), NOW());
