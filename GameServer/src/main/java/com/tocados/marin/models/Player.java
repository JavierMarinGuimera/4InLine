package com.tocados.marin.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;

import com.tocados.marin.managers.JSONManager;

public class Player {
    private Socket playerSocket;
    private Boolean created;
    private Integer columns;
    private Boolean isWinner;
    private Integer score = 0;
    private String username;
    private Integer position;

    private BufferedReader reader;
    private PrintStream writer;

    /**
     * @param playerSocket
     * @param columns
     */
    public Player(Socket playerSocket) {
        this.playerSocket = playerSocket;

        try {
            this.reader = new BufferedReader(new InputStreamReader(this.playerSocket.getInputStream()));
            this.writer = new PrintStream(this.playerSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        readInfo();
    }

    private void readInfo() {
        while (true) {
            try {
                String jsonString = this.reader.readLine();

                if (jsonString == null) {
                    throw new Exception("Socket connection lost.");
                }

                // Parsing jsonString to jsonObject.
                Map<String, Object> jsonMap = JSONManager.getMapFromJsonString(jsonString);

                if (jsonMap.containsKey(JSONManager.COLUMN) && jsonMap.containsKey(JSONManager.USERNAME)) {
                    this.columns = (Integer) jsonMap.get(JSONManager.COLUMN);
                    this.username = (String) jsonMap.get(JSONManager.USERNAME);
                    this.writer.println("OK");
                } else {
                    this.writer.println("ERROR");
                }

                this.created = true;

                break;
            } catch (Exception e) {
                this.created = false;
                break;
            }
        }
    }

    /**
     * @return the playerSocket
     */
    public Socket getPlayerSocket() {
        return playerSocket;
    }

    /**
     * @param playerSocket the playerSocket to set
     */
    public void setPlayerSocket(Socket playerSocket) {
        this.playerSocket = playerSocket;
    }

    /**
     * @return the created
     */
    public Boolean getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Boolean created) {
        this.created = created;
    }

    /**
     * @return the columns
     */
    public Integer getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    /**
     * @return the isWinner
     */
    public Boolean getIsWinner() {
        return isWinner;
    }

    /**
     * @param isWinner the isWinner to set
     */
    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }

    /**
     * @return the score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return the reader
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * @param reader the reader to set
     */
    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * @return the writer
     */
    public PrintStream getWriter() {
        return writer;
    }

    /**
     * @param writer the writer to set
     */
    public void setWriter(PrintStream writer) {
        this.writer = writer;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Integer position) {
        this.position = position;
    }
}
