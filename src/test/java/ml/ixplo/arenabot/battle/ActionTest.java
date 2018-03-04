package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.ItemTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ixplo
 * 05.05.2017.
 */
public class ActionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);
    private static final int EXPECTED = 100;

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = TestHelper.WARRIOR;

    @Before
    public void setUp() throws Exception {
        Action.setDb(testHelper.getDb());
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }

    @Test (expected = IllegalArgumentException.class)
    public void clearActions() throws Exception {
        Action.addAction(warrior.getUserId());
        Action.setActionId(warrior.getUserId(),"Атака");
        String actionTypeBefore = Action.getActionType(warrior.getUserId(), 1);
        Assert.assertEquals("a", actionTypeBefore);
        Action.clearActions(warrior.getUserId());
        Action.getActionType(warrior.getUserId(), 1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void nullActionIdTest() {
        Action action = Action.create(0, null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userNotExistsActionTest() {
        Action action = Action.create(0, Action.ATTACK, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetUserNonExistsActionTest() {
        Action action = Action.create(Presets.WARRIOR_ID, Action.ATTACK, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void spellIdFromAttackActionTest() {
        Action action = Action.create(Presets.WARRIOR_ID, Action.ATTACK, Presets.MAGE_ID, 0);
        Action.getCastId(Presets.WARRIOR_ID, 1);
    }

    @Test
    public void settersActionTest() {
        String expectedMessage = "Maг запустил стрелой в Мага на все 100";

        Action action = Action.create(Presets.MAGE_ID, Action.MAGIC, Presets.MAGE_ID, 0);
        action.setPercent(EXPECTED);
        action.setMessage(expectedMessage);
        action.setCastId(Presets.MAGIC_ARROW_SPELL_ID);
        Action.save(Presets.MAGE_ID, action);

        Assert.assertEquals(EXPECTED, Action.getPercent(Presets.MAGE_ID, 1));
        Assert.assertEquals(expectedMessage, action.getMessage());
        Assert.assertEquals(Presets.MAGE_ID, Action.getTargetId(Presets.MAGE_ID, 1));
        Assert.assertEquals(Presets.MAGIC_ARROW_SPELL_ID, Action.getCastId(Presets.MAGE_ID, 1));

    }

    @Test (expected = IllegalArgumentException.class)
    @Ignore("Переделать create без spellId")
    public void createAction() {
        Action.create(warrior.getUserId(), "a", Presets.MAGE_ID, EXPECTED, "1am");
    }

}