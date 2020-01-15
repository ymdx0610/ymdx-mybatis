package com.ymdx.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: ExtSelect
 * @Description: TODO
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-15 11:25
 * @Version: 1.0
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExtSelect {
    String value();
}
