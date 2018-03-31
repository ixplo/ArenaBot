package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.config.Config;
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