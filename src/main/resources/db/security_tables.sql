-- 创建用户登录失败记录表
CREATE TABLE `ums_admin_login_failure` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `ip` varchar(64) DEFAULT NULL COMMENT '登录IP',
  `failure_time` datetime NOT NULL COMMENT '失败时间',
  `failure_reason` varchar(255) DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_failure_time` (`failure_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录失败记录表';

-- 创建用户锁定状态表
CREATE TABLE `ums_admin_locked` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `lock_time` datetime NOT NULL COMMENT '锁定时间',
  `unlock_time` datetime DEFAULT NULL COMMENT '解锁时间',
  `lock_reason` varchar(255) DEFAULT NULL COMMENT '锁定原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户锁定状态表';