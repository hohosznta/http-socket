package org.example;

public class HelloHandler implements HttpHandler {

    @Override
    public HttpResponse handle() {
        return new HttpResponse("Hello, HTTP Server!");
    }
}

