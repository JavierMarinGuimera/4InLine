package app;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static List<Socket> pendingPlayers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        while (true) {
            // Esperar petición jugador.

            // Leer tipo de tablero.

            // Asociar tablero a jugador.

            checkListForMatches();
        }
    }

    public static void checkListForMatches() {
        Socket playerMatch5, playerMatch7, playerMatch9;

        for (Socket socket : pendingPlayers) {
            if (socket == "5") {
                if (playerMatch5 != null) {
                    startMatch(playerMatch5, socket);
                } else {
                    playerMatch5 = socket;
                }
            }
            if (socket == "7") {
                if (playerMatch7 != null) {
                    startMatch(playerMatch7, socket);
                } else {
                    playerMatch7 = socket;
                }
            }
            if (socket == "9") {
                if (playerMatch9 != null) {
                    startMatch(playerMatch9, socket);
                } else {
                    playerMatch9 = socket;
                }
            }
        }
    }
}
