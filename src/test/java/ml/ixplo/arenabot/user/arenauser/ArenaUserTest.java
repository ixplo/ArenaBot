package ml.ixplo.arenabot.user.arenauser;

import ml.ixplo.arenabot.config.Constants;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

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
    private ArenaUser warrior = TestHelper.WARRIOR;
    private ArenaUser mage = TestHelper.MAGE;

    @Before
    public void setUp() throws Exception {
        testHelper.init();
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
        warrior.setNativeCon(5);
        warrior.setCurCon(5);
        warrior.setMinHit(0);
        warrior.setAttack(new BigDecimal(0.91 * warrior.getCurDex() + 0.39 * warrior.getCurStr()));
        warrior.setProtect(ArenaUser.roundDouble(0.4 * warrior.getCurDex() + 0.6 * warrior.getCurCon()));
        warrior.addHark("nativeStr",1);
        warrior.addHark("nativeCon",1);
        assertEquals(4, warrior.getCurStr());
        assertEquals(6, warrior.getCurCon());
        assertEquals(0.25, warrior.getMinHit(),0);
        assertEquals(0,warrior.getAttack().compareTo(new BigDecimal("4.28")));
        assertEquals(4.8, warrior.getProtect(),0);
        warrior.addHark("nativeStr",3);
        assertEquals(7, warrior.getNativeStr());
        assertEquals(1, warrior.getMinHit(),0);
    }

    @Test
    public void doesUserExists() throws Exception {
       assertTrue(ArenaUser.doesUserExists(362812407));
       assertFalse(ArenaUser.doesUserExists(1));
    }

    @Test
    public void getUserName() throws Exception {
        assertEquals(ArenaUser.getUserName(362812407),"ixplo");
        ArenaUser newUser = ArenaUser.create(ArenaUser.UserClass.WARRIOR);
        assertNull(newUser.getName());
    }


    @Test
    public void create() throws Exception {
        ArenaUser priest = ArenaUser.create(ArenaUser.UserClass.PRIEST);
        assertNotNull(priest);
        assertEquals(ArenaUser.UserClass.PRIEST.toString(), priest.getUserClass());
    }

    @Test
    public void setClassFeatures() throws Exception {

        ArenaUser mage = ArenaUser.create(ArenaUser.UserClass.MAGE);
        assertEquals("Атака", mage.getActionsName().get(0));

    }

    @Test
    public void dropUser() throws Exception {
        int userId = warrior.getUserId();
        warrior.dropUser();
        assertFalse(ArenaUser.doesUserExists(userId));
    }
//
//    @Test
//    public void getUser() throws Exception {
//    }
//
//
//    @Test
//    public void getUserTeam() throws Exception {
//    }
//
//    @Test
//    public void getClassName() throws Exception {
//    }
//
//    @Test
//    public void getClassName1() throws Exception {
//    }
//
//    @Test
//    public void getClassesDescr() throws Exception {
//    }
//
//    @Test
//    public void getRaceName() throws Exception {
//    }
//
//    @Test
//    public void getRaceName1() throws Exception {
//    }
//
//    @Test
//    public void getRacesName() throws Exception {
//    }
//
//    @Test
//    public void getRacesId() throws Exception {
//    }
//
//    @Test
//    public void getRacesDescr() throws Exception {
//    }
//
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