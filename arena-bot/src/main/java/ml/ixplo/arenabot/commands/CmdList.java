package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Battle;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
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
    private Bot bot;

    public CmdList() {
        super("list", "Список участников.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        bot = (Bot) absSender;
        if (ArenaUser.doesUserExists(user.getId())) {
            try {
                absSender.sendMessage(getListMessage(chat.getId()));
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
    }

    private SendMessage getListMessage(Long chatId) {
        if (bot.getRegistration().isOn()) {
            return Messages.getRegistrationListMsg(chatId);
        } else {
            return Messages.getRoundTeamsList(chatId, Battle.getBattle().getTeams());
        }
    }
}
