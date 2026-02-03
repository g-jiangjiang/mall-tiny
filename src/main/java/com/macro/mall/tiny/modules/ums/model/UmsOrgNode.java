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
 * 组织架构节点表
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_org_node")
@Schema(title = "UmsOrgNode对象", description = "组织架构节点表")
public class UmsOrgNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "节点名称")
    private String name;

    @Schema(title = "节点层级（1-5层，1为全公司层）")
    private Integer level;

    @Schema(title = "父级节点ID")
    private Long parentId;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

    @Schema(title = "启用状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "节点类型：0->全公司；1->部门管理员；2->部门；3->小组；4->岗位")
    private Integer nodeType;

    @Schema(title = "备注")
    private String note;

}
