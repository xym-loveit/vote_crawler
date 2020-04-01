package com.xym.crawler;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version v1.0
 * @type：PersonModel.java
 * @description：TODO
 * @author：xym
 * @date：2020/4/1 20:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonModel {
    @ExcelProperty(index = 1, value = "姓名")
    @ColumnWidth(value = 15)
    private String name;
    @ColumnWidth(value = 15)
    @ExcelProperty(index = 2, value = "票数")
    private Integer votes;
    @ColumnWidth(value = 15)
    @ExcelProperty(index = 0, value = "编号")
    private String no;
}
