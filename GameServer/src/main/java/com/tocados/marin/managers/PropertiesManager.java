package com.tocados.marin.managers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesManager {
    private static final String PROPERTIES_FILE = "./src/main/resources/db.properties";
    private static final String SERVER_NAME = "server.user";
    private static final String SERVER_PASSWORD = "server.password";
    private static final String SERVER_TOKEN = "server.token";

    private static FileReader fileReader;
    private static Properties properties;

    public static void main(String[] args) {
        String serverName = getPropertyByName(SERVER_NAME);
        String serverPassword = getPropertyByName(SERVER_PASSWORD);
        String serverToken = getPropertyByName(SERVER_TOKEN);

        System.out.println("Name: " + serverName + ", Password: " + serverPassword + ", Encrypted password: "
                + EncrypterManager.encryptUserPassword(serverPassword) + ", token: " + serverToken);
    }

    static {
        try {
            fileReader = new FileReader(PROPERTIES_FILE);

            properties = new Properties();
            properties.load(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PropertiesManager() {
    }

    public static String getPropertyByName(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public static Object setPropertyByName(String propertyName, String propertyValue) {
        return properties.setProperty(propertyName, propertyValue);
    }

    public static Map<String, Object> getLoginMap() {
        return new HashMap<String, Object>() {
            {
                put("username", getPropertyByName(SERVER_NAME));
                put("password", getPropertyByName(SERVER_PASSWORD));
            }
        };
    }
}
