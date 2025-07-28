package org.example.handler;

import org.example.HttpRequest;
import org.example.HttpResponse;

//컨트롤러가 어떤 방식으로 일을 처리해야 하는지
public interface HandlerAdapter {
    boolean supports(Object handler);
    HttpResponse handle(HttpRequest request, Object handler);
}
