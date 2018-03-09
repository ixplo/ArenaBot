package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.config.Config;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
}