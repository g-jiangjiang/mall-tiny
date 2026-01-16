package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "UmsDepartmentNode对象", description = "组织架构树节点")
public class UmsDepartmentNode {

    @Schema(title = "部门ID")
    private Long id;

    @Schema(title = "父部门ID")
    private Long parentId;

    @Schema(title = "部门名称")
    private String name;

    @Schema(title = "层级")
    private Integer level;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "状态")
    private Integer status;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "子部门列表")
    private List<UmsDepartmentNode> children = new ArrayList<>();

}
