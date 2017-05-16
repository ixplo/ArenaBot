package arenabot.commands;

import arenabot.ArenaBot;
import arenabot.Config;
import arenabot.Messages;
import arenabot.battle.Registration;
import arenabot.users.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

/**
 * ixplo
 * 25.04.2017.
 */
public class CmdReg extends BotCommand {

    public static final String LOGTAG = "REGCOMMAND";

    public CmdReg() {
        super("reg", "Регистрация в игру.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if(!Registration.isOn || ArenaBot.registration.getMemberStatus(user.getId()) != Config.UNREG){
            return;
        }
        ArenaBot.registration.regMember(user.getId());
        Messages.sendRegMsg(user.getId());
    }
}
