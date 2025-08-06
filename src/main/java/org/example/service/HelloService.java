package org.example.service;

import org.example.annotation.Component;
import org.example.annotation.Transactional;


@Component
public class HelloService implements ServiceMarker {

    @Transactional
    public String add(String name, String token){
        return name + token;
    }
}
