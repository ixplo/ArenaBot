package ml.ixplo.arenabot.config;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    private static String LOGTAG = "PropertiesLoader";

    public static Map<String, String> getProperties(String fileName) {
        HashMap<String, String> settings = new HashMap<>();
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            properties.load(fis);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
        Enumeration enumeration = properties.keys();
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement().toString();
            settings.put(key, properties.getProperty(key));
        }
        return settings;
    }

}
