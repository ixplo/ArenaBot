package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.user.spells.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * ixplo
 * 28.04.2017.
 */
public class Warrior extends ArenaUser implements SkillCaster {
    ArrayList<Skill> skills;
    int energy;

    public Warrior() {
        setUserClass("WARRIOR");
    }

    @Override
    public void setClassFeatures() {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void getClassFeatures() {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void putOnClassFeatures(Item item) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void putOffClassFeatures(Item item) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void doAction(String[] command) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public String doCast(ArenaUser target, int percent, String castId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
        return null;
    }

    @Override
    public void endBattleClassFeatures() {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public String getClassActionId(String actionId) {
        return actionId;
    }

    @Override
    public List<String> getCastsName() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getCastsId() {
        return new ArrayList<>();
    }

    @Override
    public void learn(int level) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void skillApply(String skillId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }
}
