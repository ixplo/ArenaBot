package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.helper.Presets;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HealTest extends BaseTest{

    @Test
    public void doActionTest() throws Exception {
        double beforeHitPoints = decreasedHitPoints();
        Action heal = Action.create(Presets.MAGE_ID, Action.HEAL, Presets.WARRIOR_ID, Presets.FULL_PERCENT);
        heal.doAction();

        assertTrue(beforeHitPoints < heal.getTarget().getCurHitPoints());
    }


    @Test
    public void unDoTest() throws Exception {
        double beforeHitPoints = decreasedHitPoints();
        Action heal = Action.create(Presets.MAGE_ID, Action.HEAL, Presets.WARRIOR_ID, Presets.FULL_PERCENT);
        heal.doAction();
        heal.unDo();
        assertEquals(beforeHitPoints, heal.getTarget().getCurHitPoints(), Presets.DELTA);
    }

    private double decreasedHitPoints() {
        warrior.addCurHitPoints(-1);
        double beforeHitPoints = warrior.getCurHitPoints();
        testHelper.getDb().updateUser(warrior);
        return beforeHitPoints;
    }

}