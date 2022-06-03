package com.tocados.marin.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.tocados.marin.apps.ServerApp;
import com.tocados.marin.managers.JSONManager;
import com.tocados.marin.managers.MessageManager;
import com.tocados.marin.managers.MessageManager.Messages;

public class GameMatch extends Thread {
    private static int TIMEOUT = 10000;
    private static int MAX_ROWS = 6;

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
        res += "\n}";
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
        PrintStream player2Writer = this.player2.getWriter();

        // Send to the users that they have a match and what is their position.
        player1.setPosition(1);
        player2.setPosition(2);
        player1Writer.println(JSONManager.mountHasOponentJson(1));
        player2Writer.println(JSONManager.mountHasOponentJson(2));

        Integer column = null;

        while (!this.matchEnded) {
            this.rounds++;

            // Returns null if the server is closed.
            if ((column = getUserColumn(player1Reader, column)) == null)
                break;

            if (checkMatchStatus(column, this.player1, this.player2)) {
                player1Writer.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.WINNER, this.player1.getScore()));
                player2Writer.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.LOSER, this.player2.getScore()));
            } else {
                player2Writer.println(JSONManager.mountColumnJson(column));
                column = null;
            }

            if (this.matchEnded)
                break;

            if ((column = getUserColumn(player2Reader, column)) == null)
                break;

            if (checkMatchStatus(column, this.player2, this.player1)) {
                player2Writer.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.WINNER, this.player2.getScore()));
                player1Writer.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.LOSER, this.player1.getScore()));
            } else {
                player1Writer.println(JSONManager.mountColumnJson(column));
                column = null;
            }

            System.out.println(this.board);
        }

        if (this.player1.getIsWinner() == null) {
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

        MessageManager.showXMessage(Messages.MATCH_ENDED);
        ServerApp.endMatch(this);
    }

    /**
     * Read the column of the current user playing.
     * 
     * @param playerReader
     * @param column
     * @return
     */
    private Integer getUserColumn(BufferedReader playerReader, Integer column) {
        System.out.println(column);
        while (column == null) {
            try {
                if (this.player1.getIsWinner() == null && this.player2.getIsWinner() == null && !this.matchEnded) {
                    String playerRequest = playerReader.readLine();
                    System.out.println(playerRequest);

                    // User column
                    column = (Integer) JSONManager.getMapFromJsonString(playerRequest)
                            .get(JSONManager.COLUMN);
                    return column;
                } else {
                    return column;
                }
            } catch (IOException e) {
                if (!ServerApp.run) {
                    return null;
                }
                continue;
            }
        }
        return column;
    }

    /**
     * Check the match status with the new chip.
     * 
     * @param column         The new chip column.
     * @param playerPlaying  The player that has entered the chip.
     * @param playerToNotify The player that is waiting the response.
     * @return true if the current player won the match; false if not won.
     */
    private Boolean checkMatchStatus(Integer column, Player playerPlaying, Player playerToNotify) {
        if (this.board.get(column).size() < MAX_ROWS) {
            this.board.get(column).add(playerPlaying.getPosition());
            playerPlaying.setScore(playerPlaying.getScore() + 5);

            List<Integer[]> chipsCoordenates = getArroundChipsMap(column, this.board.get(column).size() - 1);
            Integer wins = 0;

            if (chipsCoordenates != null) {
                for (Integer[] map : chipsCoordenates) {
                    if (checkFor4InLine(map, column, Math.max(this.board.get(column).size() - 1, 0))) {
                        playerPlaying.setIsWinner(true);
                        playerToNotify.setIsWinner(false);
                        wins++;
                        this.matchEnded = true;
                    }
                }
            }

            if (wins != 0) {
                if (wins == 1) {
                    playerPlaying.setScore(playerPlaying.getScore() + 20);
                } else if (wins > 1) {
                    playerPlaying.setScore(playerPlaying.getScore() + 50);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Mounts a map with the chips arround the last one entered by the user
     */
    private List<Integer[]> getArroundChipsMap(Integer column, Integer row) {
        List<Integer[]> chipsCoordenates = new ArrayList<>();

        // If the column is the first one, the start value will be 0.
        Integer x = Math.max(column - 1, 0);

        // If the row is the first one, the start value will be 0.
        Integer y = Math.max(this.board.get(column).size() - 2, 0);

        for (int i = x; i < Math.min(x + 3, this.board.size()); i++) {
            /**
             * For each column, we need to check if has at least one value to check.
             * Example (the X is the newest user chip):
             * 
             * 1 2 3
             * _ X _
             * _ o x
             * x o o x
             * 
             * On the example, the first column will not be read, the second will only read
             * the bottom "o" and on the third will read only the "x".
             */
            if (this.board.get(i).size() - 2 >= y) {
                for (int j = y; j < Math.min(y + 3, this.board.get(i).size()); j++) {
                    /**
                     * If we are looking on the newest user's chip, we will be avoid it.
                     * 
                     * Else if: Will check if the newest user's chip is the same chip that we are
                     * checking right now.
                     */
                    if ((i == column && j == this.board.get(column).size() - 1)) {
                        continue;
                    } else if (this.board.get(column) != null
                            && (this.board.get(column).get(row) == this.board.get(i).get(j))) {
                        chipsCoordenates.add(new Integer[] { i, j });
                    }
                }
            }
        }

        System.out.println(chipsCoordenates.size());

        return chipsCoordenates;
    }

    /**
     * Check if we have a 4 in line.
     * 
     * @param map        Integer array with 2 values of one of the closest chips.
     *                   First value = x, second value = y.
     * @param chipColumn The last entered chip column to calculate the direction of
     *                   the 4 in line.
     * @param chipRow    The last entered chip row to calculate the direction of the
     *                   4 in line.
     * @return True if 4 in line is encountered, false if not.
     */
    private Boolean checkFor4InLine(Integer[] map, Integer chipColumn, Integer chipRow) {
        // TODO - Comprobación 4 en raya diagonal.

        Integer newestChip = this.board.get(chipColumn).get(chipRow), currentChip;
        Integer x = map[0], y = map[1];

        Integer xDirection = x - chipColumn,
                yDirection = y - chipRow;

        for (int i = 0; i < 3; i++) {
            if (x < 0 || y < 0)
                return false;

            if (x > this.board.size() - 1 || this.board.get(x) == null
                    || y > this.board.get(x).size() || this.board.get(x).get(y) == null) {
                currentChip = null;
            } else {
                currentChip = this.board.get(x).get(y);
            }

            if (currentChip == null || currentChip != newestChip)
                return false;

            x += xDirection;
            y += yDirection;
        }

        return true;
    }

    private void closePlayers(PrintStream player1Writer, PrintStream player2Writer) {
        player1Writer.println(JSONManager.mountServerCloseJson());
        player2Writer.println(JSONManager.mountServerCloseJson());
    }
}
