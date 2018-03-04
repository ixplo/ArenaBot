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

    private void fillInstance(BattleState instance) {
        instance.setTeams(teams);
        instance.setCurTeamsId(teamsId);
        instance.setMembers(members);
        instance.setCurMembersId(membersId);
    }

    @Test
    public void settersTest() {
        fillInstance(instance);

        Assert.assertEquals(teams, instance.getTeams());
        Assert.assertEquals(teamsId, instance.getCurTeamsId());
        Assert.assertEquals(members, instance.getMembers());
        Assert.assertEquals(membersId, instance.getCurMembersId());
    }

    @Test
    public void equalsTest() {
        fillInstance(instance);
        fillInstance(secondInstance);

        Assert.assertEquals(instance, secondInstance);
        Assert.assertEquals(instance.hashCode(), secondInstance.hashCode());
    }

    @Test
    public void notEqualsMembersIdTest() {
        fillInstance(instance);
        fillInstance(secondInstance);
        secondInstance.setCurMembersId(new ArrayList<>());

        Assert.assertNotEquals(instance, secondInstance);
        Assert.assertNotEquals(secondInstance, instance);
    }

    @Test
    public void notEqualsMembersTest() {
        fillInstance(instance);
        fillInstance(secondInstance);
        secondInstance.setMembers(new ArrayList<>());

        Assert.assertNotEquals(instance, secondInstance);
        Assert.assertNotEquals(secondInstance, instance);
    }

    @Test
    public void notEqualsTeamsTest() {
        fillInstance(instance);
        fillInstance(secondInstance);
        secondInstance.setTeams(new ArrayList<>());

        Assert.assertNotEquals(instance, secondInstance);
        Assert.assertNotEquals(secondInstance, instance);
    }


    @Test
    public void nullEqualsMembersIdTest() {
        fillInstance(instance);
        fillInstance(secondInstance);
        instance.setCurMembersId(null);

        Assert.assertNotEquals(instance, secondInstance);
        Assert.assertNotEquals(secondInstance, instance);
    }

    @Test
    public void nullEqualsMembersTest() {
        fillInstance(instance);
        fillInstance(secondInstance);
        instance.setMembers(null);

        Assert.assertNotEquals(instance, secondInstance);
        Assert.assertNotEquals(secondInstance, instance);
    }

    @Test
    public void nullEqualsTeamsTest() {
        fillInstance(instance);
        fillInstance(secondInstance);
        instance.setTeams(null);

        Assert.assertNotEquals(instance, secondInstance);
        Assert.assertNotEquals(secondInstance, instance);
    }

    @Test
    public void notEqualsTeamsIdTest() {
        fillInstance(instance);
        fillInstance(secondInstance);
        secondInstance.setCurTeamsId(new ArrayList<>());

        Assert.assertNotEquals(instance, secondInstance);
        Assert.assertNotEquals(secondInstance, instance);
    }

    @Test
    public void nullEqualsTest() {
        fillInstance(instance);

        Assert.assertNotEquals(instance, null);
        Assert.assertNotEquals(null, instance);
    }

    @Test
    public void linkEqualsTest() {
        fillInstance(instance);
        BattleState copy = instance;
        Assert.assertEquals(instance, copy);
    }

    @Test
    public void differentClassEqualsTest() {
        fillInstance(instance);
        Assert.assertNotEquals(instance, "String");
    }

    @Test
    public void hashCodeTest() {
        fillInstance(instance);
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