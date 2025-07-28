package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String body;
    private final Map<String, String> queryParams = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(String requestLine, Map<String, String> headerMap, String body) {
        String[] parts = requestLine.split(" ");
        this.method = parts.length >= 2 ? parts[0] : "GET";

        String rawPath = parts.length >= 2 ? parts[1] : "/";
        String[] pathAndQuery = rawPath.split("\\?");
        this.path = pathAndQuery[0];

        if (pathAndQuery.length > 1) {
            String queryString = pathAndQuery[1];
            for (String param : queryString.split("&")) {
                String[] kv = param.split("=");
                if (kv.length == 2) {
                    queryParams.put(kv[0], kv[1]);
                }
            }
        }

        // 헤더 저장
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headers.put(entry.getKey().toLowerCase(), entry.getValue());
        }

        this.body = body;
    }


    public Map<String, String> getHeaders() { return headers; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public String getBody() { return body; }
    public String getMethod() { return method; }
    public String getPath() { return path; }
}

