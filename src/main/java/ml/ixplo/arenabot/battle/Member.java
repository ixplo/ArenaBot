package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.IUser;

/**
 * Class for member of registration/battle
 *
 */
public class Member implements IUser{
    private Integer userId;
    private String name;
    private String teamId;

    public Integer getUserId() {
        return userId;
    }

    public String getTeamId() {
        return teamId;
    }

    @Override
    public void setTeamId(String id) {
        teamId = id;
    }

    public int getStatus() {
        return ArenaUser.getStatus(userId);
    }

    public Member(int userId, String teamId) {
        this.userId = userId;
        name = ArenaUser.getUserName(userId);
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

}
