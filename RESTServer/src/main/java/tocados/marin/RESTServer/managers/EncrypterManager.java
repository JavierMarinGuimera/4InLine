package tocados.marin.RESTServer.managers;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncrypterManager {
    private static final String SHA_256_ALGORITHM = "SHA-256";

    /**
     * Private constructor to make this clases NOT instantiable.
     */
    private EncrypterManager() {
    }

    /**
     * Method used to encrypt the user password to save it on the DDBB.
     * 
     * @param password User's password.
     * @return Password encrypted.
     */
    public static String encryptUserPassword(String password) {
        try {
            return toHexString(getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static byte[] getSHA(String password) throws NoSuchAlgorithmException {
        /* MessageDigest instance for hashing using SHA256 */
        MessageDigest md = MessageDigest.getInstance(SHA_256_ALGORITHM);

        /*
         * digest() method called to calculate message digest of an input and return
         * array of byte
         */
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    private static String toHexString(byte[] hash) {
        /* Convert byte array of hash into digest */
        BigInteger number = new BigInteger(1, hash);

        /* Convert the digest into hex value */
        StringBuilder hexString = new StringBuilder(number.toString(16));

        /* Pad with leading zeros */
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}
