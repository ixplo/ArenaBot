package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoundTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);
    private static final int PERCENT = 85;

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = TestHelper.WARRIOR;
    private ArenaUser mage = TestHelper.MAGE;
    private Round round = testHelper.getTestRound();

    @Before
    public void setUp() throws Exception {

        Messages.setBot(testHelper.getTestBot());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCurMembersId() throws Exception {
        Assert.assertTrue(round.getCurMembersId().contains(Presets.WARRIOR_ID));
    }

    @Test
    public void getMembers() throws Exception {
        Assert.assertTrue(round.getMembers().stream().anyMatch(a -> a.getUserId().equals(Presets.MAGE_ID)));
    }

    @Test
    public void startRound() throws Exception {
        stopRoundInMillis(500);
        BattleState state = Round.execute(round.getBattleState());
        Assert.assertEquals(state, Round.getCurrent().getBattleState());
    }

    private void stopRoundInMillis(int millis) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Round.getCurrent().stop();
            }
        }, millis);
    }

    @Test
    public void botMock() throws TelegramApiException {
        Bot mock = mock(Bot.class);
        when(mock.sendMessage(any(SendMessage.class))).thenReturn(null);

        Message result = mock.sendMessage(new SendMessage());
        Assert.assertNull(result);
    }

    @Test
    public void takeAction() throws Exception {
        Action action1 = getMageActions().get(0);
        Action action2 = getMageActions().get(1);
        round.takeAction(action1);
        round.takeAction(action2);
        Assert.assertTrue(round.getOrders().get(1).getActions().get(0).equals(action1));
    }

    @Test
    public void actionToString() {
        Action action = getMageActions().get(0);
        Assert.assertTrue(action.toString().contains(String.valueOf(Presets.WARRIOR_ID)));
    }

    @Test
    public void getIndex() throws Exception {
        Assert.assertEquals(0, round.getIndex(warrior.getUserId()));
    }

    @Test(expected = ArenaUserException.class)
    public void getIndexInvalidUser() {
        round.getIndex(Presets.NON_EXIST_USER_ID);
    }

    @Test
    public void getMember() throws Exception {
        Assert.assertEquals(warrior, round.getMember(warrior.getUserId()));
    }

    @Test(expected = ArenaUserException.class)
    public void getMemberInvalidUser() {
        round.getMember(Presets.NON_EXIST_USER_ID);
    }

    @Test
    public void getAttackOnTargetList() throws Exception {
    }

    @Test
    public void priorityTest() {
        Thread round = new Thread(() -> Round.execute(this.round.getBattleState()));
        round.start();
        addOrdersTo(round);
        try {
            round.join();
        } catch (InterruptedException e) {
            LOGGER.error("Ошибка в priorityTest");
        }
    }

    @Test
    public void reminderTest() {
        StringBuilder log = new StringBuilder();
        Messages.setBot(testHelper.getTestBot(log));
        PropertiesLoader.setPropertiesLoader(testHelper.getTestPropertiesLoader());
        Thread round = new Thread(() -> Round.execute(this.round.getBattleState()));
        round.start();
        try {
            round.join();
        } catch (InterruptedException e) {
            LOGGER.error("Ошибка в reminderTest");
        }
        Assert.assertTrue(log.toString().contains(Messages.END_OF_ROUND_REMINDER));
    }

    @Test
    //todo add check that magic can apply to spellCaster
    public void toStringTest() {
        Assert.assertTrue(round.toString().contains("curTeams"));
        Order order = round.getOrders().get(0);
        Assert.assertTrue(order.toString().contains("test_team"));
        Assert.assertTrue(order.toString().contains("commonPercent=100"));
        order.addAction(Action.create(Presets.WARRIOR_ID, Action.MAGIC, Presets.WARRIOR_ID, 95, "1ma"));
        order.addAction(Action.create(Presets.WARRIOR_ID, Action.HEAL, Presets.WARRIOR_ID, 15));
        order.addAction(Action.create(Presets.WARRIOR_ID, Action.ATTACK, Presets.WARRIOR_ID, 15));
        Assert.assertTrue(order.toString().contains("5"));
    }

    private void addOrdersTo(Thread thread) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addWarriorOrder();
                addMageOrder();
                stopRoundInMillis(0);
                thread.interrupt();
            }
        }, 500);

    }

    private void addWarriorOrder() {
        List<Action> actions = getWarriorActions();
        Round.getCurrent().getOrders().get(0).getActions().addAll(actions);
    }

    private List<Action> getWarriorActions() {
        Action protect = Action.create(warrior.getUserId(), Action.PROTECT, warrior.getUserId(), 50);
        Action attack = Action.create(warrior.getUserId(), Action.ATTACK, mage.getUserId(), 25);
        Action heal = Action.create(warrior.getUserId(), Action.HEAL, warrior.getUserId(), 25);
        return Arrays.asList(protect, attack, heal);
    }

    private void addMageOrder() {
        List<Action> actions = getMageActions();
        Round.getCurrent().getOrders().get(1).getActions().addAll(actions);
    }

    private List<Action> getMageActions() {
        Action spell = Action.create(mage.getUserId(), Action.MAGIC, warrior.getUserId(), PERCENT, Presets.MAGIC_ARROW_SPELL_ID);
        Action protect = Action.create(mage.getUserId(), Action.PROTECT, mage.getUserId(), 5);
        Action attack = Action.create(mage.getUserId(), Action.ATTACK, warrior.getUserId(), 5);
        Action heal = Action.create(mage.getUserId(), Action.HEAL, mage.getUserId(), 5);
        return Arrays.asList(spell, protect, attack, heal);
    }


}