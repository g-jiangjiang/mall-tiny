-- 角色组织架构树功能数据库迁移脚本
-- 兼容旧数据，禁止滥改pom

-- 1. 创建组织架构表
DROP TABLE IF EXISTS `ums_department`;
CREATE TABLE `ums_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门ID，0表示顶级部门（全公司）',
  `name` varchar(100) NOT NULL COMMENT '部门名称',
  `level` int(4) DEFAULT '1' COMMENT '层级：1-全公司，2-5-子部门',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(1) DEFAULT '1' COMMENT '状态：0->禁用；1->启用',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构表';

-- 2. 修改角色表，添加数据范围相关字段
ALTER TABLE `ums_role` 
ADD COLUMN `data_scope` int(1) DEFAULT '1' COMMENT '数据范围：1->全部数据权限；2->自定义数据权限；3->本部门数据权限；4->本部门及以下数据权限；5->仅本人数据权限' AFTER `sort`,
ADD COLUMN `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID，当数据范围为3或4时使用' AFTER `data_scope`;

-- 2.1 修改用户表，添加部门字段
ALTER TABLE `ums_admin`
ADD COLUMN `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID' AFTER `status`;

-- 3. 创建角色数据范围关联表
DROP TABLE IF EXISTS `ums_role_dept_relation`;
CREATE TABLE `ums_role_dept_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_dept` (`role_id`, `dept_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色部门关联表';

-- 4. 初始化默认数据（全公司部门）
INSERT INTO `ums_department` (`id`, `parent_id`, `name`, `level`, `sort`, `status`, `description`) 
VALUES (1, 0, '全公司', 1, 0, 1, '顶级部门，代表整个公司');

-- 5. 为现有角色设置默认数据范围
-- 超级管理员（id=5）设置为全部数据权限
UPDATE `ums_role` SET `data_scope` = 1 WHERE `id` = 5;

-- 其他角色默认设置为本部门数据权限（需要先创建部门后手动调整）
UPDATE `ums_role` SET `data_scope` = 3, `dept_id` = 1 WHERE `data_scope` IS NULL;
