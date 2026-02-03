package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 组织架构节点DTO
 * Created by macro on 2024/01/01.
 */
@Data
@Schema(title = "UmsOrgNodeDto", description = "组织架构节点DTO")
public class UmsOrgNodeDto {

    @Schema(title = "节点ID")
    private Long id;

    @Schema(title = "节点名称")
    private String name;

    @Schema(title = "节点层级（1-5层，1为全公司层）")
    private Integer level;

    @Schema(title = "父级节点ID")
    private Long parentId;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "启用状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "节点类型：0->全公司；1->部门管理员；2->部门；3->小组；4->岗位")
    private Integer nodeType;

    @Schema(title = "备注")
    private String note;

    @Schema(title = "子节点列表")
    private List<UmsOrgNodeDto> children;

    @Schema(title = "是否拥有权限")
    private Boolean hasPermission;

    @Schema(title = "配置类型：0->默认配置；1->自定义配置")
    private Integer configType;

    @Schema(title = "配置范围：0->本部门；1->全公司；2->指定部门")
    private Integer scopeType;
}
