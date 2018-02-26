package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
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

    private AbsSender sender;
    private int percent;
    private String spellId;
    private int userId;
    private int stringsCount;
    private int target;
    private int targetIndex;
    private String actionType;
    private Long chatId;

    public CmdDo() {
        super("do", "&lt a,p,h,m &gt &ltномер&gt &lt%&gt - действовать на цель под указанным номером");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        this.sender = absSender;
        this.userId = user.getId();
        this.chatId = chat.getId();
        parse(strings);
        if (!chat.isUserChat() || badUser() || badStrings()) {
            return;
        }
        Messages.sendDoMsg(sender, chatId, actionType, target, percent);
        takeAction();
    }

    private void takeAction() {
        Integer targetId = Round.getCurrent().getMembers().get(target).getUserId();
        Action action = Action.create(userId, actionType, targetId, percent, spellId);
        ArenaUser.takeAction(action);
    }

    private boolean badStrings() {
        boolean isBad = false;
        SendMessage msg = getSendMessage();
        if (percent > 100) {
            msg.setText("Больше 100% быть не может. Инфа 146%!");
            isBad = true;
        }
        if (stringsCount == 0) {
            msg.setText("Формат: <i>/do a 1 100</i> - атаковать цель под номером 1 на 100%");
            isBad = true;
        }
        int membersCount = Round.getCurrent().getCurMembersId().size();
        if (target > membersCount - 1) {
            msg.setText("Цель под номером " + targetIndex +
                    " не найдена. Всего есть целей: " + membersCount);
            isBad = true;
        }
        if (isBad) {
            try {
                sender.sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
        return isBad;
    }

    private SendMessage getSendMessage() {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        return msg;
    }

    private void parse(String[] strings) {
        stringsCount = strings.length;
        if (stringsCount < 4) {
            spellId = "";
        } else {
            spellId = strings[3];
        }
        if (stringsCount < 3) {
            percent = 100;
        } else {
            percent = Integer.parseInt(strings[2]);
        }
        if (stringsCount < 2) {
            target = Round.getCurrent().getIndex(userId);
        } else {
            targetIndex = Integer.parseInt(strings[1]);
            target = targetIndex - 1;
        }
        if (stringsCount > 0) {
            actionType = strings[0];
        }
    }

    private boolean badUser() {
        return !ArenaUser.doesUserExists(userId) || ArenaUser.getStatus(userId) != Config.IN_BATTLE_STATUS;
    }
}
