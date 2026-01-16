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
 * 用户组织权限范围表
 * </p>
 *
 * @author macro
 * @since 2025-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_admin_org_scope")
@Schema(title = "UmsAdminOrgScope对象", description = "用户组织权限范围表")
public class UmsAdminOrgScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "用户ID")
    private Long adminId;

    @Schema(title = "组织ID")
    private Long orgId;

    @Schema(title = "权限类型：1->管理全公司；2->管理本部门")
    private Integer scopeType;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

}
