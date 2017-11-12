package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * ixplo
 * 26.04.2017.
 */
public class CmdStat extends BotCommand{
    public static final String LOGTAG = "STATCOMMAND";

    public CmdStat() {
        super("stat", "Показать характеристики персонажа");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!chat.isUserChat()){
            return;
        }

        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        try {
            absSender.sendMessage(Messages.getUserStatMsg(chat.getId(), user.getId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
