package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import org.example.annotation.Component;
import org.example.annotation.Transactional;
import org.example.proxy.ProxyFactory;
import org.reflections.Reflections;

public class ApplicationContext {
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final Map<Class<?>, Class<?>> beanDefinitions = new HashMap<>();

    public void scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> components = new HashSet<>();
        components.addAll(reflections.getTypesAnnotatedWith(Component.class));

        // 1단계: 모든 빈 정의만 먼저 등록 (실제 인스턴스 생성 X) 어떤 클래스가 Bean인지 등록
        for (Class<?> clazz : components) {
            beanDefinitions.put(clazz, clazz);

            // 이 클래스가 구현한 인터페이스도 등록
            for (Class<?> iface : clazz.getInterfaces()) {
                beanDefinitions.put(iface, clazz);
            }
        }


        // 2단계: 모든 빈 정의가 등록된 후에 빈들을 생성 (만약 1단계2단계 동시에 하면 재귀적인 연관관계를 찾지 못함)
        for (Class<?> clazz : components) {
            getBean(clazz);
        }
    }

    public <T> T getBean(Class<T> requiredType) {
        try {
            Object bean = createBean(requiredType);
            return requiredType.cast(bean);
        } catch (Exception e) {
            throw new RuntimeException("Bean 생성 실패: " + requiredType.getName(), e);
        }
    }

    private Object createBean(Class<?> type) throws Exception {
        if (singletonObjects.containsKey(type)) {
            return singletonObjects.get(type); //이미 생성된 인스턴스 있으면 그거 반환(싱글톤)
        }

        Class<?> implType = beanDefinitions.getOrDefault(type, type); //인터페이스가 있으면 실제 구현 클래스 찾고, 아니면 자기 자신 반환

        Constructor<?> selected = Arrays.stream(implType.getDeclaredConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("생성자 없음: " + implType.getName())); //여기서는 가장 많은 파라미터 가진 생성자 하나 선택, 스프링은 @autowired로 명시 해줘야 함

        Parameter[] params = selected.getParameters();
        Object[] dependencies = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Parameter param = params[i];
            Class<?> paramType = param.getType();

            if (Collection.class.isAssignableFrom(paramType)) { //만약 컬렉션 타입이라면
                Type genericType = param.getParameterizedType(); //런타임에 제네릭 정보 추출
                if (genericType instanceof ParameterizedType pt) {
                    Type actualType = pt.getActualTypeArguments()[0];
                    Class<?> actualClass = Class.forName(actualType.getTypeName());

                    List<Object> matchingBeans = beanDefinitions.keySet().stream() //해당 타입의 모든 구현체 찾기
                            .filter(actualClass::isAssignableFrom)
                            .map(clazz -> {
                                return (Object) getBean(clazz);
                            })
                            .toList();

                    dependencies[i] = matchingBeans;
                }
            } else {
                dependencies[i] = getBean(paramType);
            }
        }

        Object instance = selected.newInstance(dependencies);//모든 의존성이 준비되면 객체를 생성하고 저장

        //트랜잭션 어노테이션이 클래스나 메서드에 붙은 경우 프록시 생성
        if (needsTransactionalProxy(implType)) {
            Class<?>[] interfaces = implType.getInterfaces();
            if (interfaces.length > 0) {
                // 인터페이스 타입으로도 프록시 저장
                Object proxy = ProxyFactory.createProxy(interfaces[0], instance);
                singletonObjects.put(interfaces[0], proxy);
            }
        }else{
            singletonObjects.put(type, instance);
        }
        return instance;
    }
    
    private boolean needsTransactionalProxy(Class<?> clazz) {
        // 메서드 중 하나라도 @Transactional이 있으면 true
        return Arrays.stream(clazz.getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(Transactional.class));
    }
}


