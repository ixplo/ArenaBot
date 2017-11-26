package ml.ixplo.arenabot.messages;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagesTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);

    ArenaUser warrior;
    private TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() throws Exception {
        Messages.setBot(new Bot(testHelper.getDb()));
        warrior = TestHelper.WARRIOR;
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }


    @Test
    public void sendToRegisteredUserMsg() throws Exception {

    }

    @Test
    public void getEqipMsg() throws Exception {
        SendMessage message = Messages.getEqipMsg((long) Config.ADMIN_ID, Config.ADMIN_ID);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertEquals("sendmessage", message.getMethod());
        Assert.assertEquals("Ваш инвентарь: \n"
                + "0.<b>Ладошка</b>, \n"
                + "Оружие: <b>Ладошка</b>(0)", message.getText());
    }

    @Test
    public void getUserStatMsg() throws Exception {
        SendMessage message = Messages.getUserStatMsg((long) Config.ADMIN_ID, Config.ADMIN_ID);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertTrue(message.getText().contains("Ваши характеристики"));
        Assert.assertTrue(message.getText().contains("Победы"));
        Assert.assertTrue(message.getText().contains("Игры"));
        Assert.assertTrue(message.getText().contains("Был в бою"));
        Assert.assertTrue(message.getText().contains("Опыт"));
        Assert.assertTrue(message.getText().contains("Жизни"));
        Assert.assertTrue(message.getText().contains("Золото"));
        Assert.assertTrue(message.getText().contains("Сила"));
        Assert.assertTrue(message.getText().contains("Ловкость"));
        Assert.assertTrue(message.getText().contains("Мудрость"));
        Assert.assertTrue(message.getText().contains("Интеллект"));
        Assert.assertTrue(message.getText().contains("Телосложение"));
        Assert.assertTrue(message.getText().contains("Свободные очки"));
    }

    @Test
    public void getUserXStatMsg() throws Exception {
        SendMessage message = Messages.getUserXStatMsg((long) Config.ADMIN_ID, Config.ADMIN_ID);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertTrue(message.getText().contains("Ваши расширенные характеристики"));
        Assert.assertTrue(message.getText().contains("Урон"));
        Assert.assertTrue(message.getText().contains("Атака"));
        Assert.assertTrue(message.getText().contains("Защита"));
        Assert.assertTrue(message.getText().contains("Лечение"));
        Assert.assertTrue(message.getText().contains("Защ. от магии"));
        Assert.assertTrue(message.getText().contains("Мана"));
        Assert.assertTrue(message.getText().contains("Магич. бонусы"));
    }

    @Test
    public void getListMsg() throws Exception {
        SendMessage message = Messages.getListMsg((long) Config.ADMIN_ID);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertEquals("Еще никто не зарегистрировался", message.getText());
    }

    @Test
    public void getInlineKeyboardMsg() throws Exception {
        SendMessage message = Messages.getInlineKeyboardMsg(
                (long) Config.ADMIN_ID,
                Config.BOT_NAME,
                Arrays.asList("one", "two"),
                Arrays.asList("oneC", "twoC"));
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertEquals(Config.BOT_NAME, message.getText());
        Assert.assertTrue(message.getReplyMarkup() instanceof InlineKeyboardMarkup);
        InlineKeyboardMarkup ikm = (InlineKeyboardMarkup)message.getReplyMarkup();
        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        InlineKeyboardButton secondButton = new InlineKeyboardButton();
        firstButton.setText("one");
        secondButton.setText("two");
        Assert.assertTrue(ikm.getKeyboard().get(0).get(0).getText().equals("one"));
        Assert.assertTrue(ikm.getKeyboard().get(0).get(1).getText().equals("two"));
        Assert.assertTrue(ikm.getKeyboard().get(0).get(0).getCallbackData().equals("oneC"));
        Assert.assertTrue(ikm.getKeyboard().get(0).get(1).getCallbackData().equals("twoC"));
    }

    @Test
    public void getAnswerForInlineQuery() throws Exception {
    }

    @Test
    public void sendCreateUser() throws Exception {
    }

    @Test
    public void sendAskRace() throws Exception {
    }

    @Test
    public void sendCancelDelete() throws Exception {
    }

    @Test
    public void sendAfterDelete() throws Exception {
    }

    @Test
    public void sendExMsg() throws Exception {
    }

    @Test
    public void sendDropMsg() throws Exception {
    }

    @Test
    public void sendChannelMsg() throws Exception {
    }

    @Test
    public void sendChannelMsgReturnId() throws Exception {
    }

    @Test
    public void editChannelMsg() throws Exception {
    }

    @Test
    public void sendListToAll() throws Exception {
    }

    @Test
    public void sendListTo() throws Exception {
    }

    @Test
    public void sendResultToAll() throws Exception {
    }

    @Test
    public void sendToAll() throws Exception {
    }

    @Test
    public void sendToAll1() throws Exception {
    }

    @Test
    public void sendToAllMembers() throws Exception {
    }

    @Test
    public void sendDoMsg() throws Exception {
    }

    @Test
    public void sendRegMsg() throws Exception {
    }

    @Test
    public void sendMessage() throws Exception {
    }

    @Test
    public void sendMessage1() throws Exception {
    }

    @Test
    public void sendMessage2() throws Exception {
    }

    @Test
    public void sendChooseClassMsg() throws Exception {
    }

    @Test
    public void toOpenPrivateWithBotMsg() throws Exception {
    }

    @Test
    public void sendAskActionId() throws Exception {
    }

    @Test
    public void sendAskPercent() throws Exception {
    }

    @Test
    public void sendActionTaken() throws Exception {
    }

    @Test
    public void sendAskSpell() throws Exception {
    }

    @Test
    public void sendEmptyAnswerQuery() throws Exception {
    }

    @Test
    public void deleteMessage() throws Exception {
    }

}