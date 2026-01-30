package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 组织架构树节点DTO
 *
 * @author macro
 * @since 2024-01-29
 */
@Data
@Schema(title = "OrganizationTreeNode对象", description = "组织架构树节点DTO")
public class OrganizationTreeNode {

    @Schema(title = "组织ID")
    private Long id;

    @Schema(title = "组织名称")
    private String name;

    @Schema(title = "组织编码")
    private String code;

    @Schema(title = "父级ID")
    private Long parentId;

    @Schema(title = "层级")
    private Integer level;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "状态")
    private Integer status;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "子节点列表")
    private List<OrganizationTreeNode> children;

    @Schema(description = "是否有权限")
    private Boolean hasPermission;
}