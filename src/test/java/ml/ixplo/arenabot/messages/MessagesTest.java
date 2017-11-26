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
        SendMessage msg = Messages.getEqipMsg((long) Config.IS_ADMIN, Config.IS_ADMIN);
        LOGGER.info("Получено сообщение об инвентаре: {}", msg.toString());
        Assert.assertEquals(Config.IS_ADMIN.toString(), msg.getChatId());
        Assert.assertEquals("sendmessage", msg.getMethod());
        Assert.assertEquals("Ваш инвентарь: \n"
                + "0.<b>Ладошка</b>, \n"
                + "Оружие: <b>Ладошка</b>(0)", msg.getText());
    }

    @Test
    public void getUserStatMsg() throws Exception {
    }

    @Test
    public void getUserXStatMsg() throws Exception {
    }

    @Test
    public void getListMsg() throws Exception {
    }

    @Test
    public void getInlineKeyboardMsg() throws Exception {
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