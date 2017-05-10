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
        refreshMaxTargets();
        undoStrBonus(getCurStr());
        doIntBonus(getCurInt());
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
        refreshMaxTargets();
        undoStrBonus(item.getStrBonus());
        doIntBonus(item.getIntBonus());
    }

    @Override
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {
        refreshMaxTargets();
        if(harkToUpId.equals("nativeStr")){
            undoStrBonus(numberOfPoints);
        }
        if(harkToUpId.equals("nativeInt")){
            doIntBonus(numberOfPoints);
        }
    }

    private void refreshMaxTargets(){
        int maxArcherTargets = (int) roundDouble((0.7 * getCurWis() + 0.3 * getCurDex()) / 4, 0);
        setMaxTarget(maxArcherTargets < 1 ? 1 : maxArcherTargets);
    }

    private void doIntBonus(int numberOfPoints) {
        setMinHit(roundDouble(getMinHit() + (double)numberOfPoints / 4));
        setMaxHit(roundDouble(getMaxHit() + (double)numberOfPoints / 4));
        setAttack(roundDouble(getAttack() + 0.39 * numberOfPoints));
    }

    private void undoStrBonus(int numberOfPoints) {
        setMinHit(roundDouble(getMinHit() - (double)numberOfPoints / 4));
        setMaxHit(roundDouble(getMaxHit() - (double)numberOfPoints / 4));
        setAttack(roundDouble(getAttack() - 0.39 * numberOfPoints));
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

}
