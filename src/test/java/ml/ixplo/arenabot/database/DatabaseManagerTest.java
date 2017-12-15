package ml.ixplo.arenabot.database;

import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManagerTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);
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
    public void doesUserExists() throws Exception {
        Assert.assertTrue(db.doesUserExists(Presets.EXIST_USER_ID));
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

    @Test
    public void dropItems() throws Exception {
    }

    @Test
    public void dropItem() throws Exception {
    }

    @Test
    public void dropActions() throws Exception {
    }

    @Test
    public void dropSpells() throws Exception {
    }

    @Test
    public void getUser() throws Exception {
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

    @Test
    public void addInt() throws Exception {
    }

    @Test
    public void addString() throws Exception {
    }

    @Test
    public void addSpell() throws Exception {
    }

    @Test
    public void addAction() throws Exception {
    }

    @Test
    public void addItem() throws Exception {
    }

    @Test
    public void getItems() throws Exception {
    }

    @Test
    public void getItem() throws Exception {
    }

    @Test
    public void setTeam() throws Exception {
    }

    @Test
    public void getTeam() throws Exception {
    }

    @Test
    public void getSpellsIdToLearn() throws Exception {
    }

    @Test
    public void getIntFrom() throws Exception {
    }

    @Test
    public void getIntFrom1() throws Exception {
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