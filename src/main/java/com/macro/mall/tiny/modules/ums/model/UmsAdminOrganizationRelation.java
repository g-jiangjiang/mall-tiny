package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 管理员组织关系表
 * </p>
 *
 * @author macro
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_admin_organization_relation")
@Schema(title = "UmsAdminOrganizationRelation对象", description = "管理员组织关系表")
public class UmsAdminOrganizationRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "管理员ID")
    private Long adminId;

    @Schema(title = "组织ID")
    private Long organizationId;

    @Schema(title = "权限范围：1->本部门；2->本部门及下级；3->全公司")
    private Integer scope;

}
