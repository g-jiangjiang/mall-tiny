package com.macro.mall.tiny.modules.ums.service;

/**
 * 登录安全Service
 * Created by macro on 2024/01/01.
 */
public interface UmsLoginSecurityService {

    /**
     * 检查账号是否被锁定
     * @param username 用户名
     * @return true-已锁定，false-未锁定
     */
    boolean isAccountLocked(String username);

    /**
     * 记录登录失败次数
     * @param username 用户名
     */
    void recordLoginFail(String username);

    /**
     * 清除登录失败记录
     * @param username 用户名
     */
    void clearLoginFail(String username);

    /**
     * 获取剩余锁定时间（秒）
     * @param username 用户名
     * @return 剩余锁定时间，未锁定返回0
     */
    long getRemainingLockTime(String username);

    /**
     * 存储用户Token（用于互斥登录）
     * @param username 用户名
     * @param token JWT Token
     */
    void storeUserToken(String username, String token);

    /**
     * 验证用户Token是否有效
     * @param username 用户名
     * @param token JWT Token
     * @return true-有效，false-无效（已被挤下线）
     */
    boolean validateUserToken(String username, String token);

    /**
     * 移除用户Token
     * @param username 用户名
     */
    void removeUserToken(String username);
}
