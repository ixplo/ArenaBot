package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * ixplo
 * 29.04.2017.
 */
public class Battle extends Thread {//состоит из Rounds, в конце выводится Result
    private List<Round> rounds = new ArrayList<>();
    private List<ArenaUser> members = new ArrayList<>();
    private List<Integer> curMembersId = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private List<String> curTeamsId = new ArrayList<>();
    private BattleResult battleResult;
    private static Battle battle;

    public static Battle getBattle() {
        return battle;
    }

    Battle(List<Team> teams, List<ArenaUser> members) {
        this.members = members;
        this.teams = teams;
        int size = members.size();
        for (int i = 0; i < size; i++) {
            curMembersId.add(members.get(i).getUserId());
            members.get(i).setStatus(2);
        }
        int teamsSize = teams.size();
        for (int i = 0; i < teamsSize; i++) {
            curTeamsId.add(teams.get(i).getId());
        }
        battle = this;
        start();
    }

    public void run() {
        Thread.currentThread().setName("Battle");
        Messages.sendChannelMsg(PropertiesLoader.getChannelId(), "<a href=\"" + Config.BOT_PRIVATE + "\">БИТВА НАЧАЛАСЬ!</a>");
        int roundsCounter = 0;
        while (!isAlone()) {
            Messages.sendToAll(members, "Раунд " + ++roundsCounter);
            Round round = new Round(curMembersId, curTeamsId, members, teams);
            round.startRound();
        }
        BattleResult battleResult = new BattleResult();
        battleResult.resultBattle(curMembersId, curTeamsId, members, teams);
    }

    private boolean isAlone() {
        return curTeamsId.size() < 2;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public BattleResult getBattleResult() {
        return battleResult;
    }

    public void setBattleResult(BattleResult battleResult) {
        this.battleResult = battleResult;
    }

    public void setMembers(List<ArenaUser> members) {
        this.members = members;
    }

    public List<ArenaUser> getMembers() {
        return members;
    }
}
