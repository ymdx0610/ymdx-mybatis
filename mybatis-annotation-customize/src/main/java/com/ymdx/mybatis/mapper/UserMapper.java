package com.ymdx.mybatis.mapper;

import com.ymdx.mybatis.annotation.ExtInsert;
import com.ymdx.mybatis.annotation.ExtParam;
import com.ymdx.mybatis.annotation.ExtSelect;
import com.ymdx.mybatis.entity.User;

/**
 * @ClassName: UserMapper
 * @Description: TODO
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-15 11:20
 * @Version: 1.0
 **/
public interface UserMapper {

    @ExtInsert("insert into t_user(name,age) values(#{name},#{age})")
    public int addUser(@ExtParam("name") String name, @ExtParam("age") Integer age);

    @ExtSelect("select * from t_user where name=#{name} and age=#{age}")
    public User selectUser(@ExtParam("name") String name, @ExtParam("age") Integer age);

}
