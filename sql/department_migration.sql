-- ========================================
-- 组织架构功能迁移脚本
-- 1. 新增部门表 ums_department
-- 2. 新增角色-部门关系表 ums_role_department_relation
-- 3. 初始化默认数据
-- 4. 兼容旧数据处理
-- ========================================

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ums_department
-- ----------------------------
DROP TABLE IF EXISTS `ums_department`;
CREATE TABLE `ums_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级ID，0为顶级',
  `name` varchar(100) NOT NULL COMMENT '部门名称',
  `level` int(1) DEFAULT '0' COMMENT '层级：0->全公司，1->一级部门，2->二级部门，3->三级部门，4->四级部门',
  `sort` int(4) DEFAULT '0' COMMENT '排序',
  `status` int(1) DEFAULT '1' COMMENT '状态：0->禁用，1->启用',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织架构表';

-- ----------------------------
-- Table structure for ums_role_department_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_role_department_relation`;
CREATE TABLE `ums_role_department_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `department_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_dept` (`role_id`,`department_id`),
  KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与组织架构关系表';

-- ----------------------------
-- 初始化组织架构数据（最多5层）
-- Level 0: 全公司（顶层固定）
-- Level 1: 一级部门
-- Level 2: 二级部门
-- Level 3: 三级部门
-- Level 4: 四级部门
-- ----------------------------

-- Level 0 - 全公司（顶层固定）
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) VALUES (1, 0, '全公司', 0, 0, 1, '系统顶层组织，不可删除');

-- Level 1 - 一级部门
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) VALUES 
(2, 1, '总经办', 1, 1, 1, '总经理办公室'),
(3, 1, '技术部', 1, 2, 1, '技术研发部门'),
(4, 1, '产品部', 1, 3, 1, '产品设计部门'),
(5, 1, '运营部', 1, 4, 1, '运营管理部门'),
(6, 1, '市场部', 1, 5, 1, '市场营销部门'),
(7, 1, '财务部', 1, 6, 1, '财务管理部门'),
(8, 1, '人力资源部', 1, 7, 1, '人力资源管理部门');

-- Level 2 - 二级部门（技术部下属）
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) VALUES 
(9, 3, '前端开发组', 2, 1, 1, '前端技术开发'),
(10, 3, '后端开发组', 2, 2, 1, '后端技术开发'),
(11, 3, '测试组', 2, 3, 1, '软件测试'),
(12, 3, '运维组', 2, 4, 1, '系统运维');

-- Level 2 - 二级部门（运营部下属）
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) VALUES 
(13, 5, '电商运营组', 2, 1, 1, '电商平台运营'),
(14, 5, '内容运营组', 2, 2, 1, '内容策划运营'),
(15, 5, '用户运营组', 2, 3, 1, '用户关系运营');

-- Level 2 - 二级部门（市场部下属）
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) VALUES 
(16, 6, '品牌推广组', 2, 1, 1, '品牌推广'),
(17, 6, '渠道合作组', 2, 2, 1, '渠道合作');

-- Level 3 - 三级部门（后端开发组下属）
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) VALUES 
(18, 10, 'Java开发组', 3, 1, 1, 'Java技术开发'),
(19, 10, 'Python开发组', 3, 2, 1, 'Python技术开发');

-- Level 4 - 四级部门（Java开发组下属）
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) VALUES 
(20, 18, '核心系统组', 4, 1, 1, '核心业务系统开发'),
(21, 18, '支撑系统组', 4, 2, 1, '支撑系统开发');

-- ----------------------------
-- 初始化角色-组织架构关系
-- 为现有角色分配默认的组织范围
-- ----------------------------

-- 超级管理员角色（假设ID为5）可以访问全公司
INSERT INTO `ums_role_department_relation` (`role_id`, `department_id`) 
SELECT 5, 1 FROM DUAL WHERE EXISTS (SELECT 1 FROM `ums_role` WHERE `id` = 5);

-- 商品管理员角色（假设ID为1）可以访问技术部和产品部
INSERT INTO `ums_role_department_relation` (`role_id`, `department_id`) 
SELECT 1, id FROM `ums_department` WHERE id IN (3, 4) AND EXISTS (SELECT 1 FROM `ums_role` WHERE `id` = 1);

-- 订单管理员角色（假设ID为2）可以访问运营部和财务部
INSERT INTO `ums_role_department_relation` (`role_id`, `department_id`) 
SELECT 2, id FROM `ums_department` WHERE id IN (5, 7) AND EXISTS (SELECT 1 FROM `ums_role` WHERE `id` = 2);

-- ----------------------------
-- 旧数据兼容处理
-- 为没有设置组织范围的角色默认分配全公司权限
-- ----------------------------
INSERT INTO `ums_role_department_relation` (`role_id`, `department_id`)
SELECT r.id, 1 
FROM `ums_role` r
LEFT JOIN `ums_role_department_relation` rd ON r.id = rd.role_id
WHERE rd.role_id IS NULL;

SET FOREIGN_KEY_CHECKS=1;
