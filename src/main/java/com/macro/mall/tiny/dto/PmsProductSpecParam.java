package com.macro.mall.tiny.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class PmsProductSpecParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "规格名称不能为空")
    private String name;
    private Integer sort = 0;
    private Long categoryId;
    private List<PmsProductSpecValueParam> valueList;
}
