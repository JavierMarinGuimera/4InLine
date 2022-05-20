package app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ClientAppTest {
    public static void main(String[] args) {
        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        SSLSocket sslClientSocket;

        try {
            sslClientSocket = (SSLSocket) socketFactory.createSocket("https://localhost", 7777);
            BufferedInputStream bufInputStream = new BufferedInputStream(sslClientSocket.getInputStream());
            BufferedOutputStream bufOutputStream = new BufferedOutputStream(sslClientSocket.getOutputStream());

            bufOutputStream.write("7".getBytes());

            System.out.println(bufInputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
