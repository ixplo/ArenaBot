package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdListTest extends BaseTest {

    @Before
    public void setUp() throws Exception {
        init();
    }

    private void init() throws NoSuchFieldException, IllegalAccessException {
        warrior.setStatus(Config.IN_BATTLE_STATUS);
        testHelper.createTestRound();
        testHelper.initBattle();
    }

    @Test
    public void execute() throws Exception {
        BotCommand command = new CmdList();
        StringBuilder logger = testHelper.initLogger();
        Bot testBot = testHelper.getTestBot(logger);
        command.execute(testBot, testHelper.getUser(Presets.WARRIOR_ID), testHelper.getPrivate(), new String[0]);
        Assert.assertTrue(logger.toString().contains("TestTeam"));
    }

}