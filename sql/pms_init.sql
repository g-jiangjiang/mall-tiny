-- 商品品牌表
CREATE TABLE IF NOT EXISTS `pms_brand` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) DEFAULT NULL COMMENT '品牌名称',
  `first_letter` varchar(8) DEFAULT NULL COMMENT '品牌首字母',
  `logo` varchar(255) DEFAULT NULL COMMENT '品牌LOGO',
  `big_pic` varchar(255) DEFAULT NULL COMMENT '品牌大图',
  `brand_story` text COMMENT '品牌故事',
  `sort` int DEFAULT '0' COMMENT '排序',
  `show_status` int DEFAULT '1' COMMENT '是否显示',
  `factory_status` int DEFAULT '0' COMMENT '品牌制造商',
  `delete_status` int DEFAULT '0' COMMENT '删除状态：0->未删除，1->已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品品牌表';

-- 商品类目表
CREATE TABLE IF NOT EXISTS `pms_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父类目ID，0表示一级类目',
  `name` varchar(50) DEFAULT NULL COMMENT '类目名称',
  `level` int DEFAULT '0' COMMENT '类目层级，0表示一级，1表示二级，2表示三级',
  `product_count` int DEFAULT '0' COMMENT '类目数量',
  `product_unit` varchar(64) DEFAULT NULL COMMENT '类目单位',
  `nav_status` int DEFAULT '0' COMMENT '是否显示在导航栏',
  `show_status` int DEFAULT '1' COMMENT '显示状态',
  `icon` varchar(255) DEFAULT NULL COMMENT '类目图标',
  `keywords` varchar(255) DEFAULT NULL COMMENT '类目关键词',
  `description` varchar(255) DEFAULT NULL COMMENT '类目描述',
  `sort` int DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类目表';

-- 商品规格类型表
CREATE TABLE IF NOT EXISTS `pms_spec_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) DEFAULT NULL COMMENT '规格类型名称',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格类型表';

-- 商品规格值表
CREATE TABLE IF NOT EXISTS `pms_spec_value` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `spec_type_id` bigint DEFAULT NULL COMMENT '规格类型ID',
  `value` varchar(64) DEFAULT NULL COMMENT '规格值',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_spec_type_id` (`spec_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格值表';

