package com.macro.mall.tiny.modules.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(title = "SKU商品DTO")
public class PmsSkuParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "SKU ID")
    private Long id;

    @Schema(title = "SPU ID")
    private Long spuId;

    @NotEmpty(message = "SKU编码不能为空")
    @Schema(title = "SKU编码")
    private String skuCode;

    @Schema(title = "SKU名称")
    private String name;

    @NotNull(message = "价格不能为空")
    @Schema(title = "价格")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Schema(title = "库存")
    private Integer stock;

    @Schema(title = "规格JSON")
    private String specs;

    @Schema(title = "SKU图片")
    private String pic;
}
