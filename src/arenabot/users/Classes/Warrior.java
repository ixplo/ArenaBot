package arenabot.users.Classes;

import arenabot.users.ArenaUser;
import arenabot.users.Spells.Skill;

import java.util.ArrayList;

/**
 * ixplo
 * 28.04.2017.
 */
public class Warrior extends ArenaUser implements SkillApplicant{
    ArrayList<Skill> skills;
    int energy;

    public Warrior(Integer userId) {
        super(userId);
        setUserClass("w");
    }

    @Override
    public void setClassFeatures() {

    }

    @Override
    public void getClassFeatures() {

    }

    @Override
    public void skillApply(String skillId) {

    }


}
