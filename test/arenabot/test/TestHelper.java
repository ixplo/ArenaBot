package arenabot.test;

import arenabot.Bot;
import arenabot.database.ConnectionDB;
import arenabot.database.DatabaseManager;
import arenabot.user.ArenaUser;

import static arenabot.config.Config.TEST_DB_LINK;

public class TestHelper {

    public DatabaseManager db;
    public static ArenaUser WARRIOR;

    public TestHelper() {
        init();
        generateData();
    }

    private void generateData() {
        WARRIOR = ArenaUser.create(-1,"test_warrior", ArenaUser.UserClass.WARRIOR, "o");
        db.setUser(WARRIOR);
    }

    public void init() {
        Bot bot = new Bot();
        DatabaseManager.setConnection(new ConnectionDB(TEST_DB_LINK));
        db = DatabaseManager.getInstance();
        bot.setDb(db);

    }

    public void close() {
        WARRIOR.dropUser();
        DatabaseManager.getConnection().closeConnection();
    }
}
