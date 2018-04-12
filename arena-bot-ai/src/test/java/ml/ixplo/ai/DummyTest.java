package ml.ixplo.ai;


import ml.ixplo.arenabot.Bot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;

import java.lang.reflect.Field;

public class DummyTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void simpleDummyTest() throws Exception {
        Bot bot = new Bot();
        Update update = getUpdate();
//        bot.processNonCommandUpdate(update);

    }

    private Update getUpdate() throws NoSuchFieldException, IllegalAccessException {
        Update update = new Update();
        CallbackQuery query = new CallbackQuery();
        Field callbackQueryField = update.getClass().getDeclaredField("callbackQuery");
        callbackQueryField.setAccessible(true);
        callbackQueryField.set(update, query);
        return update;
    }
}