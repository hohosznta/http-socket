package org.example;

public class Dispatcher {

    public HttpHandler dispatch(HttpRequest request) {
        if (request.getPath().equals("/hello")) {
            return new HelloHandler();
        }

        return () -> new HttpResponse("404 Not Found: " + request.getPath());
    }
}
