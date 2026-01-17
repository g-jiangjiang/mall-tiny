package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_category")
@Schema(title = "PmsCategory对象", description = "商品类目")
public class PmsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "父类目ID，0表示一级类目")
    private Long parentId;

    @Schema(title = "类目名称")
    private String name;

    @Schema(title = "类目层级，0表示一级，1表示二级，2表示三级")
    private Integer level;

    @Schema(title = "类目数量")
    private Integer productCount;

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

    @TableField(exist = false)
    @Schema(title = "子类目")
    private List<PmsCategory> children;
}
