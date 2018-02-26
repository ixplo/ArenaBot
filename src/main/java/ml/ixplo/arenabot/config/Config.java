package ml.ixplo.arenabot.config;

import java.math.BigDecimal;

/**
 * ixplo
 * 25.04.2017.
 */
public class Config {
    public static final String PRE_TAG = "<pre>";
    public static final String CLOSE_PRE_TAG = "</pre>";
    public static final String CLASS_COLUMN = "class";

    private Config() {
    }
    public static final String BOT_NAME = "ArenaBot";
    public static final String BOT_CHANNEL_NAME = "#Arena";
    public static final String PROPERTIES_FILE = "arena.properties";
    public static final String INVALID_USER_ID = "Invalid userId: ";
    public static final String NOT_YET_IMPLEMENTED = "Not yet implemented";
    public static final String INVENTORY = "inventory";
    public static final String COUNTER = "counter";
    public static final String USER_ID = "user_Id";
    public static final String CLOSE_TAG = "</code>\n";
    public static final String IN_SLOT = "in_slot";
    public static final String DESCR = "descr";
    public static final String REGISTERED = "registered";
    public static final String DEXTERITY = "dexterity";
    public static final String WISDOM = "wisdom";
    public static final String INTELLECT = "intellect";
    public static final String CONST = "const";
    public static final String ATTACK = "attack";
    public static final String PROTECT = "protect";
    public static final String ACTIONS = "actions";
    public static final String RULES = "rules.html";      //mc(rules)
    public static final long TEST_CHANNEL_ID = -161929436;
    public static final String TEST_CHANNEL_NAME = "arena";
    public static final String VERSION_PROPERTY = "version";
    public static final String CHANNEL_ID_PROPERTY = "channel.id";
    public static final long CHANNEL_ID = Long.parseLong(PropertiesLoader.getInstance().getProperties().get(CHANNEL_ID_PROPERTY));
    public static final String CHANNEL_NAME = "#arena";
    public static final Integer ADMIN_ID = 362812407;
    public static final String BOT_TOKEN = "298781231:AAHDBJLNCjRpxmT7aazDhh5Hr7t6xNLCwr0";
    public static final String DB_CONTROLLER = "org.sqlite.JDBC";
    public static final String DB_LINK = "jdbc:sqlite:ArenaDb.sqlite";
    public static final String TEST_DB_LINK = "jdbc:sqlite:src/test/resources/TestArenaDb.sqlite";
    public static final String BOT_LINK = "https://telegram.me/ArenaMBot?start=regUser";
    public static final String BOT_PRIVATE = "https://telegram.me/ArenaMBot";
    public static final String BOT_PHOTO_LINK = "http://www.wallpapermint.com/wp-content/uploads/2014/02/Fantasy-World-Pack3-21-280x158.jpg";
    public static final String BOT_TUMB_PHOTO_LINK = "http://pixs.ru/showimage/tumbjpg_5168201_25993141.jpg";
    public static final String STATUS = "status";
    public static final String USERS = "users";
    public static final String CLASSES = "classes";
    public static final String RACES = "races";
    public static final String EQIP = "inventory";
    public static final String ROUND_ACTIONS = "ROUND_ACTIONS";
    public static final String AVAILABLE_SPELLS = "available_spells";
    public static final String SPELL_GRADE = "spell_grade";
    public static final String SPELLS = "spells";
    public static final String TEAMS = "teams";
    public static final int BOT_PHOTO_WIDTH = 280;
    public static final int BOT_PHOTO_HEIGHT = 178;
    public static final boolean DO_NOT_COMMAND_UPDATE = false;
    public static final int WIDTH = 27;
    public static final int UNREGISTERED_STATUS = 0;
    public static final int REGISTERED_STATUS = 1;
    public static final int IN_BATTLE_STATUS = 2;
    public static final int DELAY = 15;
    public static final String ROUND_DURATION = "round.duration";
    public static final int DELAY_TIME = 1000;
    public static final String END_OF_ROUND_REMIND = "end.of.round.remind";
    public static final int GOLD_FOR_MEMBER = 10;
    public static final int SCALE = 2;
    public static final int ROUNDED = BigDecimal.ROUND_DOWN;

}
