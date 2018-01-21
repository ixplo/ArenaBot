package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RoundTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = TestHelper.WARRIOR;
    private Round round;
    private List<Integer> curMembersId;
    private List<String> curTeamsId;
    private List<ArenaUser> members;
    private List<Team> teams;
    private Battle battle;
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
        battle = new Battle(teams, members);
        round = new Round(curMembersId, curTeamsId, members, teams, battle);
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

    @Test
    public void getCurrent() throws Exception {
        Assert.assertEquals(round, Round.getCurrent());
    }

}