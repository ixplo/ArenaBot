package arenabot.users.Classes;

import arenabot.Config;
import arenabot.users.ArenaUser;
import arenabot.users.Inventory.Item;
import arenabot.users.Spells.Skill;

import java.util.ArrayList;

import static arenabot.Messages.fillWithSpaces;


/**
 * ixplo
 * 28.04.2017.
 */
public class Archer extends ArenaUser implements SkillApplicant {
    ArrayList<Skill> skills;
    private int maxTarget;
    int energy;

    public Archer(Integer userId) {
        super(userId);
        setUserClass("l");
    }

    @Override
    public void setClassFeatures() {
        int maxArcherTargets = (int) roundDouble((0.7 * getCurWis() + 0.3 * getCurDex()) / 4, 0);
        setMaxTarget(maxArcherTargets < 1 ? 1 : maxArcherTargets);
        setMinHit((getCurInt() - 3) / 4);
        setMaxHit((getCurInt() - 3) / 4);
        setAttack(roundDouble(0.91 * getCurDex() + 0.39 * getCurInt()));
    }

    @Override
    public void getClassFeatures() {
        maxTarget = db.getIntFrom(Config.USERS, getUserId(), "max_target");
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        out.append(fillWithSpaces("\n<code>Кол. целей:", getMaxTarget() + "</code>\n", Config.WIDTH));
    }

    @Override
    public void putOnClassFeatures(Item item) {
        unDoCommonBonus(item);
        int maxArcherTargets = (int) roundDouble((0.7 * getCurWis() + 0.3 * getCurDex()) / 4, 0);
        setMaxTarget(maxArcherTargets < 1 ? 1 : maxArcherTargets);
        setMinHit(getMinHit() + (item.getMinHit() + (item.getIntBonus() - 3) / 4));
        setMaxHit(getMaxHit() + (item.getMaxHit() + (item.getIntBonus() - 3) / 4));
        setAttack(getAttack() + (item.getAttack() + roundDouble(0.91 * item.getDexBonus() + 0.39 * item.getIntBonus())));
    }

    private void unDoCommonBonus(Item item) {
        setMinHit(getMinHit() - (item.getMinHit() + (item.getStrBonus() - 3) / 4));
        setMaxHit(getMaxHit() - (item.getMaxHit() + (item.getStrBonus() - 3) / 4));
        setAttack(getAttack() - (item.getAttack() + roundDouble(0.91 * item.getDexBonus() + 0.39 * item.getStrBonus())));
    }

    @Override
    public void skillApply(String skillId) {

    }

    @Override
    public void setMinHit(double minHit) {
        super.setMinHit(minHit);
        db.setDoubleTo(Config.USERS, getUserId(), "min_hit", minHit);
    }

    @Override
    public void setMaxHit(double maxHit) {
        super.setMaxHit(maxHit);
        db.setDoubleTo(Config.USERS, getUserId(), "max_hit", maxHit);
    }

    @Override
    public void setAttack(double attack) {
        super.setAttack(attack);
        db.setDoubleTo(Config.USERS, getUserId(), "attack", attack);
    }

    public void setMaxTarget(int maxTarget) {
        this.maxTarget = maxTarget;
        db.setIntTo(Config.USERS, getUserId(), "max_target", maxTarget);
    }

    public int getMaxTarget() {
        return maxTarget;
    }

    private void putOn() {
        int maxArcherTargets = (int) roundDouble((0.7 * getCurWis() + 0.3 * getCurDex()) / 4, 0);
        setMaxTarget(maxArcherTargets < 1 ? 1 : maxArcherTargets);
    }


}
