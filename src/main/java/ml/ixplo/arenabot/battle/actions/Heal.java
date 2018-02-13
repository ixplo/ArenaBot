package ml.ixplo.arenabot.battle.actions;

import static ml.ixplo.arenabot.utils.Utils.roundDouble;

/**
 * ixplo
 * 08.05.2017.
 */
public class Heal extends Action {
    private double heal;

    Heal() {
        actionId = "h";
        setPriority(FOURTH);
    }

    @Override
    public void doAction() {
        heal = roundDouble(user.getHeal() * getPercent() / 100);
        if (target.getCurHitPoints() + heal > target.getMaxHitPoints()) {
            heal = roundDouble(target.getMaxHitPoints() - target.getCurHitPoints());
        }
        experience = (int) (20 * heal);
        target.addCurHitPoints(heal);
        user.addCurExp(experience);
        message = "<pre>" + user.getName() + " вылечил " + target.getName() +
                " на " + heal + "\n(жизни:+" + heal + "/" + target.getCurHitPoints() +
                " \\\\ опыт:+" + experience + "/" + user.getCurExp() + ")</pre>";
    }

    public void unDo(){
        target.addCurHitPoints(-heal);
        user.addCurExp(-experience);
        message = null;
    }
}
