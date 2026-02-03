package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * SKU规格属性值关联表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_sku_attribute_value")
@Schema(title = "PmsSkuAttributeValue对象", description = "SKU规格属性值关联表")
public class PmsSkuAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "SKU ID")
    private Long skuId;

    @Schema(title = "规格属性ID")
    private Long attributeId;

    @Schema(title = "规格属性值ID")
    private Long attributeValueId;
}
