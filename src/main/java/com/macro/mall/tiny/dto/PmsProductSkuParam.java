package com.macro.mall.tiny.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PmsProductSkuParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    private Integer stock = 0;
    private String pic;
    private String sp1;
    private String sp2;
    private String sp3;
    private String keyName;
}
