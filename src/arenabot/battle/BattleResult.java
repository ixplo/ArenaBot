package arenabot.battle;

import arenabot.users.ArenaUser;
import arenabot.Config;
import arenabot.Messages;


import java.util.List;

/**
 * ixplo
 * 29.04.2017.
 */
public class BattleResult {

    public void resultBattle(List<Integer> membersLive, List<String> teamWinner, List<ArenaUser> members, List<Team> teams) {
        //todo метод не больше одного экрана!!!
        if (membersLive.size() == 0) {
            Messages.sendToAll(members, "Победителей нет");
        } else {
            int moneyBonus = members.size() * 10 - 10;
            Messages.sendToAll(members, "<b>Битва окончена</b>, победила команда: " +
                    teamWinner.get(0) + ":" + ArenaUser.getUserName(membersLive.get(0)));
            ArenaUser.getUser(membersLive.get(0)).addUserWins();
            ArenaUser.getUser(membersLive.get(0)).addMoney(moneyBonus);
        }
        Messages.sendResultToAll(teams, members,membersLive);
        int count = members.size();
        for (int i = 0; i < count; i++) {
            ArenaUser user = members.get(i);
            user.setStatus(Config.UNREG);
            user.endBattle();
            user.setCurHitPoints(user.getMaxHitPoints());
            user.addExperience(user.getCurExp());
            user.addCurExp(-user.getCurExp());
            user.addUserGames();
            user.setLastGame();
        }
    }
}
