package ml.ixplo.arenabot.battle;


import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.IUser;
import ml.ixplo.arenabot.user.classes.UserClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BattleStateTest {

    @Test
    public void instanceTest() {
        BattleState instance = new BattleState();

        List<Team> teams = Arrays.asList(new Team(Presets.TEST_TEAM));
        List<ArenaUser> members = Arrays.asList(ArenaUser.create(UserClass.ARCHER));
        List<String> teamsId = Arrays.asList(teams.get(0).getId());
        List<Integer> membersId = Arrays.asList(members.get(0).getUserId());
        instance.setTeams(teams);
        instance.setCurTeamsId(teamsId);
        instance.setMembers(members);
        instance.setCurMembersId(membersId);


        BattleState secondInstance = new BattleState();
        secondInstance.setTeams(teams);
        secondInstance.setCurTeamsId(teamsId);
        secondInstance.setMembers(members);
        secondInstance.setCurMembersId(membersId);
        Assert.assertEquals(instance, secondInstance);
        Assert.assertEquals(instance.hashCode(), secondInstance.hashCode());

        Assert.assertEquals(teams, instance.getTeams());
        Assert.assertEquals(teamsId, instance.getCurTeamsId());
        Assert.assertEquals(members, instance.getMembers());
        Assert.assertEquals(membersId, instance.getCurMembersId());

    }
}