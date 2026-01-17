package com.macro.mall.tiny.modules.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "规格值DTO")
public class PmsSpecValueParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "规格值ID")
    private Long id;

    @Schema(title = "规格类型ID")
    private Long specTypeId;

    @NotEmpty(message = "规格值不能为空")
    @Schema(title = "规格值")
    private String value;

    @Schema(title = "排序")
    private Integer sort;
}
