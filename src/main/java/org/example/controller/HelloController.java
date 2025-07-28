package org.example.controller;

import org.example.HttpResponse;
import org.example.annotation.Post;
import org.example.annotation.RequestBody;
import org.example.annotation.RequestHeader;
import org.example.annotation.RequestParam;
import org.example.controller.request.HelloRequest;

public class HelloController {
    @Post("/hello")
    public HttpResponse hello(
            @RequestParam("age") int age,
            @RequestBody HelloRequest request,
            @RequestHeader("X-Auth-Token") String token
    ) {
        System.out.println(token);
        return new HttpResponse("Hello " + request.name + ", age: " + age + ", token: " + token);
    }

}

