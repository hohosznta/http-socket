package org.example.handler;

import org.example.annotation.Component;

import java.lang.reflect.Method;

@Component
public class TransactionalHandler implements AnnotationHandler {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("트랜잭션 시작");
    }

    @Override
    public void after(Method method, Object[] args, Object target) {
        System.out.println("트랜잭션 커밋");
    }

    @Override
    public void onError(Method method, Object[] args, Object target, Throwable e) {
        System.out.println("트랜잭션 롤백: " + e.getMessage());
    }
}
