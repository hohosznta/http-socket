package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server has started on 127.0.0.1:8080 \nWaiting for a connection...");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected!");

                try (
                        InputStream in = client.getInputStream();
                        OutputStream out = client.getOutputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                ) {
                    // 1. 요청라인 + 헤더 읽기
                    String requestLine = reader.readLine(); // e.g. POST /hello HTTP/1.1
                    int contentLength = 0;

                    String line;
                    while ((line = reader.readLine()) != null && !line.isBlank()) {
                        if (line.toLowerCase().startsWith("content-length:")) {
                            contentLength = Integer.parseInt(line.split(":")[1].trim());
                        }
                    }

                    // 2. 본문 읽기
                    char[] bodyChars = new char[contentLength];
                    int read = reader.read(bodyChars);
                    String body = new String(bodyChars, 0, read);

                    // 3. 디스패처 실행
                    HttpRequest httpRequestDto = new HttpRequest(requestLine, body);
                    Dispatcher dispatcher = new Dispatcher();
                    HttpResponse response = dispatcher.dispatch(httpRequestDto);

                    out.write(response.getResponseData().getBytes(StandardCharsets.UTF_8));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    client.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
