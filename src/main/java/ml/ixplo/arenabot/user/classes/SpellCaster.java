package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.user.ArenaUser;

public abstract class SpellCaster extends ArenaUser implements ISpellCaster{
    protected double maxMana;
    protected double curMana;
    protected int spellPoints;
    protected double magicAttack;
}
