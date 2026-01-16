-- ----------------------------
-- Table structure for ums_organization
-- ----------------------------
DROP TABLE IF EXISTS `ums_organization`;
CREATE TABLE `ums_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '组织名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级ID',
  `level` int(4) DEFAULT NULL COMMENT '层级(1-5)',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(1) DEFAULT '1' COMMENT '启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织架构表';

-- ----------------------------
-- Records of ums_organization
-- ----------------------------
INSERT INTO `ums_organization` VALUES (1, '全公司', 0, 1, '公司最高层级组织', 0, NOW(), 1);
INSERT INTO `ums_organization` VALUES (2, '技术部', 1, 2, '技术部门', 0, NOW(), 1);
INSERT INTO `ums_organization` VALUES (3, '产品部', 1, 2, '产品部门', 1, NOW(), 1);
INSERT INTO `ums_organization` VALUES (4, '运营部', 1, 2, '运营部门', 2, NOW(), 1);
INSERT INTO `ums_organization` VALUES (5, '前端组', 2, 3, '前端开发组', 0, NOW(), 1);
INSERT INTO `ums_organization` VALUES (6, '后端组', 2, 3, '后端开发组', 1, NOW(), 1);

-- ----------------------------
-- Table structure for ums_admin_organization_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin_organization_relation`;
CREATE TABLE `ums_admin_organization_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(20) DEFAULT NULL COMMENT '管理员ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `scope` int(1) DEFAULT '1' COMMENT '权限范围：1->本部门；2->本部门及下级；3->全公司',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员组织关系表';

-- ----------------------------
-- Records of ums_admin_organization_relation
-- ----------------------------
INSERT INTO `ums_admin_organization_relation` VALUES (1, 3, 1, 3);
INSERT INTO `ums_admin_organization_relation` VALUES (2, 4, 2, 2);

-- ----------------------------
-- Function for getting all children organizations
-- ----------------------------
DROP FUNCTION IF EXISTS `get_organization_children`;
DELIMITER //
CREATE FUNCTION `get_organization_children`(rootId BIGINT)
RETURNS VARCHAR(4000)
BEGIN
  DECLARE sTemp VARCHAR(4000);
  DECLARE sTempChd VARCHAR(4000);
  
  SET sTemp = '$';
  SET sTempChd = CAST(rootId AS CHAR);
  
  WHILE sTempChd IS NOT NULL DO
    SET sTemp = CONCAT(sTemp, ',', sTempChd);
    SELECT GROUP_CONCAT(id) INTO sTempChd FROM ums_organization WHERE FIND_IN_SET(parent_id, sTempChd) > 0;
  END WHILE;
  
  RETURN sTemp;
END //
DELIMITER ;
