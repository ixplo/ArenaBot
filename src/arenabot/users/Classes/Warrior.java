package arenabot.users.Classes;

import arenabot.users.ArenaUser;
import arenabot.users.Inventory.Item;
import arenabot.users.Spells.Skill;

import java.util.ArrayList;

/**
 * ixplo
 * 28.04.2017.
 */
public class Warrior extends ArenaUser implements SkillApplicant{
    ArrayList<Skill> skills;
    int energy;

    public Warrior() {
        setUserClass("w");
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
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {

    }

    @Override
    public String doCast(ArenaUser target, int percent, String castId) {
        return null;
    }

    @Override
    public void endBattleClassFeatures() {

    }

    @Override
    public void skillApply(String skillId) {

    }


}
