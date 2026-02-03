package com.macro.mall.tiny.modules.ums.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.date.DateStringConverter;
import lombok.Data;

import java.util.Date;

/**
 * 用户导出DTO
 * Created by macro on 2024/01/01.
 */
@Data
public class UmsAdminExportDto {

    @ExcelProperty("用户ID")
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String username;

    @ExcelProperty("昵称")
    @ColumnWidth(20)
    private String nickName;

    @ExcelProperty("邮箱")
    @ColumnWidth(25)
    private String email;

    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String note;

    @ExcelProperty(value = "创建时间", converter = DateStringConverter.class)
    @ColumnWidth(20)
    private Date createTime;

    @ExcelProperty(value = "最后登录时间", converter = DateStringConverter.class)
    @ColumnWidth(20)
    private Date loginTime;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String statusText;
}
