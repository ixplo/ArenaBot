package arenabot.battle.actions;

import arenabot.user.Inventory.Item;
import arenabot.battle.Round;

import java.util.List;

/**
 * ixplo
 * 08.05.2017.
 */
public class Protect extends Action {
    private double protect;
    private List<Action> attackOnTargetList;

    Protect() {
        actionId = "p";
    }

    @Override
    public void doAction() {
        protect = user.getProtect() * getPercent() / 100;
        attackOnTargetList = Round.round.getAttackOnTargetList(getTarget().getUserId());
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
