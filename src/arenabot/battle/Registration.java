package arenabot.battle;

import arenabot.users.ArenaUser;
import arenabot.Config;
import arenabot.database.DatabaseManager;

import java.util.Timer;
import java.util.ArrayList;
import java.util.List;

/**
 * ixplo
 * 29.04.2017.
 */
public class Registration {
    public static boolean isOn;
    List<Member> members;
    List<Team> teams;

    static DatabaseManager db;

    public static void setDb(DatabaseManager dbManager) {
        db = dbManager;
    }

    public Registration() {
        isOn = true;
        members = new ArrayList<>();
        teams = new ArrayList<>();
    }

    public static void dropStatus(){
        db.dropStatus();
    }

    public void startBattle() {
        if (!isOn) return;
        Battle battle = new Battle(getTeams(), ArenaUser.getUsers(getMembersId()));
        isOn = false;
    }

    private List<Team> getTeams() {
        int count = getTeamsCount();
        List<Team> teams = new ArrayList<>();
        List<String> teamsId = getTeamsName();//todo via teamsId
        for (int i = 0; i < count; i++) {
            teams.add(getTeam(teamsId.get(i)));
        }
        return teams;
    }

    public void regMember(Integer userId) {
        String teamId;
        if (Team.isRegisteredTeam(ArenaUser.getUserTeam(userId))) {
            teamId = regIfTeamRegistered(userId);
        } else {
            teamId = nextUnregistered();
        }
        Member.addMember(userId, teamId);
        if (getTeamsCount() > 1) {
            Timer timer = new Timer();
            RegTimer regTimer = new RegTimer(this, Config.DELAY);
            timer.schedule(regTimer, Config.DELAY);
        }
    }

    public void unregMember(Integer id) {
        Member.removeMember(id);
    }

    private String regIfTeamRegistered(Integer userId) {
        //todo проверка на то, есть ли команда в teams
        //todo если нет в списке, то newRegisteredTeam();
        //todo regMember(userId);
        return ArenaUser.getUserTeam(userId);
    }

    private String nextUnregistered() {
        String nextId = "ком." + (getTeamsCount() + 1);
        Team.addTeam(nextId);
        return nextId;
    }

    public int getTeamsCount() {
        return db.getCountDistinct(Config.USERS, "team", "status", Config.REG);
    }

    public int getMembersCount() {
        return db.getCountDistinct(Config.USERS, "id", "status", Config.REG);
    }

    public String getMemberTeam(Integer userId) {
        return getMember(userId).teamId;
    }

    public Member getMember(Integer userId) {
        return Member.getMember(userId);
    }

    public List<Member> getMembers() {
        int count = getMembersCount();
        List<Member> members = new ArrayList<>();
        List<Integer> membersId = getMembersId();
        for (int i = 0; i < count; i++) {
            members.add(getMember(membersId.get(i)));
        }
        return members;
    }

    private List<Integer> getMembersId() {
        return db.getInts(Config.USERS, "status", 1, "id");
    }

    public Member getLastMember() {
        int lastCount = members.size() - 1;
        return members.get(lastCount);
    }

    public int getMemberStatus(Integer userId) {
        return getMember(userId).getStatus();
    }

    public List<String> getMembersName() {
        return db.getStrings(Config.USERS, "status", 1, "name");
    }

    public List<String> getTeamsName() {
        return db.getStrings(Config.USERS, "status", 1, "team");
    }

    public Team getTeam(String id) {
        Team team = db.getTeam(id);
        List<Member> teamMembers = new ArrayList<>();
        List<Member> allMembers = getMembers();
        int allMembersCount = getMembersCount();
        for (int i = 0; i < allMembersCount; i++) {
            if (allMembers.get(i).teamId.equals(id)) {
                teamMembers.add(allMembers.get(i));
            }
        }
        team.setMembers(teamMembers);
        return team;
    }
}
