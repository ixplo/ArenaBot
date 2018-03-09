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
    private ArenaUser warrior = testHelper.WARRIOR;

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
        Action.setActionIdFromCallback(warrior.getUserId(),"Атака");
        String actionTypeBefore = Action.getActionType(warrior.getUserId(), 1);
        Assert.assertEquals("a", actionTypeBefore);
        Action.clearActions(warrior.getUserId());
        Action.getActionType(warrior.getUserId(), 1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void nullActionIdTest() {
        Action.create(0, null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userNotExistsActionTest() {
        Action.create(0, Action.ATTACK, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetUserNonExistsActionTest() {
        Action.create(Presets.WARRIOR_ID, Action.ATTACK, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void spellIdFromAttackActionTest() {
        Action.create(Presets.WARRIOR_ID, Action.ATTACK, Presets.MAGE_ID, 0);
        Action.getCastId(Presets.WARRIOR_ID, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalActionIdTest() {
        Action.create(Presets.WARRIOR_ID, "wrong", Presets.MAGE_ID, 0);
    }

    @Test

    public void staticSettersTest() {
        Action action = Action.create(Presets.MAGE_ID, Action.MAGIC, Presets.MAGE_ID, 0);
        Action.save(action);

        Action.setPercent(Presets.MAGE_ID, EXPECTED);
        Action.setCastId(Presets.MAGE_ID, Presets.MAGIC_ARROW_SPELL_ID);
        Action.setTargetId(Presets.MAGE_ID, Presets.WARRIOR_ID);

        Assert.assertEquals(EXPECTED, Action.getPercent(Presets.MAGE_ID, 1));
        Assert.assertEquals(Presets.MAGIC_ARROW_SPELL_ID, Action.getCastId(Presets.MAGE_ID, 1));
        Assert.assertEquals(Presets.WARRIOR_ID, Action.getTargetId(Presets.MAGE_ID, 1));
    }

    @Test
    public void settersActionTest() {
        String expectedMessage = "Maг запустил стрелой в Мага на все 100";

        Action action = Action.create(Presets.MAGE_ID, Action.MAGIC, Presets.MAGE_ID, 0);
        action.setPercent(EXPECTED);
        action.setMessage(expectedMessage);
        action.setCastId(Presets.MAGIC_ARROW_SPELL_ID);
        action.setExperience(EXPECTED);
        Action.save(action);

        Assert.assertEquals(EXPECTED, Action.getPercent(Presets.MAGE_ID, 1));
        Assert.assertEquals(expectedMessage, action.getMessage());
        Assert.assertEquals(Presets.MAGE_ID, Action.getTargetId(Presets.MAGE_ID, 1));
        Assert.assertEquals(Presets.MAGIC_ARROW_SPELL_ID, Action.getCastId(Presets.MAGE_ID, 1));
        Assert.assertEquals(EXPECTED, action.getExperience());

        action.setTarget(warrior);
        action.setUser(warrior);

        Assert.assertTrue(Presets.WARRIOR_ID == action.getUser().getUserId());
        Assert.assertTrue(Presets.WARRIOR_ID == action.getTarget().getUserId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullActionIdFromCallbackTest() {
        Action.setActionIdFromCallback(Presets.WARRIOR_ID, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWrongActionIdFromCallbackTest() {
        Action.setActionIdFromCallback(Presets.WARRIOR_ID, "wrongType");
    }

    @Test
    public void setActionIdFromCallbackTest() {
        Action action = Action.create(Presets.WARRIOR_ID, Action.MAGIC, Presets.MAGE_ID, 0);
        Action.save(action);
        Action.setActionIdFromCallback(Presets.WARRIOR_ID, "Атака");
        Assert.assertEquals(Action.ATTACK, Action.getActionType(Presets.WARRIOR_ID, 1));
        Action.setActionIdFromCallback(Presets.WARRIOR_ID, "Защита");
        Assert.assertEquals(Action.PROTECT, Action.getActionType(Presets.WARRIOR_ID, 1));
        Action.setActionIdFromCallback(Presets.WARRIOR_ID, "Магия");
        Assert.assertEquals(Action.MAGIC, Action.getActionType(Presets.WARRIOR_ID, 1));
        Action.setActionIdFromCallback(Presets.WARRIOR_ID, "Лечение");
        Assert.assertEquals(Action.HEAL, Action.getActionType(Presets.WARRIOR_ID, 1));

    }

    @Test
    @Ignore("Нужно переопределять в наследниках")
    public void equalsTest() {
        Action action1 = Action.create(Presets.WARRIOR_ID, Action.ATTACK, Presets.MAGE_ID, 0);
        Action action2 = Action.create(Presets.WARRIOR_ID, Action.HEAL, Presets.MAGE_ID, 0);
        Action action3 = Action.create(Presets.WARRIOR_ID, Action.HEAL, Presets.WARRIOR_ID, 100);
        Action action4 = Action.create(Presets.MAGE_ID, Action.HEAL, Presets.WARRIOR_ID, 100);
        Assert.assertNotEquals(action1, action2);
        Assert.assertNotEquals(action3, action4);
        Assert.assertEquals(action2, action3);
    }

    @Test (expected = IllegalArgumentException.class)
    @Ignore("Доделать проверку spellId")
    public void createAction() {
        Action.create(warrior.getUserId(), "a", Presets.MAGE_ID, EXPECTED, "1am");
    }

}