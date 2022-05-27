package com.tocados.marin.apps;

import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tocados.marin.managers.ConsoleManager;
import com.tocados.marin.managers.MessageManager;
import com.tocados.marin.managers.MessageManager.Messages;
import com.tocados.marin.models.GameMatch;
import com.tocados.marin.models.Player;

public class ServerApp {
    private static final int PORT = 7777;
    private static final int TIMEOUT = 5000;

    public static Boolean run = true;

    private static Map<Integer, Player> playersWaiting = new HashMap<>() {
        {
            put(5, null);
            put(7, null);
            put(9, null);
        }
    };
    private static List<Player> pendingPlayers = new ArrayList<>();
    private static List<GameMatch> currentGameMatches = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ConsoleManager consoleManager = new ConsoleManager();
        consoleManager.start();

        ServerSocket serverSocket = new ServerSocket(PORT);
        serverSocket.setSoTimeout(TIMEOUT);

        MessageManager.showXMessage(Messages.WAITING_FOR_USERS);
        while (run) {

            // Wait the user connection with the server.
            try {
                pendingPlayers.add(new Player(serverSocket.accept()));
                System.out.println("Siguiente jugador");
                MessageManager.showXMessage(Messages.USER_FOUND);
            } catch (SocketTimeoutException e) {
                continue;
            }

            checkListForMatches();
        }

        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public static void checkListForMatches() {
        for (Player player : pendingPlayers) {
            checkCurrentPlayers(player, 5);
            checkCurrentPlayers(player, 7);
            checkCurrentPlayers(player, 9);
        }
    }

    private static void checkCurrentPlayers(Player player, Integer columns) {
        if (player.getColumns() == columns) {
            System.out.println(playersWaiting);

            if (playersWaiting.get(columns) != null) {
                startMatch(playersWaiting.get(columns), player, columns);
                playersWaiting.put(columns, null);
            } else {
                playersWaiting.put(columns, player);
            }
        }
    }

    private static void startMatch(Player player1, Player player2, Integer columns) {
        MessageManager.showXMessage(Messages.MATCH_FOUND);
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
