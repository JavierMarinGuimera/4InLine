package managers;

public class MessageManager {

    public enum Messages {
        WELCOME_MESSAGE("Game server started!"),
        FAREWELL_MESSAGE("Game server finished!"),
        WAITING_USER_RESPONSE("Waiting user response...");

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
