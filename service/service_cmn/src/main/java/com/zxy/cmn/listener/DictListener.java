package com.zxy.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zxy.cmn.mapper.DictSetMapper;
import com.zxy.yygh.model.cmn.Dict;
import com.zxy.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * @ClassName : DictListener  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/4  20:08
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictSetMapper dictSetMapper;

    public DictListener(DictSetMapper dictSetMapper) {
        this.dictSetMapper = dictSetMapper;
    }


    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictSetMapper.insert(dict);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
