package org.example.handler;

import org.example.HttpRequest;
import org.example.HttpResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof AnnotationHandlerMapping.HandlerMethod;
    }

    @Override
    public HttpResponse handle(HttpRequest request, Object handler) {
        AnnotationHandlerMapping.HandlerMethod handlerMethod = (AnnotationHandlerMapping.HandlerMethod) handler;
        try {
            // invoke 메서드로 reflection 사용... 왜냐... 안그러면 컨트롤러 하나 추가할 때마다 if-else 코드도 직접 추가해야 함
            return (HttpResponse) handlerMethod.method.invoke(handlerMethod.controller, request); //실제로 실행되어 결과(HttpResponse) 생성
        } catch (Exception e) {
            return new HttpResponse("500 Internal Server Error: " + e.getMessage());
        }
    }
}


