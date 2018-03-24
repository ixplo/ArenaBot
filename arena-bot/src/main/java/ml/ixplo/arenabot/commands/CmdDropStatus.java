package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

/**
 * ixplo
 * 26.04.2017.
 */
public class CmdDropStatus extends BotCommand{
    public static final String LOGTAG = "DROPSTATUSCOMMAND";

    public CmdDropStatus() {
        super("dropstatus", "Начать регистрацию");
    }
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!chat.isUserChat()){
            return;
        }
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if (!user.getId().equals(Config.ADMIN_ID)) {
            return;
        }
        Registration.dropStatus();
    }
}
