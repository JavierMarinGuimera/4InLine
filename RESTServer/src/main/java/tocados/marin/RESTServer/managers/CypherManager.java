package tocados.marin.RESTServer.managers;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CypherManager {
    private static final int BITS_256 = 256;

    private static final boolean ENCRYPT_MODE = true;
    private static final boolean DECRYPT_MODE = false;

    /**
     * Private constructor to make this clases NOT instantiable.
     */
    private CypherManager() {
    }

    /**
     * Method used to encrypt the user password to save it on the DDBB.
     * 
     * @param password User's password.
     * @return Password encrypted.
     */
    public static String encryptUserPassword(String password) {
        try {
            return new String(
                    encryptOrDecryptData(passwordKeyGeneration(password, BITS_256), password.getBytes("UTF-8"),
                            ENCRYPT_MODE));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Method used to decrypt the user password from the DDBB.
     * 
     * @param password User's DDBB password.
     * @return Password decrypted.
     */
    public static String decryptUserPassword(String password) {
        try {
            return new String(
                    encryptOrDecryptData(passwordKeyGeneration(password, BITS_256), password.getBytes("UTF-8"),
                            DECRYPT_MODE));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Method to generate a SecretKey from the user password.
     * 
     * @param password User's password
     * @param keySize  Key size. 128, 192 or 256 bits are permitted.
     * @return The generated SecretKey.
     */
    private static SecretKey passwordKeyGeneration(String password, int keySize) {
        SecretKey sKey = null;

        if ((keySize == 128) || (keySize == 192) || (keySize == 256)) {
            try {
                byte[] data = password.getBytes("UTF-8"); // Transform password in a byte[], codified in UTF-8

                MessageDigest md = MessageDigest.getInstance("SHA-256"); // Get a MessageDigest instance for create
                                                                         // SHA-256 messages

                byte[] hash = md.digest(data); // Digest the data obtained from the password

                byte[] key = Arrays.copyOf(hash, keySize / 8); // Cut the hash to keySize bits (keysize/8 bytes)

                sKey = new SecretKeySpec(key, "AES"); // Create the SecretKey object based on AES encrypt method from
                                                      // the obtained key
            } catch (Exception ex) {
                System.err.println("Could not generate the AES key:" + ex);
            }

        }

        return sKey;
    }

    /**
     * Method that encrypts or decrypts the data using the SecretKey.
     * 
     * @param sKey    SecretKey used to encrypt or decrypt.
     * @param data    Data to encrypt or decrypt.
     * @param encrypt True for encrypt; False for decrypt.
     * @return The data result on a byte array.
     */
    private static byte[] encryptOrDecryptData(SecretKey sKey, byte[] data, boolean encrypt) {
        byte[] dataToHandle = null;

        if (sKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init((encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE), sKey);
                dataToHandle = cipher.doFinal(data);
            } catch (Exception ex) {
                System.err.println("Could not " + (encrypt ? "encrypt" : "decrypt") + " the data: " + ex);

            }
        }

        return dataToHandle;
    }
}
