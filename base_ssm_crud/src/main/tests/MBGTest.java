import com.bscrud.utils.GenerateMybatisCode;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @ClassName MBGTest
 * @Description TODO
 * @Author YinJiaWei
 * @Date 2020/6/15 21:27
 * @Version 1.0
 **/
public class MBGTest {
    public static void main(String[] args) throws InterruptedException, SQLException,
            InvalidConfigurationException, XMLParserException, IOException {
        //执行mybatis逆向工程生成代码，其中参数是mbg配置文件
        GenerateMybatisCode.Generate(null);
    }
}
