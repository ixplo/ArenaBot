package arenabot.user.inventory;

import arenabot.database.ConnectionDB;
import arenabot.database.DatabaseManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static arenabot.Config.TEST_DB_LINK;

public class ItemTest {

    private DatabaseManager db;

    @Before
    public void setUp() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB(TEST_DB_LINK));
        db = DatabaseManager.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        DatabaseManager.getConnection().closeConnection();
    }

    @Test
    public void getEqipAmount() throws Exception {
    }

    @Test
    public void getItems() throws Exception {
    }

    @Test
    public void getEqipIndex() throws Exception {
    }

    @Test
    public void isItemInSlot() throws Exception {
    }

    @Test
    public void getItemId() throws Exception {
    }

    @Test
    public void putOn() throws Exception {
    }

    @Test
    public void putOff() throws Exception {
    }

    @Test
    public void getItemName() throws Exception {
    }

    @Test
    public void getItem() throws Exception {
    }

    @Test
    public void markAsPuttedOn() throws Exception {
    }

    @Test
    public void markAsPuttedOff() throws Exception {
    }

}