package com.macro.mall.tiny.modules.ums.service;

public interface UmsAdminLoginAttemptService {

    void loginSucceeded(String username);

    void loginFailed(String username);

    boolean isLocked(String username);

    long getRemainingLockTime(String username);

    int getFailedAttempts(String username);
}
