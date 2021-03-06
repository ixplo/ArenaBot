package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.helper.Presets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdHarkTest extends BaseTest {
    private BotCommand command = new CmdHark();

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
    public void executeStrengthTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"s", "2"});
        Assert.assertTrue(logger.toString().contains("Вы подняли Силу на: <b>2</b>"));
    }

    @Test
    public void executeDexTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"d", "1"});
        Assert.assertTrue(logger.toString().contains("Вы подняли Ловкость на: <b>1</b>"));
    }

    @Test
    public void executeWisTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"w", "1"});
        Assert.assertTrue(logger.toString().contains("Вы подняли Мудрость на: <b>1</b>"));
    }

    @Test
    public void executeIntTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"i", "4"});
        Assert.assertTrue(logger.toString().contains("Вы подняли Интеллект на: <b>4</b>"));
    }

    @Test
    public void executeConTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"c", "4"});
        Assert.assertTrue(logger.toString().contains("Вы подняли Телосложение на: <b>4</b>"));
    }

    @Test
    public void executeWrongHarkTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"wrong", "4"});
        Assert.assertEquals(CmdHark.EMPTY_ERROR, logger.toString());
    }

    @Test
    public void executeWrongNumberTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"s", "wrong"});
        Assert.assertEquals(CmdHark.EMPTY_ERROR, logger.toString());
    }

    @Test
    public void executeNegativeNumberTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"s", "-1"});
        Assert.assertTrue(logger.toString().contains("Что, правда уменьшить хотите?"));
    }

    @Test
    public void executeWrongUserTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.NON_EXIST_USER_ID),
                testHelper.getPrivate(), new String[]{"s", "4"});
        Assert.assertEquals(Presets.EMPTY, logger.toString());
    }

    @Test(expected = ArenaUserException.class)
    public void executeWrongBotTest() throws Exception {
        command.execute(testHelper.getBadBot(), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"s", "4"});
    }

    @Test
    public void executeWrongChatTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getChannel(), new String[]{"s", "4"});
        Assert.assertEquals(Presets.EMPTY, logger.toString());
    }

    @Test
    public void executeNotEnoughTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[]{"c", "5"});
        Assert.assertTrue(logger.toString().contains("Недостаточно свободных очков"));
    }

    @Test
    public void executeEmptyStringsTest() throws Exception {
        StringBuilder logger = testHelper.initLogger();
        command.execute(testHelper.getTestBot(logger), testHelper.getUser(Presets.WARRIOR_ID),
                testHelper.getPrivate(), new String[0]);
        Assert.assertEquals(CmdHark.EMPTY_ERROR, logger.toString());
    }

}