package com.macro.mall.tiny.modules.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(title = "SPU商品DTO")
public class PmsSpuParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "SPU ID")
    private Long id;

    @NotEmpty(message = "商品名称不能为空")
    @Schema(title = "商品名称")
    private String name;

    @Schema(title = "品牌ID")
    private Long brandId;

    @Schema(title = "商品类目ID")
    private Long categoryId;

    @Schema(title = "商品详情")
    private String detail;

    @Schema(title = "商品主图")
    private String pic;

    @Schema(title = "商品单位")
    private String unit;

    @Schema(title = "商品描述")
    private String description;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "SKU列表")
    private List<PmsSkuParam> skuList;
}
