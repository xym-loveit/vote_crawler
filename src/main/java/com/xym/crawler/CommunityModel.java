package com.xym.crawler;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.*;

/**
 * @version v1.0
 * @type：CommunityModel.java
 * @description：TODO
 * @author：xym
 * @date：2020/4/1 20:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityModel {

    @ExcelProperty(index = 1, value = "小区名称")
    @ColumnWidth(value = 30)
    private String name;
    @ColumnWidth(value = 15)
    @ExcelProperty(index = 2, value = "票数")
    private Integer votes;
    @ColumnWidth(value = 15)
    @ExcelProperty(index = 0, value = "编号")
    private String no;
}
