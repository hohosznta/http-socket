package org.example;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String body; // 추가

    public HttpRequest(String requestLine, String body) {
        String[] parts = requestLine.split(" ");
        this.method = parts.length >= 2 ? parts[0] : "GET";
        this.path = parts.length >= 2 ? parts[1] : "/";
        this.body = body;
    }

    public String getBody() {return body;}

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}

