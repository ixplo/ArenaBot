package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.messages.Messages;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Timer;


public class RegTimerTaskTest {
    private Timer regTimer;
    private RegTimerTask regTimerTask;
    private TestHelper testHelper = new TestHelper();
    private Registration registration;
    private volatile StringBuilder log;
    @Before
    public void setUp() throws Exception {
        log = new StringBuilder();
        Messages.setBot(testHelper.getTestBot(log));
        registration = testHelper.getTestRegistration();
    }

    @After
    public void tearDown() throws Exception {
        if (regTimer != null) {
            regTimer.cancel();
        }
        registration = null;
    }

    @Test
    public void run() throws Exception {
        regTimer = new Timer();
        regTimerTask = new RegTimerTask(registration, regTimer, Presets.DELAY_IN_SECONDS);
        regTimer.schedule(regTimerTask, 0, 1000);
        Thread.sleep(2500);
        Assert.assertTrue(log.toString().contains("осталось: 2 сек"));
        Assert.assertTrue(log.toString().contains("осталось: 1 сек"));
        Assert.assertTrue(log.toString().contains("осталось: 0 сек"));
        Assert.assertFalse(log.toString().contains("осталось: 3 сек"));
    }

    @Test
    public void setLeftToReg() throws Exception {
    }

    @Test
    public void getMessageId() throws Exception {
    }

}