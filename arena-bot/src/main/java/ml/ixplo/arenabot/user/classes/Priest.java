package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.classes.spellcaster.SpellCaster;

import java.util.List;

/**
 * ixplo
 * 28.04.2017.
 */
public class Priest extends SpellCaster {

    public Priest() {
        setUserClass("PRIEST");
    }

    @Override
    public void setClassFeatures() {
        super.setClassFeatures();
        setSpell("1ap");
    }

    public static List<String> getAllSpellsName() {
        return getDb().getStrings(Config.SPELLS, "class", UserClass.PRIEST.toString(), "name");
    }
}
