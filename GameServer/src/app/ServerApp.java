package app;

import java.util.ArrayList;
import java.util.List;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import models.GameMatch;
import models.Player;

public class ServerApp {
    private static final int PORT = 7777;

    public static Boolean run = true;
    private static List<Player> pendingPlayers = new ArrayList<>();
    private static List<GameMatch> currentGameMatches = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) ssocketFactory.createServerSocket(PORT);

        while (run) {
            pendingPlayers.add(new Player((SSLSocket) sslServerSocket.accept()));

            // Esperar petición jugador.

            // Leer tipo de tablero.

            // Asociar tablero a jugador.

            checkListForMatches();
            break;
        }

        if (sslServerSocket != null) {
            sslServerSocket.close();
        }
    }

    public static void checkListForMatches() {
        Player playerMatch5 = null, playerMatch7 = null, playerMatch9 = null;

        for (Player player : pendingPlayers) {
            if (player.getColumns() == 5) {
                if (playerMatch5 != null) {
                    startMatch(playerMatch5, player, 5);
                } else {
                    playerMatch5 = player;
                }
            }
            if (player.getColumns() == 7) {
                if (playerMatch7 != null) {
                    startMatch(playerMatch7, player, 7);
                } else {
                    playerMatch7 = player;
                }
            }
            if (player.getColumns() == 9) {
                if (playerMatch9 != null) {
                    startMatch(playerMatch9, player, 9);
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

    public static void showMatches() {
        currentGameMatches.forEach((gameMatch) -> {
            System.out.println(gameMatch);
        });
    }

    public static void endGames() {
        currentGameMatches.forEach((gameMatch) -> {
            gameMatch.endMatch();
        });
    }
}
