package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();

        context.scan("org.example"); //패키지 스캔해서 클래스 등록

        MyServer server = context.getBean(MyServer.class); //필요한 빈 생성 및 의존성 주입(여기서는 명시적으로 시작)
        server.run();
    }
}
