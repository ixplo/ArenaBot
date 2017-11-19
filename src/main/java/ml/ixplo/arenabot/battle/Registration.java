package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;

import java.util.Timer;
import java.util.ArrayList;
import java.util.List;

/**
 * ixplo
 * 29.04.2017.
 */
public class Registration {
    public static boolean isOn;
    private static Timer regTimer;
    private static RegTimerTask regTimerTask;
    private List<Member> members;
    private List<Team> teams;

    static DatabaseManager db;

    public static void setDb(DatabaseManager dbManager) {
        db = dbManager;
    }

    public Registration() {
        isOn = true;
        members = new ArrayList<>();
        teams = new ArrayList<>();
    }

    public static void dropStatus() {
        db.dropStatus();
    }

    public void startBattle() {
        if (!isOn) return;
        new Battle(getTeams(), ArenaUser.getUsers(getMembersId()));
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
            teamId = addNextUnregistered();
        }
        Member.addMember(userId, teamId);
        if (getTeamsCount() > 1) {
            if (regTimer != null) {
                regTimer.cancel();
            }
            regTimer = new Timer();
            regTimerTask = new RegTimerTask(this, regTimer, Config.DELAY);
            regTimer.schedule(regTimerTask, 0, 1000);
        }
    }

    public void unregMember(Integer userId) {
        Member.removeMember(userId);
        if (!Team.isRegisteredTeam(ArenaUser.getUserTeam(userId))) {
            ArenaUser.getUser(userId).setTeam("");
        }
        if (regTimer != null) {
            Messages.editChannelMsg(Config.CHANNEL_ID, regTimerTask.getMessageId(), "Таймер остановлен");
            regTimer.cancel();
        }
    }

    private String regIfTeamRegistered(Integer userId) {
        //todo проверка на то, есть ли команда в teams
        //todo если нет в списке, то newRegisteredTeam();
        //todo regMember(userId);
        return ArenaUser.getUserTeam(userId);
    }

    private String addNextUnregistered() {
        String nextId;
        int i = 0;
        do {
            nextId = "ком." + (getTeamsCount() + ++i);
        } while (getTeamsName().contains(nextId));
        Team.addTeam(nextId);
        return nextId;
    }

    public int getTeamsCount() {
        return db.getCountDistinct(Config.USERS, "team", "status", Config.REG);
    }

    public List<String> getTeamsName() {
        return db.getStrings(Config.USERS, "status", Config.REG, "team");
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

    public String getList() {
        StringBuilder msgText = new StringBuilder();
        int teamsCount = getTeamsCount();
        msgText.append("Команды:");
        List<String> teams = getTeamsName();
        for (int i = 0; i < teamsCount; i++) {
            msgText.append("\n").append(i + 1).append(".[").append(teams.get(i)).append("] ");
            List<String> teamMembers = Team.getTeam(teams.get(i)).getRegisteredMembersName();
            int teamMembersCount = teamMembers.size();
            for (int j = 0; j < teamMembersCount; j++) {
                msgText.append(j == 0 ? "" : ", ")
                        .append(teamMembers.get(j));
            }
        }
        return msgText.toString();
    }

    private List<Integer> getMembersId() {
        return db.getInts(Config.USERS, "status", Config.REG, "id");
    }

    public Member getLastMember() {
        int lastCount = members.size() - 1;
        return members.get(lastCount);
    }

    public int getMemberStatus(Integer userId) {
        return getMember(userId).getStatus();
    }

    public List<String> getMembersName() {
        return db.getStrings(Config.USERS, "status", Config.REG, "name");
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
