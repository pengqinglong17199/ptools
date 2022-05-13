package com.pql.ptools.commons.lombok.annotations;

import java.lang.annotation.*;

/**
 * 枚举描述 对类以及字段 自动生成相应的desc方法
 *
 * @author pengqinglong
 * @since 2022/5/9
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
//@Repeatable(EnumDesc.class)
public @interface EnumDesc {

    String[] filed() default {"desc"};

}