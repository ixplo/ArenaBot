package ml.ixplo.arenabot.database;

import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.classes.UserClass;
import ml.ixplo.arenabot.user.items.Item;
import org.telegram.telegrambots.logging.BotLogger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/***
 * ixplo
 * 24.04.2017.
 ***/
public class DatabaseManager {
    public static final String NO_SUCH_USER = "No such user in database: ";
    public static final String ID = "id";
    public static final String TEAM_COLUMN = "team";
    public static final String CUR_MANA = "cur_mana";
    public static final String S_POINTS = "s_points";
    private static final String LOGTAG = "DATABASEMANAGER";
    private static final String SELECT = "Select ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";
    private static final String VAR = "=?";
    private static final String SEMICOLON = ";";
    private static final String AND = " AND ";
    private static final String UPDATE = "UPDATE ";
    private static final String SET = " SET ";
    private static final String GAMES_COLUMN = "games";
    private static final String EMPTY = "";
    private static final String COUNT = "count(";

    private static volatile DatabaseManager instance;
    private static volatile ConnectionDB connection;

    /***
     * Private constructor (due to Singleton)
     ***/
    private DatabaseManager() {
        if (connection == null) {
            connection = new ConnectionDB();
        }
        final int currentVersion = connection.checkVersion();
        BotLogger.info(LOGTAG, "Current db version: " + currentVersion);
    }

    /***
     * Get Singleton getInstance
     *
     * @return getInstance of the class
     ***/
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

    public static void setConnection(ConnectionDB conn) {
        if (connection != null) {
            connection.closeConnection();
        }
        connection = conn;
    }

    public static ConnectionDB getConnection() {
        return connection;
    }

