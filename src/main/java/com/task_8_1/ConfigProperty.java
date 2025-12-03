package com.task_8_1;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperty {
    String configFileName() default "src/main/java/com/task_7_1/Config.properties";
    String propertyName() default "";
    String type() default "String";
}
