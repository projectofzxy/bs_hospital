package com.zxy.cmn.controller;

import com.zxy.cmn.service.DictService;
import com.zxy.common.result.Result;
import com.zxy.yygh.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName : DictController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/4  17:45
 */
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;
    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable long id){
        List<Dict> list=dictService.findChildData(id);
        return Result.ok(list);
    }

    @ApiOperation(value="导出")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);
    }
    @ApiOperation(value = "导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }
    //根据dicode查询下层节点
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(@PathVariable String dictCode){
        List<Dict> list=dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        String dictName=dictService.getDictName(dictCode,value);
        return dictName;
    }
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value){
        String dictName=dictService.getDictName("",value);
        return dictName;
    }
}
