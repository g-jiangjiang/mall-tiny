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
 * 角色组织架构关联表
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_role_organization_relation")
@Schema(title = "UmsRoleOrganizationRelation对象", description = "角色组织架构关联表")
public class UmsRoleOrganizationRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "角色ID")
    private Long roleId;

    @Schema(title = "组织架构ID")
    private Long organizationId;

    @Schema(title = "权限范围：0->仅当前组织；1->当前组织及下级组织")
    private Integer scope;

    @Schema(title = "创建时间")
    private Date createTime;
}