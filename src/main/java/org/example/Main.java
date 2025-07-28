package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server has started on 127.0.0.1:8080 \nWaiting for a connection..."); //한 스레드 당 한 소켓 - 요청 후 답변 반환할 때까지 다른 요청 진입 못함. (병렬 처리 안됨) 즉 동접자가 많으려면 사람 수 만큼 소켓이 있어야 할것. 그래서 ulimit 소켓 파일디스크립터 제한이 생기는 것임..
        try (ServerSocket serverSocket = new ServerSocket(8080)) { //8080포트에 서버 열기
            while (true) {
                Socket client = serverSocket.accept(); //클라이언트가 접속할 때까지 대기, TCP연결이 성립되면 그 연결을 담당하는 소켓 객체 반환
                System.out.println("Client connected!");

                try (
                        InputStream in = client.getInputStream(); //클라이언트가 보낸 요청을 서버가 읽는 통로
                        OutputStream out = client.getOutputStream(); //서버가 응답 데이터를 클라이언트에게 보내는 통로
                        Scanner s = new Scanner(in, "UTF-8")  //InputStream은 바이트 단위로 읽는데 서버는 문자열로 받고 싶으니 다시 디코딩
                ) {
                    String requestData = s.hasNextLine() ? s.nextLine() : ""; //클라이언트가 보낸 데이터

                    HttpRequest httpRequestDto = new HttpRequest(requestData); // 데이터 분석하여 객체로 바꿈
                    Dispatcher dispatcher = new Dispatcher();
                    HttpResponse response = dispatcher.dispatch(httpRequestDto); //요청 경로에 따라 어떤 핸들러 쓸 건지 셜정
                    out.write(response.getResponseData().getBytes()); //응답 클라이언트에게 보내기 (문자열을 바이트로 반환해서 전송0
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    client.close(); //오류가 나던 안나던 무조건 소켓 자원 반납해라... 만약 파일 디스크립터가 반납하지 않으면 os가 자원을 계속 사용중이라 인식해서 서버가 죽거나 연결이 안되는 문제가 생김(프로세스 당 사용 할 수 있는 FD의 제한이 있기 때문에! )
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//스프링 부트에서는 accept를 Tomcat이 대신 처리.. 소켓 열고 닫는 것도 톰캣이 자동 처리.. Spring boot는 요청을 잘 파싱해서 controller에 연결을 해준다.(dispatcher servlet이)  우리는 HTTP 로직만 잘 짜면 됨!