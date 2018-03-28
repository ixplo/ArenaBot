package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Battle;
import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

public class CmdDoTest extends BaseTest {

    private static final String IDENTIFIER = "do";
    private static final String USERCHATTYPE = "private";

    @Test
    public void commandTest() throws Exception {
        BotCommand command = new CmdDo();
        Assert.assertEquals(IDENTIFIER, command.getCommandIdentifier());
        Assert.assertTrue(command.getDescription().contains("действовать на цель под указанным номером"));
    }

    @Test
    public void executeTest() throws Exception {
        init();
        StringBuilder log = testHelper.initLogger();

        BotCommand command = new CmdDo();
        command.execute(testHelper.getTestBot(log), getUser(), getChat(), new String[]{"a", "1", "100"});

        Assert.assertEquals("Атаковать игрока <b>test_warrior</b> на 100 процентов", log.toString());
    }

    private void init() throws NoSuchFieldException, IllegalAccessException {
        warrior.setStatus(Config.IN_BATTLE_STATUS);
        testHelper.getTestRound();
        initBattle();
    }

    private void initBattle() throws NoSuchFieldException, IllegalAccessException {
        Class<Battle> battleClass = Battle.class;
        Field battle = battleClass.getDeclaredField("battle");
        battle.setAccessible(true);
        battle.set(battleClass, Mockito.mock(Battle.class));
    }

    private User getUser() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        Field id = user.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(user, Presets.WARRIOR_ID);
        return user;
    }

    private Chat getChat() throws NoSuchFieldException, IllegalAccessException {
        Chat chat = new Chat();
        Field id = chat.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(chat, Presets.CHANNEL_ID);

        Field type = chat.getClass().getDeclaredField("type");
        type.setAccessible(true);
        type.set(chat, USERCHATTYPE);
        return chat;
    }
}