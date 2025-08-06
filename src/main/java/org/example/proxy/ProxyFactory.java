package org.example.proxy;



import org.example.annotation.Component;
import org.example.handler.GenericInvocationHandler;

import java.lang.reflect.Proxy;

@Component
public class ProxyFactory {
    public static <T> T createProxy(Class<?> interfaceType, Object target) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new GenericInvocationHandler(target)
        );
    }
}
