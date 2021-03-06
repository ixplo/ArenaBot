package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.classes.Mage;
import ml.ixplo.arenabot.user.spells.Spell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * ixplo
 * 08.05.2017.
 */
public class SpellTest {

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = testHelper.WARRIOR;
    private Mage mage = (Mage) testHelper.MAGE;

    @After
    public void tearDown() {
        testHelper.close();
    }

    @Test
    public void getSpellGrade() {
        Assert.assertTrue(Spell.getSpellGrade(mage.getUserId(), Presets.MAGIC_ARROW_SPELL_ID) == 1);
        Assert.assertTrue(Spell.getSpellGrade(mage.getUserId(), Presets.PRIEST_SPELL_ID) == 0);
        Assert.assertTrue(Spell.getSpellGrade(warrior.getUserId(), Presets.MAGIC_ARROW_SPELL_ID) == 0);
    }

    @Test
    public void getSpellBonus() {
        Spell spell = Spell.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        double spellBonus = spell.getSpellBonus(mage.getUserId());
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_ONE_BONUS, spellBonus, Presets.DELTA);
        double emptySpellBonus = spell.getSpellBonus(warrior.getUserId());
        Assert.assertEquals(Presets.EMPTY_BONUS, emptySpellBonus, Presets.DELTA);


    }

    @Test
    public void getSpell() {
        Spell spell = Spell.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        Assert.assertEquals(Presets.MAGIC_ARROW_SPELL_ID, spell.getId());
        Assert.assertEquals(Presets.MAGE_SPELL_NAME, spell.getName());
        Assert.assertEquals(Presets.MAGE_SPELL_DAMAGE, spell.getDamage());
        Assert.assertEquals(Presets.MAGE_SPELL_MANACOST, spell.getManaCost());
        Assert.assertEquals(Presets.MAGE_SPELL_EXP_BONUS, spell.getExpBonus());
        Assert.assertEquals(Presets.MAGE_SPELL_EFFECT, spell.getEffect());
        Assert.assertEquals(Presets.MAGE_SPELL_ARMOR, spell.getArmor());
        Assert.assertEquals(Presets.MAGE_SPELL_DURATION, spell.getDuration());
        Assert.assertEquals(Presets.MAGE_SPELL_FIRST_LEVEL, spell.getLevel());
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_ONE_BONUS, spell.getGradeOneBonus(), Presets.DELTA);
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_TWO_BONUS, spell.getGradeTwoBonus(), Presets.DELTA);
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_THREE_BONUS, spell.getGradeThreeBonus(), Presets.DELTA);
        Assert.assertEquals(Presets.MAGE_SPELL_PROBABILITY, spell.getProbability());
    }

    @Test
    public void getSpellTest() {
        Spell expectedSpell = Spell.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        Spell mageSpell = mage.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        Assert.assertEquals(expectedSpell, mageSpell);
    }

    @Test
    public void getSpellGradeTest() {
        Spell mageSpell = mage.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        Assert.assertEquals(Presets.SPELL_GRADE_ONE, mageSpell.getGrade());

        mageSpell.setGrade(Presets.SPELL_GRADE_TWO);
        Assert.assertEquals(Presets.SPELL_GRADE_TWO, mageSpell.getGrade());
    }

    @Test
    public void spellGradesTest() {
        Spell mageSpell = mage.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_ONE_BONUS, mageSpell.getBonus(), Presets.DELTA);
        mageSpell.setGrade(2);
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_TWO_BONUS, mageSpell.getBonus(), Presets.DELTA);
        mageSpell.setGrade(3);
        Assert.assertEquals(Presets.MAGE_SPELL_GRADE_THREE_BONUS, mageSpell.getBonus(), Presets.DELTA);
    }

    @Test
    public void equalsTest() {
        Spell first = mage.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        Spell second = first;
        Assert.assertEquals(first, second);
        Assert.assertNotEquals(first, null);
        Assert.assertNotEquals(first, mage);
        second = mage.getSpell(Presets.MAGIC_ARROW_SPELL_ID);
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
        second.setGrade(Presets.SPELL_GRADE_THREE);
        Assert.assertNotEquals(first, second);

    }
}
