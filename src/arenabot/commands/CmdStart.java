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

/**
 * This command starts the conversation with the bot
 */
public class CmdStart extends BotCommand {

    public static final String LOGTAG = "STARTCOMMAND";

    public CmdStart() {
        super("start", "Создать нового персонажа");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (ArenaUser.doesUserExists(user.getId())) {
            if (!chat.isUserChat()) {
                return;
            }
            Messages.sendToRegisteredUserMsg(absSender, chat.getId(), user.getId());
            return;
        }
        if (strings.length != 0 && strings[0].equals("regUser")) {
            Messages.sendChooseClassMsg(absSender, chat.getId());
            return;
        }
        try {
            absSender.sendMessage(Messages.toOpenPrivateWithBotMsg(chat.getId(),user));
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}

