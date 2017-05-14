package arenabot.battle;

import arenabot.users.ArenaUser;
import arenabot.Config;

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

    private List<Member> members = new ArrayList<>();

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member) {
        members.add(member);
        member.teamId = id;
    }

    static public void refreshTeamsId(List<ArenaUser> members, List<Integer> curMembersId, List<String> teamsId){
        teamsId.clear();
        for (ArenaUser user: members){
            if (curMembersId.contains(user.getUserId())) {
                teamsId.add(user.getTeamName());
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

    static boolean addTeam(String id) {
        Team team = new Team(id);
        return Registration.db.setTeam(team);
    }

    boolean addTeam() {
        return Registration.db.setTeam(this);
    }

    public static boolean isRegisteredTeam(String teamId) {
        return Registration.db.getIntFrom(Config.TEAMS, teamId, "registered") > 0;
    }

    public List<String> getRegisteredMembersName() {//работает только для регистрации
        return Registration.db.getStringsBy(Config.USERS, "name", "team", name, "status", 1);
    }

    public List<Integer> getBattleMembersId() {//работает только для боя
        return Registration.db.getIntsBy(Config.USERS, "id", "team", name, "status", 2);
    }
}
