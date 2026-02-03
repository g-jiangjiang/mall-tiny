package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 角色组织架构配置DTO
 * Created by macro on 2024/01/01.
 */
@Data
@Schema(title = "UmsRoleOrgDto", description = "角色组织架构配置DTO")
public class UmsRoleOrgDto {

    @Schema(title = "角色ID")
    private Long roleId;

    @Schema(title = "角色名称")
    private String roleName;

    @Schema(title = "配置类型：0->默认配置；1->自定义配置")
    private Integer configType;

    @Schema(title = "配置范围：0->本部门；1->全公司；2->指定部门")
    private Integer scopeType;

    @Schema(title = "组织架构树")
    private List<UmsOrgNodeDto> orgTree;

    @Schema(title = "选中的组织架构节点ID列表")
    private List<Long> selectedOrgIds;
}
