package com.tocados.marin.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
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
        while (!this.matchEnded) {
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

        if (this.player1.getIsWinner() != null) {
            sendPlayerMessages(player1Writer, player2Writer);
        } else {
            closePlayers(player1Writer, player2Writer);
        }

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
        playerPlaying.setScore(playerPlaying.getScore() + 5);

        List<Integer[]> chipsCoordenates = getArroundChipsMap(column);
        Integer wins = 0;

        if (chipsCoordenates != null) {
            for (Integer[] map : chipsCoordenates) {
                if (checkFor4InLine(map, column, this.board.get(column).size() - 1)) {
                    playerPlaying.setIsWinner(true);
                    playerToNotify.setIsWinner(false);
                    this.matchEnded = true;
                }
            }
        }

        if (wins != 0) {
            if (wins == 1) {

            } else if (wins > 1) {

            }
        }
    }

    /**
     * Mounts a map with the chips arround the last one entered by the user
     */
    private List<Integer[]> getArroundChipsMap(Integer column) {
        List<Integer[]> chipsCoordenates = new ArrayList<>();

        // If the column is the first one, the start value will be 0.
        Integer x = Math.max(column - 1, 0);

        // If the row is the first one, the start value will be 0.
        Integer y = Math.max(this.board.get(column).size() - 1, 0);

        for (int i = x; i < Math.min(x + 3, this.board.size()); i++) {
            /**
             * For each column, we need to check if has at least one value to check.
             * Example (the X is the newest user chip):
             * 
             * 1 2 3
             * _ X _
             * _ o x
             * x o o
             * 
             * On the example, the first column will not be read, the second will only read
             * the bottom "o" and on the third will read only the "x".
             */
            if (this.board.get(i).size() >= y) {
                for (int j = y; j < Math.min(y + 3, this.board.get(i).size()); j++) {
                    /**
                     * If we are looking on the newest user's chip, we will be avoid it.
                     * 
                     * Else if: Will check if the newest user's chip is the same chip that we are
                     * checking right now.
                     */
                    if ((i == column && j == this.board.get(column).size())) {
                        continue;
                    } else if (this.board.get(column).get(y + 2) == this.board.get(i).get(j)) {
                        chipsCoordenates.add(new Integer[] { i, j });
                    }
                }
            }
        }

        this.board.get(column).size();

        return chipsCoordenates;
    }

    private Boolean checkFor4InLine(Integer[] map, Integer chipColumn, Integer chipRow) {
        Integer newestChip = this.board.get(chipColumn).get(chipRow), currentChip;
        Integer x = map[0], y = map[1];

        Integer xDirection = (Math.max(x, chipColumn) - Math.min(x, chipColumn)),
                yDirection = (Math.max(y, chipRow) - Math.min(y, chipRow));

        for (int i = 0; i < 4; i++) {
            x -= xDirection;
            y -= yDirection;
            currentChip = this.board.get(x).get(y);
            if (currentChip == null || currentChip != newestChip)
                return false;
        }

        return true;
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

    private void closePlayers(PrintStream player1Writer, PrintStream player2Writer) {
        // player1Writer.println(JSONManager.);
        // player2Writer.println(x);
    }
}
