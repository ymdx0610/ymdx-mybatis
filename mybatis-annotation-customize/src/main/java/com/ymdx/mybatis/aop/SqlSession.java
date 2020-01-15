package com.ymdx.mybatis.aop;

import java.lang.reflect.Proxy;

/**
 * @ClassName: SqlSession
 * @Description: TODO
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-15 10:58
 * @Version: 1.0
 **/
public class SqlSession {

    public static <T> T getMapper(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MyInvocationHandlerForMybatis(clazz));
    }

}
