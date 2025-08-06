package org.example.controller;

import org.example.HttpResponse;
import org.example.annotation.*;
import org.example.controller.request.HelloRequest;
import org.example.service.HelloService;
import org.example.service.ServiceMarker;

@Component
public class HelloController implements ControllerMarker{

    private final ServiceMarker helloService;

    public HelloController(ServiceMarker helloService) {
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

