package org.example;

public class HttpRequest {
    private final String method;
    private final String path;

    public HttpRequest(String requestLine) {
        // 예: GET /hello HTTP/1.1
        String[] parts = requestLine.split(" ");
        if (parts.length >= 2) {
            this.method = parts[0];
            this.path = parts[1];
        } else {
            this.method = "GET";
            this.path = "/";
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}

