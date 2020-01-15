package com.ymdx.mybatis;

import com.ymdx.mybatis.aop.SqlSession;
import com.ymdx.mybatis.entity.User;
import com.ymdx.mybatis.mapper.UserMapper;

/**
 * @ClassName: Main
 * @Description: TODO
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-15 11:27
 * @Version: 1.0
 **/
public class Main {

    public static void main(String[] args) {
        try {
            UserMapper userMapper = SqlSession.getMapper(UserMapper.class);
//            int result = userMapper.addUser("范闲", 30);
//            System.out.println(result);

            User user = userMapper.selectUser("范闲", 30);
            System.out.println(user);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
