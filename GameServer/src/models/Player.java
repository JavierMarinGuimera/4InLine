package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

public class Player {
    private Socket playerSocket;
    private Integer columns;
    private String username;
    private String token;

    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * @param playerSocket
     * @param columns
     */
    public Player(SSLSocket playerSocket) {
        this.playerSocket = playerSocket;

        try {

            this.reader = new BufferedReader(new InputStreamReader(this.playerSocket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(this.playerSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        readColumns();
    }

    private void readColumns() {
        while (true) {
            try {
                Integer columns = this.reader.read();
                this.columns = columns;
                this.writer.write("OK");
                break;
            } catch (Exception e) {
                try {
                    this.writer.write("SOMETHING WENT WRONG");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
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
    public BufferedWriter getWriter() {
        return writer;
    }

    /**
     * @param writer the writer to set
     */
    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }
}
