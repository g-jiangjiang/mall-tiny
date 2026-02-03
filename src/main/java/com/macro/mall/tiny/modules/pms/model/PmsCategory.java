package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品类目表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_category")
@Schema(title = "PmsCategory对象", description = "商品类目表")
public class PmsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "父类目ID，0表示顶级类目")
    private Long parentId;

    @Schema(title = "类目名称")
    private String name;

    @Schema(title = "类目级别：1->一级；2->二级；3->三级")
    private Integer level;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "类目图标")
    private String icon;

    @Schema(title = "类目描述")
    private String description;

    @Schema(title = "显示状态：0->不显示；1->显示")
    private Integer status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
