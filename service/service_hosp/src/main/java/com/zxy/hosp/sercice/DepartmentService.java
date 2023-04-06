package com.zxy.hosp.sercice;


import com.zxy.yygh.model.hosp.Department;
import com.zxy.yygh.vo.hosp.DepartmentQueryVo;
import com.zxy.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface DepartmentService {
    void save(Map<String, Object> objectMap);

    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
