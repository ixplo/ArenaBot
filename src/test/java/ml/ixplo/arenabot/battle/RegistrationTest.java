package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(RegistrationTest.class);

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = TestHelper.WARRIOR;
    private Registration registration = new Registration();

    @Before
    public void setUp() throws Exception {
        Registration.setDb(testHelper.getDb());
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }

    @Test
    public void dropStatus() throws Exception {
        warrior.setStatus(Config.IN_BATTLE);
        Registration.dropStatus();
        Assert.assertEquals(Config.UNREG, warrior.getStatus());
    }

    @Test
    public void startBattle() throws Exception {
    }

    @Test
    public void regMember() throws Exception {
        registration.regMember(warrior.getUserId());
        Assert.assertEquals(Config.REG, warrior.getStatus());
    }

    @Test
    public void unregMember() throws Exception {
        warrior.setStatus(Config.REG);
        registration.unregMember(warrior.getUserId());
        Assert.assertEquals(Config.UNREG, warrior.getStatus());
    }

    @Test
    public void getTeamsCount() throws Exception {
    }

    @Test
    public void getTeamsName() throws Exception {
    }

    @Test
    public void getMembersCount() throws Exception {
    }

    @Test
    public void getMemberTeam() throws Exception {
    }

    @Test
    public void getMember() throws Exception {
    }

    @Test
    public void getMembers() throws Exception {
    }

    @Test
    public void getList() throws Exception {
    }

    @Test
    public void getLastMember() throws Exception {
    }

    @Test
    public void getMemberStatus() throws Exception {
    }

    @Test
    public void getMembersName() throws Exception {
    }

    @Test
    public void getTeam() throws Exception {
    }

}