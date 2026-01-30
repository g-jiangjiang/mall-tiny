package com.macro.mall.tiny.modules.ums.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class UmsAdminExcelDto {

    @ExcelProperty(value = "用户ID", index = 0)
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty(value = "用户名", index = 1)
    @ColumnWidth(15)
    private String username;

    @ExcelIgnore
    private String password;

    @ExcelProperty(value = "昵称", index = 2)
    @ColumnWidth(15)
    private String nickName;

    @ExcelProperty(value = "邮箱", index = 3)
    @ColumnWidth(25)
    private String email;

    @ExcelProperty(value = "状态", index = 4)
    @ColumnWidth(10)
    private String statusDesc;

    @ExcelProperty(value = "创建时间", index = 5)
    @ColumnWidth(20)
    private Date createTime;

    @ExcelProperty(value = "最后登录时间", index = 6)
    @ColumnWidth(20)
    private Date loginTime;

    @ExcelProperty(value = "备注", index = 7)
    @ColumnWidth(30)
    private String note;
}
