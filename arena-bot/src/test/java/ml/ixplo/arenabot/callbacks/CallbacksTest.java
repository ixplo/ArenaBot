package ml.ixplo.arenabot.callbacks;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.messages.Messages;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class CallbacksTest extends BaseTest {

    private Bot bot;
    private Update update;

    @Before
    public void setUp() throws Exception {
        bot = new Bot();
        update = getBlankUpdate();
    }

    private Update getBlankUpdate() throws NoSuchFieldException, IllegalAccessException {
        Update update = new Update();
        addQueryTo(update);
        return update;
    }

    @Test(expected = InvocationTargetException.class)
    public void UnsupportedOperationExceptionTest() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor<Callbacks> constructor = Callbacks.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void handleCallbackCommand() throws Exception {
        bot.processNonCommandUpdate(update);
    }

    private void addQueryTo(Update update) throws NoSuchFieldException, IllegalAccessException {
        Field callbackQueryField = update.getClass().getDeclaredField("callbackQuery");
        callbackQueryField.setAccessible(true);
        CallbackQuery query = getCallbackQuery();
        callbackQueryField.set(update, query);
    }

    private CallbackQuery getCallbackQuery() throws NoSuchFieldException, IllegalAccessException {
        CallbackQuery query = new CallbackQuery();
        setField(query, "data", "learnSpell_1");
        setField(query, "from", testHelper.getUser(Presets.WARRIOR_ID));
        setField(query, "message", getMessage());
        setField(query, "id", Presets.QUERY_ID);

        return query;
    }

    private Message getMessage() throws NoSuchFieldException, IllegalAccessException {
        Message message = new Message();
        setField(message, "chat", testHelper.getPrivate());
        return message;
    }

    private void setField(Object query, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field dataField = query.getClass().getDeclaredField(name);
        dataField.setAccessible(true);
        dataField.set(query, value);
    }
}