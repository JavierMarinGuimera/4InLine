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
    private Integer columns;
    private Boolean isWinner;

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
                System.out.println(this.reader.readLine());
                String jsonString = this.reader.readLine();

                // Parsing jsonString to jsonObject.
                Map<String, Object> jsonMap = JSONManager.getMapFromJsonString(jsonString);

                if (jsonMap.containsKey(JSONManager.COLUMN)) {
                    this.columns = (Integer) jsonMap.get(JSONManager.COLUMN);
                    this.writer.println("OK");
                } else {
                    this.writer.println("ERROR");
                }

                break;
            } catch (Exception e) {
                this.writer.println("SOMETHING WENT WRONG");
                continue;
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
}
