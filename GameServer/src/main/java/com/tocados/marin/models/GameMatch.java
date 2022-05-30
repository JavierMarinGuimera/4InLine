package com.tocados.marin.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.tocados.marin.managers.JSONManager;
import com.tocados.marin.managers.MessageManager.Messages;

public class GameMatch extends Thread {
    private static int TIMEOUT = 10000;

    private Player player1;
    private Player player2;
    private Stack<Stack<Integer>> board;
    private Integer rounds = 0;
    private Boolean matchEnded = false;

    /**
     * @param player1
     * @param player2
     */
    public GameMatch(Player player1, Player player2, Integer columns) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Stack<>();

        for (int i = 0; i < columns; i++) {
            this.board.add(new Stack<Integer>());
        }
    }

    /**
     * @return the player1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @param player1 the player1 to set
     */
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    /**
     * @return the player2
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * @param player2 the player2 to set
     */
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    /**
     * @return the rounds
     */
    public Integer getRounds() {
        return rounds;
    }

    /**
     * @param rounds the rounds to set
     */
    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    /**
     * @return the matchEnded
     */
    public Boolean getMatchEnded() {
        return matchEnded;
    }

    /**
     * @param matchEnded the matchEnded to set
     */
    public void setMatchEnded(Boolean matchEnded) {
        this.matchEnded = matchEnded;
    }

    public void endMatch() {
        this.matchEnded = true;
    }

    @Override
    public String toString() {
        String res = "";
        res += "{\n";
        res += "\tMatch rounds: " + this.rounds;
        res += "}";
        return res;
    }

    @Override
    public void run() {
        try {
            this.player1.getPlayerSocket().setSoTimeout(TIMEOUT);
            this.player2.getPlayerSocket().setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // Player 1 streams;
        BufferedReader player1Reader = this.player1.getReader();
        PrintStream player1Writer = this.player1.getWriter();

        // Player 2 streams;
        BufferedReader player2Reader = this.player2.getReader();
        PrintStream player2Writer = this.player1.getWriter();

        player1Writer.println(JSONManager.mountHasOponentJson(true));
        player2Writer.println(JSONManager.mountHasOponentJson(true));

        Integer column;
        while (true) {
            this.rounds++;

            // TODO - rondas
            try {
                if (this.player1.getIsWinner() == null) {
                    // User 1 column
                    column = (Integer) JSONManager.getMapFromJsonString(player1Reader.readLine())
                            .get(JSONManager.COLUMN);
                    checkMatchStatus(column, this.player1, this.player2);
                } else {
                    break;
                }

                if (this.player2.getIsWinner() == null) {
                    // User 2 column
                    column = (Integer) JSONManager.getMapFromJsonString(player1Reader.readLine())
                            .get(JSONManager.COLUMN);
                    checkMatchStatus(column, this.player1, this.player2);
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sendPlayerMessages(player1Writer, player2Writer);

        try {
            player1Reader.close();
            player1Writer.close();

            player2Reader.close();
            player2Writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkMatchStatus(Integer column, Player playerPlaying, Player playerToNotify) {
        List<Map<Integer, Integer>> chipsCoordenates = getArroundChipsMap(column);
        Boolean isWinner = false;
        if (chipsCoordenates != null) {
            for (Map<Integer, Integer> map : chipsCoordenates) {
                if (checkFor4InLine(map)) {
                    playerPlaying.setIsWinner(true);
                    playerToNotify.setIsWinner(false);
                }
            }
        }

        if (isWinner) {
            this.matchEnded = true;
        }
    }

    /**
     * Mounts map with the chips arround the last one entered by the user
     */
    private List<Map<Integer, Integer>> getArroundChipsMap(Integer column) {
        List<Map<Integer, Integer>> chipsCoordenates = new ArrayList<>();

        // If the column is the first one, the start value will be 0.
        Integer x = Math.max(column - 1, 0);
        Integer y = Math.max(column - 1, 0);

        for (int i = x; i < Math.min(x + 2, this.player1.getColumns()); i++) {

        }

        this.board.get(column).size();

        return chipsCoordenates;
    }

    private Boolean checkFor4InLine(Map<Integer, Integer> map) {
        return false;
    }

    private void sendPlayerMessages(PrintStream player1Writer, PrintStream player2Writer) {
        if (this.player1.getIsWinner()) {
            player1Writer.println(Messages.WINNER);
            player2Writer.println(Messages.LOSER);
        } else {
            player1Writer.println(Messages.LOSER);
            player2Writer.println(Messages.WINNER);
        }
    }
}
