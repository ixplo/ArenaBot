package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.params.Hark;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.math.BigDecimal;


public class ArcherTest extends BaseTest{
    private Archer archer = (Archer) testHelper.ARCHER;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void setAttackTest() {
        archer.setAttack(BigDecimal.valueOf(10));
        Assert.assertEquals(BigDecimal.valueOf(10).setScale(Config.SCALE, Config.ROUNDED), archer.getAttack());
    }

    @Test
    public void getClassFeaturesTest() throws Exception {
        Archer user = (Archer) ArenaUser.getUser(Presets.ARCHER_ID);
        user.addHark(Hark.NATIVE_WIS, 4);
        Assert.assertEquals(2, user.getMaxTarget());
    }

    @Test
    public void getXstatTest() throws Exception {
        SendMessage userXStatMsg = Messages.getUserXStatMsg(Presets.ARCHER_ID);
        Assert.assertTrue(userXStatMsg.toString().contains("Кол. целей:"));
    }
}