package arenabot.user.classes;

/**
 * ixplo
 * 30.04.2017.
 */
public interface SpellCaster {
    void castSpell(String spellId);
    void learnSpell(int spellLevel);
    void manaRegen();
}
