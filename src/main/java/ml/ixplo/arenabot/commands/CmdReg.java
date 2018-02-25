package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
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
        //todo переделать
        if (!((Bot)absSender).getRegistration().isOn() || Team.getMember(user.getId()).getStatus() != Config.UNREG) {
            return;
        }
        ((Bot)absSender).getRegistration().regMember(user.getId());
        Messages.sendToAll(((Bot)absSender).getRegistration().getMembers(),
                Messages.getRegMemberMsg(user.getId(), ArenaUser.getUser(user.getId()).getTeamId()));
    }
}
