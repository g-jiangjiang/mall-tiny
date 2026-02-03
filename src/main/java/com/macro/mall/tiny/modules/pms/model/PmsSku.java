package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_sku")
@Schema(title = "PmsSku对象", description = "商品SKU表")
public class PmsSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "SPU ID")
    private Long spuId;

    @Schema(title = "SKU编码")
    private String skuCode;

    @Schema(title = "价格")
    private BigDecimal price;

    @Schema(title = "库存")
    private Integer stock;

    @Schema(title = "规格组合JSON，如{\"颜色\":\"红色\",\"尺码\":\"XL\"}")
    private String specifications;

    @Schema(title = "SKU主图")
    private String mainImage;

    @Schema(title = "状态：0->禁用；1->启用")
    private Integer status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
