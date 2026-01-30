package com.macro.mall.tiny.common;

/**
 * 系统常量定义
 * Created by macro on 2024/01/30.
 */
public class SecurityConstants {
    
    /**
     * 超级管理员角色ID
     */
    public static final Long SUPER_ADMIN_ROLE_ID = 5L;
    
    /**
     * 登录失败最大次数
     */
    public static final int MAX_LOGIN_FAILURES = 5;
    
    /**
     * 登录失败时间窗口（分钟）
     */
    public static final int LOGIN_FAILURE_WINDOW_MINUTES = 10;
    
    /**
     * 锁定时长（分钟）
     */
    public static final int LOCK_DURATION_MINUTES = 30;
    
    /**
     * Redis用户token键前缀
     */
    public static final String REDIS_KEY_USER_TOKEN_PREFIX = "ums:user:token:";
}