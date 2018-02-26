package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;

import java.util.Collections;
import java.util.List;

/**
 * состоит из Rounds, в конце выводится Result
 * ixplo
 * 29.04.2017.
 */
public class Battle extends Thread {
    private BattleState battleState;
    private static Battle battle;

    public static Battle getBattle() {
        return battle;
    }

    Battle(List<Team> teams, List<ArenaUser> members) {
        battleState = new BattleState();
        battleState.setMembers(members);
        battleState.setTeams(teams);
        int size = members.size();
        for (int i = 0; i < size; i++) {
            battleState.getCurMembersId().add(members.get(i).getUserId());
            members.get(i).setStatus(2);
        }
        int teamsSize = teams.size();
        for (int i = 0; i < teamsSize; i++) {
            battleState.getCurTeamsId().add(teams.get(i).getId());
        }
        battle = this;
        start();
    }

    @Override
    public void run() {
        Messages.sendChannelMsg(PropertiesLoader.getInstance().getChannelId(), "<a href=\"" + Config.BOT_PRIVATE + "\">БИТВА НАЧАЛАСЬ!</a>");
        int roundsCounter = 0;
        while (severalTeamsPresent()) {
            Messages.sendToAll(battleState.getMembers(), "Раунд " + ++roundsCounter);
            battleState = Round.execute(battleState);
        }
        calculateAndPrintResult();
    }

    private boolean severalTeamsPresent() {
        return battleState.getCurTeamsId().size() > 1;
    }

    public List<Team> getTeams() {
        return battleState.getTeams();
    }

    private void calculateAndPrintResult() {
        List<ArenaUser> allMembers = battleState.getMembers();
        String winnerTeamId = battleState.getCurTeamsId().get(0);
        int firstWinnerId = battleState.getCurMembersId().get(0);
        if (battleState.getCurMembersId().isEmpty()) {
            Messages.sendToAll(allMembers, "Победителей нет");
        } else {
            int moneyBonus = allMembers.size() * Config.GOLD_FOR_MEMBER - Config.GOLD_FOR_MEMBER;
            //todo исправить случай, если победителей несколько (в одной команде)
            Messages.sendToAll(allMembers, "<b>Битва окончена</b>, победила команда: "
                    + winnerTeamId + ":" + ArenaUser.getUserName(firstWinnerId));
            ArenaUser.getUser(firstWinnerId).addUserWins();
            ArenaUser.getUser(firstWinnerId).addMoney(moneyBonus);
        }
        Messages.sendResultToAll(battleState.getTeams(), allMembers, battleState.getCurMembersId());
        int count = allMembers.size();
        for (int i = 0; i < count; i++) {
            ArenaUser user = allMembers.get(i);
            user.setStatus(Config.UNREGISTERED_STATUS);
            user.endBattleClassFeatures();
            user.setCurHitPoints(user.getMaxHitPoints());
            user.addExperience(user.getCurExp());
            user.addCurExp(-user.getCurExp());
            user.addUserGames();
            user.setLastGame();
        }
        Messages.sendToAll(allMembers,
                Messages.getInlineKeyboardMsg(PropertiesLoader.getInstance().getChannelId(), "Регистрация началась:",
                        Collections.singletonList("Сыграть снова"), Collections.singletonList("reg_user")));
    }
}
