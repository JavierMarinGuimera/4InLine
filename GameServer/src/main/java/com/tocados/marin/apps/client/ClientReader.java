package com.tocados.marin.apps.client;

public class ClientReader extends Thread {

    private ClientReader() {

    }

    public static ClientReader getInstance() {
        return new ClientReader();
    }

    @Override
    public void run() {
        super.run();
    }

}
