/*
Navicat MySQL Data Transfer

角色组织架构功能升级脚本
兼容旧数据，新增组织架构相关表

Date: 2024-01-01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- 为现有角色表添加角色类型字段（兼容旧数据）
-- ----------------------------
ALTER TABLE `ums_role` 
ADD COLUMN `role_type` int(1) DEFAULT '0' COMMENT '角色类型：0->普通角色；1->超级管理员；2->部门管理员' AFTER `sort`;

-- 更新现有角色的角色类型
UPDATE `ums_role` SET `role_type` = 1 WHERE `name` = '超级管理员';
UPDATE `ums_role` SET `role_type` = 2 WHERE `name` LIKE '%管理员%' AND `name` != '超级管理员';

-- ----------------------------
-- Table structure for ums_org_node
-- 组织架构节点表
-- ----------------------------
DROP TABLE IF EXISTS `ums_org_node`;
CREATE TABLE `ums_org_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL COMMENT '节点名称',
  `level` int(4) DEFAULT NULL COMMENT '节点层级（1-5层，1为全公司层）',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级节点ID',
  `sort` int(4) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` int(1) DEFAULT '1' COMMENT '启用状态：0->禁用；1->启用',
  `node_type` int(4) DEFAULT '0' COMMENT '节点类型：0->全公司；1->部门管理员；2->部门；3->小组；4->岗位',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_level` (`level`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='组织架构节点表';

-- ----------------------------
-- Table structure for ums_role_org
-- 角色组织架构关系表
-- ----------------------------
DROP TABLE IF EXISTS `ums_role_org`;
CREATE TABLE `ums_role_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织架构节点ID',
  `org_name` varchar(200) DEFAULT NULL COMMENT '组织架构节点名称',
  `org_level` int(4) DEFAULT NULL COMMENT '组织架构层级（1-5层）',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级组织架构ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `config_type` int(1) DEFAULT '0' COMMENT '配置类型：0->默认配置；1->自定义配置',
  `scope_type` int(1) DEFAULT '0' COMMENT '配置范围：0->本部门；1->全公司；2->指定部门',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_scope_type` (`scope_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色组织架构关系表';

-- ----------------------------
-- 初始化默认组织架构数据（五层结构）
-- ----------------------------

-- 第一层：全公司
INSERT INTO `ums_org_node` (`name`, `level`, `parent_id`, `sort`, `create_time`, `update_time`, `status`, `node_type`, `note`) 
VALUES ('全公司', 1, 0, 0, NOW(), NOW(), 1, 0, '顶层固定节点');

SET @company_id = LAST_INSERT_ID();

-- 第二层：部门管理员（默认节点）
INSERT INTO `ums_org_node` (`name`, `level`, `parent_id`, `sort`, `create_time`, `update_time`, `status`, `node_type`, `note`) 
VALUES ('部门管理员', 2, @company_id, 0, NOW(), NOW(), 1, 1, '二层默认节点');

SET @dept_admin_id = LAST_INSERT_ID();

-- 第三层：部门（示例数据）
INSERT INTO `ums_org_node` (`name`, `level`, `parent_id`, `sort`, `create_time`, `update_time`, `status`, `node_type`, `note`) 
VALUES 
('技术部', 3, @dept_admin_id, 0, NOW(), NOW(), 1, 2, '技术部门'),
('市场部', 3, @dept_admin_id, 1, NOW(), NOW(), 1, 2, '市场部门'),
('运营部', 3, @dept_admin_id, 2, NOW(), NOW(), 1, 2, '运营部门'),
('人事部', 3, @dept_admin_id, 3, NOW(), NOW(), 1, 2, '人事部门'),
('财务部', 3, @dept_admin_id, 4, NOW(), NOW(), 1, 2, '财务部门');

-- 获取技术部ID
SET @tech_dept_id = (SELECT id FROM ums_org_node WHERE name = '技术部' AND level = 3);

-- 第四层：小组（示例数据-技术部下属）
INSERT INTO `ums_org_node` (`name`, `level`, `parent_id`, `sort`, `create_time`, `update_time`, `status`, `node_type`, `note`) 
VALUES 
('前端组', 4, @tech_dept_id, 0, NOW(), NOW(), 1, 3, '前端开发小组'),
('后端组', 4, @tech_dept_id, 1, NOW(), NOW(), 1, 3, '后端开发小组'),
('测试组', 4, @tech_dept_id, 2, NOW(), NOW(), 1, 3, '测试小组'),
('运维组', 4, @tech_dept_id, 3, NOW(), NOW(), 1, 3, '运维小组');

-- 获取前端组ID
SET @frontend_group_id = (SELECT id FROM ums_org_node WHERE name = '前端组' AND level = 4);

-- 第五层：岗位（示例数据-前端组下属）
INSERT INTO `ums_org_node` (`name`, `level`, `parent_id`, `sort`, `create_time`, `update_time`, `status`, `node_type`, `note`) 
VALUES 
('前端高级工程师', 5, @frontend_group_id, 0, NOW(), NOW(), 1, 4, '高级前端工程师岗位'),
('前端中级工程师', 5, @frontend_group_id, 1, NOW(), NOW(), 1, 4, '中级前端工程师岗位'),
('前端初级工程师', 5, @frontend_group_id, 2, NOW(), NOW(), 1, 4, '初级前端工程师岗位');

-- ----------------------------
-- 初始化现有角色的组织架构配置（兼容旧数据）
-- ----------------------------

-- 为超级管理员配置全公司范围
SET @super_admin_role_id = (SELECT id FROM ums_role WHERE name = '超级管理员' LIMIT 1);
SET @company_node_id = (SELECT id FROM ums_org_node WHERE level = 1 LIMIT 1);

INSERT INTO `ums_role_org` (`role_id`, `org_id`, `org_name`, `org_level`, `parent_id`, `create_time`, `update_time`, `config_type`, `scope_type`)
SELECT 
    r.id as role_id,
    @company_node_id as org_id,
    '全公司' as org_name,
    1 as org_level,
    0 as parent_id,
    NOW() as create_time,
    NOW() as update_time,
    0 as config_type,
    1 as scope_type
FROM ums_role r
WHERE r.name = '超级管理员'
AND NOT EXISTS (
    SELECT 1 FROM ums_role_org ro WHERE ro.role_id = r.id
);

-- 为部门管理员配置本部门范围
SET @dept_admin_node_id = (SELECT id FROM ums_org_node WHERE level = 2 LIMIT 1);

INSERT INTO `ums_role_org` (`role_id`, `org_id`, `org_name`, `org_level`, `parent_id`, `create_time`, `update_time`, `config_type`, `scope_type`)
SELECT 
    r.id as role_id,
    @dept_admin_node_id as org_id,
    '部门管理员' as org_name,
    2 as org_level,
    @company_node_id as parent_id,
    NOW() as create_time,
    NOW() as update_time,
    0 as config_type,
    0 as scope_type
FROM ums_role r
WHERE r.name LIKE '%管理员%'
AND r.name != '超级管理员'
AND NOT EXISTS (
    SELECT 1 FROM ums_role_org ro WHERE ro.role_id = r.id
);

-- 为其他角色配置默认本部门范围
INSERT INTO `ums_role_org` (`role_id`, `org_id`, `org_name`, `org_level`, `parent_id`, `create_time`, `update_time`, `config_type`, `scope_type`)
SELECT 
    r.id as role_id,
    @dept_admin_node_id as org_id,
    '部门管理员' as org_name,
    2 as org_level,
    @company_node_id as parent_id,
    NOW() as create_time,
    NOW() as update_time,
    0 as config_type,
    0 as scope_type
FROM ums_role r
WHERE NOT EXISTS (
    SELECT 1 FROM ums_role_org ro WHERE ro.role_id = r.id
);

SET FOREIGN_KEY_CHECKS=1;
