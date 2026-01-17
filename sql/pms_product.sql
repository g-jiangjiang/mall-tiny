-- 商品类目表
CREATE TABLE `pms_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '类目名称',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父类目ID，0表示一级类目',
  `level` int(11) DEFAULT NULL COMMENT '类目级别',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `icon` varchar(500) DEFAULT NULL COMMENT '图标',
  `description` text COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(11) DEFAULT 1 COMMENT '状态：0->禁用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品类目表';

-- 商品SPU表
CREATE TABLE `pms_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `brand_id` bigint(20) NOT NULL COMMENT '品牌ID',
  `category_id` bigint(20) NOT NULL COMMENT '类目ID',
  `description` text COMMENT '商品详情',
  `pic` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `sale` int(11) DEFAULT 0 COMMENT '销量',
  `sub_title` varchar(500) DEFAULT NULL COMMENT '副标题',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(11) DEFAULT 1 COMMENT '状态：0->下架；1->上架',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品SPU表';

-- 商品SKU表
CREATE TABLE `pms_product_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_code` varchar(64) NOT NULL COMMENT 'SKU编码',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int(11) DEFAULT 0 COMMENT '库存',
  `sale` int(11) DEFAULT 0 COMMENT '销量',
  `pic` varchar(500) DEFAULT NULL COMMENT '图片',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 商品SKU规格关联表
CREATE TABLE `pms_product_sku_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sku_id` bigint(20) NOT NULL COMMENT 'SKU ID',
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `spec_value_id` bigint(20) NOT NULL COMMENT '规格值ID',
  `spec_name` varchar(64) NOT NULL COMMENT '规格名称',
  `spec_value` varchar(64) NOT NULL COMMENT '规格值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku_spec` (`sku_id`, `spec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU规格关联表';

-- 商品规格类型表
CREATE TABLE `pms_product_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '规格名称',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `category_id` bigint(20) DEFAULT NULL COMMENT '关联类目ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品规格类型表';

-- 商品规格值表
CREATE TABLE `pms_product_spec_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `value` varchar(64) NOT NULL COMMENT '规格值',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品规格值表';

-- 测试数据
INSERT INTO `pms_category` (`name`, `parent_id`, `level`, `sort`, `description`, `status`) VALUES
('电子产品', 0, 1, 1, '电子产品类目', 1),
('手机', 1, 2, 1, '手机类目', 1),
('电脑', 1, 2, 2, '电脑类目', 1),
('服装', 0, 1, 2, '服装类目', 1),
('男装', 4, 2, 1, '男装类目', 1),
('女装', 4, 2, 2, '女装类目', 1);

INSERT INTO `pms_product_spec` (`name`, `sort`, `category_id`) VALUES
('颜色', 1, 2),
('存储容量', 2, 2),
('尺寸', 1, 5);

INSERT INTO `pms_product_spec_value` (`spec_id`, `value`, `sort`) VALUES
(1, '黑色', 1),
(1, '白色', 2),
(1, '红色', 3),
(2, '64GB', 1),
(2, '128GB', 2),
(2, '256GB', 3),
(3, 'S', 1),
(3, 'M', 2),
(3, 'L', 3),
(3, 'XL', 4);

INSERT INTO `pms_product` (`name`, `brand_id`, `category_id`, `description`, `pic`, `sort`, `sale`, `sub_title`, `original_price`, `status`) VALUES
('华为Mate 60', 1, 2, '华为Mate 60手机详情...', 'https://example.com/mate60.jpg', 1, 1000, '华为旗舰手机', 6999.00, 1),
('iPhone 15', 2, 2, 'iPhone 15手机详情...', 'https://example.com/iphone15.jpg', 2, 2000, '苹果旗舰手机', 7999.00, 1);

INSERT INTO `pms_product_sku` (`product_id`, `sku_code`, `price`, `stock`, `sale`, `pic`) VALUES
(1, 'HUAWEI-MATE60-BLACK-256GB', 6999.00, 500, 100, 'https://example.com/mate60-black.jpg'),
(1, 'HUAWEI-MATE60-WHITE-256GB', 6999.00, 300, 50, 'https://example.com/mate60-white.jpg'),
(2, 'IPHONE15-BLACK-128GB', 7999.00, 400, 200, 'https://example.com/iphone15-black.jpg'),
(2, 'IPHONE15-WHITE-128GB', 7999.00, 200, 100, 'https://example.com/iphone15-white.jpg');

INSERT INTO `pms_product_sku_spec` (`sku_id`, `spec_id`, `spec_value_id`, `spec_name`, `spec_value`) VALUES
(1, 1, 1, '颜色', '黑色'),
(1, 2, 5, '存储容量', '256GB'),
(2, 1, 2, '颜色', '白色'),
(2, 2, 5, '存储容量', '256GB'),
(3, 1, 1, '颜色', '黑色'),
(3, 2, 4, '存储容量', '128GB'),
(4, 1, 2, '颜色', '白色'),
(4, 2, 4, '存储容量', '128GB');
