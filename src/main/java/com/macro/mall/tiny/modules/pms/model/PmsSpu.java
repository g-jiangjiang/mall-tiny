package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品SPU表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_spu")
@Schema(title = "PmsSpu对象", description = "商品SPU表")
public class PmsSpu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "SPU名称")
    private String name;

    @Schema(title = "品牌ID")
    private Long brandId;

    @Schema(title = "类目ID")
    private Long categoryId;

    @Schema(title = "商品详情")
    private String detail;

    @Schema(title = "主图")
    private String mainImage;

    @Schema(title = "副图，逗号分隔")
    private String subImages;

    @Schema(title = "状态：0->下架；1->上架")
    private Integer status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
