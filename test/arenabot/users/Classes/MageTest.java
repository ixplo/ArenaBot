package arenabot.users.Classes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ixplo
 * 09.05.2017.
 */
public class MageTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void countReceivedSpellPoints() throws Exception {
        assertEquals(0,Mage.countReceivedSpellPoints(1,98));
        assertEquals(1,Mage.countReceivedSpellPoints(2,118));
        assertEquals(2,Mage.countReceivedSpellPoints(142,99));
        assertEquals(2,Mage.countReceivedSpellPoints(300,0));
    }

}