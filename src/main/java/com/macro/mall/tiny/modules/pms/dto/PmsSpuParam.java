package com.macro.mall.tiny.modules.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * SPU创建/更新参数
 */
@Data
public class PmsSpuParam {

    @Schema(title = "SPU ID，更新时传入")
    private Long id;

    @NotBlank(message = "SPU名称不能为空")
    @Schema(title = "SPU名称", required = true)
    private String name;

    @Schema(title = "品牌ID")
    private Long brandId;

    @NotNull(message = "类目ID不能为空")
    @Schema(title = "类目ID", required = true)
    private Long categoryId;

    @Schema(title = "商品详情")
    private String detail;

    @Schema(title = "主图")
    private String mainImage;

    @Schema(title = "副图，逗号分隔")
    private String subImages;

    @Schema(title = "状态：0->下架；1->上架")
    private Integer status;

    @Schema(title = "规格ID列表")
    private List<Long> attributeIds;
}
