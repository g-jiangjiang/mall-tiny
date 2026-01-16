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
 * 组织架构表
 * </p>
 *
 * @author macro
 * @since 2026-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_organization")
@Schema(title = "UmsOrganization对象", description = "组织架构表")
public class UmsOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "组织名称")
    private String name;

    @Schema(title = "父级ID")
    private Long parentId;

    @Schema(title = "层级(1-5)")
    private Integer level;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "启用状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "子组织")
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private List<UmsOrganization> children;


}
