-- 创建组织架构表
CREATE TABLE IF NOT EXISTS `ums_organization` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '组织名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父组织ID',
  `level` tinyint NOT NULL DEFAULT '1' COMMENT '组织层级：1->公司；2->部门；3->小组；4->岗位；5->其他',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0->禁用；1->启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织架构表';

-- 创建角色组织架构关联表
CREATE TABLE IF NOT EXISTS `ums_role_organization_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `organization_id` bigint NOT NULL COMMENT '组织架构ID',
  `scope` tinyint(1) DEFAULT '0' COMMENT '权限范围：0->仅当前组织；1->当前组织及下级组织',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_organization` (`role_id`, `organization_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_organization_id` (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色组织架构关联表';

-- 初始化默认组织架构数据
INSERT IGNORE INTO `ums_organization` (`id`, `name`, `parent_id`, `level`, `sort`, `status`) VALUES
(1, '全公司', NULL, 1, 0, 1);

-- 为现有超级管理员角色添加全公司权限
-- 注意：这里假设超级管理员角色名称为'超级管理员'，如果实际情况不同，请修改
INSERT IGNORE INTO `ums_role_organization_relation` (`role_id`, `organization_id`, `scope`) 
SELECT r.id, 1, 1 FROM `ums_role` r WHERE r.name = '超级管理员';

-- 创建示例部门
INSERT IGNORE INTO `ums_organization` (`id`, `name`, `parent_id`, `level`, `sort`, `status`) VALUES
(2, '技术部', 1, 2, 1, 1),
(3, '市场部', 1, 2, 2, 1),
(4, '人事部', 1, 2, 3, 1),
(5, '财务部', 1, 2, 4, 1);

-- 为部门管理员角色添加对应部门权限
-- 注意：这里假设部门管理员角色名称为'部门管理员'，如果实际情况不同，请修改
INSERT IGNORE INTO `ums_role_organization_relation` (`role_id`, `organization_id`, `scope`) 
SELECT r.id, o.id, 1 FROM `ums_role` r, `ums_organization` o 
WHERE r.name = '部门管理员' AND o.level = 2;