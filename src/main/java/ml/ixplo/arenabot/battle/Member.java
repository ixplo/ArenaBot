package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.ArenaUser;

/**
 * ixplo
 * 29.04.2017.
 */
public class Member {
    Integer userId;
    String name;
    String teamId;
    private int status;

    public Integer getUserId() {
        return userId;
    }

    public String getTeamId() {
        return teamId;
    }

    public int getStatus() {
        return ArenaUser.getStatus(userId);
    }

    public Member(int userId) {
        this.userId = userId;
        name = ArenaUser.getUserName(userId);
    }

    public Member(int userId, String teamId) {
        this.userId = userId;
        name = ArenaUser.getUserName(userId);
        this.teamId = teamId;
        status = ArenaUser.getStatus(userId);
    }

    public static void addMember(int userId, String teamId) {
        Registration.db.setIntTo(Config.USERS, userId, "status", Config.REG);
        Registration.db.setStringTo(Config.USERS, userId, "team", teamId);
    }

    public static void removeMember(int userId) {
        Registration.db.setIntTo(Config.USERS, userId, "status", Config.UNREG);
        if (Team.getTeam(getMember(userId).getTeamId()).isRegisteredTeam())
            Registration.db.setStringTo(Config.USERS, userId, "team", "");
    }

    public static Member getMember(int userId) {
        String teamId = Registration.db.getStringFrom(Config.USERS, userId, "team");
        return new Member(userId, teamId);
    }

    public String getName() {
        return name;
    }

    //todo new Thread
}
