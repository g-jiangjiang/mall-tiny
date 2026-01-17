package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_spu")
@Schema(title = "PmsSpu对象", description = "SPU商品")
public class PmsSpu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "商品名称")
    private String name;

    @Schema(title = "品牌ID")
    private Long brandId;

    @Schema(title = "品牌名称")
    @TableField(exist = false)
    private String brandName;

    @Schema(title = "商品类目ID")
    private Long categoryId;

    @Schema(title = "商品类目名称")
    @TableField(exist = false)
    private String categoryName;

    @Schema(title = "商品详情")
    private String detail;

    @Schema(title = "商品主图")
    private String pic;

    @Schema(title = "商品单位")
    private String unit;

    @Schema(title = "商品描述")
    private String description;

    @Schema(title = "删除状态：0->未删除，1->已删除")
    private Integer deleteStatus;

    @Schema(title = "上架状态：0->下架，1->上架")
    private Integer publishStatus;

    @Schema(title = "新品状态：0->不是新品，1->是新品")
    private Integer newStatus;

    @Schema(title = "推荐状态：0->不推荐，1->推荐")
    private Integer recommandStatus;

    @Schema(title = "审核状态：0->未审核，1->已审核")
    private Integer verifyStatus;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "销量")
    private Integer sale;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @Schema(title = "SKU列表")
    private List<PmsSku> skuList;
}
