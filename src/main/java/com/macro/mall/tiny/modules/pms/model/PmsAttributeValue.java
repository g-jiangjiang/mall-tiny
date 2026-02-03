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
 * 商品规格属性值表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_attribute_value")
@Schema(title = "PmsAttributeValue对象", description = "商品规格属性值表")
public class PmsAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "规格属性ID")
    private Long attributeId;

    @Schema(title = "规格值")
    private String value;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
