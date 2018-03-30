package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.user.spells.Spell;

/**
 * ixplo
 * 08.05.2017.
 */
public class CastSpell extends Action {
    private String spellId;

    /**
     * @param spellId - String code of spell
     */
    public CastSpell(String spellId) {
        setActionId(Action.MAGIC);
        setPriority(THIRD);
        this.spellId = spellId;
        experience = Spell.getSpell(this.spellId).getExpBonus() * Spell.getSpell(this.spellId).getDamage();
    }

    @Override
    public void doAction() {
        message = user.doCast(target, getPercent(), spellId);
    }

    @Override
    public void unDo() {
        // NotImplementedException
    }
}
