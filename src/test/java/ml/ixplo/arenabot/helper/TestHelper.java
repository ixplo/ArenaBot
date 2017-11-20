package ml.ixplo.arenabot.helper;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.database.ConnectionDB;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.user.ArenaUser;

import static ml.ixplo.arenabot.config.Config.TEST_DB_LINK;

public class TestHelper {


    private DatabaseManager db;
    public static ArenaUser WARRIOR;
    public static ArenaUser MAGE;

    public TestHelper() {
        init();
        generateData();
    }

    private void generateData() {
        if (ArenaUser.doesUserExists(Presets.WARRIOR_ID)) {
            ArenaUser.dropUser(Presets.WARRIOR_ID);
        }
        if (ArenaUser.doesUserExists(Presets.MAGE_ID)) {
            ArenaUser.dropUser(Presets.MAGE_ID);
        }
        WARRIOR = ArenaUser.create(
                Presets.WARRIOR_ID,
                Presets.WARRIOR_NAME,
                ArenaUser.UserClass.WARRIOR,
                "o");
        MAGE = ArenaUser.create(
                Presets.MAGE_ID,
                Presets.MAGE_NAME,
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

    public DatabaseManager getDb() {
        return db;
    }

    public void setDb(DatabaseManager db) {
        this.db = db;
    }
}
