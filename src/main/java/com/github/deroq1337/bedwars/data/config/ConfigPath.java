package com.github.deroq1337.bedwars.data.config;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPath {

    @NotNull String value() default "";
}
