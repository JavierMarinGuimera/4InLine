package com.tocados.marin.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.tocados.marin.apps.ServerApp;
import com.tocados.marin.managers.HTTPManager;
import com.tocados.marin.managers.JSONManager;
import com.tocados.marin.managers.MessageManager;
import com.tocados.marin.managers.PropertiesManager;
import com.tocados.marin.managers.HTTPManager.Paths;
import com.tocados.marin.managers.MessageManager.Messages;
import com.tocados.marin.managers.PropertiesManager.PropertiesStrings;

public class GameMatch extends Thread {
    private static Integer TIMEOUT = 10000;
    private static Integer MAX_ROWS = 6;

    private Player player1;
    private Player player2;
    private Stack<Stack<Integer>> board;
    private Integer maxChips;
    private Integer currentChipsCount = 0;
    private Integer rounds = 0;
    private Boolean matchEnded = false;

    /**
     * Contructor of the class.
     * 
     * @param player1 Player 1 playing.
     * @param player2 Player 2 playing.
     * @param columns Columns of the board.
     */
    public GameMatch(Player player1, Player player2, Integer columns) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Stack<>();

        for (int i = 0; i < columns; i++) {
            this.board.add(new Stack<Integer>());
        }

        this.maxChips = columns * MAX_ROWS;
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

        // TODO - COMENTAR ESTO PARA QUE EL JUEGO FUNCIONE CORRETAMENTE:
        // mountCustomBoardTest();
        // mountCustomBoard1();
        // mountCustomBoard2();
        // mountCustomBoard3();

        Integer column = null;

        while (!this.matchEnded) {
            this.rounds++;

            /**
             * Part 1: Player 1 sends, player 2 gets.
             */

            /**
             * This method checks everything on the current loop and returns something
             * diferent than null if the server is closed unexpectly.
             */
            if ((column = doALoopAndCheckEverything(column, this.player1, player1Reader, player1Writer, this.player2,
                    player2Writer)) != null) {
                break;
            }

            if (this.matchEnded) {
                break;
            }

            /**
             * Part 2: Player 2 sends, player 1 gets.
             */

            /**
             * This method checks everything on the current loop and returns something
             * diferent than null if the server is closed unexpectly.
             */
            if ((column = doALoopAndCheckEverything(column, this.player2, player2Reader, player2Writer, this.player1,
                    player1Writer)) != null) {
                break;
            }
        }

