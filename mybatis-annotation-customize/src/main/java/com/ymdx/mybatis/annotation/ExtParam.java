package com.ymdx.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: ExtParam
 * @Description: TODO
 * @Author: ymdx
 * @Email: y_m_d_x@163.com
 * @Date: 2020-01-14 16:08
 * @Version: 1.0
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ExtParam {
    String value();
}
