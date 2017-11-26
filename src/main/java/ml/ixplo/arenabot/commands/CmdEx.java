package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.validate.Validation;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import static ml.ixplo.arenabot.messages.Messages.getExMsg;

/**
 * ixplo
 * 26.04.2017.
 */
public class CmdEx extends BotCommand {
    public static final String LOGTAG = "EXCOMMAND";

    public CmdEx() {
        super("ex", "&ltномер&gt Показать предмет из инвентаря");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!chat.isUserChat()) {
            return;
        }
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        // проверка, что введенный параметр - это число
        if (strings.length == 0 || !Validation.isNumeric(strings[0])) {
            Messages.sendMessage(chat.getId(),"Формат: <i>/ex 1</i> - посмотреть предмет в инвентаре под номером 1");
            return;
        }
        // проверка, что введен корректный индекс
        int eqipIndex = Integer.parseInt(strings[0]);
        if (eqipIndex > Item.getEqipAmount(user.getId())) {
            Messages.sendMessage(chat.getId(),"Количество вещей у вас: "
                    + Item.getEqipAmount(user.getId())
                    + ". Предмет под номером " + eqipIndex + " не найден.");
            return;
        }
        Messages.sendMessage(getExMsg(user.getId(), eqipIndex));
    }
}
