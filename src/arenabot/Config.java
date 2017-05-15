package arenabot;

import java.util.Arrays;
import java.util.List;

/**
 * ixplo
 * 25.04.2017.
 */
public class Config {

    static final String BOT_NAME = "ArenaBot";
    static final String VERSION = "0.3";
    static final String BOT_CHANNEL_NAME = "#Arena";
    final String rules = "rules.html";      //mc(rules)/todo it
    public static final long CHANNEL_ID = -161929436;//arena
//    public static final long CHANNEL_ID = -202049243;//#arena
    public static final Integer IS_ADMIN = 362812407;
    static final String BOT_TOKEN = "298781231:AAHDBJLNCjRpxmT7aazDhh5Hr7t6xNLCwr0";
    public static final String DB_CONTROLLER = "org.sqlite.JDBC";
    public static final String DB_LINK = "jdbc:sqlite:ArenaDb.sqlite";
    static final String BOT_LINK = "https://telegram.me/ArenaMBot?start=regUser";
    public static final String BOT_PRIVATE = "https://telegram.me/ArenaMBot";
    static final String BOT_PHOTO_LINK = "http://www.wallpapermint.com/wp-content/uploads/2014/02/Fantasy-World-Pack3-21-280x158.jpg";
    static final String BOT_TUMB_PHOTO_LINK = "http://pixs.ru/showimage/tumbjpg_5168201_25993141.jpg";
    public static final String USERS = "users";
    public static final String CLASSES = "classes";
    public static final String RACES = "races";
    public static final String EQIP = "inventory";
    public static final String AVAILABLE_SPELLS = "available_spells";
    public static final String SPELLS = "spells";
    public static final String TEAMS = "teams";
    public static final String USER_ID = "user_Id";
    static final int BOT_PHOTO_WIDTH = 280;
    static final int BOT_PHOTO_HEIGHT = 178;
    static final boolean DO_NOT_COMMAND_UPDATE = false;
    public static final int WIDTH = 27;
    public static final int UNREG=0;
    public static final int REG=1;
    public static final int IN_BATTLE=2;
    public static final int DELAY = 15;
    public static final int ROUND_TIME = 30000;
    public static final int ROUND_REMIND = 15000;

}
