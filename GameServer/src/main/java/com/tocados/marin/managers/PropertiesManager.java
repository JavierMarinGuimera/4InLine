package com.tocados.marin.managers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesManager {

    public enum PropertiesStrings {
        PROPERTIES_FILE("./src/main/resources/db.properties"),
        SERVER_NAME("server.user"),
        SERVER_PASSWORD("server.password"),
        SERVER_TOKEN("server.token");

        private String propertyString;

        PropertiesStrings(String propertyString) {
            this.propertyString = propertyString;
        }

        public String getPropertyString() {
            return propertyString;
        }
    }

    private static FileReader fileReader;
    private static Properties properties;

    static {
        try {
            fileReader = new FileReader(PropertiesStrings.PROPERTIES_FILE.getPropertyString());

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

    public static String getPropertyByName(PropertiesStrings property) {
        return properties.getProperty(property.getPropertyString());
    }

    public static Object setPropertyByName(PropertiesStrings property, String propertyValue) {
        return properties.setProperty(property.getPropertyString(), propertyValue);
    }

    public static Map<String, Object> getLoginMap() {
        return new HashMap<String, Object>() {
            {
                put("username", getPropertyByName(PropertiesStrings.SERVER_NAME));
                put("password", EncrypterManager
                        .encryptPassword(getPropertyByName(PropertiesStrings.SERVER_PASSWORD)));
            }
        };
    }
}
