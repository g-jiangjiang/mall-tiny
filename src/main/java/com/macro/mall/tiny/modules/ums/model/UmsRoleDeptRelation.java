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
@TableName("ums_role_dept_relation")
@Schema(title = "UmsRoleDeptRelation对象", description = "角色部门关联表")
public class UmsRoleDeptRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "角色ID")
    private Long roleId;

    @Schema(title = "部门ID")
    private Long deptId;

    @Schema(title = "创建时间")
    private Date createTime;

}
