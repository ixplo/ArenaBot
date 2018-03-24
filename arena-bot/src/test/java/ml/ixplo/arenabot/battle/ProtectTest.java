package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.battle.actions.Protect;
import org.junit.Assert;
import org.junit.Test;

/**
 * ixplo
 * 08.05.2017.
 */
public class ProtectTest {
    @Test
    public void doAction() {
        Protect protect = new Protect();
        String actionType = protect.getActionId();
        Assert.assertEquals("p", actionType);
    }
}
