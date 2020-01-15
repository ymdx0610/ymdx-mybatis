package com.ymdx.mybatis.aop;

import com.ymdx.mybatis.annotation.ExtInsert;
import com.ymdx.mybatis.annotation.ExtParam;
import com.ymdx.mybatis.annotation.ExtSelect;
import com.ymdx.mybatis.util.JDBCUtil;
import com.ymdx.mybatis.util.SqlUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: MyInvocationHandlerForMybatis
 * @Description: AOP，动态代理，实现自定义MyBatis注解@ExtInsert、@ExtSelect
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-15 10:56
 * @Version: 1.0
 **/
public class MyInvocationHandlerForMybatis implements InvocationHandler {

    private Object targetObject;

    public MyInvocationHandlerForMybatis(Object targetObject){
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

        // 判断方法上是否存在@ExtInsert
        ExtInsert extInsert = method.getDeclaredAnnotation(ExtInsert.class);
        if(extInsert != null){
            return insert(extInsert, method, params);
        }

        // 判断方法上是否存在@ExtSelect
        ExtSelect extSelect = method.getDeclaredAnnotation(ExtSelect.class);
        if(extSelect != null){
            return select(extSelect, method, params);
        }

        return null;
    }

    /**
     * @ExtSelect实现
     * @param extSelect
     * @param method
     * @param params
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Object select(ExtSelect extSelect, Method method, Object[] params) throws SQLException, IllegalAccessException, InstantiationException {
        // 取出@ExtSelect的值，即selectSql
        String selectSql = extSelect.value();
        System.out.println("selectSql:" + selectSql);

        // 获取方法参数，暂存到map集合中
        ConcurrentHashMap<String, Object> paramMap = getMethodParamMap(method, params);
        // 获取SQL语句上需要传递的参数
        List<String> paramNames = SqlUtil.sqlSelectParameter(selectSql);
        String[] paramNameArray = new String[paramNames.size()];
        paramNameArray = paramNames.toArray(paramNameArray);
        List<Object> paramValues = getSqlParamValues(paramNameArray, paramMap);

        // 将sql语句中的#{paramName}替换成?
        String newSelectSql = SqlUtil.parameQuestion(selectSql, paramNames);
        System.out.println(newSelectSql);

        //  执行sql，获取返回结果
        ResultSet rs = JDBCUtil.query(newSelectSql, paramValues);
        if(!rs.next()){
            return null;
        }
        // 游标向上移动一位
        rs.previous();
        // 获取返回类型
        Class<?> clazz = method.getReturnType();
        // 实例化对象
        Object obj = clazz.newInstance();
        while (rs.next()){
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                String fieldName = field.getName();
                Object fieldValue = rs.getObject(fieldName);
                field.setAccessible(true);
                field.set(obj, fieldValue);
            }
        }
        return obj;
    }

    /**
     * @ExtInsert实现
     * @param extInsert
     * @param method
     * @param params
     * @return
     */
    private Object insert(ExtInsert extInsert, Method method, Object[] params){
        // 取出@ExtInsert的值，即insertSql
        String insertSql = extInsert.value();
        System.out.println("insertSql:" + insertSql);

        // 获取方法参数，暂存到map集合中
        ConcurrentHashMap<String, Object> paramMap = getMethodParamMap(method, params);

        // 获取SQL语句上需要传递的参数
        String[] paramNames = SqlUtil.sqlInsertParameter(insertSql);
        List<Object> paramValues = getSqlParamValues(paramNames, paramMap);

        // 将sql语句中的#{paramName}替换成?
        String newInsertSql = SqlUtil.parameQuestion(insertSql, paramNames);
        System.out.println("newInsertSql:" + newInsertSql);

        // 执行sql
        return JDBCUtil.insert(newInsertSql, false, paramValues);
    }

    /**
     * 获取SQL语句上需要传递的参数集合
     * @param paramNames
     * @param paramMap
     * @return
     */
    private List<Object> getSqlParamValues(String[] paramNames, ConcurrentHashMap<String, Object> paramMap){
        List<Object> paramValues = new ArrayList<>();
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            Object paramValue = paramMap.get(paramName);
            paramValues.add(paramValue);
        }
        return paramValues;
    }

    /**
     * 获取方法参数集合
     * @param method
     * @param params
     * @return
     */
    private ConcurrentHashMap<String, Object> getMethodParamMap(Method method, Object[] params){
        ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            // 判断参数上是否存在@ExtParam
            ExtParam extParam = parameter.getDeclaredAnnotation(ExtParam.class);
            if(extParam != null){
                String paramName = extParam.value();
                Object paramValue = params[i];
                paramMap.put(paramName, paramValue);
            }
        }
        return paramMap;
    }

}
