package com.bscrud.controller;

import com.bscrud.bean.Department;
import com.bscrud.dto.Msg;
import com.bscrud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName DepartmentController
 * @Description TODO
 * @Author YinJiaWei
 * @Date 2020/6/20 17:07
 * @Version 1.0
 **/
@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //请求获得所有部门信息
    @RequestMapping("/depts")
    @ResponseBody
    public Msg getAllDepts() {
        List<Department> list = departmentService.getDeptInfo();
        return Msg.successInfo().withData("depts", list);
    }

}
