package easyexceltest;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @ClassName : ExeclListener  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/4  19:37
 */
public class ExeclListener extends AnalysisEventListener<UserData> {
    //一行一行读取，从第二行读取
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext) {
        System.out.println(userData);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息"+headMap);
    }

    //读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}
