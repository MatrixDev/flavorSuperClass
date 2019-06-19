package com.matrixdev.flavorsuperclass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rostyslav.Lesovyi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GenerateSuperClass {
	String name();
	int priority() default  0;
}
