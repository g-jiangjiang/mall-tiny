package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_brand")
@Schema(title = "PmsBrand对象", description = "商品品牌")
public class PmsBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "品牌名称")
    private String name;

    @Schema(title = "品牌首字母")
    private String firstLetter;

    @Schema(title = "品牌LOGO")
    private String logo;

    @Schema(title = "品牌大图")
    private String bigPic;

    @Schema(title = "品牌故事")
    private String brandStory;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "是否显示")
    private Integer showStatus;

    @Schema(title = "品牌制造商")
    private Integer factoryStatus;

    @Schema(title = "删除状态：0->未删除，1->已删除")
    private Integer deleteStatus;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;
}
