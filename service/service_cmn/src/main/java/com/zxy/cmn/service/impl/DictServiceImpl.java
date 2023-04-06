package com.zxy.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxy.cmn.listener.DictListener;
import com.zxy.cmn.mapper.DictSetMapper;
import com.zxy.cmn.service.DictService;
import com.zxy.yygh.model.cmn.Dict;
import com.zxy.yygh.vo.cmn.DictEeVo;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : DictServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/4  17:43
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictSetMapper, Dict> implements DictService {

    @Override
    //根据数据id查询子数据列表
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //向list集合每个dict对象中设置hasChildren
        for (Dict dict:dictList){
            Long dictId = dict.getId();
            boolean ischild = this.isChildren(dictId);
            dict.setHasChildren(ischild);
        }
        return dictList;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for(Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict,dictVo);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    @CacheEvict(value = "dict", allEntries=true)//清空缓存内容
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new  DictListener(baseMapper)).sheet().doRead();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if (StringUtils.isEmpty(dictCode)){
            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(dictQueryWrapper);
            return dict.getName();
        }
        Dict codeDict = this.getDictByDictCode(dictCode);
        Long parentId = codeDict.getId();
        Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                .eq("parent_id", parentId)
                .eq("value", value));
        return finalDict.getName();
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictcode获取对应id
        Dict dict = this.getDictByDictCode(dictCode);
        List<Dict> childData = this.findChildData(dict.getId());
        //根据id获取下层子节点
        return childData;
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code",dictCode);
        Dict codeDict = baseMapper.selectOne(dictQueryWrapper);
        return codeDict;
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0;
    }
}
