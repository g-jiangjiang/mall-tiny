package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 组织架构表
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_organization")
@Schema(title = "UmsOrganization对象", description = "组织架构表")
public class UmsOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "组织名称")
    private String name;

    @Schema(title = "组织编码")
    private String code;

    @Schema(title = "父级ID，0表示根节点")
    private Long parentId;

    @Schema(title = "层级：1->公司；2->部门；3->小组；4->岗位；5->其他")
    private Integer level;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;
}