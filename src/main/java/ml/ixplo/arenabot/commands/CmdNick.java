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
        if(ArenaUser.getStatus(user.getId())!=0){
            Messages.sendMessage(absSender, chat.getId(),"Можно сменить, если вы не зарегистрированы в бой");
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

