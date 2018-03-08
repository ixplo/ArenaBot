package ml.ixplo.arenabot.helper;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.database.ConnectionDB;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.classes.UserClass;
import ml.ixplo.arenabot.user.items.Item;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashSet;
import java.util.Set;

import static ml.ixplo.arenabot.config.Config.TEST_DB_LINK;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Bot.class)
public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestHelper.class);

    private PropertiesLoader propertiesLoader;
    private DatabaseManager db;
    public static ArenaUser WARRIOR;
    public static ArenaUser MAGE;
    public static ArenaUser EXIST_USER;
    private static Set<Integer> USERS_ID = new HashSet<>();

    public TestHelper() {
        initPropertiesLoader();
        initDb();
        fillSetOfUsersId();
        clearData();
        generateData();
        Messages.setBot(getTestBot());
    }

    private void initPropertiesLoader() {
        propertiesLoader = new PropertiesLoader(Presets.TEST_PROPERTIES);
    }

    public Bot getTestBot() {
        return getTestBot(null);
    }

    public Bot getTestBot(StringBuilder appender) {
        Bot mock = Mockito.mock(Bot.class);
        try {
            doAnswer(invocation -> {
                logMessageText(appender, ((SendMessage)invocation.getArguments()[0]).getText());
                Message fakeAnswer = Mockito.mock(Message.class);
                when(fakeAnswer.getMessageId()).thenReturn(Presets.MESSAGE_ID);
                return fakeAnswer;
            }).when(mock).sendMessage(any(SendMessage.class));

            doAnswer(invocation -> {
                logMessageText(appender, ((EditMessageText)invocation.getArguments()[0]).getText());
                return null;
            }).when(mock).editMessageText(any(EditMessageText.class));
        } catch (TelegramApiException e) {
            LOGGER.error("Send message error from test bot");
        }
        return mock;
    }

    private void logMessageText(StringBuilder appender, String text) {
        if (appender == null) {
            LOGGER.info(text);
        } else {
            appender.append(text);
        }
    }

    public Registration getTestRegistration() {
        Registration mock = Mockito.mock(Registration.class);
        when(mock.getListOfMembersToString()).thenReturn("1. TestTeam 2. SecondTeam");
        return mock;
    }

    public PropertiesLoader getTestPropertiesLoader() {
        return propertiesLoader;
    }

    private void initDb() {
        DatabaseManager.setConnection(new ConnectionDB(TEST_DB_LINK));
        db = DatabaseManager.getInstance();
        ArenaUser.setDb(db);
        Item.setDb(db);
        Action.setDb(db);
        Team.setDb(db);
    }

    public StringBuilder initLogger() {
        StringBuilder log = new StringBuilder();
        Messages.setBot(getTestBot(log));
        return log;
    }
    private void fillSetOfUsersId() {
        USERS_ID.add(Presets.WARRIOR_ID);
        USERS_ID.add(Presets.MAGE_ID);
        USERS_ID.add(Presets.EXIST_USER_ID);
        USERS_ID.add(Presets.NON_EXIST_USER_ID);
    }

    private void generateData() {
        generateExistUser();
        generateWarrior();
        generateMage();
        fillSetOfUsersId();
    }

    private void generateExistUser() {
        db.addUser(Presets.EXIST_USER_ID, Presets.EXIST_USER_NAME);
        db.setStringTo(Config.USERS, Presets.EXIST_USER_ID, "class", Presets.WARRIOR_CLASS);
        db.addItem(Presets.EXIST_USER_ID, Presets.ITEM_ID);
        EXIST_USER = db.getUser(Presets.EXIST_USER_ID);
    }

    private void generateMage() {
        MAGE = ArenaUser.create(
                Presets.MAGE_ID,
                Presets.MAGE_NAME,
                UserClass.MAGE,
                Presets.MAGE_RACE);
        MAGE.setTeamId(Presets.TEAM_OF_MAGE);
        db.updateUser(MAGE);
    }

    private void generateWarrior() {
        WARRIOR = ArenaUser.create(
                Presets.WARRIOR_ID,
                Presets.WARRIOR_NAME,
                UserClass.WARRIOR,
                Presets.WARRIOR_RACE);
        WARRIOR.setTeamId(Presets.TEST_TEAM);
        db.updateUser(WARRIOR);
    }

    private void clearData() {
        dropActions();
        dropUsers();
        dropItems();
    }

    private void dropActions() {
        db.dropActions();
    }

    private void dropUsers() {
        for (Integer userId : USERS_ID) {
            if (db.doesUserExists(userId)) {
                ArenaUser.dropUser(userId);
            }
        }
    }

    private void dropItems() {
        for (Integer userId : USERS_ID) {
            if (!Item.getItems(userId).isEmpty()) {
                db.dropItems(userId);
            }
        }
    }


    public void close() {
        clearData();
        DatabaseManager.getConnection().closeConnection();
    }

    public DatabaseManager getDb() {
        return db;
    }

    public void setDb(DatabaseManager db) {
        this.db = db;
    }
}
