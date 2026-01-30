package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminLoginFailure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户登录失败记录表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2024-01-30
 */
@Mapper
public interface UmsAdminLoginFailureMapper extends BaseMapper<UmsAdminLoginFailure> {

    /**
     * 获取指定时间范围内的登录失败次数
     * @param username 用户名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 失败次数
     */
    int countFailureByTimeRange(@Param("username") String username, 
                                @Param("startTime") Date startTime, 
                                @Param("endTime") Date endTime);

    /**
     * 删除指定时间之前的失败记录
     * @param beforeTime 时间点
     * @return 删除数量
     */
    int deleteFailureBeforeTime(@Param("beforeTime") Date beforeTime);

    /**
     * 获取最近的失败记录列表
     * @param username 用户名
     * @param limit 数量限制
     * @return 失败记录列表
     */
    List<UmsAdminLoginFailure> getRecentFailures(@Param("username") String username, 
                                                 @Param("limit") int limit);
}