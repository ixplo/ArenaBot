package arenabot.battle.actions;

import arenabot.users.ArenaUser;

/**
 * ixplo
 * 08.05.2017.
 */
public class Heal extends Action {
    private double heal;

    Heal(int userId, int targetId, int percent) {
        super(userId, targetId, percent);
        heal = roundDouble(user.getHeal() * percent / 100);
    }

    @Override
    public void doAction() {
        if (target.getCurHitPoints() + heal > target.getMaxHitPoints()) {
            heal = roundDouble(target.getMaxHitPoints() - target.getCurHitPoints());
        }
        experience = (int) (20 * heal * getPercent() / 100);
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
