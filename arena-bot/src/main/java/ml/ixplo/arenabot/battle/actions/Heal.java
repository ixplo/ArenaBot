package ml.ixplo.arenabot.battle.actions;

import static ml.ixplo.arenabot.utils.Utils.roundDouble;

/**
 * ixplo
 * 08.05.2017.
 */
public class Heal extends Action {
    private double healValue;

    Heal() {
        setActionId(Action.HEAL);
        setPriority(ml.ixplo.arenabot.battle.actions.Action.FOURTH);
    }

    @Override
    public void doAction() {
        healValue = roundDouble(user.getHeal() * getPercent() / 100);
        if (target.getCurHitPoints() + healValue > target.getMaxHitPoints()) {
            healValue = roundDouble(target.getMaxHitPoints() - target.getCurHitPoints());
        }
        experience = (int) (20 * healValue);
        target.addCurHitPoints(healValue);
        user.addCurExp(experience);
        message = "<pre>" + user.getName() + " вылечил " + target.getName() +
                " на " + healValue + "\n(жизни:+" + healValue + "/" + target.getCurHitPoints() +
                " \\\\ опыт:+" + experience + "/" + user.getCurExp() + ")</pre>";
    }

    @Override
    public void unDo(){
        target.addCurHitPoints(-healValue);
        user.addCurExp(-experience);
        message = null;
    }
}
