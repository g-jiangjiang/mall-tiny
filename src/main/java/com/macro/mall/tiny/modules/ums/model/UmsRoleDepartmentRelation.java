package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_role_department_relation")
@Schema(title = "UmsRoleDepartmentRelation对象", description = "角色与组织架构关系表")
public class UmsRoleDepartmentRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "角色ID")
    private Long roleId;

    @Schema(title = "部门ID")
    private Long departmentId;

    public UmsRoleDepartmentRelation() {}

    public UmsRoleDepartmentRelation(Long roleId, Long departmentId) {
        this.roleId = roleId;
        this.departmentId = departmentId;
    }
}
