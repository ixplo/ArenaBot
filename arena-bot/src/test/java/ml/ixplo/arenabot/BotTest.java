package ml.ixplo.arenabot;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Member;
import org.junit.Test;

import java.util.List;

public class BotTest extends BaseTest {

    @Test
    public void startBotTest() throws Exception {
        Bot bot = new Bot();
        List<Member> members = bot.getRegistration().getMembers();
    }
}
