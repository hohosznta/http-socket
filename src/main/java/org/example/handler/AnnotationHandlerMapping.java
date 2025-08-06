package org.example.handler;

import org.example.HttpRequest;
import org.example.annotation.Component;
import org.example.annotation.Post;
import org.example.controller.ControllerMarker;
import org.example.service.ServiceMarker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnnotationHandlerMapping {

    static class HandlerMethod { //바깥 클래스 의존을 하지 않으니 static 사용함.
        final Object controller;
        final Method method;

        HandlerMethod(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }
    }

    private final Map<String, HandlerMethod> handlerMap = new HashMap<>();

    public AnnotationHandlerMapping(List<ControllerMarker> controllers, List<ServiceMarker> services) {
        for (Object controller : controllers) {
            Class<?> clazz = controller.getClass();

            for (Method method : clazz.getDeclaredMethods()) { //클래스 내부의 메소드를 모두 순회하여
                if (method.isAnnotationPresent(Post.class)) { //@Post 어노테이션이 붙은 메서드를 찾음
                    String path = method.getAnnotation(Post.class).value();
                    String key = "POST " + path; //해당 경로를 키로, 실행할 컨트롤러 메서드를
                    // ex) handlerMap.put("POST /hello", new HandlerMethod(new HelloController(), helloMethod));
                    handlerMap.put(key, new HandlerMethod(controller, method));  //handlerMap에 저장하는 초기화 작업 (서버 시작 시)
                }
            }
        }
    }

    public Object getHandler(HttpRequest request) {

        String key = request.getMethod() + " " + request.getPath(); //키를 만들고
        System.out.println("key"+ key);
        return handlerMap.get(key); //handlerMap에서 찾아서 HandlerMethod 반환
    }
}

