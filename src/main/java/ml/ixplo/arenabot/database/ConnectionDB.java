package ml.ixplo.arenabot.database;

import ml.ixplo.arenabot.config.Config;
import org.telegram.telegrambots.logging.BotLogger;

import java.sql.*;

/**
 * ixplo
 * 24.04.2017.
 */
public class ConnectionDB {
    private static final String LOGTAG = "CONNECTIONDB";
    private Connection currentConnection;

    public ConnectionDB() {
        this.currentConnection = openConnection(Config.DB_LINK);
    }
    public ConnectionDB(String dbLink) {
        currentConnection = openConnection(dbLink);
    }

    private Connection openConnection(String dbLink) {
        Connection connection = null;
        try {
            Class.forName(Config.DB_CONTROLLER).newInstance();
            connection = DriverManager.getConnection(dbLink);
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            BotLogger.error(LOGTAG, e);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            this.currentConnection.close();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e);
        }

    }

    public ResultSet runSqlQuery(String query) throws SQLException {
        final Statement statement;
        statement = this.currentConnection.createStatement();
        return statement.executeQuery(query);
    }

    public Boolean executeQuery(String query) throws SQLException {
        final Statement statement = this.currentConnection.createStatement();
        return statement.execute(query);
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return this.currentConnection.prepareStatement(query);
    }

    public PreparedStatement getPreparedStatement(String query, int flags) throws SQLException {
        return this.currentConnection.prepareStatement(query, flags);
    }

    public int checkVersion() {
        int max = 0;
        try {
            final DatabaseMetaData metaData = this.currentConnection.getMetaData();
            final ResultSet res = metaData.getTables(null, null, "",
                    new String[]{"TABLE"});
            while (res.next()) {
                if (res.getString("TABLE_NAME").compareTo("Versions") == 0) {
                    final ResultSet result = runSqlQuery("SELECT Max(Version) FROM Versions");
                    while (result.next()) {
                        max = (max > result.getInt(1)) ? max : result.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e);
        }
        return max;
    }

    /**
     * Initilize a transaction in database
     * @throws SQLException If initialization fails
     */
    public void initTransaction() throws SQLException {
        this.currentConnection.setAutoCommit(false);
    }

    /**
     * Finish a transaction in database and commit changes
     * @throws SQLException If a rollback fails
     */
    public void commitTransaction() throws SQLException {
        try {
            this.currentConnection.commit();
        } catch (SQLException e) {
            if (this.currentConnection != null) {
                this.currentConnection.rollback();
            }
        } finally {
            this.currentConnection.setAutoCommit(false);
        }
    }
}
