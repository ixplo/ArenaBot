package ml.ixplo.arenabot.config;

import ml.ixplo.arenabot.exception.ArenaUserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {
    private static PropertiesLoader propertiesLoader;

    static {
        propertiesLoader = new PropertiesLoader();
    }

    private static Map<String, String> settings = new HashMap<>();

    private String propertiesFile = Config.PROPERTIES_FILE;

    public PropertiesLoader() {
    }

    public PropertiesLoader(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public static PropertiesLoader getInstance() {
        return propertiesLoader;
    }

    public static void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        PropertiesLoader.propertiesLoader = propertiesLoader;
    }

    private static Map<String, String> getProperties(String file) {
        try {
            loadSettingsFrom(file);
        } catch (IOException e) {
            throw new ArenaUserException("Не удалось найти файл " + file);
        }
        return settings;
    }

    private static void loadSettingsFrom(String fileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        properties.load(fis);
        Enumeration enumeration = properties.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            settings.put(key, properties.getProperty(key));
        }
    }

    public Map<String, String> getProperties() {
        return getProperties(propertiesFile);
    }


    public long getChannelId() {
        return getLong(Config.CHANNEL_ID_PROPERTY);
    }

    public long getLong(String key) {
        return Long.parseLong(getProperty(key));
    }

    public String getProperty(String key) {
        String value = getProperties().get(key);
        if (value == null) {
            throw new ArenaUserException(String.format("Нет такого параметра %s в файле настроек %s", key, propertiesFile));
        }
        return value;
    }

}
