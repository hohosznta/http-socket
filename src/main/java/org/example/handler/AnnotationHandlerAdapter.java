package org.example.handler;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.ReflectionJsonParser;
import org.example.annotation.RequestBody;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof AnnotationHandlerMapping.HandlerMethod;
    }

    @Override
    public HttpResponse handle(HttpRequest request, Object handler) {
        AnnotationHandlerMapping.HandlerMethod handlerMethod = (AnnotationHandlerMapping.HandlerMethod) handler;
        Method method = handlerMethod.method;
        Object controller = handlerMethod.controller;

        try {
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];

                if (param.isAnnotationPresent(RequestBody.class)) {
                    String json = request.getBody(); //본문 JSON 문자열
                    Class<?> paramType = param.getType(); //어느 dto 클래스인지
                    Object parsed = ReflectionJsonParser.deserialize(json, paramType); // 파싱
                    args[i] = parsed;
                } else if (param.getType() == HttpRequest.class) {
                    args[i] = request;
                } else {
                    args[i] = null;
                }
            }

            return (HttpResponse) method.invoke(controller, args);

        } catch (Exception e) {
            return new HttpResponse("500 Internal Server Error: " + e.getMessage());
        }
    }
}



