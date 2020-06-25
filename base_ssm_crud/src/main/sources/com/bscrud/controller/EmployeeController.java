package com.bscrud.controller;

import com.bscrud.bean.Employee;
import com.bscrud.dto.Msg;
import com.bscrud.enums.PageHelperEnum;
import com.bscrud.enums.ReturnValidInfo;
import com.bscrud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EmployeeController
 * @Description 处理员工CRUD请求
 * @Author YinJiaWei
 * @Date 2020/6/17 20:22
 * @Version 1.0
 **/
@Controller
public class EmployeeController {
    /**
     * rest风格的uri
     * /emp/{id} GET 查询
     * /emp POST 插入
     * /emp/{id} PUT 修改
     * /emp/{id} DELETE 删除
     */

    @Autowired
    private EmployeeService employeeService;

    /**
     * Ajax查询所有员工数据（分页查询）
     */
    @RequestMapping(value = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pageNum) {
        //引入pageHelper插件
        //在查询之前只要调用,传入页码以及每页大小,默认为5
        PageHelper.startPage(pageNum, PageHelperEnum.PAGESIZE1.getPageSize());
        //startPage后面紧跟的这个查询就是分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，只需将pageInfo交给页面即可
        //它封装了详细的分页信息，包括有我们查询出来的数据,第二个参数代表连续显示的页数，默认为5
        PageInfo page = new PageInfo(emps, PageHelperEnum.PAGESIZE1.getPageSize());
        return Msg.successInfo().withData("pageInfo", page);
    }

    /**
     * 查询所有员工数据（分页查询）
     *
     * @return list.jsp
     */
    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pageNum, Model model) {
        //引入pageHelper插件
        //在查询之前只要调用,传入页码以及每页大小,默认为5
        PageHelper.startPage(pageNum, PageHelperEnum.PAGESIZE1.getPageSize());
        //startPage后面紧跟的这个查询就是分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，只需将pageInfo交给页面即可
        //它封装了详细的分页信息，包括有我们查询出来的数据,第二个参数代表连续显示的页数
        PageInfo page = new PageInfo(emps, 5);
        model.addAttribute("pageInfo", page);
        return "list";
    }

    /**
     * 插入新员工，使用JSR303校验
     *
     * @param
     * @return
     */
    //@RequestMapping(value = "/emp", method = RequestMethod.POST)
    //@ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            Map<String, String> map = new HashMap<>();
            for (FieldError error : errors) {
                switch (error.getField()) {
                    case "empName":
                        map.put("empName", ReturnValidInfo.NAMENOTMATCH.getInfo());
                        break;
                    case "email":
                        map.put("email", ReturnValidInfo.EMAILNOTMATCH.getInfo());
                        break;
                    default:
                        break;
                }
            }
            return Msg.failInfo().withData("errorFields", map);
        }
        employeeService.saveEmploy(employee);
        return Msg.successInfo();
    }

    /**
     * 插入新员工，手动校验
     *
     * @param employee
     * @param request
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(Employee employee, HttpServletRequest request) {
        String user = request.getParameter("empName");
        String email = request.getParameter("email");
        System.out.println(employee);
        ReturnValidInfo userInfo = employeeService.userOrEmailIsValid(user, "user");
        ReturnValidInfo emailInfo = employeeService.userOrEmailIsValid(email, "email");
        if (userInfo.getCode() == 100 && emailInfo.getCode() == 200) {
            employeeService.saveEmploy(employee);
            return Msg.successInfo();
        }
        Map<String, String> map = new HashMap<>();
        if (userInfo.getCode() > 100) map.put("empName", userInfo.getInfo());
        if (emailInfo.getCode() > 200) map.put("email", emailInfo.getInfo());
        return Msg.failInfo().withData("errorFields", map);
    }

    /**
     * 检验用户名和email是否可用
     *
     * @param data
     * @param userOrEmail
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    public Msg checkUser(String data, String userOrEmail) {
        ReturnValidInfo info = employeeService.userOrEmailIsValid(data, userOrEmail);
        if (info == null) return Msg.failInfo().withData("valid_msg_code", -1)
                .withData("valid_msg_body", "获取信息失败");
        return Msg.successInfo().withData("valid_msg_code", info.getCode())
                .withData("valid_msg_body", info.getInfo());
    }

    /**
     * 获取单个员工信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmpInfo(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmpInfo(id);
        return Msg.successInfo().withData("empInfo", employee);
    }

    /**
     * 更新员工
     * 注意这里的占位符与employee里面的empId对应
     * 如果直接发PUT类型的ajax请求，会导致请求体中有数据，
     * 但是employee对象只接受到empId的值，其他值都为null,即封装不上。
     * 原因：
     * tomcat:将请求体中数据封装为一个map
     * request.getParameter("empName")就是从该map中取值
     * springMVC封装POJO对象时，会利用request.getParameter(...)方法给POJO对象每个属性赋值
     * Ajax发送请求引发的血案：
     * PUT请求，请求体中的数据，request.getParameter(...)都拿不到，
     * Tomcat一看是put就不会封装请求体重的数据为map，只有post形式的请求
     * 才会封装请求体为map
     * 解决上述问题：在web.xml中配置HttpPutFormContentFilter过滤器即可解决
     * HttpPutFormContentFilter会帮我们封装请求体为map，重新包装request请求，重写getParameter方法
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    @ResponseBody
    public Msg updateEmp(Employee employee) {
        System.out.println(employee);
        employeeService.updateEmp(employee);
        return Msg.successInfo();
    }

    /**
     * 员工删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg delMoreEmps(@PathVariable("ids") String ids) {
        if (ids.contains("-")) {
            //删除多个员工
            String[] str_ids = ids.split("-");
            List<Integer> list_ids = new ArrayList<>();
            for (String id : str_ids) {
                list_ids.add(Integer.valueOf(id));
            }
            employeeService.deleteBatch(list_ids);
        } else {
            //删除单个员工
            employeeService.delSingleEmp(Integer.valueOf(ids));
        }
        return Msg.successInfo();
    }

}