-- SPU商品表
CREATE TABLE IF NOT EXISTS `pms_spu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) DEFAULT NULL COMMENT '商品名称',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `category_id` bigint DEFAULT NULL COMMENT '商品类目ID',
  `detail` text COMMENT '商品详情',
  `pic` varchar(255) DEFAULT NULL COMMENT '商品主图',
  `unit` varchar(64) DEFAULT NULL COMMENT '商品单位',
  `description` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `delete_status` int DEFAULT '0' COMMENT '删除状态：0->未删除，1->已删除',
  `publish_status` int DEFAULT '0' COMMENT '上架状态：0->下架，1->上架',
  `new_status` int DEFAULT '0' COMMENT '新品状态：0->不是新品，1->是新品',
  `recommand_status` int DEFAULT '0' COMMENT '推荐状态：0->不推荐，1->推荐',
  `verify_status` int DEFAULT '0' COMMENT '审核状态：0->未审核，1->已审核',
  `sort` int DEFAULT '0' COMMENT '排序',
  `sale` int DEFAULT '0' COMMENT '销量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SPU商品表';

-- SKU商品表
CREATE TABLE IF NOT EXISTS `pms_sku` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `spu_id` bigint DEFAULT NULL COMMENT 'SPU ID',
  `sku_code` varchar(64) DEFAULT NULL COMMENT 'SKU编码',
  `name` varchar(128) DEFAULT NULL COMMENT 'SKU名称',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `stock` int DEFAULT '0' COMMENT '库存',
  `specs` text COMMENT '规格JSON',
  `pic` varchar(255) DEFAULT NULL COMMENT 'SKU图片',
  `delete_status` int DEFAULT '0' COMMENT '删除状态：0->未删除，1->已删除',
  `publish_status` int DEFAULT '0' COMMENT '上架状态：0->下架，1->上架',
  `sale` int DEFAULT '0' COMMENT '销量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku_code` (`sku_code`),
  KEY `idx_spu_id` (`spu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU商品表';

-- 插入测试数据 - 品牌
INSERT INTO `pms_brand` (`name`, `first_letter`, `logo`, `big_pic`, `brand_story`, `sort`, `show_status`, `factory_status`) VALUES
('Apple', 'A', 'http://example.com/apple_logo.jpg', 'http://example.com/apple_big.jpg', '苹果公司是美国一家跨国科技公司', 0, 1, 1),
('Samsung', 'S', 'http://example.com/samsung_logo.jpg', 'http://example.com/samsung_big.jpg', '三星集团是韩国最大的跨国企业集团', 1, 1, 1),
('Huawei', 'H', 'http://example.com/huawei_logo.jpg', 'http://example.com/huawei_big.jpg', '华为技术有限公司，总部位于中国广东省深圳市', 2, 1, 1),
('Xiaomi', 'X', 'http://example.com/xiaomi_logo.jpg', 'http://example.com/xiaomi_big.jpg', '小米科技有限责任公司', 3, 1, 1),
('Nike', 'N', 'http://example.com/nike_logo.jpg', 'http://example.com/nike_big.jpg', '耐克公司是一家美国跨国公司', 4, 1, 1);

-- 插入测试数据 - 类目
INSERT INTO `pms_category` (`parent_id`, `name`, `level`, `product_unit`, `nav_status`, `show_status`, `keywords`, `description`, `sort`) VALUES
(0, '手机', 0, '台', 1, 1, '手机,智能手机', '手机类目', 0),
(0, '电脑', 0, '台', 1, 1, '电脑,笔记本', '电脑类目', 1),
(0, '服装', 0, '件', 1, 1, '服装,衣服', '服装类目', 2),
(1, '智能手机', 1, '台', 1, 1, '智能手机', '智能手机类目', 0),
(1, '功能手机', 1, '台', 0, 1, '功能手机', '功能手机类目', 1),
(2, '笔记本', 1, '台', 1, 1, '笔记本', '笔记本类目', 0),
(2, '台式机', 1, '台', 0, 1, '台式机', '台式机类目', 1),
(3, '男装', 1, '件', 1, 1, '男装', '男装类目', 0),
(3, '女装', 1, '件', 1, 1, '女装', '女装类目', 1);

-- 插入测试数据 - 规格类型
INSERT INTO `pms_spec_type` (`name`, `sort`) VALUES
('颜色', 0),
('尺寸', 1),
('内存', 2),
('存储', 3);

-- 插入测试数据 - 规格值
INSERT INTO `pms_spec_value` (`spec_type_id`, `value`, `sort`) VALUES
(1, '红色', 0),
(1, '蓝色', 1),
(1, '黑色', 2),
(1, '白色', 3),
(2, 'S', 0),
(2, 'M', 1),
(2, 'L', 2),
(2, 'XL', 3),
(3, '8GB', 0),
(3, '16GB', 1),
(3, '32GB', 2),
(4, '128GB', 0),
(4, '256GB', 1),
(4, '512GB', 2);

-- 插入测试数据 - SPU
INSERT INTO `pms_spu` (`name`, `brand_id`, `category_id`, `detail`, `pic`, `unit`, `description`, `publish_status`, `sort`) VALUES
('iPhone 15', 1, 4, 'iPhone 15 详细介绍', 'http://example.com/iphone15.jpg', '台', '苹果最新款手机', 1, 0),
('Galaxy S24', 2, 4, 'Galaxy S24 详细介绍', 'http://example.com/galaxys24.jpg', '台', '三星最新款手机', 1, 1),
('Mate 60 Pro', 3, 4, 'Mate 60 Pro 详细介绍', 'http://example.com/mate60pro.jpg', '台', '华为最新款手机', 1, 2),
('MacBook Pro', 1, 6, 'MacBook Pro 详细介绍', 'http://example.com/macbookpro.jpg', '台', '苹果笔记本电脑', 1, 0),
('ThinkPad X1', 2, 6, 'ThinkPad X1 详细介绍', 'http://example.com/thinkpadx1.jpg', '台', '联想笔记本电脑', 1, 1);

-- 插入测试数据 - SKU
INSERT INTO `pms_sku` (`spu_id`, `sku_code`, `name`, `price`, `stock`, `specs`, `pic`, `publish_status`) VALUES
(1, 'IP15-128-BLK', 'iPhone 15 128GB 黑色', 5999.00, 100, '{"颜色":"黑色","存储":"128GB"}', 'http://example.com/iphone15-blk.jpg', 1),
(1, 'IP15-256-BLK', 'iPhone 15 256GB 黑色', 6999.00, 80, '{"颜色":"黑色","存储":"256GB"}', 'http://example.com/iphone15-blk.jpg', 1),
(1, 'IP15-128-WHT', 'iPhone 15 128GB 白色', 5999.00, 120, '{"颜色":"白色","存储":"128GB"}', 'http://example.com/iphone15-wht.jpg', 1),
(2, 'GS24-128-BLU', 'Galaxy S24 128GB 蓝色', 4999.00, 150, '{"颜色":"蓝色","存储":"128GB"}', 'http://example.com/gs24-blu.jpg', 1),
(2, 'GS24-256-BLU', 'Galaxy S24 256GB 蓝色', 5499.00, 100, '{"颜色":"蓝色","存储":"256GB"}', 'http://example.com/gs24-blu.jpg', 1),
(3, 'MT60-256-BLK', 'Mate 60 Pro 256GB 黑色', 6999.00, 200, '{"颜色":"黑色","存储":"256GB"}', 'http://example.com/mt60-blk.jpg', 1),
(3, 'MT60-512-BLK', 'Mate 60 Pro 512GB 黑色', 7999.00, 150, '{"颜色":"黑色","存储":"512GB"}', 'http://example.com/mt60-blk.jpg', 1),
(4, 'MBP-16-512', 'MacBook Pro 16英寸 512GB', 19999.00, 50, '{"尺寸":"16英寸","存储":"512GB"}', 'http://example.com/mbp-16.jpg', 1),
(4, 'MBP-14-256', 'MacBook Pro 14英寸 256GB', 14999.00, 80, '{"尺寸":"14英寸","存储":"256GB"}', 'http://example.com/mbp-14.jpg', 1),
(5, 'TPX1-14-256', 'ThinkPad X1 14英寸 256GB', 12999.00, 60, '{"尺寸":"14英寸","存储":"256GB"}', 'http://example.com/tpx1-14.jpg', 1);
