package ml.ixplo.arenabot.database;

import ml.ixplo.arenabot.helper.Presets;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void checkVersion() throws Exception {
        Assert.assertEquals(Presets.TEST_DB_VERSION, conn.checkVersion());
    }

    @Test
    public void commitTest() throws SQLException {
        conn.initTransaction();
        conn.executeQuery("INSERT OR REPLACE INTO teams" +
                "(id,name,registered,is_public,games,wins,descr,html_name) " +
                "VALUES(\'transTeam\',\'trans\',1,1,100,100,\'Тестовая команда\',\'<b>team</b>\');");
        conn.executeQuery("UPDATE teams SET name=\'Geeks\' WHERE id=\'transTeam\';");
        conn.commitTransaction();
        check();
    }

    @Test
    public void rollbackTest() throws SQLException {
        final String first = "INSERT OR REPLACE INTO teams(id,name,registered,is_public,games,wins,descr,html_name) VALUES(\'transTeam\',";
        final String beforeName = "\'Geeks\'";
        final String afterName = "\'afterName\'";
        final String last = ",1,1,100,100,\'Тестовая команда\',\'<b>team</b>\');";
        conn.executeQuery(first + beforeName + last);
        try {
            conn.initTransaction();
            conn.executeQuery(first + afterName + last);
            conn.executeQuery("wrong SQL");
            conn.commitTransaction();
        } catch (Exception e) {
            conn.rollBack();
        }
        check();
    }

    private void check() throws SQLException {
        PreparedStatement preparedStatement = conn.getPreparedStatement("SELECT name FROM teams WHERE id=\'transTeam\';");
        ResultSet resultSet = preparedStatement.executeQuery();
        String name = null;
        if (resultSet.next()) {
            name = resultSet.getString("name");
        }
        Assert.assertEquals("Geeks", name);
    }

    @Test
    public void initTransaction() throws Exception {
    }

    @Test
    public void commitTransaction() throws Exception {
    }

}