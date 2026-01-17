package com.macro.mall.tiny.modules.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(title = "规格类型DTO")
public class PmsSpecTypeParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "规格类型ID")
    private Long id;

    @NotEmpty(message = "规格类型名称不能为空")
    @Schema(title = "规格类型名称")
    private String name;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "规格值列表")
    private List<PmsSpecValueParam> specValueList;
}
