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
import com.macro.mall.tiny.common.service.RedisService;
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

import com.macro.mall.tiny.common.util.DesensitizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final String LOGIN_FAILURE_PREFIX = "login:failure:";
    private static final String ACCOUNT_LOCK_PREFIX = "account:lock:";
    private static final String USER_VALID_TOKEN_PREFIX = "user:valid:token:";
    private static final int MAX_LOGIN_FAILURE = 5;
    private static final int LOGIN_FAILURE_WINDOW_MINUTES = 10;
    private static final int LOCK_DURATION_MINUTES = 30;

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
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            LOGGER.warn("登录失败，用户名或密码为空");
            return null;
        }
        if (isAccountLocked(username)) {
            String lockKey = ACCOUNT_LOCK_PREFIX + username;
            Long remainingTime = redisService.getExpire(lockKey);
            LOGGER.warn("用户{}登录失败，帐号已被锁定，剩余锁定时间：{}秒", username, remainingTime);
            return null;
        }
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                increaseLoginFailure(username);
                String failureKey = LOGIN_FAILURE_PREFIX + username;
                Long failureCount = redisService.get(failureKey);
                int remainingAttempts = Math.max(0, MAX_LOGIN_FAILURE - (failureCount != null ? failureCount.intValue() : 0));
                LOGGER.warn("用户{}登录失败，密码错误，剩余尝试次数：{}", username, remainingAttempts);
                return null;
            }
            if(!userDetails.isEnabled()){
                LOGGER.warn("用户{}登录失败，帐号已被禁用", username);
                return null;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            setCurrentValidToken(username, token);
            resetLoginFailure(username);
            updateLoginTimeByUsername(username);
            insertLoginLog(username);
            LOGGER.info("用户{}登录成功", username);
        } catch (UsernameNotFoundException e) {
            LOGGER.warn("用户{}不存在", username);
            return null;
        } catch (AuthenticationException e) {
            increaseLoginFailure(username);
            LOGGER.warn("用户{}登录异常:{}", username, e.getMessage());
        }
        return token;
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
    public boolean isAccountLocked(String username) {
        String lockKey = ACCOUNT_LOCK_PREFIX + username;
        Boolean hasLock = redisService.hasKey(lockKey);
        if (Boolean.TRUE.equals(hasLock)) {
            Long expire = redisService.getExpire(lockKey);
            return expire != null && expire > 0;
        }
        return false;
    }

    @Override
    public void increaseLoginFailure(String username) {
        String failureKey = LOGIN_FAILURE_PREFIX + username;
        Long count = redisService.incr(failureKey, 1);
        if (count == 1) {
            redisService.expire(failureKey, LOGIN_FAILURE_WINDOW_MINUTES * 60);
        }
        if (count >= MAX_LOGIN_FAILURE) {
            String lockKey = ACCOUNT_LOCK_PREFIX + username;
            redisService.set(lockKey, true, LOCK_DURATION_MINUTES * 60);
        }
    }

    @Override
    public void resetLoginFailure(String username) {
        String failureKey = LOGIN_FAILURE_PREFIX + username;
        redisService.del(failureKey);
        String lockKey = ACCOUNT_LOCK_PREFIX + username;
        redisService.del(lockKey);
    }

    @Override
    public String getCurrentValidToken(String username) {
        String tokenKey = USER_VALID_TOKEN_PREFIX + username;
        Object token = redisService.get(tokenKey);
        return token != null ? token.toString() : null;
    }

    @Override
    public void setCurrentValidToken(String username, String token) {
        String tokenKey = USER_VALID_TOKEN_PREFIX + username;
        redisService.set(tokenKey, token, 604800);
    }

    @Override
    public boolean isValidCurrentToken(String username, String token) {
        String currentToken = getCurrentValidToken(username);
        return token != null && token.equals(currentToken);
    }

    @Override
    public void logout(String username) {
        String tokenKey = USER_VALID_TOKEN_PREFIX + username;
        redisService.del(tokenKey);
    }

    @Override
    public List<UmsAdmin> exportAllUsers() {
        List<UmsAdmin> adminList = list();
        if (CollUtil.isEmpty(adminList)) {
            return adminList;
        }
        return adminList.stream().map(admin -> {
            UmsAdmin desensitizedAdmin = new UmsAdmin();
            BeanUtils.copyProperties(admin, desensitizedAdmin);
            desensitizedAdmin.setUsername(DesensitizationUtil.desensitizeUsername(admin.getUsername()));
            desensitizedAdmin.setNickName(DesensitizationUtil.desensitizeNickName(admin.getNickName()));
            desensitizedAdmin.setPhone(DesensitizationUtil.desensitizePhone(admin.getPhone()));
            desensitizedAdmin.setEmail(DesensitizationUtil.desensitizeEmail(admin.getEmail()));
            desensitizedAdmin.setIcon(DesensitizationUtil.desensitizeIcon(admin.getIcon()));
            desensitizedAdmin.setPassword(DesensitizationUtil.desensitizePassword(admin.getPassword()));
            desensitizedAdmin.setRemark(DesensitizationUtil.desensitizeRemark(admin.getRemark()));
            return desensitizedAdmin;
        }).collect(Collectors.toList());
    }
}
