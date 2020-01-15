package com.ymdx.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: ExtInsert
 * @Description: TODO
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-14 16:07
 * @Version: 1.0
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExtInsert {
    String value();
}
