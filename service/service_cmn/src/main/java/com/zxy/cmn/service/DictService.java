package com.zxy.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxy.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);


    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
