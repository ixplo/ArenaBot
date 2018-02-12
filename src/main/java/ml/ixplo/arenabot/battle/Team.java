package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * ixplo
 * 29.04.2017.
 */
public class Team {
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

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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

    public static void refreshTeamsId(List<? extends IUser> members, List<Integer> curMembersId, List<String> teamsId){
        teamsId.clear();
        for (IUser member: members){
            if (curMembersId.contains(member.getUserId())) {
                teamsId.add(member.getTeamId());
            }
        }
    }

    public boolean isRegisteredTeam() {
        return Registration.db.getIntFrom(Config.TEAMS, id, "registered") > 0;
    }

    public static Team getTeam(String id) {
        if (Registration.db.getTeam(id) == null) {
            addTeam(id);
            return new Team(id);
        }
        return Registration.db.getTeam(id);
    }

    static void addTeam(String id) {
        Team team = new Team(id);
        Registration.db.setTeam(team);
    }

    boolean addTeam() {
        return Registration.db.setTeam(this);
    }

    public static boolean isRegisteredTeam(String teamId) {
        return Registration.db.getIntFrom(Config.TEAMS, teamId, "registered") > 0;
    }

    public static void addMember(int userId, String teamId) {
        Registration.db.setIntTo(Config.USERS, userId, Config.STATUS, Config.REG);
        Registration.db.setStringTo(Config.USERS, userId, "team", teamId);
    }

    public static void removeMember(int userId) {
        Registration.db.setIntTo(Config.USERS, userId, Config.STATUS, Config.UNREG);
        if (Team.getTeam(getMember(userId).getTeamId()).isRegisteredTeam())
            Registration.db.setStringTo(Config.USERS, userId, "team", "");
    }

    public static Member getMember(int userId) {
        String teamId = Registration.db.getStringFrom(Config.USERS, userId, "team");
        return new Member(userId, teamId);
    }

    /**
     * работает только для регистрации
     * @return список всех участников, которые зарегистрировались
     */
    public List<String> getRegisteredMembersName() {
        return Registration.db.getStringsBy(Config.USERS, "name", "team", name, Config.STATUS, 1);
    }

    /**
     * работает только для боя
     * @return список всех участников, которые находятся в бою
     */
    public List<Integer> getBattleMembersId() {
        return Registration.db.getIntsBy(Config.USERS, "id", "team", name, Config.STATUS, 2);
    }
}
