package com.tocados.marin.managers;

import java.util.Scanner;

import com.tocados.marin.apps.ServerApp;
import com.tocados.marin.managers.MessageManager.Messages;

public class ConsoleManager extends Thread {

    private static Scanner sc;

    public ConsoleManager() {
        sc = new Scanner(System.in);
    }

    private String showMenuAndScan() {
        System.out.println("0: End server and matches.");
        System.out.println("1: Show current matches.");
        System.out.println("2: Show players count.");
        return sc.nextLine();
    }

    @Override
    public void run() {
        String option;
        MessageManager.showXMessage(Messages.WELCOME_MESSAGE);

        while (ServerApp.run) {
            option = showMenuAndScan();

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

        MessageManager.showXMessage(Messages.FAREWELL_MESSAGE);

        sc.close();
    }
}
