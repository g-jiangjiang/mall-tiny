-- 创建组织架构表
CREATE TABLE IF NOT EXISTS ums_organization (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '组织名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父级组织ID',
    level INT DEFAULT 1 COMMENT '层级(1-5，1为全公司)',
    description VARCHAR(500) DEFAULT NULL COMMENT '描述',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status INT DEFAULT 1 COMMENT '状态：0->禁用；1->启用',
    is_default INT DEFAULT 0 COMMENT '是否默认组织：0->否；1->是',
    PRIMARY KEY (id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构表';

-- 创建用户组织权限范围表
CREATE TABLE IF NOT EXISTS ums_admin_org_scope (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    admin_id BIGINT NOT NULL COMMENT '管理员ID',
    org_id BIGINT NOT NULL COMMENT '组织ID',
    scope_type INT DEFAULT 1 COMMENT '权限类型：1->管理全公司；2->管理本部门',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_admin_org (admin_id, org_id),
    INDEX idx_admin_id (admin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户组织权限范围表';

-- 添加全公司默认组织
INSERT INTO ums_organization (name, parent_id, level, description, sort, status, is_default)
SELECT '全公司', 0, 1, '顶层组织', 0, 1, 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ums_organization WHERE is_default = 1);

-- 为现有管理员添加默认权限（如果没有权限配置的话）
INSERT INTO ums_admin_org_scope (admin_id, org_id, scope_type)
SELECT a.id, o.id, 1
FROM ums_admin a
CROSS JOIN (SELECT id FROM ums_organization WHERE is_default = 1) o
WHERE NOT EXISTS (
    SELECT 1 FROM ums_admin_org_scope s WHERE s.admin_id = a.id
);

-- 为ums_admin表添加org_id字段（如果不存在）
ALTER TABLE ums_admin ADD COLUMN IF NOT EXISTS org_id BIGINT DEFAULT NULL COMMENT '所属组织ID';

-- 更新现有用户的org_id为默认组织
UPDATE ums_admin a
LEFT JOIN ums_organization o ON o.is_default = 1
SET a.org_id = o.id
WHERE a.org_id IS NULL;