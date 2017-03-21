package com.xiaosw.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><br/>ClassName : {@link UserBehaviour}
 * <br/>Description : 用户行为统计
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-21 11:11:16</p>
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface UserBehaviour {

    String value() default "";

}
