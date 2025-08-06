package org.example.handler;


import org.example.annotation.Component;
import org.example.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class GenericInvocationHandler implements InvocationHandler {

    private final Object target;
    private final Map<Class<? extends Annotation>, AnnotationHandler> handlers = new HashMap<>();

    public GenericInvocationHandler(Object target) {
        this.target = target;

        // 어노테이션-핸들러 매핑 등록
        handlers.put(Transactional.class, new TransactionalHandler());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method actualMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        Annotation[] annotations = actualMethod.getAnnotations();

        try {
            for (Annotation ann : annotations) {
                AnnotationHandler h = handlers.get(ann.annotationType());
                if (h != null) h.before(actualMethod, args, target);
            }

            Object result = actualMethod.invoke(target, args);

            for (Annotation ann : annotations) {
                AnnotationHandler h = handlers.get(ann.annotationType());
                if (h != null) h.after(actualMethod, args, target);
            }

            return result;
        } catch (Throwable t) {
            for (Annotation ann : annotations) {
                AnnotationHandler h = handlers.get(ann.annotationType());
                if (h != null) h.onError(actualMethod, args, target, t);
            }
            throw t;
        }
    }
}
