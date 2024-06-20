package util;

import java.util.Properties;

public class Config {
    private Properties properties;

    public Config() {
        properties = new Properties();
        // Load your properties here
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}
