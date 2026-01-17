package com.macro.mall.tiny.security.component;

import com.macro.mall.tiny.modules.ums.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicSecurityServiceImpl implements DynamicSecurityService {

    @Autowired
    private UmsResourceService resourceService;

    @Override
    public Map<String, ConfigAttribute> loadDataSource() {
        Map<String, ConfigAttribute> map = new HashMap<>();
        resourceService.list().forEach(resource -> {
            map.put(resource.getUrl(), new SecurityConfig(resource.getId().toString()));
        });
        return map;
    }
}
