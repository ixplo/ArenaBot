package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.user.ArenaUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class BattleTest extends BaseTest{
    private List<Team> teams = testHelper.getTestRound().getTeams();
    private List<ArenaUser> members = (List<ArenaUser>) testHelper.getTestRound().getMembers();
    private Battle battle;
    @Before
    public void setUp() {
        battle = new Battle(teams, members);
    }

    @After
    public void tearDown() {
        try {
            Class<Battle> battleClass = Battle.class;
            Field battleField = battleClass.getDeclaredField("battle");
            battleField.setAccessible(true);
            battleField.set(battleClass, null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Reflection error");
        }
    }

    @Test
    public void getBattle() {
        Assert.assertEquals(battle, Battle.getBattle());
    }

    @Test
    public void run() throws InterruptedException {
        battle.getCurTeamsId().remove(0);
        Round.getCurrent().stop();
        battle.interrupt();
        battle.join();
    }

    @Test
    public void getTeams() {
        Assert.assertEquals(teams, battle.getTeams());
    }

}