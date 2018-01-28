package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.battle.Round;

import java.math.BigDecimal;
import java.util.List;

/**
 * ixplo
 * 08.05.2017.
 */
public class Protect extends Action {
    private double protect;
    private List<Action> attackOnTargetList;

    public Protect() {
        actionId = "p";
    }

    @Override
    public void doAction() {
        protect = user.getProtect() * getPercent() / 100;
        attackOnTargetList = Round.getCurrent().getAttackOnTargetList(getTarget().getUserId());
        if (attackOnTargetList.size() == 0) {
            return;
        }
        for (Action attackAction : attackOnTargetList) {
            BigDecimal attack = attackAction.user.getAttack().multiply(new BigDecimal(attackAction.getPercent() / 100));
            if (attack.doubleValue() > protect) {
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
