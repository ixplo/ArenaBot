package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.config.Config;
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
        //todo переделать
        if(((Bot)absSender).getRegistration().isOn() && Team.getMember(user.getId()).getStatus() != Config.REGISTERED_STATUS){
            return;
        }
        ((Bot)absSender).getRegistration().unregMember(user.getId());
        messageBuilder.append("<b>").append(userName).append("</b> вышел из команды ").
                append(Team.getMember(user.getId()).getTeamId());
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
