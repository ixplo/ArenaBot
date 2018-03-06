package ml.ixplo.arenabot.user.classes.skillcaster;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SkillCaster extends ArenaUser implements ISkillCaster{

    @Override
    public void setClassFeatures() {
        actionsName = Arrays.asList("Атака", "Защита", "Лечение");
    }

    @Override
    public void endBattleClassFeatures() {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void getClassFeatures() {
        //no class features
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        //no class features
    }

    @Override
    public void putOnClassFeatures(Item item) {
        //no class features
    }

    @Override
    public void putOffClassFeatures(Item item) {
        //no class features
    }

    @Override
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {
        //no class features
    }

    @Override
    public void skillApply(String skillId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public String doCast(ArenaUser target, int percent, String castId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
        return null;
    }

    @Override
    public List<String> getCastsName() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getCastsIdForCallbacks() {
        return new ArrayList<>();
    }

    @Override
    public void learn(int level) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public String getClassActionId(String actionId) {
        return actionId;
    }
}
