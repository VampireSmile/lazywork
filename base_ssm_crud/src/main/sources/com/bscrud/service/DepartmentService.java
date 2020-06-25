package com.bscrud.service;

import com.bscrud.bean.Department;
import com.bscrud.dao.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName DepartmentService
 * @Description TODO
 * @Author YinJiaWei
 * @Date 2020/6/20 17:07
 * @Version 1.0
 **/
@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> getDeptInfo() {
        List<Department> departmentList = departmentMapper.selectByExample(null);
        return departmentList;
    }


}
