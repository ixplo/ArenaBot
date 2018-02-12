package ml.ixplo.arenabot.config;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    private PropertiesLoader() {
    }

    private static final String LOGTAG = "PropertiesLoader";
    private static Map<String, String> settings = new HashMap<>();

    public static Map<String, String> getProperties() {
        return getProperties(Config.PROPERTIES_FILE);
    }

    public static Map<String, String> getProperties(String file) {
        try {
            loadSettingsFrom(file);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
        return settings;
    }

    public static long getChannelId() {
        return Long.parseLong(getProperties().get("channel.id"));
    }

    public static String getVersion() {
        return getProperties().get("version");
    }

    private static void loadSettingsFrom(String fileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        properties.load(fis);
        Enumeration enumeration = properties.keys();
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement().toString();
            settings.put(key, properties.getProperty(key));
        }
    }

}
