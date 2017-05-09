package arenabot.battle.actions;

import arenabot.users.Inventory.Item;
import arenabot.battle.Round;

import java.util.List;

/**
 * ixplo
 * 08.05.2017.
 */
public class Protect extends Action {
    private double protect;
    private List<Action> attackOnTargetList;

    Protect(int userId, int targetId, int percent) {
        super(userId, targetId, percent);
        protect = user.getProtect() * percent / 100;
        attackOnTargetList = Round.round.getActionsByTarget(targetId);
    }

    @Override
    public void doAction() {
        if (attackOnTargetList.size() == 0) {
            return;
        }
        for (Action attackAction : attackOnTargetList) {
            double attack = attackAction.user.getAttack() * attackAction.getPercent() / 100;
            if (attack > protect) {
                return;
            }
            experience = (int) (4 * ((Attack) attackAction).getHit());
            user.addCurExp(experience);
            ((Attack) attackAction).unDo();
            attackAction.message = "<pre>" + attackAction.user.getName() + " пытался ударить " + target.getName() +
                    " оружием [" + Item.getItemName(attackAction.user.getUserId(), attackAction.user.getCurWeapon()) + "], но ему не удалось " +
                    "\n(" + user.getName() + "[опыт:+" + experience + "/" + user.getCurExp() + ")</pre>";
        }
    }
}
