package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegistrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationTest.class);

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = testHelper.WARRIOR;
    private Registration registration;

    @Before
    public void setUp() throws Exception {
        Registration.setDb(testHelper.db());
        Messages.setBot(testHelper.getTestBot());
        registration = new Registration();
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }

    @Test
    public void dropStatus() throws Exception {
        warrior.setStatus(Config.IN_BATTLE_STATUS);
        Registration.dropStatus();
        Assert.assertEquals(Config.UNREGISTERED_STATUS, warrior.getStatus());
    }

    @Test
    public void startBattleRegistrationIsOff() throws Exception {
        Assert.assertNull(Battle.getBattle());
        registration.setIsOn(false);
        registration.startBattle();
        Assert.assertNull(Battle.getBattle());

    }

    @Test
    public void regMember() throws Exception {
        registration.regMember(warrior.getUserId());
        Assert.assertEquals(Config.REGISTERED_STATUS, warrior.getStatus());
    }

    @Test
    public void unregMember() throws Exception {
        warrior.setStatus(Config.REGISTERED_STATUS);
        registration.unregMember(warrior.getUserId());
        Assert.assertEquals(Config.UNREGISTERED_STATUS, warrior.getStatus());
    }

    @Test
    public void getMembersCountRegOne() throws Exception {
        Assert.assertEquals(0, registration.getMembersCount());
        registration.regMember(Presets.WARRIOR_ID);
        Assert.assertEquals(1, registration.getMembersCount());
    }

    @Test
    public void getMembersCountRegTwo() throws Exception {
        registration.regMember(Presets.WARRIOR_ID);
        registration.regMember(Presets.MAGE_ID);
        Assert.assertEquals(2, registration.getMembersCount());
    }

    @Test
    public void getMembersCountRegUnreg() throws Exception {
        registration.regMember(Presets.WARRIOR_ID);
        registration.unregMember(Presets.WARRIOR_ID);
        Assert.assertEquals(0, registration.getMembersCount());
    }

    @Test
    public void getMembersCountRegTwice() throws Exception {
        registration.regMember(Presets.WARRIOR_ID);
        registration.regMember(Presets.WARRIOR_ID);
        Assert.assertEquals(1, registration.getMembersCount());
    }

    @Test
    public void getMembersCountRegUnregAnother() throws Exception {
        registration.regMember(Presets.WARRIOR_ID);
        registration.unregMember(Presets.MAGE_ID);
        Assert.assertEquals(1, registration.getMembersCount());
    }

    @Test
    public void getTeams() throws Exception {
        registration.regMember(Presets.EXIST_USER_ID);
        registration.regMember(Presets.WARRIOR_ID);
        registration.regMember(Presets.MAGE_ID);
        List<Team> teams = registration.getTeams();
        Assert.assertEquals(3, teams.size());
        Assert.assertTrue(teams.stream().anyMatch((a) -> a.getId().equals(Presets.TEST_TEAM)));
    }

}