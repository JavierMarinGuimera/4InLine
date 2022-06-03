package com.tocados.marin.apps.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientWriter extends Thread {

    private BufferedReader writer;

    private ClientWriter() {
        if (this.writer == null) {
            this.writer = new BufferedReader(new InputStreamReader(System.in));
        }
    }

    public static ClientWriter getInstance() {
        return new ClientWriter();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
    }

    public String writeMessage() {
        try {
            if (this.writer.ready()) {
                return this.writer.readLine();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return "";
    }
}
