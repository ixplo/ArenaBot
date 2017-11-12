package arenabot.test;

import arenabot.Bot;
import arenabot.database.ConnectionDB;
import arenabot.database.DatabaseManager;
import arenabot.user.ArenaUser;

import static arenabot.config.Config.TEST_DB_LINK;

public class TestHelper {

    public DatabaseManager db;
    public static ArenaUser WARRIOR;
    public static ArenaUser MAGE;

    public TestHelper() {
        init();
        generateData();
    }

    private void generateData() {
        WARRIOR = ArenaUser.create(-1,"test_warrior", ArenaUser.UserClass.WARRIOR, "o");
        MAGE = ArenaUser.create(-2, "test_mage", ArenaUser.UserClass.MAGE, "e");
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
        WARRIOR.dropUser();
        MAGE.dropUser();
        DatabaseManager.getConnection().closeConnection();
    }
}
