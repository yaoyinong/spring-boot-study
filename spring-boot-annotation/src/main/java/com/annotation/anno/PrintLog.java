package com.annotation.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yaoyinong
 * @date 2022/7/19 20:56
 * @description 打印日志
 *
 * 注解作用域
 * @Target(ElementType.TYPE) 接口、类、枚举、注解
 * @Target(ElementType.FIELD) 字段、枚举的常量
 * @Target(ElementType.METHOD) 方法
 * @Target(ElementType.PARAMETER) 方法参数
 * @Target(ElementType.CONSTRUCTOR) 构造函数
 * @Target(ElementType.LOCAL_VARIABLE) 局部变量
 * @Target(ElementType.ANNOTATION_TYPE) 注解
 * @Target(ElementType.PACKAGE) 包
 * 注解使用场景
 * @Retention(RetentionPolicy.SOURCE) 这种类型的Annotations只在源代码级别保留, 编译时就会被忽略
 * @Retention(RetentionPolicy.CLASS) 这种类型的Annotations编译时被保留, 在class文件中存在, 但JVM将会忽略
 * @Retention(RetentionPolicy.RUNTIME) 这种类型的Annotations将被JVM保留, 所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrintLog {


}
