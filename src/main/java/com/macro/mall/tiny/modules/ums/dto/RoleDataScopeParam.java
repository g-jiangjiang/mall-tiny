package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "RoleDataScopeParam对象", description = "角色数据范围参数")
public class RoleDataScopeParam {

    @Schema(title = "角色ID")
    private Long roleId;

    @Schema(title = "数据范围：1->全部数据权限；2->自定义数据权限；3->本部门数据权限；4->本部门及以下数据权限；5->仅本人数据权限")
    private Integer dataScope;

    @Schema(title = "部门ID，当数据范围为3或4时使用")
    private Long deptId;

    @Schema(title = "自定义数据权限的部门ID列表，当数据范围为2时使用")
    private List<Long> deptIds;

}
