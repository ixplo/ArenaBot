package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.battle.Round;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.List;

/**
 * ixplo
 * 08.05.2017.
 */
public class Protect extends Action {

    public Protect() {
        setActionId(Action.PROTECT);
        setPriority(SECOND);
    }

    @Override
    public void doAction() {
        double protectValue = user.getProtect() * getPercent() / 100;
        List<Action> attackOnTargetList = Round.getCurrent().getAttackOnTargetList(getTarget().getUserId());
        if (attackOnTargetList.isEmpty()) {
            return;
        }
        for (Action attackAction : attackOnTargetList) {
            BigDecimal attack = attackAction.user.getAttack().multiply(new BigDecimal(attackAction.getPercent() / 100));
            if (attack.doubleValue() > protectValue) {
                return;
            }
            experience = (int) (4 * ((Attack) attackAction).getHit());
            user.addCurExp(experience);
            ((Attack) attackAction).unDo();
            attackAction.message = "<pre>" + attackAction.user.getName() + " пытался ударить " + target.getName() +
                    " оружием [" + Item.getItemName(attackAction.user.getUserId(), attackAction.user.getCurWeaponIndex()) + "], но ему не удалось " +
                    "\n(" + user.getName() + "[опыт:+" + experience + "/" + user.getCurExp() + ")</pre>";
        }
    }

    @Override
    public void unDo() {
        throw new NotImplementedException();
    }
}
