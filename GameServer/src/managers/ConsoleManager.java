package managers;

import java.util.Scanner;

import app.ServerApp;
import managers.MessageManager.Messages;

public class ConsoleManager extends Thread {

    private static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    @Override
    public void run() {
        String option;
        while (ServerApp.run) {
            MessageManager.showXMessage(Messages.WELCOME_MESSAGE);
            option = showMenuAndScan();

            switch (option) {
                case "0":
                    ServerApp.run = false;
                    break;

                case "1":
                    ServerApp.showMatches();
                    break;

                default:
                    System.out.println("Wrong option");
                    break;
            }
        }

        MessageManager.showXMessage(Messages.FAREWELL_MESSAGE);

        sc.close();
    }

    private String showMenuAndScan() {
        System.out.println("0: End server and matches.");
        System.out.println("1: Show current matches.");
        return sc.nextLine();
    }
}
