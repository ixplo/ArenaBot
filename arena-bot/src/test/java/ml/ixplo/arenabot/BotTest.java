package ml.ixplo.arenabot;

import ml.ixplo.arenabot.battle.Member;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class BotTest extends BaseTest {

    @Test
    @Ignore("тест посылает команды в реальный чат Телеграм")
    public void startBotTest() throws Exception {
        Bot bot = new Bot();
        List<Member> members = bot.getRegistration().getMembers();
    }
}
