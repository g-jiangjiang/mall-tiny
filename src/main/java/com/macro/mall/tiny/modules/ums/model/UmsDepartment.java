package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_department")
@Schema(title = "UmsDepartment对象", description = "组织架构表")
public class UmsDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "父级ID，0表示顶级")
    private Long parentId;

    @Schema(title = "部门名称")
    private String name;

    @Schema(title = "层级：0->全公司，1->一级部门，2->二级部门，3->三级部门，4->四级部门")
    private Integer level;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "启用状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "描述")
    private String description;
}
