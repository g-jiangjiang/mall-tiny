package com.macro.mall.tiny.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class PmsProductSpecValueParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "规格值不能为空")
    private String value;
    private Integer sort = 0;
}
