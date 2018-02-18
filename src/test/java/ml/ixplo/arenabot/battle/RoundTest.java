package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
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
    private Round round;
    private List<Integer> curMembersId;
    private List<String> curTeamsId;
    private List<ArenaUser> members;
    private List<Team> teams;

    @Before
    public void setUp() throws Exception {
        curMembersId = new ArrayList<>();
        curMembersId.add(warrior.getUserId());
        curTeamsId = new ArrayList<>();
        curTeamsId.add(warrior.getTeamId());
        members = new ArrayList<>();
        members.add(warrior);
        teams = new ArrayList<>();
        teams.add(new Team(warrior.getTeamId()));
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
//    @Ignore("доделать мок бота. Не мокаются final методы")
    public void startRound() throws Exception {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Round.getCurrent().stop();
            }
        }, 500);
        BattleState state = Round.execute(round.getBattleState());
        Assert.assertEquals(state, Round.getCurrent().getBattleState());
    }

    @Test
    public void botMock() throws TelegramApiException {
        //arrange
        Bot i = mock(Bot.class);
        when(i.sendMessage(any(SendMessage.class))).thenReturn(null);

        //act
        Message result = i.sendMessage(new SendMessage());
        //assert
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
    public void getActionsByTarget() throws Exception {
    }

    @Test
    public void getAttackOnTargetList() throws Exception {
    }

    @Test
    public void getOrders() throws Exception {
    }
}