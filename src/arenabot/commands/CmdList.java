package arenabot.commands;

import arenabot.messages.Messages;
import arenabot.battle.Battle;
import arenabot.battle.Registration;
import arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * ixplo
 * 25.04.2017.
 */
public class CmdList extends BotCommand {

    public static final String LOGTAG = "LISTCOMMAND";

    public CmdList() {
        super("list", "Список участников.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if(!Registration.isOn){
            Messages.sendListTo(chat.getId(), Battle.battle.getTeams());
            return;
        }
        try {
            absSender.sendMessage(Messages.getListMsg(chat.getId()));
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
