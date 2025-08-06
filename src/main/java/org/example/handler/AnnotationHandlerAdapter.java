package org.example.handler;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.ReflectionJsonParser;
import org.example.annotation.Component;
import org.example.annotation.RequestBody;
import org.example.annotation.RequestHeader;
import org.example.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
public class AnnotationHandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof AnnotationHandlerMapping.HandlerMethod;
    }

    public HttpResponse handle(HttpRequest request, Object handler) {
        AnnotationHandlerMapping.HandlerMethod handlerMethod = (AnnotationHandlerMapping.HandlerMethod) handler;
        Method method = handlerMethod.method;
        Object controller = handlerMethod.controller;

        try {
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Class<?> type = param.getType();

                // @RequestBody
                if (param.isAnnotationPresent(RequestBody.class)) {
                    String json = request.getBody();
                    Object parsed = ReflectionJsonParser.deserialize(json, type);
                    args[i] = parsed;
                    continue;
                }

                // @RequestParam
                if (param.isAnnotationPresent(RequestParam.class)) {
                    RequestParam annotation = param.getAnnotation(RequestParam.class);
                    String key = annotation.value();
                    String value = request.getQueryParams().get(key);

                    args[i] = convertValue(value, type);
                    continue;
                }

                // @RequestHeader
                if (param.isAnnotationPresent(RequestHeader.class)) {
                    RequestHeader annotation = param.getAnnotation(RequestHeader.class);
                    String key = annotation.value().toLowerCase();
                    String value = request.getHeaders().get(key);

                    args[i] = convertValue(value, type);
                    continue;
                }

                // HttpRequest 직접 주입
                if (type == HttpRequest.class) {
                    args[i] = request;
                    continue;
                }

                // 기타 타입은 null 처리
                args[i] = null;
            }

            return (HttpResponse) method.invoke(controller, args);

        } catch (Exception e) {
            e.printStackTrace(); // 디버깅을 위해 로그 출력
            return new HttpResponse("500 Internal Server Error: " + e.getMessage());
        }
    }

    private Object convertValue(String value, Class<?> type) {
        if (value == null) return null;

        try {
            if (type == String.class) return value;
            if (type == int.class || type == Integer.class) return Integer.parseInt(value);
            if (type == long.class || type == Long.class) return Long.parseLong(value);
            if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            return null;
        }

        return null;
    }
}
