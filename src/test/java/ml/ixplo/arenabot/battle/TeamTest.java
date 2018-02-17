package ml.ixplo.arenabot.battle;


import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TeamTest {

    private TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() {
        Team.setDb(testHelper.getDb());
    }

    @Test
    public void getTeam() {
        Team team = Team.getTeam(Presets.TEST_TEAM);
        team.setRegistered(true);
        assertEquals(Presets.TEST_TEAM, team.getId());
        assertTrue(team.isRegistered());
    }

    @Test
    public void getTeamNotRegistered() {
        Team team = Team.getTeam(Presets.WRONG_TEAM_ID);
        assertEquals(Presets.WRONG_TEAM_ID, team.getId());
        assertFalse(team.isRegistered());
        team.drop();
    }
}