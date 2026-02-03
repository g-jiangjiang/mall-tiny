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
 * 商品规格属性表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_attribute")
@Schema(title = "PmsAttribute对象", description = "商品规格属性表")
public class PmsAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "规格名称")
    private String name;

    @Schema(title = "规格类型：0->规格；1->参数")
    private Integer type;

    @Schema(title = "录入方式：0->手工录入；1->从列表选择")
    private Integer inputType;

    @Schema(title = "可选值列表，逗号分隔")
    private String selectList;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
