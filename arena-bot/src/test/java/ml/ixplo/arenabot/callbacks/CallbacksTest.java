package ml.ixplo.arenabot.callbacks;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.helper.Presets;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.ApiConstants;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;
import java.net.Socket;

public class CallbacksTest extends BaseTest {

    private static final String HTTP_SERVER_URL = "localhost:80/";
    private Bot bot;
    private Update update;

    @Before
    public void setUp() throws Exception {
        startHttpServer();
        setBaseUrl();
        bot = new Bot() {
            public String getBaseUrl() {
                return "";
            }
        };
        update = getBlankUpdate();
    }

    private void startHttpServer() throws IOException {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(80);
                Socket socket = serverSocket.accept();
                OutputStream outputStream = socket.getOutputStream();
                String html = "<html>" +
                        "<head><title>Chat server</title><head>" +
                        "<body><h1>Привет, нубас</h1>" +
                        "<br/>" +
                        "<img src='http://m.top55.info/fileadmin/images/top55_news/bk_info_orig_41267_1447994871.jpg'>" +
                        "</body>" +
                        "</html>";
                String header = "HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html; charset=utf-8\n" +
                        "Content-Length: " + html.length() + "\n" +
                        "Connection: close\n\n";
                String result = header + html;
                outputStream.write(result.getBytes());
            } catch (IOException e) {
                LOGGER.error("HttpServer error");
            }
        });
    }

    private void setBaseUrl() throws NoSuchFieldException, IllegalAccessException {
        Field base_url = ApiConstants.class.getDeclaredField("BASE_URL");
        base_url.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(base_url, base_url.getModifiers() & ~Modifier.FINAL);
        base_url.set(null, HTTP_SERVER_URL);
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
    // need http-server to response requests
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