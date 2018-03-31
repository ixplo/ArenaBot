package ml.ixplo.arenabot.helper;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Battle;
import ml.ixplo.arenabot.battle.BattleState;
import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.battle.Round;
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ml.ixplo.arenabot.config.Config.TEST_DB_LINK;
import static org.mockito.ArgumentMatchers.any;

public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestHelper.class);
    private static final String USERCHATTYPE = "private";
    private static final String CHANNELCHATTYPE = "channel";

    private PropertiesLoader propertiesLoader;
    private DatabaseManager db;
    public ArenaUser WARRIOR;
    public ArenaUser MAGE;
    public ArenaUser ARCHER;
    public ArenaUser EXIST_USER;
    private Set<Integer> USERS_ID = new HashSet<>();

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

    public Bot getBadBot() throws TelegramApiException {
        Bot mock = Mockito.mock(Bot.class);
        Mockito.when(mock.sendMessage(any(SendMessage.class))).thenThrow(TelegramApiException.class);
        return mock;
    }

    public Bot getTestBot() {
        return getTestBot(null);
    }

    public Bot getTestBot(StringBuilder appender) {
        Bot mock = Mockito.mock(Bot.class);
        try {
            Mockito.doAnswer(invocation -> {
                logMessageText(appender, ((SendMessage)invocation.getArguments()[0]).getText());
                Message fakeAnswer = Mockito.mock(Message.class);
                Mockito.when(fakeAnswer.getMessageId()).thenReturn(Presets.MESSAGE_ID);
                return fakeAnswer;
            }).when(mock).sendMessage(ArgumentMatchers.any(SendMessage.class));

            Mockito.doAnswer(invocation -> {
                logMessageText(appender, ((EditMessageText)invocation.getArguments()[0]).getText());
                return null;
            }).when(mock).editMessageText(ArgumentMatchers.any(EditMessageText.class));
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
        Mockito.when(mock.getListOfMembersToString()).thenReturn("1. TestTeam 2. SecondTeam");
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

    public void initBattle() throws NoSuchFieldException, IllegalAccessException {
        Class<Battle> battleClass = Battle.class;
        Field battle = battleClass.getDeclaredField("battle");
        battle.setAccessible(true);
        battle.set(battleClass, Mockito.mock(Battle.class));
    }

    public Chat getChannel() throws NoSuchFieldException, IllegalAccessException {
        return getTyped(chatWithId(), CHANNELCHATTYPE);
    }

    public Chat getPrivate() throws NoSuchFieldException, IllegalAccessException {
        return getTyped(chatWithId(), USERCHATTYPE);
    }

    private Chat getTyped(Chat chat, String channelType) throws NoSuchFieldException, IllegalAccessException {
        Field type = chat.getClass().getDeclaredField("type");
        type.setAccessible(true);
        type.set(chat, channelType);
        return chat;
    }

    private Chat chatWithId() throws NoSuchFieldException, IllegalAccessException {
        Chat chat = new Chat();
        Field id = chat.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(chat, Presets.CHANNEL_ID);
        return chat;
    }

    public User getUser(int userId) throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        Field id = user.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(user, userId);
        return user;
    }

    public StringBuilder initLogger() {
        StringBuilder log = new StringBuilder();
        Messages.setBot(getTestBot(log));
        return log;
    }
    private void fillSetOfUsersId() {
        USERS_ID.add(Presets.WARRIOR_ID);
        USERS_ID.add(Presets.MAGE_ID);
        USERS_ID.add(Presets.ARCHER_ID);
        USERS_ID.add(Presets.EXIST_USER_ID);
        USERS_ID.add(Presets.NON_EXIST_USER_ID);
    }

    private void generateData() {
        generateExistUser();
        generateWarrior();
        generateMage();
        generateArcher();
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

    private void generateArcher() {
        ARCHER = ArenaUser.create(
                Presets.ARCHER_ID,
                Presets.ARCHER_NAME,
                UserClass.ARCHER,
                Presets.ARCHER_RACE);
        WARRIOR.setTeamId(Presets.TEST_TEAM);
        db.updateUser(ARCHER);
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
        if (DatabaseManager.getConnection().checkVersion() == Presets.TEST_DB_VERSION) {
            clearData();
        }
        DatabaseManager.getConnection().closeConnection();
    }

    public DatabaseManager db() {
        return db;
    }

    public Round createTestRound() {
        List<Integer> curMembersId;
        List<String> curTeamsId;
        List<ArenaUser> members;
        List<Team> teams;
        ArenaUser warrior = WARRIOR;
        ArenaUser mage = MAGE;

        curMembersId = new ArrayList<>();
        curMembersId.add(warrior.getUserId());
        curMembersId.add(mage.getUserId());

        curTeamsId = new ArrayList<>();
        curTeamsId.add(warrior.getTeamId());
        curTeamsId.add(mage.getTeamId());

        members = new ArrayList<>();
        members.add(warrior);
        members.add(mage);

        Team teamOne = Team.getTeam(warrior.getTeamId());
        teamOne.addMember(warrior);

        Team teamTwo = Team.getTeam(mage.getTeamId());
        teamTwo.addMember(mage);

        teams = new ArrayList<>();
        teams.add(teamOne);
        teams.add(teamTwo);

        BattleState battleState = new BattleState();
        battleState.setMembers(members);
        battleState.setCurMembersId(curMembersId);
        battleState.setTeams(teams);
        battleState.setCurTeamsId(curTeamsId);
        Round round = new Round(battleState);
        setCurrent(round);
        return round;
    }

    private void setCurrent(Round round) {
        try {
            Field current = Round.class.getDeclaredField("current");
            current.setAccessible(true);
            current.set(Round.class, round);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Error on reflection call");
        }
    }
}
