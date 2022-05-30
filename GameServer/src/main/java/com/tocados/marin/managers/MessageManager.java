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
        WINNER("winner"),
        LOSER("loser"),
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
