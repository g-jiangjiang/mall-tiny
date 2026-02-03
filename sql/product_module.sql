-- 商品配置模块数据库表结构
-- 基于 Spring Boot 3 + MyBatis-Plus

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pms_brand
-- ----------------------------
DROP TABLE IF EXISTS `pms_brand`;
CREATE TABLE `pms_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '品牌名称',
  `logo` varchar(255) DEFAULT NULL COMMENT '品牌logo',
  `description` varchar(500) DEFAULT NULL COMMENT '品牌描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(1) DEFAULT '1' COMMENT '显示状态：0->不显示；1->显示',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品品牌表';

-- ----------------------------
-- Table structure for pms_category
-- ----------------------------
DROP TABLE IF EXISTS `pms_category`;
CREATE TABLE `pms_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父类目ID，0表示顶级类目',
  `name` varchar(64) NOT NULL COMMENT '类目名称',
  `level` int(1) DEFAULT '1' COMMENT '类目级别：1->一级；2->二级；3->三级',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '类目图标',
  `description` varchar(500) DEFAULT NULL COMMENT '类目描述',
  `status` int(1) DEFAULT '1' COMMENT '显示状态：0->不显示；1->显示',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品类目表';

-- ----------------------------
-- Table structure for pms_attribute
-- ----------------------------
DROP TABLE IF EXISTS `pms_attribute`;
CREATE TABLE `pms_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '规格名称',
  `type` int(1) DEFAULT '0' COMMENT '规格类型：0->规格；1->参数',
  `input_type` int(1) DEFAULT '0' COMMENT '录入方式：0->手工录入；1->从列表选择',
  `select_list` varchar(500) DEFAULT NULL COMMENT '可选值列表，逗号分隔',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(1) DEFAULT '1' COMMENT '状态：0->禁用；1->启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规格属性表';

-- ----------------------------
-- Table structure for pms_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `pms_attribute_value`;
CREATE TABLE `pms_attribute_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attribute_id` bigint(20) NOT NULL COMMENT '规格属性ID',
  `value` varchar(100) NOT NULL COMMENT '规格值',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(1) DEFAULT '1' COMMENT '状态：0->禁用；1->启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规格属性值表';

-- ----------------------------
-- Table structure for pms_spu
-- ----------------------------
DROP TABLE IF EXISTS `pms_spu`;
CREATE TABLE `pms_spu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT 'SPU名称',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `category_id` bigint(20) NOT NULL COMMENT '类目ID',
  `detail` text COMMENT '商品详情',
  `main_image` varchar(255) DEFAULT NULL COMMENT '主图',
  `sub_images` varchar(2000) DEFAULT NULL COMMENT '副图，逗号分隔',
  `status` int(1) DEFAULT '1' COMMENT '状态：0->下架；1->上架',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品SPU表';

-- ----------------------------
-- Table structure for pms_spu_attribute
-- ----------------------------
DROP TABLE IF EXISTS `pms_spu_attribute`;
CREATE TABLE `pms_spu_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spu_id` bigint(20) NOT NULL COMMENT 'SPU ID',
  `attribute_id` bigint(20) NOT NULL COMMENT '规格属性ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SPU规格关联表';

-- ----------------------------
-- Table structure for pms_sku
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku`;
CREATE TABLE `pms_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spu_id` bigint(20) NOT NULL COMMENT 'SPU ID',
  `sku_code` varchar(100) NOT NULL COMMENT 'SKU编码',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int(11) DEFAULT '0' COMMENT '库存',
  `specifications` varchar(500) DEFAULT NULL COMMENT '规格组合JSON，如{"颜色":"红色","尺码":"XL"}',
  `main_image` varchar(255) DEFAULT NULL COMMENT 'SKU主图',
  `status` int(1) DEFAULT '1' COMMENT '状态：0->禁用；1->启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku_code` (`sku_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品SKU表';

-- ----------------------------
-- Table structure for pms_sku_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_attribute_value`;
CREATE TABLE `pms_sku_attribute_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sku_id` bigint(20) NOT NULL COMMENT 'SKU ID',
  `attribute_id` bigint(20) NOT NULL COMMENT '规格属性ID',
  `attribute_value_id` bigint(20) NOT NULL COMMENT '规格属性值ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SKU规格属性值关联表';

SET FOREIGN_KEY_CHECKS=1;
