package ml.ixplo.arenabot.user.classes;

/**
 * ixplo
 * 30.04.2017.
 */
public interface ISpellCaster {
    void castSpell(String spellId);
    void learnSpell(int spellLevel);
    void manaRegen();
}
