package arenabot.users;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ArenaUserTest {


    @Test
    public void setClassFeatures() throws Exception {

        ArenaUser mage = ArenaUser.create(ArenaUser.UserClass.MAGE);
        assertEquals("Атака", mage.getActionsName().get(0));

    }

}