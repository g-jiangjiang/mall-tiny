package com.macro.mall.tiny.modules.ums.service;

import com.macro.mall.tiny.modules.ums.model.UmsAdminLocked;

/**
 * 安全服务接口
 * Created by macro on 2024/01/30.
 */
public interface UmsSecurityService {

    /**
     * 检查用户是否被锁定
     * @param username 用户名
     * @return 锁定记录，未锁定返回null
     */
    UmsAdminLocked checkUserLocked(String username);

    /**
     * 记录登录失败
     * @param username 用户名
     * @param ip 登录IP
     * @param reason 失败原因
     * @return 是否触发锁定
     */
    boolean recordLoginFailure(String username, String ip, String reason);

    /**
     * 解锁用户
     * @param username 用户名
     * @return 是否成功解锁
     */
    boolean unlockUser(String username);

    /**
     * 记录用户token，用于互斥登录
     * @param username 用户名
     * @param token JWT token
     */
    void recordUserToken(String username, String token);

    /**
     * 检查用户token是否有效
     * @param username 用户名
     * @param token JWT token
     * @return 是否有效
     */
    boolean isTokenValid(String username, String token);

    /**
     * 清除用户token记录
     * @param username 用户名
     */
    void clearUserToken(String username);

}