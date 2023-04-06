package easyexceltest;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName : UserData  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/4  19:23
 */
@Data
public class UserData {
    @ExcelProperty(value = "用户编号",index = 0)
    private int uid;
    @ExcelProperty(value = "用户名称",index = 1)
    private String username;
}
