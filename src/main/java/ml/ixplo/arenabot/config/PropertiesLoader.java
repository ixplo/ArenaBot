package ml.ixplo.arenabot.config;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    private static String LOGTAG = "PropertiesLoader";
    private static Map<String, String> settings = new HashMap<>();

    public static Map<String, String> getProperties(String file) {
        try {
            loadSettingsFrom(file);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
        return settings;
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
