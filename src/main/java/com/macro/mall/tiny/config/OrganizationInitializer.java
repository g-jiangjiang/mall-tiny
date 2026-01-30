package com.macro.mall.tiny.config;

import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动后初始化组织架构
 *
 * @author macro
 * @since 2024-01-29
 */
@Component
public class OrganizationInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationInitializer.class);

    @Autowired
    private UmsOrganizationService organizationService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            logger.info("开始初始化组织架构...");
            organizationService.initDefaultOrganization();
            logger.info("组织架构初始化完成");
        } catch (Exception e) {
            logger.error("组织架构初始化失败", e);
        }
    }
}