package org.example.handler;

import org.example.HttpHandler;
import org.example.HttpRequest;

//어느 컨트롤러에게 맡길지 결정
public interface HandlerMapping {
    Object getHandler(HttpRequest request);
}
