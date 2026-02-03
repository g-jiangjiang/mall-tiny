package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * SPU规格关联表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_spu_attribute")
@Schema(title = "PmsSpuAttribute对象", description = "SPU规格关联表")
public class PmsSpuAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "SPU ID")
    private Long spuId;

    @Schema(title = "规格属性ID")
    private Long attributeId;
}
