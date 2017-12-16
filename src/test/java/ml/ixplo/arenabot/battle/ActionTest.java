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
        String actionTypeAfter = Action.getActionType(warrior.getUserId(), 1);

    }

    @Test (expected = IllegalArgumentException.class)
    @Ignore("Переделать create без spellId")
    public void createAction() {
        Action.create(warrior.getUserId(), "a", Presets.MAGE_ID, 100, "1am");
    }

}