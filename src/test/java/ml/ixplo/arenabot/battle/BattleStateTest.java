package ml.ixplo.arenabot.battle;


import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.classes.UserClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleStateTest {
    private BattleState instance;
    private BattleState secondInstance;
    private List<Team> teams;
    private List<ArenaUser> members;
    private List<String> teamsId;
    private List<Integer> membersId;

    @Before
    public void setUp() {
        instance = new BattleState();
        secondInstance = new BattleState();
        teams = Arrays.asList(new Team(Presets.TEST_TEAM));
        members = Arrays.asList(ArenaUser.create(UserClass.ARCHER));
        teamsId = Arrays.asList(teams.get(0).getId());
        membersId = Arrays.asList(members.get(0).getUserId());

    }

    private void fill(BattleState instance) {
        instance.setTeams(teams);
        instance.setCurTeamsId(teamsId);
        instance.setMembers(members);
        instance.setCurMembersId(membersId);
    }

    @Test
    public void settersTest() {
        fill(instance);

        Assert.assertEquals(teams, instance.getTeams());
        Assert.assertEquals(teamsId, instance.getCurTeamsId());
        Assert.assertEquals(members, instance.getMembers());
        Assert.assertEquals(membersId, instance.getCurMembersId());
    }

    @Test
    public void equalsTest() {
        fill(instance);
        fill(secondInstance);

        Assert.assertEquals(instance, secondInstance);
        Assert.assertEquals(instance.hashCode(), secondInstance.hashCode());
    }

    @Test
    public void notEqualsMembersIdTest() {
        fill(instance);
        fill(secondInstance);

        instance.setCurMembersId(new ArrayList<>());
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setCurMembersId(new ArrayList<>());
        Assert.assertEquals(instance, secondInstance);
    }

    @Test
    public void notEqualsMembersTest() {
        fill(instance);
        fill(secondInstance);

        instance.setMembers(new ArrayList<>());
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setMembers(new ArrayList<>());
        Assert.assertEquals(instance, secondInstance);
    }

    @Test
    public void notEqualsTeamsTest() {
        fill(instance);
        fill(secondInstance);

        instance.setTeams(new ArrayList<>());
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setTeams(new ArrayList<>());
        Assert.assertEquals(instance, secondInstance);
    }


    @Test
    public void nullEqualsMembersIdTest() {
        fill(instance);
        fill(secondInstance);

        instance.setCurMembersId(null);
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setCurMembersId(null);
        Assert.assertEquals(instance, secondInstance);
    }

    @Test
    public void nullEqualsMembersTest() {
        fill(instance);
        fill(secondInstance);

        instance.setMembers(null);
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setMembers(null);
        Assert.assertEquals(instance, secondInstance);
    }

    @Test
    public void nullEqualsTeamsTest() {
        fill(instance);
        fill(secondInstance);

        instance.setTeams(null);
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setTeams(null);
        Assert.assertEquals(secondInstance, instance);
    }

    @Test
    public void notEqualsTeamsIdTest() {
        fill(instance);
        fill(secondInstance);

        instance.setCurTeamsId(new ArrayList<>());
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setCurTeamsId(new ArrayList<>());
        Assert.assertEquals(instance, secondInstance);
    }

    @Test
    public void nullEqualsTeamsIdTest() {
        fill(instance);
        fill(secondInstance);

        instance.setCurTeamsId(null);
        Assert.assertNotEquals(instance, secondInstance);

        secondInstance.setCurTeamsId(null);
        Assert.assertEquals(instance, secondInstance);
    }

    @Test
    public void nullEqualsTest() {
        fill(instance);

        Assert.assertNotEquals(instance, null);
        Assert.assertNotEquals(null, instance);
    }

    @Test
    public void linkEqualsTest() {
        fill(instance);
        BattleState copy = instance;
        Assert.assertEquals(instance, copy);
    }

    @Test
    public void differentClassEqualsTest() {
        fill(instance);
        Assert.assertNotEquals(instance, "String");
    }

    @Test
    public void hashCodeTest() {
        fill(instance);
        int hashCode1 = instance.hashCode();
        instance.setCurTeamsId(null);
        int hashCode2 = instance.hashCode();
        instance.setTeams(null);
        int hashCode3 = instance.hashCode();
        instance.setCurMembersId(null);
        int hashCode4 = instance.hashCode();
        instance.setMembers(null);
        int hashCode5 = instance.hashCode();
        Assert.assertTrue(0 == hashCode5);
    }
}