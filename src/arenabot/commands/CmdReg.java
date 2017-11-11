package arenabot.commands;

import arenabot.Bot;
import arenabot.config.Config;
import arenabot.messages.Messages;
import arenabot.battle.Registration;
import arenabot.user.ArenaUser;
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
        if(!Registration.isOn || Bot.registration.getMemberStatus(user.getId()) != Config.UNREG){
            return;
        }
        Bot.registration.regMember(user.getId());
        Messages.sendRegMsg(user.getId());
    }
}
