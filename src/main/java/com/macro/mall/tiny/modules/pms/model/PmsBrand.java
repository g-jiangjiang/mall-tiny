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
 * 商品品牌表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_brand")
@Schema(title = "PmsBrand对象", description = "商品品牌表")
public class PmsBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "品牌名称")
    private String name;

    @Schema(title = "品牌logo")
    private String logo;

    @Schema(title = "品牌描述")
    private String description;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "显示状态：0->不显示；1->显示")
    private Integer status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
