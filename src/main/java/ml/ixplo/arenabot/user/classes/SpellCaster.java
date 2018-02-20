package ml.ixplo.arenabot.user.classes;

/**
 * ixplo
 * 30.04.2017.
 */
public interface SpellCaster {
    String CUR_MANA = "cur_mana";
    String S_POINTS = "s_points";
    void castSpell(String spellId);
    void learnSpell(int spellLevel);
    void manaRegen();
}
