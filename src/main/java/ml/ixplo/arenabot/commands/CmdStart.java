package ml.ixplo.arenabot.commands;

/**
 * ixplo
 * 25.04.2017.
 */

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import static ml.ixplo.arenabot.messages.Messages.getChooseClassMsg;
import static ml.ixplo.arenabot.messages.Messages.getGreetingsMsg;
import static ml.ixplo.arenabot.messages.Messages.getOpenPrivateWithBotMsg;

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

        String userName;
        if (ArenaUser.doesUserExists(user.getId())) {
            if (!chat.isUserChat()) {
                return;
            }
            Messages.sendMessage(getGreetingsMsg(user.getId()));
            return;
        }
        if (strings.length != 0 && strings[0].equals("regUser")) {
            Messages.sendMessage(getChooseClassMsg(chat.getId()));
            return;
        }
        if (user.getFirstName() != null) {
            if (user.getLastName() != null) {
                userName = user.getFirstName() + " " + user.getLastName();
            } else userName = user.getFirstName();
        } else userName = user.getLastName();
        Messages.sendMessage(getOpenPrivateWithBotMsg(user.getId(), userName));
    }
}

