package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.spells.Spell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * ixplo
 * 08.05.2017.
 */
public class MagicSpellTest {

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = TestHelper.WARRIOR;
    private ArenaUser mage = TestHelper.MAGE;

    @After
    public void tearDown() {
        testHelper.close();
    }

    @Test
    public void getSpellGrade() {
        Assert.assertTrue(Spell.getSpellGrade(mage.getUserId(), Presets.MAGE_SPELL_ID) == 1);
        Assert.assertTrue(Spell.getSpellGrade(mage.getUserId(), Presets.PRIEST_SPELL_ID) == 0);
        Assert.assertTrue(Spell.getSpellGrade(warrior.getUserId(), Presets.MAGE_SPELL_ID) == 0);
    }

    @Test
    public void getSpell() {
        Spell spell = Spell.getSpell(Presets.MAGE_SPELL_ID);
        Assert.assertEquals(Presets.MAGE_SPELL_ID, spell.getId());
        Assert.assertEquals(Presets.MAGE_SPELL_NAME, spell.getName());
        Assert.assertEquals(Presets.MAGE_SPELL_DAMAGE, spell.getDamage());
        Assert.assertEquals(Presets.MAGE_SPELL_MANACOST, spell.getManaCost());
        Assert.assertEquals(Presets.MAGE_SPELL_EXP_BONUS, spell.getExpBonus());
        Assert.assertEquals(Presets.MAGE_SPELL_EFFECT, spell.getEffect());
        Assert.assertEquals(Presets.MAGE_SPELL_ARMOR, spell.getArmor());
        Assert.assertEquals(Presets.MAGE_SPELL_DURATION, spell.getDuration());
        Assert.assertEquals(Presets.MAGE_SPELL_LEVEL, spell.getLevel());
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_ONE_BONUS, spell.getGradeOneBonus(), 0.1);
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_TWO_BONUS, spell.getGradeTwoBonus(), 0.1);
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_THREE_BONUS, spell.getGradeThreeBonus(), 0.1);
        Assert.assertEquals(Presets.MAGE_SPELL_PROBABILITY, spell.getProbability());
    }
}
