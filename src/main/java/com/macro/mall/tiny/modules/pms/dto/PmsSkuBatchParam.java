package com.macro.mall.tiny.modules.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * SKU批量配置参数
 */
@Data
public class PmsSkuBatchParam {

    @NotNull(message = "SPU ID不能为空")
    @Schema(title = "SPU ID", required = true)
    private Long spuId;

    @Schema(title = "SKU列表（新增和更新混合）")
    private List<PmsSkuItem> skuList;

    @Schema(title = "需要删除的SKU ID列表")
    private List<Long> deleteSkuIds;

    /**
     * SKU单项
     */
    @Data
    public static class PmsSkuItem {

        @Schema(title = "SKU ID，更新时传入")
        private Long id;

        @Schema(title = "SKU编码")
        private String skuCode;

        @Schema(title = "价格")
        private java.math.BigDecimal price;

        @Schema(title = "库存")
        private Integer stock;

        @Schema(title = "规格组合JSON，如{\"颜色\":\"红色\",\"尺码\":\"XL\"}")
        private String specifications;

        @Schema(title = "SKU主图")
        private String mainImage;

        @Schema(title = "状态：0->禁用；1->启用")
        private Integer status;
    }
}
