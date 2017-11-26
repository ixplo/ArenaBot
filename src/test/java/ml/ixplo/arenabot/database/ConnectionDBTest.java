package ml.ixplo.arenabot.database;

import ml.ixplo.arenabot.helper.Presets;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static ml.ixplo.arenabot.config.Config.TEST_DB_LINK;

public class ConnectionDBTest {
    ConnectionDB conn = new ConnectionDB(TEST_DB_LINK);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void closeConnection() throws Exception {
    }

    @Test
    public void runSqlQuery() throws Exception {
    }

    @Test
    public void executeQuery() throws Exception {
    }

    @Test
    public void getPreparedStatement() throws Exception {
    }

    @Test
    public void getPreparedStatement1() throws Exception {
    }

    @Test
    public void checkVersion() throws Exception {
        Assert.assertEquals(Presets.TEST_DB_VERSION, conn.checkVersion());
    }

    @Test
    public void initTransaction() throws Exception {
    }

    @Test
    public void commitTransaction() throws Exception {
    }

}