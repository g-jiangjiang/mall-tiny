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
 * 用户锁定状态表
 * </p>
 *
 * @author macro
 * @since 2024-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_admin_locked")
@Schema(title = "UmsAdminLocked对象", description = "用户锁定状态表")
public class UmsAdminLocked implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "锁定时间")
    private Date lockTime;

    @Schema(title = "解锁时间")
    private Date unlockTime;

    @Schema(title = "锁定原因")
    private String lockReason;

}