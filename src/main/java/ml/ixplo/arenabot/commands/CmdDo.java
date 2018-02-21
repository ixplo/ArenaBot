package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

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
        int target;
        int percent;
        String spellId;

        if (strings.length < 4) {
            spellId = "";
        } else {
            spellId = strings[3];
        }
        if (strings.length < 3) {
            percent = 100;
        } else {
            percent = Integer.parseInt(strings[2]);
        }
        if (strings.length < 2) {
            target = Round.getCurrent().getIndex(user.getId());
        } else {
            target = Integer.parseInt(strings[1]) - 1;
        }
        SendMessage msg = new SendMessage();
        msg.setChatId(chat.getId());
        msg.enableHtml(true);
        if (percent > 100) {
            msg.setText("Больше 100% быть не может. Инфа 146%!");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            return;
        }
        if (strings.length == 0) {
            msg.setText("Формат: <i>/do a 1 100</i> - атаковать цель под номером 1 на 100%");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            return;
        }
        if (target > Round.getCurrent().getCurMembersId().size() - 1) {
            msg.setText("Цель под номером " + Integer.parseInt(strings[1]) +
                    " не найдена. Всего есть целей: " + Round.getCurrent().getCurMembersId().size());
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            return;
        }
        String actionType = strings[0];
        Messages.sendDoMsg(absSender, chat.getId(), actionType, target, percent);

        Integer targetId = Round.getCurrent().getMembers().get(target).getUserId();
        Action action = Action.create(user.getId(), actionType, targetId, percent, spellId);
        ArenaUser.takeAction(action);
    }
}
