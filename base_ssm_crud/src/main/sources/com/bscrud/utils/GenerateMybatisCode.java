package com.bscrud.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GenerateMybatisCode
 * @Description TODO
 * @Author YinJiaWei
 * @Date 2020/6/17 20:02
 * @Version 1.0
 **/
public class GenerateMybatisCode {
    private static final String DEFAULT_MBGFILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\mbg.xml";

    public static void Generate(String mbgFilePath) throws IOException, XMLParserException,
            InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        String curPath = mbgFilePath == null ? DEFAULT_MBGFILEPATH : mbgFilePath;
        File configFile = new File(curPath);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        System.out.println("Generate success!!!");
    }
}
