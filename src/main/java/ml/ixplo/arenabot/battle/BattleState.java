package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.user.ArenaUser;

import java.util.ArrayList;
import java.util.List;

public class BattleState {
    private List<ArenaUser> members = new ArrayList<>();
    private List<Integer> curMembersId = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private List<String> curTeamsId = new ArrayList<>();

    public List<ArenaUser> getMembers() {
        return members;
    }

    public void setMembers(List<ArenaUser> members) {
        this.members = members;
    }

    public List<Integer> getCurMembersId() {
        return curMembersId;
    }

    public void setCurMembersId(List<Integer> curMembersId) {
        this.curMembersId = curMembersId;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<String> getCurTeamsId() {
        return curTeamsId;
    }

    public void setCurTeamsId(List<String> curTeamsId) {
        this.curTeamsId = curTeamsId;
    }
}
