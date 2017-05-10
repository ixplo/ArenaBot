package arenabot.database;

import arenabot.users.ArenaUser;
import arenabot.Config;
import arenabot.users.Inventory.Item;
import arenabot.battle.Team;
import org.telegram.telegrambots.logging.BotLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ixplo
 * 24.04.2017.
 */
public class DatabaseManager {
    private static final String LOGTAG = "DATABASEMANAGER";

    private static volatile DatabaseManager instance;
    private static volatile ConnectionDB connection;

    /**
     * Private constructor (due to Singleton)
     */
    private DatabaseManager() {
        connection = new ConnectionDB();
        final int currentVersion = connection.checkVersion();
        BotLogger.info(LOGTAG, "Current db version: " + currentVersion);
    }

    /**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static DatabaseManager getInstance() {
        final DatabaseManager currentInstance;
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public boolean doesUserExists(Integer userId) {
        int status = -1;
        try {
            final PreparedStatement preparedStatement = connection.getPreparedStatement("Select id FROM users WHERE Id=?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                status = result.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status > 0;
    }

    public boolean dropStatus() {
        int updatedRows = 0;
        try {
            String queryText = "UPDATE users SET status='0';";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public boolean dropUser(Integer userId) {
        int deletedRows = 0;
        try {
            final PreparedStatement preparedStatement = connection.getPreparedStatement("DELETE FROM users WHERE Id=?;");
            preparedStatement.setInt(1, userId);

            deletedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedRows > 0;
    }

    public boolean dropItems(Integer userId) {
        int deletedRows = 0;
        try {
            final PreparedStatement preparedStatement = connection.getPreparedStatement("DELETE FROM inventory WHERE user_Id=?;");
            preparedStatement.setInt(1, userId);

            deletedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedRows > 0;
    }

    public ArenaUser getUser(Integer userId) {
        ArenaUser arenaUser = null;
        try {
            final PreparedStatement preparedStatement = connection.getPreparedStatement("Select * FROM users WHERE Id=?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                arenaUser = ArenaUser.create(userId, result.getString("class"));
                arenaUser.setUserClass(result.getString("class"));
                arenaUser.setName(result.getString("name"));
                arenaUser.setUserTitle(result.getString("title"));
                arenaUser.setUserPostTitle(result.getString("post_title"));
                arenaUser.setTeam(result.getString("team"));
                arenaUser.setTeamRank(result.getString("team_rank"));
                arenaUser.setRace(result.getString("race"));
                arenaUser.setDescr(result.getString("descr"));
                arenaUser.setSex(result.getInt("sex"));
                arenaUser.setUserGames(result.getInt("games"));
                arenaUser.setUserWins(result.getInt("wins"));
                arenaUser.setNativeStr(result.getInt("strangth"));//strength
                arenaUser.setNativeDex(result.getInt("dexterity"));
                arenaUser.setNativeWis(result.getInt("wisdom"));
                arenaUser.setNativeInt(result.getInt("intellect"));
                arenaUser.setNativeCon(result.getInt("const"));
                arenaUser.setFreePoints(result.getInt("free_points"));
                arenaUser.setMaxHitPoints(result.getDouble("hp"));
                arenaUser.setMoney(result.getInt("money"));
                arenaUser.setExperience(result.getInt("exp"));
                arenaUser.setLevel(result.getInt("level"));
                arenaUser.setCurStr(result.getInt("cur_str"));
                arenaUser.setCurDex(result.getInt("cur_dex"));
                arenaUser.setCurWis(result.getInt("cur_wis"));
                arenaUser.setCurInt(result.getInt("cur_int"));
                arenaUser.setCurCon(result.getInt("cur_con"));
                arenaUser.setMinHit(result.getDouble("min_hit"));
                arenaUser.setMaxHit(result.getDouble("max_hit"));
                arenaUser.setAttack(result.getDouble("attack"));
                arenaUser.setProtect(result.getDouble("protect"));
                arenaUser.setHeal(result.getDouble("heal"));
                arenaUser.setMagicProtect(result.getDouble("m_protect"));
                arenaUser.setCurHitPoints(result.getDouble("cur_hp"));
                arenaUser.setCurExp(result.getInt("cur_exp"));
                arenaUser.setLastGame(result.getLong("last_game"));
                arenaUser.setCurWeapon(result.getInt("cur_weapon"));
                arenaUser.setStatus(result.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arenaUser;
    }

    public boolean setUser(ArenaUser arenaUser) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement =
                    connection.getPreparedStatement("INSERT OR REPLACE INTO users " +
                            "(id," +
                            "name," +
                            "title," +
                            "post_Title," +
                            "team," +
                            "team_Rank," +
                            "race," +
                            "class," +
                            "descr," +
                            "sex," +
                            "games," +
                            "wins," +
                            "strangth," +
                            "dexterity," +
                            "wisdom," +
                            "intellect," +
                            "const," +
                            "free_points," +
                            "hp," +
                            "money," +
                            "exp," +
                            "level," +
                            "cur_str," +
                            "cur_dex," +
                            "cur_wis," +
                            "cur_int," +
                            "cur_con," +
                            "min_hit," +
                            "max_hit," +
                            "attack," +
                            "protect," +
                            "heal," +
                            "m_protect," +
                            "cur_hp," +
                            "cur_exp," +
                            "last_game," +
                            "cur_weapon," +
                            "status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            preparedStatement.setInt(1, arenaUser.getUserId());
            preparedStatement.setString(2, arenaUser.getName());
            preparedStatement.setString(3, arenaUser.getUserTitle());
            preparedStatement.setString(4, arenaUser.getUserPostTitle());
            preparedStatement.setString(5, arenaUser.getTeam());
            preparedStatement.setString(6, arenaUser.getTeamRank());
            preparedStatement.setString(7, arenaUser.getRace());
            preparedStatement.setString(8, arenaUser.getUserClass());
            preparedStatement.setString(9, arenaUser.getDescr());
            preparedStatement.setInt(10, arenaUser.getSex());
            preparedStatement.setInt(11, arenaUser.getUserGames());
            preparedStatement.setInt(12, arenaUser.getUserWins());
            preparedStatement.setInt(13, arenaUser.getNativeStr());
            preparedStatement.setInt(14, arenaUser.getNativeDex());
            preparedStatement.setInt(15, arenaUser.getNativeWis());
            preparedStatement.setInt(16, arenaUser.getNativeInt());
            preparedStatement.setInt(17, arenaUser.getNativeCon());
            preparedStatement.setInt(18, arenaUser.getFreePoints());
            preparedStatement.setDouble(19, arenaUser.getMaxHitPoints());
            preparedStatement.setInt(20, arenaUser.getMoney());
            preparedStatement.setInt(21, arenaUser.getExperience());
            preparedStatement.setInt(22, arenaUser.getLevel());
            preparedStatement.setInt(23, arenaUser.getCurStr());
            preparedStatement.setInt(24, arenaUser.getCurDex());
            preparedStatement.setInt(25, arenaUser.getCurWis());
            preparedStatement.setInt(26, arenaUser.getCurInt());
            preparedStatement.setInt(27, arenaUser.getCurCon());
            preparedStatement.setDouble(28, arenaUser.getMinHit());
            preparedStatement.setDouble(29, arenaUser.getMaxHit());
            preparedStatement.setDouble(30, arenaUser.getAttack());
            preparedStatement.setDouble(31, arenaUser.getProtect());
            preparedStatement.setDouble(32, arenaUser.getHeal());
            preparedStatement.setDouble(33, arenaUser.getMagicProtect());
            preparedStatement.setDouble(34, arenaUser.getCurHitPoints());
            preparedStatement.setInt(35, arenaUser.getCurExp());
            preparedStatement.setLong(36, arenaUser.getLastGame());
            preparedStatement.setInt(37, arenaUser.getCurWeapon());
            preparedStatement.setInt(38, arenaUser.getStatus());

            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public String getSlot(int userId, Integer eqipIndex) {
        String resultString = "";
        try {
            String queryText = "Select in_slot FROM inventory WHERE user_id=? AND counter=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, eqipIndex);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                resultString = result.getString("in_slot");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public boolean setSpell(Integer userId, String spellId, int spellGrade) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement =
                    connection.getPreparedStatement("INSERT OR REPLACE INTO available_spells " +
                            "(id," +
                            "user_id" +
                            "spell_grade) VALUES(?,?,?);");
            preparedStatement.setString(1, spellId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, spellGrade);

            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public boolean setItem(Integer userId, String itemId) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement =
                    connection.getPreparedStatement("INSERT OR REPLACE INTO Inventory " +
                            "(id," +
                            "user_Id," +
                            "counter) VALUES(?,?,?);");
            preparedStatement.setString(1, itemId);
            preparedStatement.setInt(2, userId);
            int count = getCount(Config.EQIP, "user_Id", userId) + 1;
            preparedStatement.setInt(3, count);

            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public Item getItem(String itemId) {
        Item item = null;
        try {
            final PreparedStatement preparedStatement = connection.getPreparedStatement("Select * FROM items WHERE Id=?");
            preparedStatement.setString(1, itemId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                item = new Item(itemId);
                item.setName(result.getString("name"));
                item.setPrice(result.getInt("price"));
                item.setMinHit(result.getInt("hit_min"));
                item.setMaxHit(result.getInt("hit_max"));
                item.setAttack(result.getInt("attack"));
                item.setProtect(result.getInt("protect"));
                item.setStrBonus(result.getInt("strength"));//strength
                item.setDexBonus(result.getInt("dexterity"));
                item.setWisBonus(result.getInt("wisdom"));
                item.setIntBonus(result.getInt("intellect"));
                item.setConBonus(result.getInt("const"));
                item.setStrNeeded(result.getInt("need_str"));
                item.setDexNeeded(result.getInt("need_dex"));
                item.setWisNeeded(result.getInt("need_wis"));
                item.setIntNeeded(result.getInt("need_int"));
                item.setConNeeded(result.getInt("need_con"));
                item.setisWeapon(result.getInt("is_on_att") == 1);
                item.setSlot(result.getString("slot"));
                item.setShop(result.getString("shop"));
                item.setRace(result.getString("race"));
                item.setDescr(result.getString("descr"));
                item.setItemsSet(result.getString("items_set"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public boolean setTeam(Team team) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement =
                    connection.getPreparedStatement("INSERT OR REPLACE INTO teams " +
                            "(Id," +
                            "name," +
                            "registered," +
                            "is_public," +
                            "games," +
                            "wins," +
                            "descr," +
                            "html_name) VALUES(?,?,?,?,?,?,?,?);");
            preparedStatement.setString(1, team.getId());
            preparedStatement.setString(2, team.getName());
            preparedStatement.setInt(3, team.isRegistered() ? 1 : 0);
            preparedStatement.setInt(4, team.isPublic() ? 1 : 0);
            preparedStatement.setInt(5, team.getGames());
            preparedStatement.setInt(6, team.getWins());
            preparedStatement.setString(7, team.getDescr());
            preparedStatement.setString(8, team.getHtmlName());
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public Team getTeam(String id) {
        Team team = null;
        try {
            final PreparedStatement preparedStatement = connection.getPreparedStatement("Select * FROM teams WHERE Id=?");
            preparedStatement.setString(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                team = new Team(result.getString("id"));
                team.setName(result.getString("name"));
                team.setRegistered(result.getInt("registered") > 0);
                team.setPublic(result.getInt("is_public") > 0);
                team.setGames(result.getInt("games"));
                team.setWins(result.getInt("wins"));
                team.setDescr(result.getString("descr"));
                team.setHtmlName(result.getString("html_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return team;
    }

    public int getIntFrom(String tableName, Integer id, String columnName) {
        int resultInt = -1;
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                resultInt = result.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInt;
    }

    public int getIntFrom(String tableName, String id, String columnName) {//overload
        int resultInt = -1;
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                resultInt = result.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInt;
    }

    public double getDoubleFrom(String tableName, Integer id, String columnName) {
        double resultDouble = -1;
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                resultDouble = result.getDouble(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultDouble;
    }

    public ArrayList<Integer> getInts(String tableName, String columnId, Integer id, String columnName) {
        ArrayList<Integer> resultIntArr = new ArrayList<>();
        try {//SELECT id FROM users WHERE status='1';
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + columnId + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                resultIntArr.add(result.getInt(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultIntArr;
    }

    public String getStringFrom(String tableName, String id, String columnName) {
        String resultString = "";
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                resultString = result.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public String getStringFrom(String tableName, Integer id, String columnName) {//overload
        String resultString = "";
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                resultString = result.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public String getStringBy(String tableName, String findByColumn, Integer id, String selectedColumn) {
        String resultString = "";
        try {
            String queryText = "Select " + selectedColumn + " FROM " + tableName + " WHERE " + findByColumn + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                resultString = result.getString(selectedColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public int getIntByBy(String tableName, String columnName, String firstColumn, String firstId, String secondColumn, Integer secondId) {
        int resultInt = -1;
        try {//SELECT counter FROM inventory WHERE id='waa' AND userId='362812407';
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + firstColumn + "=? AND " + secondColumn + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, firstId);
            preparedStatement.setInt(2, secondId);
            final ResultSet result = preparedStatement.executeQuery();
            if(result.next()) {
                resultInt = result.getInt(columnName);
            }else throw new RuntimeException("No such int: " + queryText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInt;
    }

    public String getStringByBy(String tableName, String columnName, String firstColumn, int firstId, String secondColumn, Integer secondId) {
        String resultString = "";
        try {//SELECT id FROM inventory WHERE counter='1' AND userId='362812407';
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + firstColumn + "=? AND " + secondColumn + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, firstId);
            preparedStatement.setInt(2, secondId);
            final ResultSet result = preparedStatement.executeQuery();
            if(result.next()) {
                resultString = result.getString(columnName);
            }else throw new RuntimeException("No such int: " + queryText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public ArrayList<String> getStrings(String tableName, String columnId, Integer id, String columnName) {
        ArrayList<String> resultStringArr = new ArrayList<>();
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + columnId + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                resultStringArr.add(result.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultStringArr;
    }

    public ArrayList<String> getStrings(String tableName, String columnId, String id, String columnName) {
        ArrayList<String> resultStringArr = new ArrayList<>();
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + columnId + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, id);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                resultStringArr.add(result.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultStringArr;
    }

    public ArrayList<String> getStringsBy(String tableName, String columnName, String firstColumn, String firstId, String secondColumn, Integer secondId) {
        ArrayList<String> resultStringArr = new ArrayList<>();
        try {//SELECT name FROM users WHERE status='1' AND team='findById';
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + firstColumn + "=? AND " + secondColumn + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, firstId);
            preparedStatement.setInt(2, secondId);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                resultStringArr.add(result.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultStringArr;
    }

    public ArrayList<String> getStringsBy(String tableName, String columnName, String firstColumn, int firstId, String secondColumn, Integer secondId) {
        ArrayList<String> resultStringArr = new ArrayList<>();
        try {//SELECT name FROM users WHERE status='1' AND team='findById';
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + firstColumn + "=? AND " + secondColumn + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, firstId);
            preparedStatement.setInt(2, secondId);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                resultStringArr.add(result.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultStringArr;
    }

    public ArrayList<Integer> getIntsBy(String tableName, String columnName, String firstColumn, String firstId, String secondColumn, Integer secondId) {
        ArrayList<Integer> resultStringArr = new ArrayList<>();
        try {//SELECT name FROM users WHERE status='1' AND team='findById';
            String queryText = "Select " + columnName + " FROM " + tableName + " WHERE " + firstColumn + "=? AND " + secondColumn + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, firstId);
            preparedStatement.setInt(2, secondId);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                resultStringArr.add(result.getInt(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultStringArr;
    }

    public boolean setLongTo(String tableName, Integer id, String recordColumn, long recordValue) {
        int updatedRows = 0;
        try {//"UPDATE users SET ?=? WHERE id=?;"
            String queryText = "UPDATE " + tableName + " SET " + recordColumn + "=? WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setLong(1, recordValue);
            preparedStatement.setInt(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public boolean setDoubleTo(String tableName, Integer id, String recordColumn, double recordValue) {
        int updatedRows = 0;
        try {//"UPDATE users SET ?=? WHERE id=?;"
            String queryText = "UPDATE " + tableName + " SET " + recordColumn + "=? WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setDouble(1, recordValue);
            preparedStatement.setInt(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public boolean setIntTo(String tableName, Integer id, String recordColumn, Integer recordValue) {
        int updatedRows = 0;
        try {//"UPDATE users SET ?=? WHERE id=?;"
            String queryText = "UPDATE " + tableName + " SET " + recordColumn + "=? WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, recordValue);
            preparedStatement.setInt(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public boolean setIntTo(String tableName, String id, String recordColumn, Integer recordValue) {//overload
        int updatedRows = 0;
        try {//"UPDATE users SET ?=? WHERE id=?;"
            String queryText = "UPDATE " + tableName + " SET " + recordColumn + "=? WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, recordValue);
            preparedStatement.setString(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public boolean setStringTo(String tableName, Integer id, String recordColumn, String recordValue) {
        int updatedRows = 0;
        try {//"UPDATE users SET ?=? WHERE id=?;"
            String queryText = "UPDATE " + tableName + " SET " + recordColumn + "=? WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, recordValue);
            preparedStatement.setInt(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public boolean setStringTo(String tableName, String id, String recordColumn, String recordValue) {
        int updatedRows = 0;
        try {//"UPDATE users SET ?=? WHERE id=?;"
            String queryText = "UPDATE " + tableName + " SET " + recordColumn + "=? WHERE id=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, recordValue);
            preparedStatement.setString(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public int getCount(String tableName, String columnName, Integer value) {
        try {
            String queryText = "Select count(" + columnName + ") FROM " + tableName + " WHERE " + columnName + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, value);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt("count(" + columnName + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCountDistinct(String tableName, String columnOfCount, String columnName, Integer value) {
        try {
            String queryText = "Select count(distinct " + columnOfCount + ") FROM " + tableName + " WHERE " + columnName + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setInt(1, value);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt("count(distinct " + columnOfCount + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int getCount(String tableName, String columnName, String value) {//overload
        try {
            String queryText = "Select count(" + columnName + ") FROM " + tableName + " WHERE " + columnName + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setString(1, value);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt("count(" + columnName + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCount(String tableName, String columnName, Double value) {//overload
        try {
            String queryText = "Select count(" + columnName + ") FROM " + tableName + " WHERE " + columnName + "=?;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            preparedStatement.setDouble(1, value);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt("count(" + columnName + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getColumn(String tableName, String columnName) {
        List<String> column = new LinkedList<>();
        try {
            String queryText = "Select " + columnName + " FROM " + tableName + " order by rowid;";
            final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                column.add(result.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return column;
    }
}
