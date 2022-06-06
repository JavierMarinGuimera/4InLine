package com.tocados.marin.managers;

import java.util.Scanner;

import com.tocados.marin.apps.ServerApp;
import com.tocados.marin.managers.MessageManager.Messages;

public class ConsoleManager extends Thread {

    public enum ConsoleManagerMessages {
        END_SERVER("0: End server and matches."),
        SHOW_MATCHES("1: Show current matches."),
        SHOW_WAITING_PLAYERS("2: Show players count.");

        private String msg;

        ConsoleManagerMessages(String msg) {
            this.msg = msg;
        }

        /**
         * @return the msg
         */
        public String getMsg() {
            return msg;
        }

        /**
         * @param msg the msg to set
         */
        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    private static Scanner sc;

    public ConsoleManager() {
        sc = new Scanner(System.in);
    }

    @Override
    public void run() {
        String option;
        MessageManager.showXMessage(Messages.WELCOME_MESSAGE);

        while (ServerApp.run) {
            option = showMenuAndScan();

            makeTaskWithOption(option);
        }

        MessageManager.showXMessage(Messages.FAREWELL_MESSAGE);

        sc.close();
    }
    
    private String showMenuAndScan() {
        for (ConsoleManagerMessages consoleMessage : ConsoleManagerMessages.values()) {
            System.out.println(consoleMessage.getMsg());
        }
        return sc.nextLine();
    }

    private void makeTaskWithOption(String option) {
        switch (option) {
            case "0":
                ServerApp.run = false;
                break;

            case "1":
                ServerApp.showMatches();
                break;

            case "2":
                ServerApp.showPlayersCount();
                break;

            default:
                System.out.println("Wrong option");
                break;
        }

        System.out.println();
    }
}
