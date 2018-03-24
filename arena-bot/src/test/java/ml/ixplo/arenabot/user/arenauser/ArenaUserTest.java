package ml.ixplo.arenabot.user.arenauser;

import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.classes.UserClass;
import ml.ixplo.arenabot.utils.Utils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * ixplo
 * 01.05.2017.
 */
public class ArenaUserTest {
    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = testHelper.WARRIOR;
    private ArenaUser mage = testHelper.MAGE;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }

    @Test
    public void addHark() throws Exception {

        warrior.setNativeStr(3);
        warrior.setCurStr(3);
        warrior.setNativeDex(3);
        warrior.setCurDex(3);
        warrior.setNativeInt(3);
        warrior.setCurInt(3);
        warrior.setNativeWis(3);
        warrior.setCurWis(3);
        warrior.setNativeCon(5);
        warrior.setCurCon(5);
        warrior.setMinHit(0);
        warrior.setAttack(new BigDecimal(0.91 * warrior.getCurDex() + 0.39 * warrior.getCurStr()));
        warrior.setProtect(Utils.roundDouble(0.4 * warrior.getCurDex() + 0.6 * warrior.getCurCon()));
        warrior.addHark("nativeStr", 1);
        warrior.addHark("nativeCon", 1);
        Assert.assertEquals(4, warrior.getCurStr());
        Assert.assertEquals(6, warrior.getCurCon());
        Assert.assertEquals(0.25, warrior.getMinHit(), 0);
        assertEquals(0, warrior.getAttack().compareTo(new BigDecimal("4.28")));
        Assert.assertEquals(4.8, warrior.getProtect(), 0);
        warrior.addHark("nativeStr", 3);
        Assert.assertEquals(7, warrior.getNativeStr());
        Assert.assertEquals(1, warrior.getMinHit(), 0);
        warrior.addHark("nativeDex", 3);
        Assert.assertEquals(6, warrior.getNativeDex());
        warrior.addHark("nativeWis", 3);
        Assert.assertEquals(6, warrior.getNativeWis());
        warrior.addHark("nativeInt", 3);
        Assert.assertEquals(6, warrior.getNativeInt());
    }

    @Test
    public void addHarkSpellCaster() throws Exception {

        mage.setNativeStr(3);
        mage.setCurStr(3);
        mage.setNativeDex(3);
        mage.setCurDex(3);
        mage.setNativeInt(3);
        mage.setCurInt(3);
        mage.setNativeWis(3);
        mage.setCurWis(3);
        mage.setNativeCon(5);
        mage.setCurCon(5);
        mage.setMinHit(0);
        mage.setAttack(new BigDecimal(0.91 * mage.getCurDex() + 0.39 * mage.getCurStr()));
        mage.setProtect(Utils.roundDouble(0.4 * mage.getCurDex() + 0.6 * mage.getCurCon()));
        mage.addHark("nativeStr", 1);
        mage.addHark("nativeCon", 1);
        Assert.assertEquals(4, mage.getCurStr());
        Assert.assertEquals(6, mage.getCurCon());
        Assert.assertEquals(0.25, mage.getMinHit(), 0);
        assertEquals(0, mage.getAttack().compareTo(new BigDecimal("4.28")));
        Assert.assertEquals(4.8, mage.getProtect(), 0);
        mage.addHark("nativeStr", 3);
        Assert.assertEquals(7, mage.getNativeStr());
        Assert.assertEquals(1, mage.getMinHit(), 0);
        mage.addHark("nativeDex", 3);
        Assert.assertEquals(6, mage.getNativeDex());
        mage.addHark("nativeWis", 3);
        Assert.assertEquals(6, mage.getNativeWis());
        mage.addHark("nativeInt", 3);
        Assert.assertEquals(6, mage.getNativeInt());
    }

    @Test
    public void doesUserExists() throws Exception {
        Assert.assertTrue(ArenaUser.doesUserExists(Presets.WARRIOR_ID));
        Assert.assertFalse(ArenaUser.doesUserExists(Presets.NON_EXIST_USER_ID));
    }

    @Test
    public void getUserName() throws Exception {
        assertEquals(Presets.WARRIOR_NAME, ArenaUser.getUserName(Presets.WARRIOR_ID));
        ArenaUser newUser = ArenaUser.create(UserClass.WARRIOR);
        Assert.assertNull(newUser.getName());
    }


    @Test
    public void create_allGood() throws Exception {
        ArenaUser priest = ArenaUser.create(UserClass.PRIEST);
        Assert.assertNotNull(priest);
        assertEquals(UserClass.PRIEST.toString(), priest.getUserClass());
    }

    @Test
    public void createArcher_allGood() throws Exception {
        ArenaUser archer = ArenaUser.create(UserClass.ARCHER);
        Assert.assertNotNull(archer);
        assertEquals(UserClass.ARCHER.toString(), archer.getUserClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_nullClass() throws Exception {
        ArenaUser.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_unknownClass() throws Exception {
        ArenaUser.create(UserClass.valueOf("nonExist"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_existed() throws Exception {
        ArenaUser.create(
                Presets.WARRIOR_ID,
                Presets.WARRIOR_NAME,
                UserClass.WARRIOR,
                "o");
    }

    @Test
    public void setClassFeatures() throws Exception {

        ArenaUser mage = ArenaUser.create(UserClass.MAGE);
        assertEquals("Атака", mage.getActionsName().get(0));

    }

    @Test
    public void dropUser() throws Exception {
        warrior.dropUser();
        Assert.assertFalse(ArenaUser.doesUserExists(warrior.getUserId()));
    }

    @Test(expected = ArenaUserException.class)
    public void dropUserNonExists() throws Exception {
        warrior.dropUser();
        warrior.dropUser();
    }

    @Test
    public void getParams() throws NoSuchFieldException {
        Map<String, Object> params = warrior.getParams();
        Map<String, Object> mageParams = mage.getParams();
    }

    @Test
    public void getUser_allGood() throws Exception {
        Map<String, Object> params1 = warrior.getParams();
        Map<String, Object> params2 = ArenaUser.getUser(warrior.getUserId()).getParams();
        assertEquals(params1, params2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUser_nonExist() throws Exception {
        ArenaUser.getUser(Presets.NON_EXIST_USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItem_wrongItemId() {
        warrior.addItem(Presets.WRONG_ITEM_ID);
    }

    @Test
    public void addItem_allGood() {
        warrior.addItem(Presets.FLAMBERG);
        Assert.assertEquals(2, warrior.getItems().size());
        Assert.assertEquals(Presets.FLAMBERG, warrior.getItems().get(1).getItemId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void dropItem_wrongItemId() {
        warrior.dropItem(Presets.WRONG_ITEM_INDEX);
    }

    @Test
    public void dropItem_allGood() {
        warrior.addItem(Presets.FLAMBERG);
        warrior.dropItem(1);
        Assert.assertEquals(1, warrior.getItems().size());
        Assert.assertEquals("waa", warrior.getItems().get(0).getItemId());
    }

    @Test
    public void getStatus() {
        Assert.assertEquals(0, ArenaUser.getStatus(warrior.getUserId()));
        warrior.setStatus(1);
        Assert.assertEquals(1, ArenaUser.getStatus(warrior.getUserId()));

    }
    @Test
    public void getUserTeam() throws Exception {
        Assert.assertEquals(Presets.TEST_TEAM, ArenaUser.getUserTeamId(Presets.WARRIOR_ID));
    }

    @Test
    public void getClassName() throws Exception {
        Assert.assertEquals(Presets.WARRIOR_CLASS_NAME, warrior.getClassName());
    }

    @Test
    public void getClassNameByUserId() throws Exception {
        Assert.assertEquals(Presets.WARRIOR_CLASS_NAME, ArenaUser.getClassName(Presets.WARRIOR_ID));
    }

    @Test
    public void getClassNameById() throws Exception {
        Assert.assertEquals(Presets.WARRIOR_CLASS_NAME, ArenaUser.getClassName(Presets.WARRIOR_CLASS));
    }

    @Test
    public void getClassesDescr() throws Exception {
        List<String> classesDescr = ArenaUser.getClassesDescr();
        for (String classDescr : classesDescr) {
            Assert.assertTrue(containsOneOf(classDescr, Presets.USER_CLASSES_NAMES));
        }
    }

    private boolean containsOneOf(String text, List<String> tokens) {
        for (String token : tokens) {
            if (text.contains(token)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void getRaceName() throws Exception {
        Assert.assertEquals(Presets.WARRIOR_RACE_NAME, warrior.getRaceName());
    }

    @Test
    public void getRaceNameByUserId() throws Exception {
        Assert.assertEquals(Presets.WARRIOR_RACE_NAME, ArenaUser.getRaceName(Presets.WARRIOR_ID));
    }

    @Test
    public void getRaceNameById() throws Exception {
        Assert.assertEquals(Presets.WARRIOR_RACE_NAME, ArenaUser.getRaceName(Presets.WARRIOR_RACE));
    }

    @Test
    public void getRacesName() throws Exception {
        List<String> racesName = ArenaUser.getRacesName();
        for (String raceName : racesName) {
            Assert.assertTrue(containsOneOf(raceName, Presets.USER_RACES_NAMES));
        }
    }

    @Test
    public void getRacesId() throws Exception {
        List<String> racesId = ArenaUser.getRacesId();
        for (String raceId : racesId) {
            Assert.assertTrue(containsOneOf(raceId, Presets.USER_RACES_ID));
        }
    }

    @Test
    public void getRacesDescr() throws Exception {
        List<String> racesDescr = ArenaUser.getRacesDescr();
        for (String raceDescr : racesDescr) {
            Assert.assertTrue(containsOneOf(raceDescr, Presets.USER_RACES_NAMES));
        }
    }

    @Test
    public void doesUserEsists() throws Exception {
        Assert.assertTrue(ArenaUser.doesUserExists(Presets.EXIST_USER_ID));
        Assert.assertFalse(ArenaUser.doesUserExists(Presets.NON_EXIST_USER_ID));
    }

    @Test
    public void addCurHitPoints() throws Exception {
        double curHitPoints = warrior.getCurHitPoints();
        warrior.addCurHitPoints(Presets.ADD_HIT_POINTS);
        curHitPoints += Presets.ADD_HIT_POINTS;
        Assert.assertEquals(curHitPoints, warrior.getCurHitPoints(), Presets.DELTA);
    }

    @Test
    public void getActionsId() throws Exception {
        List<String> actionsId = mage.getActionsIdForInlineKeyboard();
        Assert.assertTrue(actionsId.stream().anyMatch(a -> a.contains("Атака")));
        Assert.assertTrue(actionsId.stream().anyMatch(a -> a.contains("Защита")));
        Assert.assertTrue(actionsId.stream().anyMatch(a -> a.contains("Лечение")));
        Assert.assertTrue(actionsId.stream().anyMatch(a -> a.contains("spell")));
    }

    @Test
    public void addCurExp() {
        int curExp = warrior.getCurExp();
        warrior.addCurExp(Presets.ADD_EXP);
        curExp += Presets.ADD_EXP;
        Assert.assertEquals(curExp, warrior.getCurExp());
    }

    @Test
    public void addExperience() {
        int experience = warrior.getExperience();
        warrior.addExperience(Presets.ADD_EXP);
        experience += Presets.ADD_EXP;
        Assert.assertEquals(experience, warrior.getExperience());
    }

    @Test
    public void addUserGames() {
        int games = warrior.getUserGames();
        warrior.addUserGames();
        games++;
        Assert.assertEquals(games, warrior.getUserGames());
    }

    @Test
    public void addUserWins() {
        int wins = warrior.getUserWins();
        warrior.addUserWins();
        wins++;
        Assert.assertEquals(wins, warrior.getUserWins());
    }

    @Test
    public void addMoney() {
        int money = warrior.getMoney();
        warrior.addMoney(Presets.MONEY);
        money += Presets.MONEY;
        Assert.assertEquals(money, warrior.getMoney());
    }

    @Test
    public void setLastGame() {
        long previous = warrior.getLastGame();
        warrior.setLastGame();
        Assert.assertTrue(warrior.getLastGame() > previous);
    }

    @Test
    public void getUsers() {
        List<ArenaUser> users = ArenaUser.getUsers(Arrays.asList(Presets.WARRIOR_ID, Presets.MAGE_ID));
        Assert.assertTrue(users.size() == 2);
        Assert.assertTrue(users.get(0).getUserId().equals(Presets.WARRIOR_ID) ||
                users.get(0).getUserId().equals(Presets.MAGE_ID));
        Assert.assertTrue(users.get(1).getUserId().equals(Presets.WARRIOR_ID) ||
                users.get(1).getUserId().equals(Presets.MAGE_ID));
    }

    @Test
    public void getItem() {
        warrior.addItem(Presets.FLAMBERG);
        Assert.assertEquals(Presets.FLAMBERG, warrior.getItem(1).getItemId());
    }

//    @Test
//    public void putOn() throws Exception {
//    }
//
//    @Test
//    public void putOff() throws Exception {
//    }
//
//    @Test
//    public void getItemsId() throws Exception {
//    }
//
//    @Test
//    public void getItems() throws Exception {
//    }
//
//    @Test
//    public void getEqipIndex() throws Exception {
//    }
//
//    @Test
//    public void isItemInSlot() throws Exception {
//    }
//
//    @Test
//    public void getItemId() throws Exception {
//    }
//
//    @Test
//    public void getEqipAmount() throws Exception {
//    }

}