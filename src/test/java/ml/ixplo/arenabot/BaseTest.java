package ml.ixplo.arenabot;

import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected TestHelper testHelper = new TestHelper();
    protected ArenaUser warrior = TestHelper.WARRIOR;
    protected ArenaUser mage = TestHelper.MAGE;

    @Before
    public void setUp() throws Exception {

    }


    @After
    public void tearDown() {
        testHelper.close();
    }

}