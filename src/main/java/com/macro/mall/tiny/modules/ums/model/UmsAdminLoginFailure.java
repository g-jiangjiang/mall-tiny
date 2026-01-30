package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户登录失败记录表
 * </p>
 *
 * @author macro
 * @since 2024-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_admin_login_failure")
@Schema(title = "UmsAdminLoginFailure对象", description = "用户登录失败记录表")
public class UmsAdminLoginFailure implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "登录IP")
    private String ip;

    @Schema(title = "失败时间")
    private Date failureTime;

    @Schema(title = "失败原因")
    private String failureReason;

}