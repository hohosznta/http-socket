package org.example.handler;

import java.lang.reflect.Method;

public interface AnnotationHandler {
    default void before(Method method, Object[] args, Object target) {}
    default void after(Method method, Object[] args, Object target) {}
    default void onError(Method method, Object[] args, Object target, Throwable e) {}
}
