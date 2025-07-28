package org.example.controller;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.annotation.Post;

public class HelloController {
    @Post("/hello")
    public HttpResponse hello(HttpRequest request) {
        return new HttpResponse("Hello!");
    }
}

