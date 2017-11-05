package arenabot.battle.actions;

import arenabot.user.Inventory.Item;

import java.util.Random;

/**
 * ixplo
 * 08.05.2017.
 */
public class Attack extends Action {
    private double hit;

    Attack() {
        actionId = "a";
    }

    @Override
    public void doAction() {
        hit = roundDouble(randomDouble(user.getMinHit(), user.getMaxHit()) * getPercent() / 100);
        experience = (int) (9 * hit);
        target.addCurHitPoints(-hit);
        user.addCurExp(experience);
        message = "<pre>" + user.getName() + " напал на " + target.getName() +
                " с оружием [" + Item.getItemName(user.getUserId(), user.getCurWeapon()) + "] и ранил его на " +
                hit + "\n(жизни:-" + hit + "/" + target.getCurHitPoints() + " \\\\ опыт:+" + experience + "/" + user.getCurExp() + ")</pre>";
    }

    public void unDo(){
        target.addCurHitPoints(hit);
        user.addCurExp(-experience);
        message = "";
    }

    private double randomDouble(double min, double max) {
        if (min - max == 0) {
            return min;
        }
        Random rnd = new Random();
        return (rnd.nextInt((int) (max - min) * 100) + min * 100) / 100;
    }

    public double getHit() {
        return hit;
    }

}
