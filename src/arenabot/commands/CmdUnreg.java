package arenabot.commands;

import arenabot.ArenaBot;
import arenabot.Config;
import arenabot.users.ArenaUser;
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
public class CmdUnreg extends BotCommand {

    public static final String LOGTAG = "UNREGCOMMAND";

    public CmdUnreg() {
        super("unreg", "Отменить регистрацию в игру.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        String userName = ArenaUser.getUserName(user.getId());
        if(ArenaBot.registration.isOn && ArenaBot.registration.getMemberStatus(user.getId()) != Config.REG){
            return;
        }
        ArenaBot.registration.unregMember(user.getId());
        messageBuilder.append("<b>").append(userName).append("</b> вышел из команды ").
                append(ArenaBot.registration.getMemberTeam(user.getId()));
        SendMessage answer = new SendMessage();
        answer.enableHtml(true);
        answer.setChatId(chat.getId().toString());
        answer.setText(messageBuilder.toString());
        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
