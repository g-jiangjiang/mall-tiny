package com.macro.mall.tiny.modules.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.exception.Asserts;
import com.macro.mall.tiny.domain.AdminUserDetails;
import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam;
import com.macro.mall.tiny.modules.ums.dto.UpdateAdminPasswordParam;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminLoginLogMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.model.*;
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService;
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsAdminService;
import com.macro.mall.tiny.security.util.JwtTokenUtil;
import com.macro.mall.tiny.security.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.macro.mall.tiny.common.service.RedisService;

/**
 * 后台管理员管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper,UmsAdmin> implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;
    @Autowired
    private UmsAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private UmsRoleMapper roleMapper;
    @Autowired
    private UmsResourceMapper resourceMapper;
    @Autowired
    private RedisService redisService;

    // 登录失败计数key前缀
    private static final String LOGIN_FAIL_COUNT_KEY = "ums:admin:fail_count:";
    // 登录锁定key前缀
    private static final String LOGIN_LOCK_KEY = "ums:admin:lock:";
    // 用户当前token key前缀
    private static final String USER_CURRENT_TOKEN_KEY = "ums:admin:current_token:";
    // 登录失败次数限制
    private static final int LOGIN_FAIL_MAX_COUNT = 5;
    // 锁定时间（分钟）
    private static final int LOCK_TIME_MINUTES = 30;
    // 失败计数时间窗口（分钟）
    private static final int FAIL_COUNT_WINDOW_MINUTES = 10;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdmin admin = getCacheService().getAdmin(username);
        if(admin!=null) return  admin;
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername,username);
        List<UmsAdmin> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            getCacheService().setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername,umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = list(wrapper);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        baseMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("密码不正确");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
//            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public String loginWithProtection(String username, String password) {
        String token = null;
        // 检查是否被锁定
        if (isLocked(username)) {
            long remainingLockTime = getRemainingLockTime(username);
            throw new RuntimeException("账号已被锁定，请" + remainingLockTime + "分钟后重试");
        }
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                // 登录失败，增加失败计数
                incrementLoginFailCount(username);
                Asserts.fail("密码不正确");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            // 登录成功，清除失败计数
            clearLoginFailCount(username);
            // 互斥登录：保存当前token并失效旧token
            handleExclusiveLogin(username, token);
//            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 检查用户是否被锁定
     */
    private boolean isLocked(String username) {
        String lockKey = LOGIN_LOCK_KEY + username;
        Boolean hasKey = redisService.hasKey(lockKey);
        return hasKey != null && hasKey;
    }

    /**
     * 获取剩余锁定时间（分钟）
     */
    private long getRemainingLockTime(String username) {
        String lockKey = LOGIN_LOCK_KEY + username;
        Long expire = redisService.getExpire(lockKey);
        if (expire == null || expire <= 0) {
            return 0;
        }
        return (expire + 59) / 60; // 向上取整
    }

    /**
     * 增加登录失败计数
     */
    private void incrementLoginFailCount(String username) {
        String failCountKey = LOGIN_FAIL_COUNT_KEY + username;
        Long count = redisService.incr(failCountKey, 1);
        if (count == 1) {
            // 第一次失败，设置过期时间
            redisService.expire(failCountKey, FAIL_COUNT_WINDOW_MINUTES * 60L);
        }
        if (count >= LOGIN_FAIL_MAX_COUNT) {
            // 达到失败次数限制，锁定账号
            String lockKey = LOGIN_LOCK_KEY + username;
            redisService.set(lockKey, "LOCKED", LOCK_TIME_MINUTES * 60L);
            // 清除失败计数
            redisService.del(failCountKey);
        }
    }

    /**
     * 清除登录失败计数
     */
    private void clearLoginFailCount(String username) {
        String failCountKey = LOGIN_FAIL_COUNT_KEY + username;
        redisService.del(failCountKey);
    }

    /**
     * 处理互斥登录
     */
    private void handleExclusiveLogin(String username, String newToken) {
        String tokenKey = USER_CURRENT_TOKEN_KEY + username;
        // 获取旧token并失效
        String oldToken = (String) redisService.get(tokenKey);
        if (oldToken != null) {
            // 可以在这里记录旧token失效，或者直接覆盖
        }
        // 保存新token，设置与JWT相同的过期时间
        Long expiration = jwtTokenUtil.getExpiredDateFromToken(newToken).getTime() - System.currentTimeMillis();
        if (expiration > 0) {
            redisService.set(tokenKey, newToken, expiration / 1000);
        }
    }

    /**
     * 添加登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if(admin==null) return;
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }

    /**
     * 根据用户名修改登录时间
     */
    private void updateLoginTimeByUsername(String username) {
        UmsAdmin record = new UmsAdmin();
        record.setLoginTime(new Date());
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername,username);
        update(record,wrapper);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public Page<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsAdmin> page = new Page<>(pageNum,pageSize);
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<UmsAdmin> lambda = wrapper.lambda();
        if(StrUtil.isNotEmpty(keyword)){
            lambda.like(UmsAdmin::getUsername,keyword);
            lambda.or().like(UmsAdmin::getNickName,keyword);
        }
        return page(page,wrapper);
    }

    @Override
    public boolean update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = getById(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        boolean success = updateById(admin);
        getCacheService().delAdmin(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        getCacheService().delAdmin(id);
        boolean success = removeById(id);
        getCacheService().delResourceList(id);
        return success;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        QueryWrapper<UmsAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminRoleRelation::getAdminId,adminId);
        adminRoleRelationService.remove(wrapper);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationService.saveBatch(list);
        }
        getCacheService().delResourceList(adminId);
        return count;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return roleMapper.getRoleList(adminId);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        List<UmsResource> resourceList = getCacheService().getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            return  resourceList;
        }
        resourceList = resourceMapper.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            getCacheService().setResourceList(adminId,resourceList);
        }
        return resourceList;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername,param.getUsername());
        List<UmsAdmin> adminList = list(wrapper);
        if(CollUtil.isEmpty(adminList)){
            return -2;
        }
        UmsAdmin umsAdmin = adminList.get(0);
        if(!passwordEncoder.matches(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        updateById(umsAdmin);
        getCacheService().delAdmin(umsAdmin.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        //获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public UmsAdminCacheService getCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }

    @Override
    public void logout(String token) {
        if (token == null) {
            return;
        }
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (username == null) {
            return;
        }
        String tokenKey = USER_CURRENT_TOKEN_KEY + username;
        String currentToken = (String) redisService.get(tokenKey);
        // 只有当token是当前有效token时才登出
        if (token.equals(currentToken)) {
            redisService.del(tokenKey);
        }
    }

    @Override
    public boolean validateTokenExclusively(String token) {
        if (token == null) {
            return false;
        }
        // 获取用户名
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (username == null) {
            return false;
        }
        // 验证token是否是当前用户的有效token
        String tokenKey = USER_CURRENT_TOKEN_KEY + username;
        String currentToken = (String) redisService.get(tokenKey);
        return token.equals(currentToken);
    }

    @Override
    public List<Map<String, Object>> exportUserList() {
        List<UmsAdmin> adminList = list();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (UmsAdmin admin : adminList) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", admin.getId());
            userMap.put("username", admin.getUsername());
            userMap.put("nickName", admin.getNickName());
            userMap.put("status", admin.getStatus());
            userMap.put("createTime", admin.getCreateTime());
            userMap.put("loginTime", admin.getLoginTime());
            
            // 数据脱敏：邮箱脱敏
            if (admin.getEmail() != null) {
                userMap.put("email", desensitizeEmail(admin.getEmail()));
            } else {
                userMap.put("email", "");
            }
            
            // 不导出敏感信息
            // userMap.put("password", admin.getPassword());
            // userMap.put("icon", admin.getIcon());
            // userMap.put("note", admin.getNote());
            
            result.add(userMap);
        }
        
        return result;
    }

    /**
     * 邮箱脱敏
     */
    private String desensitizeEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }
        // 保留前两位和域名部分
        String prefix = email.substring(0, 2);
        String domain = email.substring(atIndex);
        return prefix + "***" + domain;
    }
}
