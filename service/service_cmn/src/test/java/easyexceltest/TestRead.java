package easyexceltest;

import com.alibaba.excel.EasyExcel;

/**
 * @ClassName : TestRead  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/4  19:41
 */
public class TestRead {
    public static void main(String[] args) {
        String filenmae="E:\\xls\\01.xlsx";
        EasyExcel.read(filenmae,UserData.class,new ExeclListener()).sheet().doRead();
    }
}
