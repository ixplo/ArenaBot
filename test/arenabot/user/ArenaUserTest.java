package arenabot.user;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArenaUserTest {


    @Test
    public void setClassFeatures() throws Exception {

        ArenaUser mage = ArenaUser.create(ArenaUser.UserClass.MAGE);
        assertEquals("Атака", mage.getActionsName().get(0));

    }

}