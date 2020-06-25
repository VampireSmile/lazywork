package com.bscrud.service;

import com.bscrud.bean.Employee;
import com.bscrud.bean.EmployeeExample;
import com.bscrud.dao.EmployeeMapper;
import com.bscrud.enums.ReturnValidInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName EmployeeService
 * @Description TODO
 * @Author YinJiaWei
 * @Date 2020/6/17 20:28
 * @Version 1.0
 **/
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper mapper;

    /**
     * 查询所有员工
     *
     * @return List<Employee>
     */
    public List<Employee> getAll() {
        return mapper.selectByExampleWithDeptName(null);
    }

    /**
     * 插入员工
     * <p>
     * 1.如果想要支持JSR303需要导入hibernate validator
     * 2.需要给Employee实体中添加校验注解
     *
     * @param employee
     * @return
     */
    public void saveEmploy(Employee employee) {
        mapper.insertSelective(employee);
    }

    /**
     * 检验用户名和email
     *
     * @param data
     * @return
     */
    public ReturnValidInfo userOrEmailIsValid(String data, String userOrEmail) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        String regName = "(^[a-zA-Z0-9_-]{3,12}$)|(^[\\d\\u2E80-\\u9FFF]{2,6}$)";
        String regEmail = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
        if (userOrEmail.equals("user")) {
            if (data.matches(regName)) {
                criteria.andEmpNameEqualTo(data);
                long count = mapper.countByExample(employeeExample);
                return count == 0 ? ReturnValidInfo.NAMEOK : ReturnValidInfo.NAMEEXISTS;
            } else return ReturnValidInfo.NAMENOTMATCH;
        } else if (userOrEmail.equals("email")) {
            if (data.matches(regEmail)) {
                criteria.andEmailEqualTo(data);
                long count = mapper.countByExample(employeeExample);
                return count == 0 ? ReturnValidInfo.EMAILOK : ReturnValidInfo.EMAILEXISTS;
            } else return ReturnValidInfo.EMAILNOTMATCH;
        }
        return null;
    }

    /**
     * 按照员工id获取员工信息
     *
     * @param id
     * @return
     */
    public Employee getEmpInfo(Integer id) {
        Employee employee = mapper.selectByPrimaryKey(id);
        return employee;
    }

    /**
     * 员工更新
     *
     * @param employee
     */
    public int updateEmp(Employee employee) {
        return mapper.updateByPrimaryKeySelective(employee);
    }

    /**
     * 员工删除
     *
     * @param id
     * @return
     */
    public int delSingleEmp(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public int deleteBatch(List<Integer> ids) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpIdIn(ids);
        return mapper.deleteByExample(example);
    }
}
