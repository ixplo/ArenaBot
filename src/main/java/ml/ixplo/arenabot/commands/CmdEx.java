package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

/**
 * ixplo
 * 26.04.2017.
 */
public class CmdEx extends BotCommand {
    public static final String LOGTAG = "EXCOMMAND";

    public CmdEx() {
        super("ex", "&ltномер&gt Показать предмет из инвентаря");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!chat.isUserChat()) {
            return;
        }
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        Messages.sendExMsg(absSender, chat.getId(), strings, user.getId());
    }
}
