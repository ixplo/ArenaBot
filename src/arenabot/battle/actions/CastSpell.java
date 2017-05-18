package arenabot.battle.actions;

import arenabot.users.ArenaUser;
import arenabot.users.Classes.Mage;
import arenabot.users.Spells.Spell;

/**
 * ixplo
 * 08.05.2017.
 */
public class CastSpell extends Action {
    String spellId;

    public CastSpell(int userId, int targetId, int percent, String spellId) {
        super(userId, targetId, percent);
        actionId = "m";
        this.spellId = spellId;
        experience = Spell.getSpell(this.spellId).getExpBonus() * Spell.getSpell(this.spellId).getDamage();
    }

    @Override
    public void doAction() {
        message = user.doCast(target, getPercent(), spellId);
    }
}
