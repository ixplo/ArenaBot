package ml.ixplo.arenabot.user.classes.spellcaster;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ixplo
 * 09.05.2017.
 */
public class SpellCasterTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void countReceivedSpellPoints() throws Exception {
        assertEquals(0, SpellCaster.countReceivedSpellPoints(1, 98));
        assertEquals(1, SpellCaster.countReceivedSpellPoints(2, 118));
        assertEquals(2, SpellCaster.countReceivedSpellPoints(142, 99));
        assertEquals(2, SpellCaster.countReceivedSpellPoints(300, 0));
    }

}