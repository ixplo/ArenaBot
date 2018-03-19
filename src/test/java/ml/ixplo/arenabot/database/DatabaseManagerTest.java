package ml.ixplo.arenabot.database;

import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class DatabaseManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);
    private DatabaseManager db;
    private TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() throws Exception {
        db = testHelper.getDb();
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }

    @Test
    public void checkVersion() {
        Assert.assertEquals(Presets.TEST_DB_VERSION, DatabaseManager.getConnection().checkVersion());
    }

    @Test
    public void getInstanceTest() throws Exception {
        Class<DatabaseManager> databaseManagerClass = DatabaseManager.class;
        Field instance = databaseManagerClass.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(databaseManagerClass, null);

        Assert.assertNotNull(DatabaseManager.getInstance());
    }

    @Test
    public void doesUserExists() throws Exception {
        Assert.assertTrue(db.doesUserExists(Presets.EXIST_USER_ID));
    }

    @Test(expected = DbException.class)
    public void doesUserExistException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.doesUserExists(Presets.EXIST_USER_ID);
    }

    @Test
    public void dropStatus() throws Exception {
    }

    @Test
    public void dropUser() throws Exception {
        Assert.assertTrue(db.doesUserExists(Presets.EXIST_USER_ID));
        db.dropUser(Presets.EXIST_USER_ID);
        Assert.assertFalse(db.doesUserExists(Presets.EXIST_USER_ID));
    }

    @Test(expected = DbException.class)
    public void dropUserException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.dropUser(Presets.EXIST_USER_ID);
    }

    @Test(expected = DbException.class)
    public void dropItemsException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.dropItems(Presets.EXIST_USER_ID);
    }

    @Test(expected = DbException.class)
    public void dropItemException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.dropItem(Presets.WARRIOR_ID, Presets.ITEM_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dropNotExistsItem() throws Exception {
        db.dropItem(Presets.WARRIOR_ID, Presets.FLAMBERG);
    }

    @Test(expected = DbException.class)
    public void dropActionsWrongConnection() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.dropActions();
    }

    @Test(expected = DbException.class)
    public void dropActionsForUserWrongConnection() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.dropActions(Presets.WARRIOR_ID);
    }

    @Test(expected = DbException.class)
    public void dropTeamWrongConnection() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.dropTeam(Presets.TEST_TEAM);
    }

    @Test(expected = DbException.class)
    public void dropSpellsException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.dropSpells(Presets.WARRIOR_ID);
    }

    @Test
    public void getUserNotExists() throws Exception {
        Assert.assertNull(db.getUser(Presets.NON_EXIST_USER_ID));
    }

    @Test(expected = DbException.class)
    public void getUserWrongConnection() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getUser(Presets.WARRIOR_ID);
    }

    @Test
    public void setUser() throws Exception {
    }

    @Test
    public void getSlot() throws Exception {
    }

    @Test
    public void addUser() throws Exception {
        db.addUser(Presets.NON_EXIST_USER_ID, Presets.MAGE_NAME);
        Assert.assertTrue(db.doesUserExists(Presets.NON_EXIST_USER_ID));
    }

    @Test(expected = DbException.class)
    public void addUserExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.addUser(Presets.WARRIOR_ID, Presets.WARRIOR_NAME);
    }

    @Test(expected = DbException.class)
    public void updateUserExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.updateUser(testHelper.ARCHER);
    }

    @Test(expected = DbException.class)
    public void getSlotExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getSlot(Presets.WARRIOR_ID, Presets.ITEM_INDEX);
    }

    @Test
    public void addInt() throws Exception {
    }

    @Test
    public void addString() throws Exception {
    }

    @Test(expected = DbException.class)
    public void addSpellExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.addSpell(Presets.WARRIOR_ID, Presets.MAGIC_ARROW_SPELL_ID, Presets.SPELL_GRADE_ONE);
    }

    @Test(expected = DbException.class)
    public void saveActionWrongConnection() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.saveAction(Action.create(Presets.WARRIOR_ID, Action.ATTACK, Presets.WARRIOR_ID, Presets.FULL_PERCENT));
    }

    @Test(expected = DbException.class)
    public void addActionExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.addAction(Presets.WARRIOR_ID);
    }

    @Test(expected = DbException.class)
    public void addItemException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.addItem(Presets.WARRIOR_ID, Presets.FLAMBERG);
    }

    @Test(expected = DbException.class)
    public void addItemNonExistsUserTest() throws Exception {
        db.addItem(Presets.NON_EXIST_USER_ID, Presets.FLAMBERG);
    }

    @Test(expected = DbException.class)
    public void getItemsWrongConnection() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getItems(Presets.WARRIOR_ID);
    }

    @Test(expected = DbException.class)
    public void getItemExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getItem(Presets.ITEM_ID);
    }

    @Test(expected = DbException.class)
    public void setTeamExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.setTeam(new Team(Presets.TEST_TEAM));
    }

    @Test
    public void setRegisteredTeam() throws Exception {
        String blackguards = "blackguards";
        Team team = new Team(blackguards);
        team.setRegistered(true);
        team.setPublic(true);
        db.setTeam(team);
        Assert.assertTrue(db.getTeam(blackguards).isPublic());
        Assert.assertTrue(db.getTeam(blackguards).isRegistered());
        db.dropTeam(blackguards);
    }

    @Test(expected = DbException.class)
    public void getTeamExceptionTest() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getTeam(Presets.TEST_TEAM);
    }

    @Test(expected = DbException.class)
    public void getSpellsIdToLearn() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getSpellsIdToLearn(Presets.MAGE_ID, 1);
    }

    @Test(expected = DbException.class)
    public void getIntFromException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getIntFrom(Config.USERS, Presets.WARRIOR_ID, DatabaseManager.TEAM_COLUMN);
    }

    @Test(expected = DbException.class)
    public void getIntByStringFromException() throws Exception {
        DatabaseManager.setConnection(new ConnectionDB());
        db.getIntFrom(Config.INVENTORY, Presets.FLAMBERG, "in_slot");
    }

    @Test(expected = DbException.class)
    public void getIntFromNonExistsColumn() throws Exception {
        db.getIntFrom(Config.USERS, Presets.WARRIOR_ID, "wrong");
    }

    @Test
    public void getIntFromZeroResult() throws Exception {
        Assert.assertEquals(0, db.getIntFrom(Config.USERS, Presets.WARRIOR_ID, DatabaseManager.CUR_MANA));
    }

    @Test
    public void getIntFromEmptyResult() throws Exception {
        Assert.assertEquals(-1, db.getIntFrom(Config.USERS, Presets.NON_EXIST_USER_ID, DatabaseManager.CUR_MANA));
    }

    @Test
    public void getIntBy() throws Exception {
    }

    @Test
    public void getDoubleFrom() throws Exception {
    }

    @Test
    public void getDoubleFrom1() throws Exception {
    }

    @Test
    public void getInts() throws Exception {
    }

    @Test
    public void getStringFrom() throws Exception {
    }

    @Test
    public void getStringFrom1() throws Exception {
    }

    @Test
    public void getStringBy() throws Exception {
    }

    @Test
    public void getIntByBy() throws Exception {
    }

    @Test
    public void getIntByBy1() throws Exception {
    }

    @Test
    public void getStringByBy() throws Exception {
    }

    @Test
    public void getStrings() throws Exception {
    }

    @Test
    public void getStrings1() throws Exception {
    }

    @Test
    public void getStringsBy() throws Exception {
    }

    @Test
    public void getStringsBy1() throws Exception {
    }

    @Test
    public void getIntsBy() throws Exception {
    }

    @Test
    public void setLongTo() throws Exception {
    }

    @Test
    public void setDoubleTo() throws Exception {
    }

    @Test
    public void setBigDecimalTo() throws Exception {
    }

    @Test
    public void setIntTo() throws Exception {
    }

    @Test
    public void setIntTo1() throws Exception {
    }

    @Test
    public void setStringTo() throws Exception {
    }

    @Test
    public void setStringTo1() throws Exception {
    }

    @Test
    public void getCount() throws Exception {
    }

    @Test
    public void getCountDistinct() throws Exception {
    }

    @Test
    public void getCount1() throws Exception {
    }

    @Test
    public void getCount2() throws Exception {
    }

    @Test
    public void getColumn() throws Exception {
    }

}