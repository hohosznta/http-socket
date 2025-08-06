package org.example.service;

import org.example.annotation.Component;

@Component
public class HelloService {

    public String add(String name, String token){
        return name + token;
    }
}
