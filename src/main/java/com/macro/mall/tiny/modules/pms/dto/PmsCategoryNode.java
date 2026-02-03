package com.macro.mall.tiny.modules.pms.dto;

import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类目树节点DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsCategoryNode extends PmsCategory {

    @Schema(title = "子类目列表")
    private List<PmsCategoryNode> children;
}
