package com.tocados.marin.apps;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import com.tocados.marin.managers.ConsoleManager;
import com.tocados.marin.managers.MessageManager;
import com.tocados.marin.managers.MessageManager.Messages;
import com.tocados.marin.models.GameMatch;
import com.tocados.marin.models.Player;

public class ServerApp {
    private static final int PORT = 7777;

    public static Boolean run = true;

    private static Player playerMatch5 = null, playerMatch7 = null, playerMatch9 = null;
    private static List<Player> pendingPlayers = new ArrayList<>();
    private static List<GameMatch> currentGameMatches = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ConsoleManager consoleManager = new ConsoleManager();
        consoleManager.start();

        ServerSocket serverSocket = new ServerSocket(PORT);

        while (run) {
            MessageManager.showXMessage(Messages.WAITING_FOR_USER);

            pendingPlayers.add(new Player(serverSocket.accept()));
            pendingPlayers.add(new Player(serverSocket.accept()));

            checkListForMatches();
            break;
        }

        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public static void checkListForMatches() {
        for (Player player : pendingPlayers) {
            if (player.getColumns() == 5) {
                if (playerMatch5 != null) {
                    startMatch(playerMatch5, player, 5);
                    playerMatch5 = null;
                } else {
                    playerMatch5 = player;
                }
            }
            if (player.getColumns() == 7) {
                if (playerMatch7 != null) {
                    startMatch(playerMatch7, player, 7);
                    playerMatch7 = null;
                } else {
                    playerMatch7 = player;
                }
            }
            if (player.getColumns() == 9) {
                if (playerMatch9 != null) {
                    startMatch(playerMatch9, player, 9);
                    playerMatch9 = null;
                } else {
                    playerMatch9 = player;
                }
            }
        }
    }

    private static void startMatch(Player player1, Player player2, Integer columns) {
        GameMatch gameMatch = new GameMatch(player1, player2, columns);
        gameMatch.start();
        currentGameMatches.add(gameMatch);
    }

    /**
     * Info server
     */

    public static void showMatches() {
        currentGameMatches.forEach((gameMatch) -> {
            System.out.println(gameMatch);
        });
    }

    public static void showPlayersCount() {
        System.out.println("Current players waiting: " + pendingPlayers.size());
    }

    /**
     * End server
     */

    public static void endGames() {
        currentGameMatches.forEach((gameMatch) -> {
            gameMatch.endMatch();
        });
    }
}
