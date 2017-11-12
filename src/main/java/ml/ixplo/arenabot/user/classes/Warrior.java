package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.user.spells.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * ixplo
 * 28.04.2017.
 */
public class Warrior extends ArenaUser implements SkillApplicant{
    ArrayList<Skill> skills;
    int energy;

    public Warrior() {
        setUserClass("WARRIOR");
    }

    @Override
    public void setClassFeatures() {

    }

    @Override
    public void getClassFeatures() {

    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {

    }

    @Override
    public void putOnClassFeatures(Item item) {

    }

    @Override
    public void putOffClassFeatures(Item item) {
        //todo
    }

    @Override
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {

    }

    @Override
    public void doAction(String[] command) {

    }

    @Override
    public String doCast(ArenaUser target, int percent, String castId) {
        return null;
    }

    @Override
    public void endBattleClassFeatures() {

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

    }

    @Override
    public void skillApply(String skillId) {

    }


}
