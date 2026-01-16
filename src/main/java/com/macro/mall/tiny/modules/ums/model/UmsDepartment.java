package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_department")
@Schema(title = "UmsDepartment对象", description = "组织架构表")
public class UmsDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "父部门ID，0表示顶级部门（全公司）")
    private Long parentId;

    @Schema(title = "部门名称")
    private String name;

    @Schema(title = "层级：1-全公司，2-5-子部门")
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
