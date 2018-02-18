package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoundTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = TestHelper.WARRIOR;
    private ArenaUser mage = TestHelper.MAGE;
    private Round round;
    private List<Integer> curMembersId;
    private List<String> curTeamsId;
    private List<ArenaUser> members;
    private List<Team> teams;

    @Before
    public void setUp() throws Exception {
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
        round = new Round(battleState);

        Messages.setBot(testHelper.getTestBot());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCurMembersId() throws Exception {
        Assert.assertEquals(curMembersId, round.getCurMembersId());
    }

    @Test
    public void getMembers() throws Exception {
        Assert.assertEquals(members, round.getMembers());
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
    }

    @Test
    public void getIndex() throws Exception {
        Assert.assertEquals(0, round.getIndex(warrior.getUserId()));
    }

    @Test
    public void getMember() throws Exception {
        Assert.assertEquals(warrior, round.getMember(warrior.getUserId()));
    }

    @Test
    public void getAttackOnTargetList() throws Exception {
    }

    @Test
    public void priorityTest() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Round.execute(round.getBattleState());
            }
        });
        thread.start();
        addOrders(thread);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addOrders(Thread thread) {
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
        Action protect = Action.create(warrior.getUserId(), Action.PROTECT, warrior.getUserId(), 50);
        Action attack = Action.create(warrior.getUserId(), Action.ATTACK, mage.getUserId(), 25);
        Action heal = Action.create(warrior.getUserId(), Action.HEAL, warrior.getUserId(), 25);
        Round.getCurrent().getOrders().get(0).getActions().addAll(Arrays.asList(protect, attack, heal));
    }

    private void addMageOrder() {
        Action spell = Action.create(mage.getUserId(), Action.MAGIC, warrior.getUserId(), 85, Presets.MAGIC_ARROW_SPELL_ID);
        Action protect = Action.create(mage.getUserId(), Action.PROTECT, mage.getUserId(), 5);
        Action attack = Action.create(mage.getUserId(), Action.ATTACK, warrior.getUserId(), 5);
        Action heal = Action.create(mage.getUserId(), Action.HEAL, mage.getUserId(), 5);
        Round.getCurrent().getOrders().get(1).getActions().addAll(Arrays.asList(spell, protect, attack, heal));
    }

    @Test
    public void getOrders() throws Exception {
    }
}