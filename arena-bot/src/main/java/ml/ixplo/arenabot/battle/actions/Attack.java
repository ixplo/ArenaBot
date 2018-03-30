package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.user.items.Item;

import java.util.Random;

import static ml.ixplo.arenabot.utils.Utils.roundDouble;

/**
 * ixplo
 * 08.05.2017.
 */
public class Attack extends Action {
    private static final int HUNDRED_PERCENT = 100;
    private static final int EXP_FOR_ONE_HIT_POINT = 9;
    private double hit;

    Attack() {
        setActionId(Action.ATTACK);
        setPriority(FIRST);
    }

    @Override
    public void doAction() {
        hit = roundDouble(randomDouble(user.getMinHit(), user.getMaxHit()) * getPercent() / HUNDRED_PERCENT);
        experience = (int) (EXP_FOR_ONE_HIT_POINT * hit);
        target.addCurHitPoints(-hit);
        user.addCurExp(experience);
        message = "<pre>" + user.getName() + " напал на " + target.getName()
                + " с оружием [" + Item.getItemName(user.getUserId(), user.getCurWeaponIndex()) + "] и ранил его на "
                + hit + "\n(жизни:-" + hit + "/" + target.getCurHitPoints() + " \\\\ опыт:+" + experience + "/" + user.getCurExp() + ")</pre>";
    }

    @Override
    public void unDo() {
        target.addCurHitPoints(hit);
        user.addCurExp(-experience);
        message = "";
    }

    private double randomDouble(double min, double max) {
        if (min - max == 0) {
            return min;
        }
        Random rnd = new Random();
        return (rnd.nextInt((int) (max - min) * HUNDRED_PERCENT) + min * HUNDRED_PERCENT) / HUNDRED_PERCENT;
    }

    public double getHit() {
        return hit;
    }

}
