package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminLocked;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户锁定状态表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2024-01-30
 */
@Mapper
public interface UmsAdminLockedMapper extends BaseMapper<UmsAdminLocked> {

    /**
     * 根据用户名查询锁定记录
     * @param username 用户名
     * @return 锁定记录
     */
    UmsAdminLocked selectByUsername(@Param("username") String username);

    /**
     * 根据用户名删除锁定记录
     * @param username 用户名
     * @return 删除数量
     */
    int deleteByUsername(@Param("username") String username);

}