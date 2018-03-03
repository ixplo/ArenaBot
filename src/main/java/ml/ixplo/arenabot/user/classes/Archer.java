package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.classes.skillcaster.SkillCaster;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.utils.Utils;

import java.math.BigDecimal;

import static ml.ixplo.arenabot.messages.Messages.fillWithSpaces;

/**
 * Archer can attack multiple targets. Damage depends on intellect
 */
public class Archer extends SkillCaster {
    private int maxTarget;

    public Archer() {
        setUserClass("ARCHER");
    }

    @Override
    public void setClassFeatures() {
        super.setClassFeatures();
        refreshMaxTargets();
        undoStrBonus(getCurStr());
        doIntBonus(getCurInt());
    }

    @Override
    public void getClassFeatures() {
        maxTarget = getDb().getIntFrom(Config.USERS, getUserId(), "max_target");
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
    public void putOffClassFeatures(Item item) {
        refreshMaxTargets();
        doStrBonus(item.getStrBonus());
        undoIntBonus(item.getIntBonus());
    }

    @Override
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {
        refreshMaxTargets();
        if (harkToUpId.equals("nativeStr")) {
            undoStrBonus(numberOfPoints);
        }
        if (harkToUpId.equals("nativeInt")) {
            doIntBonus(numberOfPoints);
        }
    }

    @Override
    public void doAction(String[] command) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    private void refreshMaxTargets() {
        int maxArcherTargets = (int) Utils.roundDouble((0.7 * getCurWis() + 0.3 * getCurDex()) / 4, 0);
        setMaxTarget(maxArcherTargets < 1 ? 1 : maxArcherTargets);
    }

    private void doIntBonus(int numberOfPoints) {
        setMinHit(Utils.roundDouble(getMinHit() + (double) numberOfPoints / 4));
        setMaxHit(Utils.roundDouble(getMaxHit() + (double) numberOfPoints / 4));
        setAttack(getAttack().add(BigDecimal.valueOf(0.39 * numberOfPoints)));
    }

    private void undoStrBonus(int numberOfPoints) {
        setMinHit(Utils.roundDouble(getMinHit() - (double) numberOfPoints / 4));
        setMaxHit(Utils.roundDouble(getMaxHit() - (double) numberOfPoints / 4));
        setAttack(getAttack().subtract(BigDecimal.valueOf(0.39 * numberOfPoints)));
    }

    private void undoIntBonus(int numberOfPoints) {
        doIntBonus(-numberOfPoints);
    }

    private void doStrBonus(int numberOfPoints) {
        undoStrBonus(-numberOfPoints);
    }

    @Override
    public void setMinHit(double minHit) {
        super.setMinHit(minHit);
        getDb().setDoubleTo(Config.USERS, getUserId(), "min_hit", minHit);
    }

    @Override
    public void setMaxHit(double maxHit) {
        super.setMaxHit(maxHit);
        getDb().setDoubleTo(Config.USERS, getUserId(), "max_hit", maxHit);
    }

    @Override
    public void setAttack(BigDecimal attack) {
        super.setAttack(attack);
        getDb().setBigDecimalTo(Config.USERS, getUserId(), "attack", attack);
    }

    private void setMaxTarget(int maxTarget) {
        this.maxTarget = maxTarget;
        getDb().setIntTo(Config.USERS, getUserId(), "max_target", maxTarget);
    }

    private int getMaxTarget() {
        return maxTarget;
    }

}
