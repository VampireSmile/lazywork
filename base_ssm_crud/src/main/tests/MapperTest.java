import com.bscrud.bean.Employee;
import com.bscrud.bean.EmployeeExample;
import com.bscrud.dao.DepartmentMapper;
import com.bscrud.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @ClassName MapperTest
 * @Description TODO
 * @Author YinJiaWei
 * @Date 2020/6/16 21:12
 * @Version 1.0
 * 推荐使用spring的单元测试，可以自动注入我们需要的组件
 * 需要使用servlet3.0以上的支持
 * @ContextConfiguration指定spring配置文件的位置
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/spring-mybatis.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SqlSession sqlsession;//为了获取批量操作的mapper

    @Test
    public void testCRUD() throws FileNotFoundException, UnsupportedEncodingException {
//        System.out.println(departmentMapper);
        //1.插入几个部门
//        Department record = new Department();
//        record.setDeptName("开发部");
//        departmentMapper.insertSelective(record);
//        record.setDeptName("测试部");
//        departmentMapper.insertSelective(record);
        //2.插入员工数据
//        Employee employee = new Employee();
//        employee.setEmpName("张三");
//        employee.setEmail("772k@gmail.com");
//        employee.setGender("M");
//        employee.setdId(1);
//        employeeMapper.insertSelective(employee);
        //3.批量插入多个员工，使用可以批量操作的SQLSession
//        EmployeeMapper mapper = sqlsession.getMapper(EmployeeMapper.class);
//
//        String path = System.getProperty("user.dir") + "\\src\\main\\tests\\testData\\mapperTestData.json";
//        String string = JsonToString.JsonFileToString(path);
//        System.out.println(string);
//        Gson gson = new Gson();
//        ArrayList<testEm> list = gson.fromJson(string,
//                new TypeToken<ArrayList<testEm>>() {
//                }.getType());
//
//        testEm类中主要是这些属性：
//                  private String name;
//                  private String email;
//                  private String gender;
//                  private Integer did;
//        for (testEm e : list) {
//            System.out.println(e);
//            Employee employee = new Employee();
//            employee.setEmpName(e.getName());
//            employee.setEmail(e.getEmail());
//            employee.setGender(e.getGender());
//            employee.setdId(e.getDid());
//            mapper.insertSelective(employee);
//        }
//        System.out.println("插入完成！");
        List<Employee> res = employeeMapper.selectByExampleWithDeptName(new EmployeeExample());
        for (Employee employee : res)
            System.out.println(employee);
    }
}
