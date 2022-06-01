package com.tocados.marin.apps;

import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    private static List<GameMatch> currentGameMatches = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ConsoleManager consoleManager = new ConsoleManager();
        consoleManager.start();

        if (!loginServer()) {
            MessageManager.showXMessage(Messages.LOGIN_FAILED);
            return;
        }

        /**
         * Here starts the game server:
         */
        ServerSocket serverSocket = new ServerSocket(PORT);
        serverSocket.setSoTimeout(TIMEOUT);

        MessageManager.showXMessage(Messages.WAITING_FOR_USERS);
        while (run) {

            // Wait the user connection with the server.
            try {
                checkListForMatches(new Player(serverSocket.accept()));
                MessageManager.showXMessage(Messages.USER_FOUND);
            } catch (SocketTimeoutException e) {
                continue;
            }
        }

        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    /**
     * Tries to make a login with the current server credentials.
     * 
     * @return True if worked; false if not.
     */
    private static Boolean loginServer() {
        /**
         * TODO - Activar esto cuando finalice todo.
         */
        // Map<String, Object> jsonMap = JSONManager
        // .getMapFromJsonString(HTTPManager.makeRequest(Paths.USERS_LOGIN_PATH,
        // PropertiesManager.getLoginMap()));

        // if (jsonMap == null || !jsonMap.containsKey("token")) {
        // return false;
        // }

        // PropertiesManager.setPropertyByName(PropertiesStrings.SERVER_TOKEN, (String)
        // jsonMap.get("token"));

        return true;
    }

    public static void checkListForMatches(Player newPlayer) {
        for (Map.Entry<Integer, Player> playerWaiting : playersWaiting.entrySet()) {
            if (playerWaiting != null) {
                checkCurrentPlayers(newPlayer, playerWaiting.getKey());
            }
        }
    }

    private static void checkCurrentPlayers(Player player, Integer columns) {
        if (player.getColumns() == columns) {
            System.out.println(playersWaiting);

            if (playersWaiting.get(columns) != null) {
                startMatch(playersWaiting.get(columns), player, columns);

                // Remove the player waiting on
                playersWaiting.put(columns, null);
            } else {
                playersWaiting.put(columns, player);
            }
        }
    }

    private static void startMatch(Player player1, Player player2, Integer columns) {
        MessageManager.showXMessage(Messages.MATCH_FOUND);
        System.out.println();
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

    public static void endMatch(GameMatch gameMatch) {
        currentGameMatches.remove(gameMatch);
    }

    public static void showPlayersCount() {
        int count = 0;

        for (Entry<Integer, Player> player : playersWaiting.entrySet()) {
            if (player.getValue() != null)
                count++;
        }
        System.out.println("Current players waiting: " + count);
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
