package org.example;

import org.example.controller.HelloController;
import org.example.handler.AnnotationHandlerAdapter;
import org.example.handler.AnnotationHandlerMapping;
import org.example.handler.HandlerAdapter;
import org.example.handler.HandlerMapping;

import java.util.List;

public class Dispatcher {

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public Dispatcher() {
        List<Object> controllers = List.of(new HelloController());

        this.handlerMappings = List.of(
                new AnnotationHandlerMapping(controllers)
        );
        this.handlerAdapters = List.of(
                new AnnotationHandlerAdapter()
        );
    }

    public HttpResponse dispatch(HttpRequest request) {
        Object handler = getHandler(request);
        if (handler == null) {
            return new HttpResponse("404 Not Found: " + request.getPath());
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        return adapter.handle(request, handler);
    }

    private Object getHandler(HttpRequest request) {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) return handler;
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) return adapter;
        }
        throw new IllegalStateException("No adapter found for handler: " + handler);
    }
}

