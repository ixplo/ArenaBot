package arenabot.test;

import arenabot.ArenaBot;
import arenabot.database.ConnectionDB;
import arenabot.database.DatabaseManager;
import arenabot.user.ArenaUser;

import static arenabot.Config.TEST_DB_LINK;

public class TestHelper {

    private DatabaseManager db;
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
        ArenaBot arenaBot = new ArenaBot();
        DatabaseManager.setConnection(new ConnectionDB(TEST_DB_LINK));
        db = DatabaseManager.getInstance();
        arenaBot.setDb(db);

    }

    public void close() {
        DatabaseManager.getConnection().closeConnection();
    }
}
