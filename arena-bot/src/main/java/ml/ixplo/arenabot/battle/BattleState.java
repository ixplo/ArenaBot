package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.user.ArenaUser;

import java.util.ArrayList;
import java.util.List;

public class BattleState {
    private static final int HASH_CONST = 31;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BattleState that = (BattleState) obj;

        if (members != null ? !members.equals(that.members) : that.members != null) {
            return false;
        }
        if (curMembersId != null ? !curMembersId.equals(that.curMembersId) : that.curMembersId != null) {
            return false;
        }
        if (teams != null ? !teams.equals(that.teams) : that.teams != null) {
            return false;
        }
        return curTeamsId != null ? curTeamsId.equals(that.curTeamsId) : that.curTeamsId == null;
    }

    @Override
    public int hashCode() {
        int result = members != null ? members.hashCode() : 0;
        result = HASH_CONST * result + (curMembersId != null ? curMembersId.hashCode() : 0);
        result = HASH_CONST * result + (teams != null ? teams.hashCode() : 0);
        result = HASH_CONST * result + (curTeamsId != null ? curTeamsId.hashCode() : 0);
        return result;
    }
}
