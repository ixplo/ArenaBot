package ml.ixplo.arenabot.messages;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;

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
    public void getGreetingsMsg() throws Exception {
        SendMessage message = Messages.getGreetingsMsg(Config.ADMIN_ID);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertTrue(message.getText().contains("Ваши характеристики"));
        Assert.assertTrue(message.getText().contains("Ваши расширенные характеристики"));
        Assert.assertTrue(message.getText().contains("Ваш инвентарь"));
    }

    @Test
    public void getCreateUserMsg() throws Exception {
        SendMessage message = Messages.getCreateUserMsg(Config.ADMIN_ID);
        LOGGER.info(message.getText());
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertTrue(message.getText().contains("Ваши характеристики"));
        Assert.assertTrue(message.getText().contains("Ваши расширенные характеристики"));
        Assert.assertTrue(message.getText().contains("Ваш инвентарь"));
    }

    @Test
    public void getEqipMsg() throws Exception {
        SendMessage message = Messages.getEqipMsg(Config.ADMIN_ID);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertEquals("sendmessage", message.getMethod());
        Assert.assertEquals("Ваш инвентарь: \n"
                + "0.<b>Ладошка</b>, \n"
                + "Оружие: <b>Ладошка</b>(0)", message.getText());
    }

    @Test
    public void getUserStatMsg() throws Exception {
        SendMessage message = Messages.getUserStatMsg(Config.ADMIN_ID);
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
        SendMessage message = Messages.getUserXStatMsg(Config.ADMIN_ID);
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
        InlineKeyboardMarkup ikm = (InlineKeyboardMarkup) message.getReplyMarkup();
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
        AnswerInlineQuery answerInlineQuery = Messages.getAnswerForInlineQuery(new InlineQuery());
        InlineQueryResultArticle inlineQueryResultArticle = (InlineQueryResultArticle) answerInlineQuery.getResults().get(0);
        Assert.assertEquals(Config.BOT_PRIVATE, inlineQueryResultArticle.getUrl());
    }

    @Test
    public void selectedUserClassQuery() throws Exception {
        AnswerCallbackQuery answerCallbackQuery = Messages.selectedUserClassQuery(
                "AnyQueryId", Presets.WARRIOR_CLASS);
        Assert.assertEquals("AnyQueryId", answerCallbackQuery.getCallbackQueryId());
        Assert.assertEquals("Вы выбрали класс: " + warrior.getClassName(), answerCallbackQuery.getText());
    }

    @Test
    public void getCreateUserQuery() {
        AnswerCallbackQuery answerCallbackQuery = Messages.getCreateUserQuery(
                "AnyQueryId",
                warrior.getClassName(),
                warrior.getRaceName());
        Assert.assertEquals("AnyQueryId", answerCallbackQuery.getCallbackQueryId());
        Assert.assertEquals("Ваш персонаж "
                + warrior.getClassName() + "/"
                + warrior.getRaceName() + " создан!", answerCallbackQuery.getText());
    }

    @Test
    public void getChooseRaceMsg() {
        SendMessage message = Messages.getChooseRaceMsg((long) Config.ADMIN_ID, Presets.WARRIOR_CLASS);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertTrue(message.getText().contains("Выберите расу персонажа"));
        Assert.assertTrue(message.getReplyMarkup() instanceof InlineKeyboardMarkup);
        InlineKeyboardMarkup ikm = (InlineKeyboardMarkup) message.getReplyMarkup();
        Assert.assertTrue(ikm.getKeyboard().get(0).get(1).getCallbackData().contains(Presets.WARRIOR_CLASS));
    }

    @Test
    public void sendCancelDelete() throws Exception {
    }

    @Test
    public void sendAfterDelete() throws Exception {
    }

    @Test
    public void getExMsg() throws Exception {
        SendMessage message = Messages.getExMsg(warrior.getUserId(),Presets.ITEM_INDEX);
        Assert.assertEquals(warrior.getUserId(), Integer.parseInt(message.getChatId()));
        Assert.assertTrue(warrior.getItems().get(Presets.ITEM_INDEX).getInfo().contains(Presets.ITEM_NAME));
    }

    @Test
    public void getDropMsg() throws Exception {
        SendMessage message = Messages.getDropMsg(warrior.getUserId());
        Assert.assertEquals(warrior.getUserId(), Integer.parseInt(message.getChatId()));
        Assert.assertEquals("<b>Удалить</b> персонажа без возможности восстановления?", message.getText());
        Assert.assertTrue(message.getReplyMarkup() instanceof InlineKeyboardMarkup);
        InlineKeyboardMarkup ikm = (InlineKeyboardMarkup) message.getReplyMarkup();
        Assert.assertTrue(ikm.getKeyboard().get(0).get(0).getText().equals("Удалить"));
        Assert.assertTrue(ikm.getKeyboard().get(0).get(1).getText().equals("Отмена"));
        Assert.assertTrue(ikm.getKeyboard().get(0).get(0).getCallbackData().equals("del_Delete"));
        Assert.assertTrue(ikm.getKeyboard().get(0).get(1).getCallbackData().equals("del_Cancel"));
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
    public void getChooseClassMsg() throws Exception {
        SendMessage message = Messages.getChooseClassMsg((long) Config.ADMIN_ID);
        Assert.assertEquals(Config.ADMIN_ID.toString(), message.getChatId());
        Assert.assertTrue(message.getText().contains("Выберите класс персонажа"));
    }

    @Test
    public void getOpenPrivateWithBotMsg() throws Exception {
        SendMessage message = Messages.getOpenPrivateWithBotMsg(warrior.getUserId(), warrior.getName());
        Assert.assertEquals(warrior.getUserId(), Integer.parseInt(message.getChatId()));
        Assert.assertTrue(message.getText().contains("Добро пожаловать"));
        Assert.assertTrue(message.getText().contains(warrior.getName()));
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