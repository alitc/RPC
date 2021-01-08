package com.netty.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
//定义被它所注解的注解保留多久，一共有三种策略
/**
 * source：注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；被编译器忽略
 *
 * class：注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期
 *
 * runtime：注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
 */
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface NettyRpcService {
    Class<?> value();

    String version() default "";
}
