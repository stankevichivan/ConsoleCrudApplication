package com.sivan.crudapp.db.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DbPropertiesUtil {

    public static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private DbPropertiesUtil() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inputStream = DbPropertiesUtil.class.getClassLoader().getResourceAsStream("db/db.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
