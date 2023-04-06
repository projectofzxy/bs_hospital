package easyexceltest;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : testwrite  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/4  19:25
 */
public class testwrite {
    public static void main(String[] args) {
        String filenmae="E:\\xls\\01.xlsx";
        List<UserData> list=new ArrayList<>();
        for (int i = 0; i < 10 ; i++){
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("zxy"+i);
            list.add(userData);
        }
        EasyExcel.write(filenmae,UserData.class).sheet("用户信息").doWrite(list);
    }
}
