package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD}) //클래스나 메서드에 붙기 가능
@Retention(RetentionPolicy.RUNTIME) //런타임에도 JVM에 유지되며, Spring은 이를 리플렉션으로 읽을 수 있음.
public @interface ResponseBody {
}

