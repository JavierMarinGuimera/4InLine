package com.tocados.marin.managers;

public class MessageManager {

    public enum Messages {
        LOGIN_FAILED("Login failded! Check server credentials on db.properties."),
        WELCOME_MESSAGE("Game server started!"),
        FAREWELL_MESSAGE("Game server finished!"),
        WAITING_FOR_USERS("Waiting for users..."),
        WAITING_USER_RESPONSE("Waiting user response..."),
        USER_FOUND("User found!"),
        MATCH_FOUND("Match found!"),
        MATCH_ENDED("Match ended!"),
        SERVER_CLOSED("Server has been closed unexpectly."),
        WIN("win"),
        LOSE("lose"),
        DRAW("draw"),
        OPPONENT_GOT_4_IN_LINE("Opponent has made a 4 in line."),
        DRAW_RESULT("Both got draw."),
        REST_SERVER_RESPONSE("REST Server response: "),
        NOTHING("");

        private String msg;

        Messages(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }
    }

    private MessageManager() {
    }

    public static void showXMessage(Messages msg) {
        System.out.println(msg.getMessage());
    }
}
