package com.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)//注解在运行时生效
@Target(ElementType.FIELD)//注解在属性生效
public @interface BaseCloudAnnotation {
    boolean isRetainedField () default false;
}