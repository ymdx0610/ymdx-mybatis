package com.ymdx.mybatis.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SqlUtil
 * @Description: TODO
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-15 10:31
 * @Version: 1.0
 **/
public class SqlUtil {

    /**
     * 获取insert 参数名称
     * @param sql
     * @return
     */
    public static String[] sqlInsertParameter(String sql) {
        int startIndex = sql.indexOf("values");
        int endIndex = sql.length();
        String substring = sql.substring(startIndex + 6, endIndex).
                replace("(", "").
                replace(")", "").
                replace("#{", "").
                replace("}", "");
        String[] split = substring.split(",");
        return split;
    }

    /**
     * 获取select 参数名称
     * @param sql
     * @return
     */
    public static List<String> sqlSelectParameter(String sql) {
        int startIndex = sql.indexOf("where");
        int endIndex = sql.length();
        String substring = sql.substring(startIndex + 5, endIndex);
        String[] split = substring.split("and");
        List<String> listArr = new ArrayList<>();
        for (String string : split) {
            String[] sp2 = string.split("=");
            listArr.add(sp2[0].trim());
        }
        return listArr;
    }

    /**
     * 将sql语句中的#{paramName}替换成?
     * @param sql
     * @param paramName
     * @return
     */
    public static String parameQuestion(String sql, String[] paramName) {
        for (int i = 0; i < paramName.length; i++) {
            String string = paramName[i];
            sql = sql.replace("#{" + string + "}", "?");
        }
        return sql;
    }

    /**
     * 将SQL语句的参数替换变为?
     * @param sql
     * @param paramName
     * @return
     */
    public static String parameQuestion(String sql, List<String> paramName) {
        for (int i = 0; i < paramName.size(); i++) {
            String string = paramName.get(i);
            sql = sql.replace("#{" + string + "}", "?");
        }
        return sql;
    }

    public static void main(String[] args) {
        String sql = "insert into t_user(name,age) values(#{name},#{age})";
        String[] sqlParameter = sqlInsertParameter(sql);
        for (String string : sqlParameter) {
            System.out.println(string);
        }

        List<String> sqlSelectParameter = SqlUtil
                .sqlSelectParameter("select * from t_user where name=#{name} and age=#{age} ");
        for (String string : sqlSelectParameter) {
            System.out.println(string);
        }
    }

}
