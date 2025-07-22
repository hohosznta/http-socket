package org.example;

public class HttpResponse {
    private final String responseData;

    public HttpResponse(String body) {
        this.responseData =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + body.getBytes().length + "\r\n" +
                        "\r\n" +
                        body;
    }

    public String getResponseData() {
        return responseData;
    }
}
