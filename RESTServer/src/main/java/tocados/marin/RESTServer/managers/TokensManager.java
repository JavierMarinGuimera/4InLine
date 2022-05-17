package tocados.marin.RESTServer.managers;

import java.security.SecureRandom;
import java.util.Base64;

public class TokensManager {
    /**
     * Calc of 1 week to miliseconds:
     * 1 week * 7 days * 24 hours * 60 minuts * 60 seconds * 1000 miliseconds
     */
    public static final int TOKEN_EXPIRATION_TIME = 1 * 7 * 24 * 60 * 60 * 1000;

    private static final int BYTE_SIZE = 64;

    private TokensManager() {
    }

    public static String generateRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();

        byte[] randomBytes = new byte[BYTE_SIZE];
        secureRandom.nextBytes(randomBytes);

        return base64Encoder.encodeToString(randomBytes);
    }
}
