package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.messages.Messages;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
    public void runTest() throws Exception {
        regTimer = new Timer();
        regTimerTask = new RegTimerTask(registration, regTimer, Presets.DELAY_IN_SECONDS);
        regTimerTask.setDelayToRegistration(Presets.ONE_SECOND);
        regTimer.schedule(regTimerTask, 0, Presets.MILLIS_IN_SECOND);

        Thread.sleep(1500);

        Assert.assertTrue(log.toString().contains("осталось: 1 сек"));
        Assert.assertTrue(log.toString().contains("Битва началась!"));
        Assert.assertFalse(log.toString().contains("осталось: 2 сек"));
        Assert.assertTrue(Presets.MESSAGE_ID == regTimerTask.getMessageId());
    }

    @Test
    @Ignore("Test for just looking. Unignore it if you want to see the log")
    public void runWithLogger() throws Exception {
        Messages.setBot(testHelper.getTestBot());
        regTimer = new Timer();
        regTimerTask = new RegTimerTask(registration, regTimer, Config.DELAY_IN_SECONDS);
        regTimer.schedule(regTimerTask, 0, Presets.MILLIS_IN_SECOND);

        Thread.sleep(17000);
    }

}