    public boolean doesUserExists(Integer userId) {
        String queryText = "Select id FROM users WHERE Id=" + userId;
        try (final PreparedStatement statement = connection.getPreparedStatement(queryText);
             final ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return false;
    }

    public void dropStatus() {
        connection.executeQuery("UPDATE users SET status='0';");
    }

    public void dropUser(Integer userId) {
        String queryText = "DELETE FROM users WHERE Id" + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void dropItems(Integer userId) {
        String queryText = "DELETE FROM inventory WHERE user_Id" + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void dropItem(Integer userId, String itemId) {
        int deletedRows = 0;
        String queryText = "DELETE FROM inventory WHERE user_Id" + VAR + AND + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, itemId);
            deletedRows = preparedStatement.executeUpdate();
            if (deletedRows <= 0) {
                throw new IllegalArgumentException("Нет вещи у игрока(userId=" + userId + ") с itemId=" + itemId);
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void dropActions(Integer userId) {
        String queryText = "DELETE FROM round_actions WHERE " + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void dropActions() {
        String queryText = "DELETE FROM round_actions";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void dropSpells(int userId) {
        String queryText = "DELETE FROM available_spells WHERE user_" + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public ArenaUser getUser(Integer userId) {
        ArenaUser arenaUser = null;
        String queryText = "Select * FROM users WHERE Id" + VAR;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    arenaUser = ArenaUser.create(UserClass.valueOf(result.getString(Config.CLASS_COLUMN)));
                    arenaUser.setUserId(result.getInt(ID));
                    arenaUser.setUserClass(result.getString(Config.CLASS_COLUMN));
                    arenaUser.setName(result.getString("name"));
                    arenaUser.setUserTitle(result.getString("title"));
                    arenaUser.setUserPostTitle(result.getString("post_title"));
                    arenaUser.setTeamId(result.getString(TEAM_COLUMN));
                    arenaUser.setTeamRank(result.getString("team_rank"));
                    arenaUser.setRace(result.getString("race"));
                    arenaUser.setDescr(result.getString(Config.DESCR));
                    arenaUser.setSex(result.getInt("sex"));
                    arenaUser.setUserGames(result.getInt(GAMES_COLUMN));
                    arenaUser.setUserWins(result.getInt("wins"));
                    arenaUser.setNativeStr(result.getInt("strangth"));//strength
                    arenaUser.setNativeDex(result.getInt(Config.DEXTERITY));
                    arenaUser.setNativeWis(result.getInt(Config.WISDOM));
                    arenaUser.setNativeInt(result.getInt(Config.INTELLECT));
                    arenaUser.setNativeCon(result.getInt(Config.CONST));
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
                    arenaUser.setAttack(result.getBigDecimal(Config.ATTACK));
                    arenaUser.setProtect(result.getDouble(Config.PROTECT));
                    arenaUser.setHeal(result.getDouble("heal"));
                    arenaUser.setMagicProtect(result.getDouble("m_protect"));
                    arenaUser.setCurHitPoints(result.getDouble("cur_hp"));
                    arenaUser.setCurExp(result.getInt("cur_exp"));
                    arenaUser.setLastGame(result.getLong("last_game"));
                    arenaUser.setCurWeapon(result.getInt("cur_weapon"));
                    arenaUser.setStatus(result.getInt(Config.STATUS));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return arenaUser;
    }

    public void updateUser(ArenaUser arenaUser) {
        String queryText = "UPDATE users SET " +
                "name" + VAR + "," +
                "title" + VAR + "," +
                "post_Title" + VAR + "," +
                TEAM_COLUMN + VAR + "," +
                "team_Rank" + VAR + "," +
                "race" + VAR + "," +
                Config.CLASS_COLUMN + VAR + "," +
                "descr" + VAR + "," +
                "sex" + VAR + "," +
                GAMES_COLUMN + VAR + "," +
                "wins" + VAR + "," +
                "strangth" + VAR + "," +
                "dexterity" + VAR + "," +
                "wisdom" + VAR + "," +
                "intellect" + VAR + "," +
                "const" + VAR + "," +
                "free_points" + VAR + "," +
                "hp" + VAR + "," +
                "money" + VAR + "," +
                "exp" + VAR + "," +
                "level" + VAR + "," +
                "cur_str" + VAR + "," +
                "cur_dex" + VAR + "," +
                "cur_wis" + VAR + "," +
                "cur_int" + VAR + "," +
                "cur_con" + VAR + "," +
                "min_hit" + VAR + "," +
                "max_hit" + VAR + "," +
                "attack" + VAR + "," +
                "protect" + VAR + "," +
                "heal" + VAR + "," +
                "m_protect" + VAR + "," +
                "cur_hp" + VAR + "," +
                "cur_exp" + VAR + "," +
                "last_game" + VAR + "," +
                "cur_weapon" + VAR + "," +
                "status" + VAR + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(38, arenaUser.getUserId());
            preparedStatement.setString(1, arenaUser.getName());
            preparedStatement.setString(2, arenaUser.getUserTitle());
            preparedStatement.setString(3, arenaUser.getUserPostTitle());
            preparedStatement.setString(4, arenaUser.getTeamId());
            preparedStatement.setString(5, arenaUser.getTeamRank());
            preparedStatement.setString(6, arenaUser.getRace());
            preparedStatement.setString(7, arenaUser.getUserClass());
            preparedStatement.setString(8, arenaUser.getDescr());
            preparedStatement.setInt(9, arenaUser.getSex());
            preparedStatement.setInt(10, arenaUser.getUserGames());
            preparedStatement.setInt(11, arenaUser.getUserWins());
            preparedStatement.setInt(12, arenaUser.getNativeStr());
            preparedStatement.setInt(13, arenaUser.getNativeDex());
            preparedStatement.setInt(14, arenaUser.getNativeWis());
            preparedStatement.setInt(15, arenaUser.getNativeInt());
            preparedStatement.setInt(16, arenaUser.getNativeCon());
            preparedStatement.setInt(17, arenaUser.getFreePoints());
            preparedStatement.setDouble(18, arenaUser.getMaxHitPoints());
            preparedStatement.setInt(19, arenaUser.getMoney());
            preparedStatement.setInt(20, arenaUser.getExperience());
            preparedStatement.setInt(21, arenaUser.getLevel());
            preparedStatement.setInt(22, arenaUser.getCurStr());
            preparedStatement.setInt(23, arenaUser.getCurDex());
            preparedStatement.setInt(24, arenaUser.getCurWis());
            preparedStatement.setInt(25, arenaUser.getCurInt());
            preparedStatement.setInt(26, arenaUser.getCurCon());
            preparedStatement.setDouble(27, arenaUser.getMinHit());
            preparedStatement.setDouble(28, arenaUser.getMaxHit());
            preparedStatement.setBigDecimal(29, arenaUser.getAttack());
            preparedStatement.setDouble(30, arenaUser.getProtect());
            preparedStatement.setDouble(31, arenaUser.getHeal());
            preparedStatement.setDouble(32, arenaUser.getMagicProtect());
            preparedStatement.setDouble(33, arenaUser.getCurHitPoints());
            preparedStatement.setInt(34, arenaUser.getCurExp());
            preparedStatement.setLong(35, arenaUser.getLastGame());
            preparedStatement.setInt(36, arenaUser.getCurWeaponIndex());
            preparedStatement.setInt(37, arenaUser.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public String getSlot(int userId, Integer eqipIndex) {
        String resultString = EMPTY;
        String queryText = "Select in_slot FROM inventory WHERE user_id" + VAR + " AND counter=?;";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, eqipIndex + 1);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultString = result.getString("in_slot");
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return resultString;
    }

    public void addUser(int userId, String name) {
        String queryText = "INSERT OR REPLACE INTO users(id,name) VALUES (?,?);";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void addSpell(Integer userId, String spellId, int spellGrade) {
        String queryText = "INSERT OR REPLACE INTO available_spells " +
                "(id," +
                "user_id," +
                "spell_grade) VALUES(?,?,?);";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, spellId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, spellGrade);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void addAction(Integer userId) {
        String queryText = "INSERT OR REPLACE INTO ROUND_ACTIONS (" +
                "id," +
                "counter) VALUES(?,?);";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            int count = getCount("ROUND_ACTIONS", ID, userId) + 1;
            preparedStatement.setInt(2, count);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void addItem(Integer userId, String itemId) {
        if (!doesUserExists(userId)) {
            throw new DbException("user does not exists");
        }
        // check if item non exist throw exception
        getItem(itemId);

        String queryText = "INSERT OR REPLACE INTO inventory (" +
                "id," +
                "user_Id," +
                "counter) VALUES(?,?,?);";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, itemId);
            preparedStatement.setInt(2, userId);
            int count = getCount(Config.EQIP, "user_Id", userId) + 1;
            preparedStatement.setInt(3, count);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // непонятно, как проверить тестом
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public List<Item> getItems(Integer userId) {
        List<Item> itemsList = new ArrayList<>();
        String queryText = "Select inventory.user_id," +
                "INVENTORY.counter, " +
                "INVENTORY.IN_SLOT," +
                "items.* " +
                "FROM inventory, items " +
                "WHERE inventory.user_id" + VAR + " and inventory.id=items.id";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Item item = new Item();
                    item.setOwnerId(userId);
                    item.setEqipIndex(result.getInt("counter") - 1);
                    item.setInSlot(result.getString("in_slot"));
                    setItemProperties(item, result);
                    itemsList.add(item);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return itemsList;
    }

    public Item getItem(String itemId) {
        Item item = new Item();
        String queryText = "Select * FROM items WHERE id" + VAR;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, itemId);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    setItemProperties(item, resultSet);
                } else {
                    throw new IllegalArgumentException("No item exist with item_id: " + itemId);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return item;
    }

    private void setItemProperties(Item item, ResultSet result) throws SQLException {
        item.setItemId(result.getString(ID));
        item.setName(result.getString("name"));
        item.setPrice(result.getInt("price"));
        item.setMinHit(result.getInt("hit_min"));
        item.setMaxHit(result.getInt("hit_max"));
        item.setAttack(result.getInt(Config.ATTACK));
        item.setProtect(result.getInt(Config.PROTECT));
        item.setStrBonus(result.getInt("strength"));
        item.setDexBonus(result.getInt(Config.DEXTERITY));
        item.setWisBonus(result.getInt(Config.WISDOM));
        item.setIntBonus(result.getInt(Config.INTELLECT));
        item.setConBonus(result.getInt(Config.CONST));
        item.setStrNeeded(result.getInt("need_str"));
        item.setDexNeeded(result.getInt("need_dex"));
        item.setWisNeeded(result.getInt("need_wis"));
        item.setIntNeeded(result.getInt("need_int"));
        item.setConNeeded(result.getInt("need_con"));
        item.setisWeapon(result.getInt("is_on_att") == 1);
        item.setSlot(result.getString("slot"));
        item.setShop(result.getString("shop"));
        item.setRace(result.getString("race"));
        item.setDescr(result.getString(Config.DESCR));
        item.setItemsSet(result.getString("items_set"));
    }

    public void setTeam(Team team) {
        String queryText = "INSERT OR REPLACE INTO teams (" +
                "id," +
                "name," +
                "registered," +
                "is_public," +
                "games," +
                "wins," +
                "descr," +
                "html_name) VALUES(?,?,?,?,?,?,?,?);";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, team.getId());
            preparedStatement.setString(2, team.getName());
            preparedStatement.setInt(3, team.isRegistered() ? 1 : 0);
            preparedStatement.setInt(4, team.isPublic() ? 1 : 0);
            preparedStatement.setInt(5, team.getGames());
            preparedStatement.setInt(6, team.getWins());
            preparedStatement.setString(7, team.getDescr());
            preparedStatement.setString(8, team.getHtmlName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public Team getTeam(String id) {
        Team team = null;
        String queryText = "Select * FROM teams WHERE Id" + VAR;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    team = new Team(id);
                    team.setName(result.getString("name"));
                    team.setRegistered(result.getInt("registered") > 0);
                    team.setPublic(result.getInt("is_public") > 0);
                    team.setGames(result.getInt(GAMES_COLUMN));
                    team.setWins(result.getInt("wins"));
                    team.setDescr(result.getString(Config.DESCR));
                    team.setHtmlName(result.getString("html_name"));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return team;
    }

    public void dropTeam(String id) {
        String queryText = "DELETE FROM teams WHERE Id" + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public void saveAction(Action action) {
        String queryText = "INSERT OR REPLACE INTO round_actions (id,action_type,target_id,percent,cast_id,counter) " +
                "VALUES(?,?,?,?,?,?);";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, action.getUser().getUserId());
            preparedStatement.setString(2, action.getActionId());
            preparedStatement.setInt(3, action.getTarget().getUserId());
            preparedStatement.setInt(4, action.getPercent());
            preparedStatement.setString(5, action.getCastId());
            int count = getCount("ROUND_ACTIONS", ID, action.getUser().getUserId()) + 1;
            preparedStatement.setInt(6, count);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
    }

    public List<String> getSpellsIdToLearn(int userId, int spellLevel) {
        ArrayList<String> resultStringArr = new ArrayList<>();
        String queryText = "SELECT s1.id FROM spells s1 " +
                "LEFT JOIN (SELECT id FROM available_spells " +
                "WHERE user_id" + VAR + " AND spell_grade >2) s2 " +
                "ON s1.id=s2.id " +
                "WHERE class=(SELECT class FROM users WHERE id" + VAR + ") " +
                "AND level" + VAR + " " +
                "AND s2.id IS NULL;";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, spellLevel);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    resultStringArr.add(result.getString(ID));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return resultStringArr;
    }

    public int getIntFrom(String tableName, Integer id, String columnName) {
        int resultInt = -1;
        String queryText = SELECT + columnName + FROM + tableName + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultInt = result.getInt(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return resultInt;
    }

    public int getIntFrom(String tableName, String id, String columnName) {
        int resultInt = -1;
        String queryText = SELECT + columnName + FROM + tableName + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultInt = result.getInt(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return resultInt;
    }

    public long getLongFrom(String tableName, Integer id, String columnName) {
        long resultLong = -1;
        String queryText = SELECT + columnName + FROM + tableName + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultLong = result.getLong(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return resultLong;
    }

    public double getDoubleFrom(String tableName, Integer id, String columnName) {
        double resultDouble = -1;
        String queryText = SELECT + columnName + FROM + tableName + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultDouble = result.getDouble(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return resultDouble;
    }

    public double getDoubleFrom(String tableName, String id, String columnName) {
        double resultDouble = -1;
        String queryText = SELECT + columnName + FROM + tableName + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultDouble = result.getDouble(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
            throw new DbException(e.getMessage(), e);
        }
        return resultDouble;
    }

    /***
     * SELECT id FROM users WHERE status='1';
     ***/
    public List<Integer> getInts(String tableName, String columnId, Integer id, String columnName) {
        List<Integer> resultIntArr = new ArrayList<>();
        String queryText = SELECT + columnName + FROM + tableName + WHERE + columnId + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    resultIntArr.add(result.getInt(columnName));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultIntArr;
    }

    public String getStringFrom(String tableName, String id, String columnName) {
        String resultString = EMPTY;
        String queryText = SELECT + columnName + FROM + tableName + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultString = result.getString(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultString;
    }

    public String getStringFrom(String tableName, Integer id, String columnName) {//overload
        String resultString = EMPTY;
        String queryText = SELECT + columnName + FROM + tableName + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultString = result.getString(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultString;
    }

    public String getStringBy(String tableName, String findByColumn, String id, String selectedColumn) {
        String resultString = EMPTY;
        String queryText = SELECT + selectedColumn + FROM + tableName + WHERE + findByColumn + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultString = result.getString(selectedColumn);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultString;
    }

    /***
     * SELECT counter FROM inventory WHERE id='waa' AND userId='362812407';
     ***/
    public int getIntByBy(String tableName, String columnName,
                          String firstColumn, String firstId, String secondColumn, Integer secondId) {
        int resultInt = 0;
        String queryText = SELECT + columnName
                + FROM + tableName
                + WHERE + firstColumn + VAR
                + AND + secondColumn + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, firstId);
            preparedStatement.setInt(2, secondId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultInt = result.getInt(columnName);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultInt;
    }

    /***
     * SELECT counter FROM inventory WHERE id='waa' AND userId='362812407';
     ***/
    public int getIntByBy(String tableName, String columnName, String firstColumn, int firstId, String secondColumn, int secondId) {
        int resultInt = -1;
        String queryText = SELECT + columnName
                + FROM + tableName
                + WHERE + firstColumn + VAR
                + AND + secondColumn + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, firstId);
            preparedStatement.setInt(2, secondId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultInt = result.getInt(columnName);
                } else {
                    throw new ArenaUserException("No such int: " + queryText);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultInt;
    }

    /***
     * SELECT id FROM inventory WHERE counter='1' AND userId='362812407';
     ***/
    public String getStringByBy(String tableName, String columnName, String firstColumn, int firstId, String secondColumn, Integer secondId) {
        String resultString = EMPTY;
        String queryText = SELECT + columnName
                + FROM + tableName
                + WHERE + firstColumn + VAR
                + AND + secondColumn + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, firstId);
            preparedStatement.setInt(2, secondId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    resultString = result.getString(columnName);
                } else {
                    throw new IllegalArgumentException("No such int: " + queryText);
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultString;
    }

    public List<String> getStrings(String tableName, String columnId, Integer id, String columnName) {
        List<String> resultStringArr = new ArrayList<>();
        String queryText = SELECT + columnName + FROM + tableName + WHERE + columnId + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    resultStringArr.add(result.getString(columnName));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultStringArr;
    }

    public List<String> getStrings(String tableName, String columnId, String id, String columnName) {
        List<String> resultStringArr = new ArrayList<>();
        String queryText = SELECT + columnName + FROM + tableName + WHERE + columnId + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, id);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    resultStringArr.add(result.getString(columnName));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultStringArr;
    }

    /***
     * SELECT name FROM users WHERE status='1' AND team='findById';
     ***/
    public List<String> getStringsBy(String tableName, String columnName,
                                     String firstColumn, String firstId, String secondColumn, Integer secondId) {
        List<String> resultStringArr = new ArrayList<>();
        String queryText = SELECT + columnName
                + FROM + tableName
                + WHERE + firstColumn + VAR
                + AND + secondColumn + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, firstId);
            preparedStatement.setInt(2, secondId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    resultStringArr.add(result.getString(columnName));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultStringArr;
    }

    /***
     * SELECT name FROM users WHERE status='1' AND team='findById';
     ***/
    public List<String> getStringsBy(String tableName, String columnName,
                                     String firstColumn, int firstId, String secondColumn, Integer secondId) {
        List<String> resultStringArr = new ArrayList<>();
        String queryText = SELECT + columnName
                + FROM + tableName
                + WHERE + firstColumn + VAR
                + AND + secondColumn + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, firstId);
            preparedStatement.setInt(2, secondId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    resultStringArr.add(result.getString(columnName));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultStringArr;
    }

    /***
     * SELECT name FROM users WHERE status='1' AND team='findById';
     ***/
    public List<Integer> getIntsBy(String tableName, String columnName,
                                   String firstColumn, String firstId, String secondColumn, Integer secondId) {
        List<Integer> resultStringArr = new ArrayList<>();
        String queryText = SELECT + columnName
                + FROM + tableName
                + WHERE + firstColumn + VAR
                + AND + secondColumn + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, firstId);
            preparedStatement.setInt(2, secondId);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    resultStringArr.add(result.getInt(columnName));
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return resultStringArr;
    }

    /***
     * "UPDATE users SET ?=? WHERE id=?;"
     ***/
    public void setLongTo(String tableName, Integer id, String recordColumn, long recordValue) {
        String queryText = UPDATE + tableName + SET + recordColumn + VAR + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setLong(1, recordValue);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
    }

    /***
     * "UPDATE users SET ?=? WHERE id=?;"
     ***/
    public void setDoubleTo(String tableName, Integer id, String recordColumn, double recordValue) {
        String queryText = UPDATE + tableName + SET + recordColumn + VAR + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setDouble(1, recordValue);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
    }

    /***
     * "UPDATE users SET ?=? WHERE id=?;"
     ***/
    public void setBigDecimalTo(String tableName, Integer id, String recordColumn, BigDecimal recordValue) {
        String queryText = UPDATE + tableName + SET + recordColumn + VAR + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setBigDecimal(1, recordValue);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
    }

    /***
     * "UPDATE users SET ?=? WHERE id=?;"
     ***/
    public boolean setIntTo(String tableName, Integer id, String recordColumn, Integer recordValue) {
        int updatedRows = 0;
        String queryText = UPDATE + tableName + SET + recordColumn + VAR + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, recordValue);
            preparedStatement.setInt(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return updatedRows > 0;
    }

    /***
     * "UPDATE users SET ?=? WHERE id=?;"
     ***/
    public boolean setIntTo(String tableName, String id, String recordColumn, Integer recordValue) {//overload
        int updatedRows = 0;
        String queryText = UPDATE + tableName + SET + recordColumn + VAR + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, recordValue);
            preparedStatement.setString(2, id);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return updatedRows > 0;
    }

    /***
     * "UPDATE users SET ?=? WHERE id=?;"
     ***/
    public void setStringTo(String tableName, Integer id, String recordColumn, String recordValue) {
        String queryText = UPDATE + tableName + SET + recordColumn + VAR + WHERE + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, recordValue);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
    }

    /***
     * "UPDATE users SET ?=? WHERE id=? and user_id=?;"
     ***/
    public void setStringTo(String tableName, Integer userId, String id, String recordColumn, String recordValue) {
        String queryText = UPDATE + tableName + SET + recordColumn + VAR + " WHERE id=? and user_" + ID + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setString(1, recordValue);
            preparedStatement.setString(2, id);
            preparedStatement.setInt(3, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
    }

    public int getCountDistinct(String tableName, String columnOfCount, String columnName, Integer value) {
        String queryText = "Select count(distinct " + columnOfCount + ")"
                + FROM + tableName
                + WHERE + columnName + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, value);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("count(distinct " + columnOfCount + ")");
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return -1;
    }

    public int getCount(String tableName, String columnName, Integer value) {
        String queryText = SELECT + COUNT + columnName + ") "
                + FROM + tableName
                + WHERE + columnName + VAR + SEMICOLON;
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText)) {
            preparedStatement.setInt(1, value);
            try (final ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    return result.getInt(COUNT + columnName + ")");
                }
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return -1;
    }

    public List<String> getColumn(String tableName, String columnName) {
        List<String> column = new LinkedList<>();
        String queryText = SELECT + columnName + FROM + tableName + " order by rowid;";
        try (final PreparedStatement preparedStatement = connection.getPreparedStatement(queryText);
             final ResultSet result = preparedStatement.executeQuery()) {
            while (result.next()) {
                column.add(result.getString(columnName));
            }
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e.getMessage());
        }
        return column;
    }

}
