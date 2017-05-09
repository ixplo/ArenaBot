package arenabot.users.Classes;

import arenabot.Config;
import arenabot.users.ArenaUser;
import arenabot.users.Spells.Skill;

import java.util.ArrayList;

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
        db.getIntFrom(Config.USERS, getUserId(), "max_target");
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
        db.setDoubleTo(Config.USERS,getUserId(),"attack",attack);
    }

    public void setMaxTarget(int maxTarget) {
        this.maxTarget = maxTarget;
        db.setIntTo(Config.USERS, getUserId(), "max_target", maxTarget);
    }

    public int getMaxTarget() {
        return maxTarget;
    }

    public String getXstatMsg() {
        StringBuilder out = new StringBuilder();
        //out.append(fillWithSpaces("<code>Кол. целей:", (Archer)(arenaUser).getMaxTarget() + "</code>\n", Config.WIDTH));
        return out.toString();
    }

    private void putOn() {
        int maxArcherTargets = (int) roundDouble((0.7 * getCurWis() + 0.3 * getCurDex()) / 4, 0);
        setMaxTarget(maxArcherTargets < 1 ? 1 : maxArcherTargets);
    }


}
