package com.macro.mall.tiny.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class PmsCategoryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "类目名称不能为空")
    private String name;
    private Long parentId = 0L;
    private Integer sort = 0;
    private String icon;
    private String description;
    private Integer status = 1;
}
