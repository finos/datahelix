package com.scottlogic.deg;

import com.google.inject.Module;
import com.google.inject.spi.InjectionPoint;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(InjectionPoint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface RequiresInjection {

    Class<? extends Module>[] values() default {};

}
