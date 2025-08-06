package org.example;

import org.example.annotation.Component;
import org.example.handler.AnnotationHandlerAdapter;
import org.example.handler.AnnotationHandlerMapping;

import java.util.List;

@Component
public class Dispatcher {

    private final AnnotationHandlerMapping handlerMappings;
    private final AnnotationHandlerAdapter handlerAdapters;

    public Dispatcher(AnnotationHandlerMapping handlerMappings, AnnotationHandlerAdapter handlerAdapters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    public HttpResponse dispatch(HttpRequest request) {
        String key = request.getMethod() + " " + request.getPath();

        Object handler = getHandler(request);
        if (handler == null) {
            return new HttpResponse("404 Not Found: " + request.getPath());
        }

        AnnotationHandlerAdapter adapter = getHandlerAdapter(handler);
        return adapter.handle(request, handler);
    }

    private Object getHandler(HttpRequest request) {
            Object handler = handlerMappings.getHandler(request);
            if (handler != null) return handler;

        return null;
    }

    private AnnotationHandlerAdapter getHandlerAdapter(Object handler) {
            if (handlerAdapters.supports(handler)) return handlerAdapters;

        throw new IllegalStateException("No adapter found for handler: " + handler);
    }
}


