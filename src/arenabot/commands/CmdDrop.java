package arenabot.commands;

import arenabot.Messages;
import arenabot.users.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdDrop extends BotCommand {

    public static final String LOGTAG = "DROPCOMMAND";

    public CmdDrop() {
        super("drop", "Удаление персонажа.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!chat.isUserChat()) {
            return;
        }
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        Messages.sendDropMsg(absSender, chat);
    }
}
