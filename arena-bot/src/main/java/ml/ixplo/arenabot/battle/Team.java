package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.user.IUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ixplo
 * 29.04.2017.
 */
public class Team {
    private static DatabaseManager db;
    private String id;
    private String name;
    private boolean isRegistered;
    private boolean isPublic;
    private int games;
    private int wins;
    private String descr;
    private String htmlName;

    private List<IUser> members = new ArrayList<>();

    public Team(String id) {
        this.id = id;
        setName(id);
    }

    public static void setDb(DatabaseManager db) {
        Team.db = db;
    }

    public static String getTeamId(Integer userId) {
        return getMember(userId).getTeamId();
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        db.setIntTo(Config.TEAMS, id, Config.REGISTERED, registered ? 1 : 0);
        isRegistered = registered;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getHtmlName() {
        return htmlName;
    }

    public void setHtmlName(String htmlName) {
        this.htmlName = htmlName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IUser> getMembers() {
        return members;
    }

    public void setMembers(List<IUser> members) {
        this.members = members;
    }

    public void addMember(IUser member) {
        members.add(member);
        member.setTeamId(id);
    }

    public static void addMember(int userId, String teamId) {
        db.setIntTo(Config.USERS, userId, Config.STATUS, Config.REGISTERED_STATUS);
        db.setStringTo(Config.USERS, userId, DatabaseManager.TEAM_COLUMN, teamId);
    }

    /**
     * Get Set of teams. Get from members
     * @param allMembers - all members from battle begin
     * @param curMembersId - current live members
     * @return - Set of Strings - ids
     */
    public static Set<String> getTeamsId(List<? extends IUser> allMembers, List<Integer> curMembersId) {
        Set<String> teamsId = new HashSet<>();
        for (IUser member : allMembers) {
            if (curMembersId.contains(member.getUserId())) {
                teamsId.add(member.getTeamId());
            }
        }
        return teamsId;
    }

    static void addTeam(String id) {
        Team team = new Team(id);
        db.setTeam(team);
    }

    /**
     * Get info from db: 0 - unregistered
     *                   1 - registered
     * @return true if registered
     */
    public boolean isRegisteredTeam() {
        return db.getIntFrom(Config.TEAMS, id, "registered") > 0;
    }

    public static boolean isRegisteredTeam(String teamId) {
        return db.getIntFrom(Config.TEAMS, teamId, "registered") > 0;
    }

    /**
     * Get Team from db
     * @param id - team id
     * @return Team with param id
     */
    public static Team getTeam(String id) {
        Team team = db.getTeam(id);
        if (team == null) {
            addTeam(id);
            return new Team(id);
        }
        return team;
    }

    /**
     * Set unregistered status to user with userId and set user team column to null
     * @param userId - id of removed user
     */
    public static void removeMember(int userId) {
        db.setIntTo(Config.USERS, userId, Config.STATUS, Config.UNREGISTERED_STATUS);
        if (Team.getTeam(getMember(userId).getTeamId()).isRegisteredTeam()) {
            db.setStringTo(Config.USERS, userId, DatabaseManager.TEAM_COLUMN, null);
        }
    }

    public static Member getMember(int userId) {
        String teamId = db.getStringFrom(Config.USERS, userId, "team");
        return new Member(userId, teamId);
    }

    /**
     * работает только для регистрации
     *
     * @return список всех участников, которые зарегистрировались
     */
    public List<String> getRegisteredMembersName() {
        return db.getStringsBy(Config.USERS, "name", "team", name, Config.STATUS, 1);
    }

    /**
     * работает только для боя
     *
     * @return список всех участников, которые находятся в бою
     */
    public List<Integer> getBattleMembersId() {
        return db.getIntsBy(Config.USERS, "id", "team", name, Config.STATUS, 2);
    }

    public void drop() {
        db.dropTeam(id);
    }

}
