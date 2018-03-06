package ml.ixplo.arenabot.user.classes.spellcaster;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.user.classes.Mage;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.user.params.Hark;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ixplo
 * 09.05.2017.
 */
public class SpellCasterTest extends BaseTest{

    private Mage testMage = (Mage) mage;
    
    @Test
    public void countReceivedSpellPoints() throws Exception {
        assertEquals(0, SpellCaster.countReceivedSpellPoints(1, 98));
        assertEquals(1, SpellCaster.countReceivedSpellPoints(2, 118));
        assertEquals(2, SpellCaster.countReceivedSpellPoints(142, 99));
        assertEquals(2, SpellCaster.countReceivedSpellPoints(300, 0));
    }

    @Test
    public void addWisAndcurManaDontChangesInBattleTest() throws Exception {
        
        double before = testMage.getCurMana();

        testMage.addHark(Hark.NATIVE_WIS, 1);
        Assert.assertNotEquals(before, testMage.getCurMana(), Presets.DELTA);

        testMage.setStatus(Config.IN_BATTLE_STATUS);

        double expected = testMage.getCurMana();
        testMage.addHark(Hark.NATIVE_WIS, 1);
        Assert.assertEquals(expected, testMage.getCurMana(), Presets.DELTA);
    }
    
    @Test
    public void addWisTest() {
        double before = testMage.getCurMana();
        testMage.addHark(Hark.NATIVE_WIS, 1);
        double after = testMage.getCurMana();
        double difference = after - before;
        Assert.assertEquals(SpellCaster.MANA_FACTOR, difference, Presets.DELTA);
    }

    @Test
    public void putOnPutOffClassFeaturesNotInBattleTest() throws Exception {
        double before = testMage.getCurMana();
        testMage.addItem(Presets.RAINBOW_BRACELET);

        testMage.putOn(Presets.NEW_ITEM_INDEX);
        double afterPutOnNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, before, Presets.DELTA);
        Assert.assertEquals(12, afterPutOnNotInBattle, Presets.DELTA);

        testMage.putOff(Presets.NEW_ITEM_INDEX);
        double afterPutOffNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, afterPutOffNotInBattle, Presets.DELTA);
    }

    @Test
    public void putOnPutOffClassFeaturesInBattleTest() throws Exception {
        double before = testMage.getCurMana();
        testMage.addItem(Presets.RAINBOW_BRACELET);
        testMage.setStatus(Config.IN_BATTLE_STATUS);

        testMage.putOn(Presets.NEW_ITEM_INDEX);
        double afterPutOnNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, before, Presets.DELTA);
        Assert.assertEquals(9, afterPutOnNotInBattle, Presets.DELTA);

        testMage.putOff(Presets.NEW_ITEM_INDEX);
        double afterPutOffNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, afterPutOffNotInBattle, Presets.DELTA);


    }
}