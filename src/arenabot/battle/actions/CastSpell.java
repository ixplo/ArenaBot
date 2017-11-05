package arenabot.battle.actions;

import arenabot.user.spells.Spell;

/**
 * ixplo
 * 08.05.2017.
 */
public class CastSpell extends Action {
    String spellId;

    public CastSpell(String spellId) {
        actionId = "m";
        this.spellId = spellId;
        experience = Spell.getSpell(this.spellId).getExpBonus() * Spell.getSpell(this.spellId).getDamage();
    }

    @Override
    public void doAction() {
        message = user.doCast(target, getPercent(), spellId);
    }
}
