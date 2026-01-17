package com.macro.mall.tiny.modules.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "类目DTO")
public class PmsCategoryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "类目ID")
    private Long id;

    @Schema(title = "父类目ID，0表示一级类目")
    private Long parentId;

    @NotEmpty(message = "类目名称不能为空")
    @Schema(title = "类目名称")
    private String name;

    @Schema(title = "类目层级，0表示一级，1表示二级，2表示三级")
    private Integer level;

    @Schema(title = "类目单位")
    private String productUnit;

    @Schema(title = "是否显示在导航栏")
    private Integer navStatus;

    @Schema(title = "显示状态")
    private Integer showStatus;

    @Schema(title = "类目图标")
    private String icon;

    @Schema(title = "类目关键词")
    private String keywords;

    @Schema(title = "类目描述")
    private String description;

    @Schema(title = "排序")
    private Integer sort;
}
