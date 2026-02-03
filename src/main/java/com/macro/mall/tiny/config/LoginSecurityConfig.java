package com.macro.mall.tiny.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录安全配置类
 * Created by macro on 2024/01/01.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "login")
public class LoginSecurityConfig {

    /**
     * 最大登录失败次数
     */
    private Integer maxFailAttempts = 5;

    /**
     * 锁定时间（分钟）
     */
    private Integer lockDuration = 30;

    /**
     * 失败统计时间窗口（分钟）
     */
    private Integer failWindow = 10;
}
