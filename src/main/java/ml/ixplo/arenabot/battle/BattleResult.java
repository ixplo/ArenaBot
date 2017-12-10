package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.IUser;


import java.util.Collections;
import java.util.List;

/**
 * ixplo
 * 29.04.2017.
 */
public class BattleResult {

    public void resultBattle(List<Integer> membersLive, List<String> teamWinner, List<ArenaUser> members, List<Team> teams) {

        if (membersLive.size() == 0) {
            Messages.sendToAll(members, "Победителей нет");
        } else {
            int moneyBonus = members.size() * Config.GOLD_FOR_MEMBER - Config.GOLD_FOR_MEMBER;
            Messages.sendToAll(members, "<b>Битва окончена</b>, победила команда: "
                    + teamWinner.get(0) + ":" + ArenaUser.getUserName(membersLive.get(0)));
            ArenaUser.getUser(membersLive.get(0)).addUserWins();
            ArenaUser.getUser(membersLive.get(0)).addMoney(moneyBonus);
        }
        Messages.sendResultToAll(teams, members, membersLive);
        int count = members.size();
        for (int i = 0; i < count; i++) {
            ArenaUser user = members.get(i);
            user.setStatus(Config.UNREG);
            user.endBattleClassFeatures();
            user.setCurHitPoints(user.getMaxHitPoints());
            user.addExperience(user.getCurExp());
            user.addCurExp(-user.getCurExp());
            user.addUserGames();
            user.setLastGame();
        }
        Messages.sendToAll(members, Messages.getInlineKeyboardMsg(Config.CHANNEL_ID, "Регистрация началась:",
                Collections.singletonList("Сыграть снова"), Collections.singletonList("reg_user")));
    }
}
