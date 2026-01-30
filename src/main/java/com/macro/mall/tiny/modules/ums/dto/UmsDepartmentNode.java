package com.macro.mall.tiny.modules.ums.dto;

import com.macro.mall.tiny.modules.ums.model.UmsDepartment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "UmsDepartmentNode对象", description = "组织架构树节点")
public class UmsDepartmentNode extends UmsDepartment {

    @Schema(title = "子级部门")
    private List<UmsDepartmentNode> children;
}