        if (!ServerApp.run) {
            closePlayers(player1Writer, player2Writer);
        } else {
            savePlayersResults(player1, player2);
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
     * This method checks everything from the player that is sending until the
     * player that is getting the info gets the info.
     * 
     * @param column              Column in null because we need to check if we
     *                            receive anything from the current player playing.
     * @param playerPlaying       Player playing.
     * @param playerPlayingReader Player playing reader.
     * @param playerPlayingWriter Player playing writer for the opponent response.
     * @param playerWaiting       Player waiting.
     * @param playerWaitingWriter Player waiting writer to send the response
     *                            whatever it is.
     * 
     * @return -1 if the server is closed.
     */
    private Integer doALoopAndCheckEverything(Integer column, Player playerPlaying, BufferedReader playerPlayingReader,
            PrintStream playerPlayingWriter, Player playerWaiting, PrintStream playerWaitingWriter) {
        if ((column = getUserColumn(playerPlayingReader, column)) == null) {
            return -1;
        }

        if (checkMatchStatus(column, this.board.get(column).size(), playerPlaying, playerWaiting)) {
            if (this.player1.getIsWinner() != null && this.player2.getIsWinner() != null) {
                playerPlayingWriter.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.WIN, playerPlaying.getScore()));
                playerWaitingWriter.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.LOSE, playerWaiting.getScore()));
            } else {
                playerPlayingWriter.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.DRAW, playerPlaying.getScore()));
                playerWaitingWriter.println(JSONManager.mountColumnAndResultAndScoreJson(column,
                        Messages.DRAW, playerWaiting.getScore()));
            }
        } else {
            playerWaitingWriter.println(JSONManager.mountColumnJson(column));
            column = null;
        }

        return column;
    }

    /**
     * Read the column of the current user playing.
     * 
     * @param playerReader
     * @param column
     * @return
     */
    private Integer getUserColumn(BufferedReader playerReader, Integer column) {
        while (column == null) {
            try {
                if (this.player1.getIsWinner() == null && this.player2.getIsWinner() == null && !this.matchEnded) {
                    String playerRequest = playerReader.readLine();

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
    private Boolean checkMatchStatus(Integer column, Integer row, Player playerPlaying, Player playerToNotify) {
        if (this.board.get(column).size() < MAX_ROWS) {
            // Here we add the new chip to the GameMatch board
            this.board.get(column).add(playerPlaying.getPosition());
            this.currentChipsCount++;
            playerPlaying.setScore(playerPlaying.getScore() + 5);

            Chip newestChip = new Chip(column, row, this.board.get(column).get(row));
            List<Chip> arroundChips = getArroundChipsList(column, this.board.get(column).size() - 1);
            Integer wins = 0;

            if (arroundChips != null) {
                for (Chip arroundChip : arroundChips) {
                    if (checkFor4InLine(arroundChips, arroundChip, newestChip)) {
                        wins++;
                    }
                }
            }

            if (wins != 0) {
                if (wins == 1) {
                    // Single victory adds 20 points to the player.
                    playerPlaying.setScore(playerPlaying.getScore() + 20);
                } else if (wins > 1) {
                    /**
                     * Multiple victories add 20 points per win + other 10 points per win because is
                     * more difficult to hit more that 1 win at the same time.
                     */
                    playerPlaying.setScore(playerPlaying.getScore() + 20 * wins + (10 * wins));
                }

                playerPlaying.setIsWinner(true);
                playerToNotify.setIsWinner(false);
                this.matchEnded = true;

                return true;
            }

            if (isBoardFull()) {
                this.matchEnded = true;
                return true;
            }
        }

        return false;
    }

    private boolean isBoardFull() {
        return this.maxChips == currentChipsCount;
    }

    /**
     * Mounts a map with the chips arround the last one entered by the user
     */
    private List<Chip> getArroundChipsList(Integer column, Integer row) {
        List<Chip> arroundChips = new ArrayList<>();

        /**
         * If the column is the first one, the start value will be 0 and we will need to
         * check ONLY the next column if exist.
         */
        Integer x = Math.max(column - 1, 0), xMax = Math.min(x + (x == 0 && column == 0 ? 2 : 3), this.board.size());

        // If the row is the first one, the start value will be 0.
        Integer y = Math.max(this.board.get(column).size() - 2, 0), yMax;

        for (int i = x; i < xMax; i++) {
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
            if (this.board.get(i).size() - 1 >= y) {
                yMax = Math.min(y + (y == 0 && row == 0 ? 2 : 3), this.board.get(i).size());
                for (int j = y; j < yMax; j++) {
                    /**
                     * If we are looking on the newest user's chip, we will be avoid it.
                     * 
                     * Else if: Will check if the newest user's chip is the same chip that we are
                     * checking right now.
                     */
                    if ((i == column && j == this.board.get(column).size() - 1)) {
                        continue;
                    } else if (this.board.get(column).get(row) == this.board.get(i).get(j)) {
                        arroundChips.add(new Chip(i, j, this.board.get(i).get(j)));
                    }
                }
            }
        }

        return arroundChips;
    }

    /**
     * Check if we have a 4 in line.
     * 
     * @param arroundChips
     * 
     * @param map          Integer array with 2 values of one of the closest
     *                     chips.
     *                     First value = x, second value = y.
     * @param chipColumn   The last entered chip column to calculate the
     *                     direction of
     *                     the 4 in line.
     * @param chipRow      The last entered chip row to calculate the direction
     *                     of the
     *                     4 in line.
     * @return True if 4 in line is encountered, false if not.
     */
    private Boolean checkFor4InLine(List<Chip> arroundChips, Chip arroundChip,
            Chip newestChip) {

        Chip currentChip;
        // Get the coords of the arrounded chip.
        Integer x = arroundChip.getX(), y = arroundChip.getY();

        // Get the direction of the possible 4 in line.
        Integer xDirection = x - newestChip.getX(),
                yDirection = y - newestChip.getY();

        /**
         * This checks if the next chip is equals the newest chip.
         * If we find a different chip, the while will end because we have reached the
         * edge of the possible 4 in line.
         */
        while (!hasFound4inLineEdge(x + xDirection, y + yDirection, arroundChip)) {
            x += xDirection;
            y += yDirection;
        }

        /**
         * Because we have reached the edge of the possible 4 in line, we need to read
         * on the opposite way.
         */
        xDirection = -xDirection;
        yDirection = -yDirection;

        for (int i = 0; i < 4; i++) {
            // Check if we are out of the board or looking on empty tile.
            if (x < 0 || x == this.board.size() || y < 0 || y == this.board.get(x).size()
                    || y > this.board.get(x).size()) {
                return false;
            }

            currentChip = new Chip(x, y, this.board.get(x).get(y));

            /**
             * If we are playing as the player 1, if we find a "2" on the board means that
             * we didnt make a 4 in line on this direction.
             */
            if (currentChip.getValue() != arroundChip.getValue()) {
                return false;
            }

            /**
             * Remove the current chip of the arroundChips if is not the first that we
             * started from.
             */
            if (!arroundChip.equals(currentChip) && arroundChips.contains(currentChip)) {
                arroundChips.remove(currentChip);
            }

            x += xDirection;
            y += yDirection;
        }

        return true;
    }

    /**
     * This will find the edge of the 4 in line.
     * 
     * @param x           The next coord x.
     * @param y           The next coord y.
     * @param arroundChip The chip that we started from.
     * @return True if we found board border, null object or a 2 in case we are the
     *         player 1.
     */
    private boolean hasFound4inLineEdge(Integer x, Integer y, Chip arroundChip) {
        // This will check if we are out of the board.
        if (x < 0 || x == this.board.size() || y < 0 || y == this.board.get(x).size()) {
            return true;
        }

        // This will check if we are on a null object.
        if (this.board.get(x).size() <= y) {
            return true;
        }

        // This will check if we are on a different chip than the newest chip.
        if (this.board.get(x).get(y) != arroundChip.getValue()) {
            return true;
        }

        /**
         * If none of the conditionales got true, that means that we have reached
         * another equal chip.
         */
        return false;
    }

    private void savePlayersResults(Player player1, Player player2) {
        savePlayerInfo(player1);
        savePlayerInfo(player2);
    }

    private void savePlayerInfo(Player player) {
        Map<String, Object> serverInfo = new HashMap<>();
        serverInfo.put("username", PropertiesManager.getPropertyByName(PropertiesStrings.SERVER_NAME));
        serverInfo.put("token", PropertiesManager.getPropertyByName(PropertiesStrings.SERVER_TOKEN));

        Map<String, Object> playerInfo = new HashMap<>();
        playerInfo.put("username", player.getUsername());
        playerInfo.put("score", player.getScore());
        playerInfo.put("isWinner", player.getIsWinner());

        Map<String, Object> resultsMap = new HashMap<>();
        resultsMap.put("server", serverInfo);
        resultsMap.put("player", playerInfo);

        MessageManager.showXMessage(Messages.REST_SERVER_RESPONSE);
        System.out.println(HTTPManager.makeRequest(Paths.SCORES_INSERT_ONE, resultsMap));
    }

    private void closePlayers(PrintStream player1Writer, PrintStream player2Writer) {
        player1Writer.println(JSONManager.mountServerCloseJson());
        player2Writer.println(JSONManager.mountServerCloseJson());
    }

    /**
     * Caso momentaneos:
     */
    // private void mountCustomBoardTest() {
    // this.board.get(0).add(1);
    // this.board.get(1).add(1);
    // this.board.get(2).add(1);
    // }

    /**
     * Caso complejo: quíntuple 4 en raya.
     */
    // private void mountCustomBoard1() {
    // // Columna 0:
    // this.board.get(0).add(1);
    // this.board.get(0).add(2);
    // this.board.get(0).add(1);
    // this.board.get(0).add(1);
    // this.board.get(0).add(2);

    // // Columna 1:
    // this.board.get(1).add(2);
    // this.board.get(1).add(1);
    // this.board.get(1).add(2);
    // this.board.get(1).add(1);

    // // Columna 2:
    // this.board.get(2).add(1);
    // this.board.get(2).add(2);
    // this.board.get(2).add(1);
    // this.board.get(2).add(1);

    // // Columna 3:
    // this.board.get(3).add(1);
    // this.board.get(3).add(1);
    // this.board.get(3).add(1);
    // // El 4 en raya quíntuple entra aquí

    // // Columna 4:
    // this.board.get(4).add(1);
    // this.board.get(4).add(2);
    // this.board.get(4).add(1);
    // this.board.get(4).add(1);

    // // Columna 5:
    // this.board.get(5).add(2);
    // this.board.get(5).add(1);
    // this.board.get(5).add(2);
    // this.board.get(5).add(1);

    // // Columna 6:
    // this.board.get(6).add(1);
    // this.board.get(6).add(2);
    // this.board.get(6).add(1);
    // this.board.get(6).add(1);
    // this.board.get(6).add(2);
    // }

    /**
     * Caso especial: 4 en raya, pero última ficha en medio de 3.
     */
    // private void mountCustomBoard2() {
    // // Columna 0:
    // this.board.get(0).add(1);
    // // Columna 1:

    // // Columna 2:
    // this.board.get(2).add(1);
    // // Columna 3:
    // this.board.get(3).add(1);
    // }

    /**
     * Caso especial: Tablero lleno y 4 en raya última ficha.
     */
    // private void mountCustomBoard3() {
    // // Columna 0:
    // this.board.get(0).add(2);
    // this.board.get(0).add(1);
    // this.board.get(0).add(1);
    // this.board.get(0).add(1);
    // this.board.get(0).add(1);
    // this.board.get(0).add(1);
    // // Columna 1:
    // this.board.get(1).add(2);
    // this.board.get(1).add(1);
    // this.board.get(1).add(1);
    // this.board.get(1).add(1);
    // this.board.get(1).add(1);
    // this.board.get(1).add(1);
    // // Columna 2:
    // this.board.get(2).add(2);
    // this.board.get(2).add(1);
    // this.board.get(2).add(1);
    // this.board.get(2).add(1);
    // this.board.get(2).add(1);
    // this.board.get(2).add(1);
    // // Columna 3:
    // this.board.get(3).add(2);
    // this.board.get(3).add(1);
    // this.board.get(3).add(1);
    // this.board.get(3).add(1);
    // this.board.get(3).add(2);
    // this.board.get(3).add(1);
    // // Columna 4:
    // this.board.get(4).add(1);
    // this.board.get(4).add(1);
    // this.board.get(4).add(1);
    // this.board.get(4).add(1);
    // this.board.get(4).add(2);
    // }
}
