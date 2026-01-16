package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(title = "UmsDepartmentParam对象", description = "部门参数")
public class UmsDepartmentParam {

    @Schema(title = "部门ID，更新时必填")
    private Long id;

    @NotNull(message = "父部门ID不能为空")
    @Schema(title = "父部门ID")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Schema(title = "部门名称")
    private String name;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "描述")
    private String description;

}
