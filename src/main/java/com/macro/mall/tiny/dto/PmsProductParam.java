package com.macro.mall.tiny.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PmsProductParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "商品名称不能为空")
    private String name;
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    @NotNull(message = "类目ID不能为空")
    private Long categoryId;
    private String description;
    private String pic;
    private Integer sort = 0;
    private String subTitle;
    private BigDecimal originalPrice;
    private Integer status = 1;
    private List<PmsProductSkuParam> skuList;
}
