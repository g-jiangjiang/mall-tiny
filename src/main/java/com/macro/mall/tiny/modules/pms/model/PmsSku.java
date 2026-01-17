package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_sku")
@Schema(title = "PmsSku对象", description = "SKU商品")
public class PmsSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "SPU ID")
    private Long spuId;

    @Schema(title = "SKU编码")
    private String skuCode;

    @Schema(title = "SKU名称")
    private String name;

    @Schema(title = "价格")
    private BigDecimal price;

    @Schema(title = "库存")
    private Integer stock;

    @Schema(title = "规格JSON")
    private String specs;

    @Schema(title = "SKU图片")
    private String pic;

    @Schema(title = "删除状态：0->未删除，1->已删除")
    private Integer deleteStatus;

    @Schema(title = "上架状态：0->下架，1->上架")
    private Integer publishStatus;

    @Schema(title = "销量")
    private Integer sale;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;
}
