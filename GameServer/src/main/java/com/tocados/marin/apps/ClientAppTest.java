package com.tocados.marin.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.tocados.marin.managers.JSONManager;

public class ClientAppTest {
    private static String serverHost = "localhost";
    private static Integer port = 7777;

    public static void main(String[] args) {
        // Socket creation
        Socket socket1 = null;
        BufferedReader reader1 = null;
        PrintStream writer1 = null;

        Socket socket2 = null;
        BufferedReader reader2 = null;
        PrintStream writer2 = null;
        try {
            socket1 = new Socket(serverHost, port);
            reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            writer1 = new PrintStream(socket1.getOutputStream());

            socket2 = new Socket(serverHost, port);
            reader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            writer2 = new PrintStream(socket2.getOutputStream());

            mountUsers(writer1, writer2);
            System.out.println(reader1.readLine());
            System.out.println(reader2.readLine());

            Scanner sc = new Scanner(System.in);

            String serverResponse = "";
            while (!serverResponse.equals("exit")) {
                sendToPlayer(writer1, reader2, sc);
                sendToPlayer(writer2, reader1, sc);
            }

            sc.close();

            reader1.close();
            writer1.close();
            socket1.close();

            reader2.close();
            writer2.close();
            socket2.close();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static void mountUsers(PrintStream writer1, PrintStream writer2) {
        writer1.println(JSONManager.mountColumnJson(7));
        writer2.println(JSONManager.mountColumnJson(7));
    }

    private static void sendToPlayer(PrintStream writer, BufferedReader reader, Scanner sc) throws IOException {
        writer.println(JSONManager.mountColumnJson(sc.nextInt()));
        reader.readLine();
    }
}
