package org.example.controller;

import org.example.HttpResponse;
import org.example.annotation.*;
import org.example.controller.request.HelloRequest;
import org.example.service.HelloService;

@Component
public class HelloController implements ControllerMarker{

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @Post("/hello")
    public HttpResponse hello(
            @RequestParam("age") int age,
            @RequestBody HelloRequest request,
            @RequestHeader("X-Auth-Token") String token
    ) {
        System.out.println(token);
        String result = helloService.add(request.name, token);
        return new HttpResponse("Hello " + request.name + ", age: " + age + ", result: " + result);
    }
}

