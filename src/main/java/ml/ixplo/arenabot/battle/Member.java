package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.IUser;

/**
 * Class for member of registration/battle
 *
 */
public class Member implements IUser{
    //todo переделать все на геттеры в ArenaUser и сделать поля приватными
    protected static DatabaseManager db;
    protected Integer userId;
    protected String name;
    private String teamId;
    private String teamRank;

    public Member() {
    }

    public static DatabaseManager getDb() {
        return db;
    }

    public static void setDb(DatabaseManager dbManager) {
        db = dbManager;
    }

    public static boolean doesUserExists(Integer userId) {
        return db.doesUserExists(userId);
    }

    public static String getUserTeamId(Integer userId) {
        return db.getStringFrom(Config.USERS, userId, DatabaseManager.TEAM_COLUMN);
    }

    public static String getUserName(Integer userId) {
        return db.getStringFrom(Config.USERS, userId, "name");
    }

    public static int getStatus(int userId) {
        return db.getIntFrom(Config.USERS, userId, Config.STATUS);
    }

    public void setStatus(int status) {
        db.setIntTo(Config.USERS, userId, Config.STATUS, status);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTeamId() {
        return teamId;
    }

    @Override
    public void setTeamId(String teamId) {
        this.teamId = teamId;
        db.setStringTo(Config.USERS, userId, DatabaseManager.TEAM_COLUMN, teamId);
    }

    public int getStatus() {
        return getStatus(userId);
    }

    public Member(int userId, String teamId) {
        this.userId = userId;
        name = ArenaUser.getUserName(userId);
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        db.setStringTo(Config.USERS, userId, "name", name);
    }

    public String getTeamRank() {
        return teamRank;
    }

    public void setTeamRank(String teamRank) {
        this.teamRank = teamRank;
    }

}
