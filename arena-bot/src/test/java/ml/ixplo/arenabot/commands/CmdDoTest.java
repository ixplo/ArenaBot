package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.helper.Presets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdDoTest extends BaseTest {

    private static final String IDENTIFIER = "do";

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        init();
    }

    private void init() throws NoSuchFieldException, IllegalAccessException {
        warrior.setStatus(Config.IN_BATTLE_STATUS);
        testHelper.createTestRound();
        testHelper.initBattle();
    }

    @Test
    public void commandTest() throws Exception {
        BotCommand command = new CmdDo();
        Assert.assertEquals(IDENTIFIER, command.getCommandIdentifier());
        Assert.assertTrue(command.getDescription().contains("действовать на цель под указанным номером"));
    }

    @Test
    public void executeTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"a", "1", "100"});

        Assert.assertEquals("Атаковать игрока <b>test_warrior</b> на 100 процентов", log.toString());
    }

    @Test
    public void executeShortStringsTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"a", "1"});

        Assert.assertEquals("Атаковать игрока <b>test_warrior</b> на 100 процентов", log.toString());
    }

    @Test
    public void executeVeryShortStringsTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"a"});

        Assert.assertEquals("Атаковать игрока <b>test_warrior</b> на 100 процентов", log.toString());
    }

    @Test
    public void executeSpellTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.MAGE_ID),
                testHelper.getPrivate(), new String[]{"a", "1", "100", "1am"});

        Assert.assertEquals(Presets.EMPTY, log.toString());
    }

    @Test(expected = ArenaUserException.class)
    public void executeWrongBotTest() throws Exception {
        Bot bad = testHelper.getBadBot();
        BotCommand command = new CmdDo();
        command.execute(bad, testHelper.getUser(Presets.WARRIOR_ID), testHelper.getPrivate(), new String[0]);
    }

    @Test
    public void executeWrongStatusTest() throws Exception {
        warrior.setStatus(Config.REGISTERED_STATUS);
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"a", "1", "100"});

        Assert.assertEquals(Presets.EMPTY, log.toString());
    }

    @Test
    public void executeWrongUserTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.NON_EXIST_USER_ID),
                testHelper.getPrivate(), new String[]{"a", "1", "100"});

        Assert.assertEquals(Presets.EMPTY, log.toString());
    }

    @Test
    public void executeWrongChatTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.NON_EXIST_USER_ID),
                testHelper.getChannel(), new String[]{"a", "1", "100"});

        Assert.assertEquals(Presets.EMPTY, log.toString());
    }

    @Test
    public void executeWrongPercentTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"a", "1", "101"});

        Assert.assertTrue(log.toString().contains(CmdDo.PERCENT_ERROR));
    }

    @Test
    public void executeEmptyCommandTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[0]);

        Assert.assertTrue(log.toString().contains(CmdDo.EMPTY_COMMAND_ERROR));
    }

    @Test
    public void executeWrongTargetIndexTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"a", Presets.NON_EXISTS_TARGET_INDEX});

        Assert.assertTrue(log.toString().contains("Цель под номером " + Presets.NON_EXISTS_TARGET_INDEX + " не найдена"));
    }

}