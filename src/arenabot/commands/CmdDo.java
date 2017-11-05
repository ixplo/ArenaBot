package arenabot.commands;

import arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdDo extends BotCommand {
    public static final String LOGTAG = "DOCOMMAND";

    public CmdDo() {
        super("do", "&lt a,p,h,m &gt &ltномер&gt &lt%&gt - действовать на цель под указанным номером");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!chat.isUserChat()) {
            return;
        }
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if (ArenaUser.getStatus(user.getId()) != 2) {
            return;
        }
        ArenaUser.doHandler(absSender, user.getId(), chat.getId(), strings);
    }
}
