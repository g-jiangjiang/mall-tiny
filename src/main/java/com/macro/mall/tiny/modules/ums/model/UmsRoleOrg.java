package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 角色组织架构关系表
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_role_org")
@Schema(title = "UmsRoleOrg对象", description = "角色组织架构关系表")
public class UmsRoleOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "角色ID")
    private Long roleId;

    @Schema(title = "组织架构节点ID")
    private Long orgId;

    @Schema(title = "组织架构节点名称")
    private String orgName;

    @Schema(title = "组织架构层级（1-5层）")
    private Integer orgLevel;

    @Schema(title = "父级组织架构ID")
    private Long parentId;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

    @Schema(title = "配置类型：0->默认配置；1->自定义配置")
    private Integer configType;

    @Schema(title = "配置范围：0->本部门；1->全公司；2->指定部门")
    private Integer scopeType;

}
