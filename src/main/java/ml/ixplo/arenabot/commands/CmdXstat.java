package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class CmdXstat extends BotCommand {
    public static final String LOGTAG = "XSTATCOMMAND";

    public CmdXstat() {
        super("xstat", "Показать расширенные характеристики персонажа");
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
            absSender.sendMessage(Messages.getUserXStatMsg(chat.getId(), user.getId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
