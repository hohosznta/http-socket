package org.example;

import org.example.annotation.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyServer {

    private final Dispatcher dispatcher;

    public MyServer(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected!");

                try (
                        InputStream in = client.getInputStream();
                        OutputStream out = client.getOutputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                ) {
                    String requestLine = reader.readLine(); // e.g. POST /hello HTTP/1.1
                    if (requestLine == null || requestLine.isBlank()) continue;

                    Map<String, String> headers = new HashMap<>();
                    int contentLength = 0;

                    String line;
                    while ((line = reader.readLine()) != null && !line.isBlank()) {
                        int colonIndex = line.indexOf(":");
                        if (colonIndex != -1) {
                            String key = line.substring(0, colonIndex).trim().toLowerCase();
                            String value = line.substring(colonIndex + 1).trim();
                            headers.put(key, value);

                            if (key.equals("content-length")) {
                                contentLength = Integer.parseInt(value);
                            }
                        }
                    }

                    StringBuilder bodyBuilder = new StringBuilder();
                    if (contentLength > 0) {
                        int remaining = contentLength;
                        while (remaining > 0) {
                            int ch = reader.read();
                            if (ch == -1) break;
                            bodyBuilder.append((char) ch);
                            remaining--;
                        }
                    }
                    String body = bodyBuilder.toString();

                    HttpRequest httpRequest = new HttpRequest(requestLine, headers, body); // 변경된 생성자 사용
                    HttpResponse response = dispatcher.dispatch(httpRequest);

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

