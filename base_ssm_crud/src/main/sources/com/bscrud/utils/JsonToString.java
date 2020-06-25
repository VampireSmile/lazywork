package com.bscrud.utils;

import java.io.*;

/**
 * @ClassName JsonToString
 * @Description TODO
 * @Author YinJiaWei
 * @Date 2020/6/17 17:30
 * @Version 1.0
 **/
public class JsonToString {

    public static String JsonFileToString(String jsonFilePath) {
        StringBuilder stringBuilder = new StringBuilder();
        String tmp;
        try (FileInputStream fin = new FileInputStream(jsonFilePath);
             Reader reader = new InputStreamReader(fin, "UTF-8");
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            while ((tmp = bufferedReader.readLine()) != null) {
                stringBuilder.append(tmp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
