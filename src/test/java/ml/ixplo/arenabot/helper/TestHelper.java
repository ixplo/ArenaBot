package ml.ixplo.arenabot.helper;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.database.ConnectionDB;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.user.ArenaUser;

import static ml.ixplo.arenabot.config.Config.TEST_DB_LINK;

public class TestHelper {

    public DatabaseManager db;
    public static ArenaUser WARRIOR;
    public static ArenaUser MAGE;

    public TestHelper() {
        init();
        generateData();
    }

    private void generateData() {
        if (ArenaUser.doesUserExists(Constants.WARRIOR_ID)) {
            ArenaUser.dropUser(Constants.WARRIOR_ID);
        }
        if (ArenaUser.doesUserExists(Constants.MAGE_ID)) {
            ArenaUser.dropUser(Constants.MAGE_ID);
        }
        WARRIOR = ArenaUser.create(
                Constants.WARRIOR_ID,
                Constants.WARRIOR_NAME,
                ArenaUser.UserClass.WARRIOR,
                "o");
        MAGE = ArenaUser.create(
                Constants.MAGE_ID,
                Constants.MAGE_NAME,
                ArenaUser.UserClass.MAGE,
                "e");
        db.setUser(WARRIOR);
        db.setUser(MAGE);
    }

    public void init() {
        Bot bot = new Bot();
        DatabaseManager.setConnection(new ConnectionDB(TEST_DB_LINK));
        db = DatabaseManager.getInstance();
        bot.setDb(db);

    }

    public void close() {
        if (WARRIOR.doesUserExists()) {
            WARRIOR.dropUser();
        }
        if (MAGE.doesUserExists()) {
            MAGE.dropUser();
        }
        DatabaseManager.getConnection().closeConnection();
    }
}
