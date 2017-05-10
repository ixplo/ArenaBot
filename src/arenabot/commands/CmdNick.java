package arenabot.commands;

/**
 * ixplo
 * 25.04.2017.
 */

import arenabot.Messages;
import arenabot.users.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class CmdNick extends BotCommand {

    public static final String LOGTAG = "NICKCOMMAND";

    public CmdNick() {
        super("nick", "Переименовать персонажа");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if (!chat.isUserChat()) {
            return;
        }
        if (strings.length == 0) {
            Messages.sendMessage(absSender, chat.getId(),"Формат: /nick НовыйНик");
            return;
        }
        ArenaUser.getUser(user.getId()).setName(strings[0]);
        Messages.sendMessage(absSender, chat.getId(),"Вы поменяли имя персонажа на: <b>" + strings[0] + "</b>");
    }
}

