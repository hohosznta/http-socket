package org.example.controller;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.annotation.Post;
import org.example.annotation.RequestBody;
import org.example.controller.request.HelloRequest;

public class HelloController {
    @Post("/hello")
    public HttpResponse hello(@RequestBody HelloRequest request) {
        return new HttpResponse("Hello!" + request.name);
    }
}

