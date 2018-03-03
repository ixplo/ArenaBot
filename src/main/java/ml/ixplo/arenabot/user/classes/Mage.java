package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.config.Config;

import java.util.List;

/**
 * ixplo
 * 28.04.2017.
 */
public class Mage extends SpellCaster {

    public Mage() {
        setUserClass("MAGE");
    }

    @Override
    public void setClassFeatures() {
        super.setClassFeatures();
        setSpell("1am");
    }

    public static List<String> getAllSpellsName() {
        return getDb().getStrings(Config.SPELLS, "class", UserClass.MAGE.toString(), "name");
    }

}
