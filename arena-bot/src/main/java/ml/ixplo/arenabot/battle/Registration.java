package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.user.IUser;

import java.util.Timer;
import java.util.ArrayList;
import java.util.List;

/**
 * Register members and start Battle
 */
public class Registration {
    private static final int SECOND = 1000;
    private static DatabaseManager db;
    private boolean isOn;
    private Timer regTimer;
    private RegTimerTask regTimerTask;

    public static void setDb(DatabaseManager dbManager) {
        db = dbManager;
    }

    public Registration() {
        isOn = true;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    public static void dropStatus() {
        db.dropStatus();
    }

    /**
     * start battle and stop registration
     */
    public void startBattle() {
        if (!isOn) {
            return;
        }
        new Battle(getTeams(), ArenaUser.getUsers(getRegisteredMembersId()));
        isOn = false;
    }

    /**
     * Get all current teams
     *
     * @return List of teams
     */
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        List<String> teamsId = getTeamsId();
        for (int i = 0; i < getTeamsCount(); i++) {
            teams.add(getTeam(teamsId.get(i)));
        }
        return teams;
    }

    public void regMember(Integer userId) {
        addMemberToTeam(userId);
        refreshRegistrationTimer();
    }

    public void unregMember(Integer userId) {
        removeMemberFromTeam(userId);
        stopTimer();
    }

    private void addMemberToTeam(Integer userId) {
        Team.addMember(userId, getTeamIdOrCreate(userId));
    }

    private String getTeamIdOrCreate(Integer userId) {
        if (Team.isRegisteredTeam(ArenaUser.getUserTeamId(userId))) {
            return regIfTeamRegistered(userId);
        } else {
            return addNextUnregistered();
        }
    }

    private void removeMemberFromTeam(Integer userId) {
        Team.removeMember(userId);
        if (!Team.isRegisteredTeam(ArenaUser.getUserTeamId(userId))) {
            ArenaUser.getUser(userId).setTeamId("");
        }
    }

    private void refreshRegistrationTimer() {
        if (getTeamsCount() > 1) {
            if (regTimer != null) {
                regTimer.cancel();
            }
            regTimer = new Timer();
            regTimerTask = new RegTimerTask(this, regTimer, Config.DELAY_IN_SECONDS);
            regTimer.schedule(regTimerTask, 0, SECOND);
        }
    }

    private void stopTimer() {
        if (regTimer != null) {
            Messages.editChannelMsg(PropertiesLoader.getInstance().getChannelId(), regTimerTask.getMessageId(), "Таймер остановлен");
            regTimer.cancel();
        }
    }

    private String regIfTeamRegistered(Integer userId) {
        //todo проверка на то, есть ли команда в teams, если нет в списке, то newRegisteredTeam() и regMember(userId)
        return ArenaUser.getUserTeamId(userId);
    }

    private String addNextUnregistered() {
        String nextId;
        int counter = 0;
        do {
            nextId = "ком." + (getTeamsCount() + ++counter);
        }
        while (getTeamsId().contains(nextId));
        Team.addTeam(nextId);
        return nextId;
    }

    private int getTeamsCount() {
        return db.getCountDistinct(Config.USERS, DatabaseManager.TEAM_COLUMN, Config.STATUS, Config.REGISTERED_STATUS);
    }

    private List<String> getTeamsId() {
        return db.getStrings(Config.USERS, Config.STATUS, Config.REGISTERED_STATUS, DatabaseManager.TEAM_COLUMN);
    }

    /**
     * Get all members
     *
     * @return List of members
     */
    public List<Member> getMembers() {
        List<Member> membersList = new ArrayList<>();
        List<Integer> membersId = getRegisteredMembersId();
        for (int i = 0; i < getMembersCount(); i++) {
            membersList.add(getMember(membersId.get(i)));
        }
        return membersList;
    }

    public int getMembersCount() {
        return db.getCountDistinct(Config.USERS, DatabaseManager.ID, Config.STATUS, Config.REGISTERED_STATUS);
    }

    /**
     * 1.[Team1] 2.[Team2]
     *
     * @return String for List Message
     */
    public String getListOfMembersToString() {
        StringBuilder msgText = new StringBuilder();
        int teamsCount = getTeamsCount();
        msgText.append("Команды:");
        List<String> teams = getTeamsId();
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

    private Member getMember(Integer userId) {
        return Team.getMember(userId);
    }

    private List<Integer> getRegisteredMembersId() {
        return db.getInts(Config.USERS, Config.STATUS, Config.REGISTERED_STATUS, DatabaseManager.ID);
    }

    private Team getTeam(String id) {
        Team team = Team.getTeam(id);
        List<IUser> teamMembers = new ArrayList<>();
        List<Member> allMembers = getMembers();
        int allMembersCount = getMembersCount();
        for (int i = 0; i < allMembersCount; i++) {
            if (allMembers.get(i).getTeamId().equals(id)) {
                teamMembers.add(allMembers.get(i));
            }
        }
        team.setMembers(teamMembers);
        return team;
    }
}
