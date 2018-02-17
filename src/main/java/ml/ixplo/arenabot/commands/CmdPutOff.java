package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.utils.Utils;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdPutOff extends BotCommand {
    public static final String LOGTAG = "DOCOMMAND";

    public CmdPutOff() {
        super("putOff", "&lt&gt &ltномер&gt &lt%&gt - снять вещь с указанным номером в инвентаре");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        int eqipIndex;

        if (!chat.isUserChat()) {
            return;
        }
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if (strings.length != 1) {
            Messages.sendMessage(absSender, chat.getId(), "После команды введите один номер. Пример использования: /putoff 1");
            return;
        }
        if (!Utils.isInteger(strings[0])) {
            Messages.sendMessage(absSender, chat.getId(), "Вводите номер вещи. Пример использования: /putoff 1");
            return;
        }
        eqipIndex = Integer.parseInt(strings[0]);
        if (eqipIndex + 1 > ArenaUser.getUser(user.getId()).getEqipAmount()) {
            Messages.sendMessage(absSender, chat.getId(), "Неверный номер. Пример использования: /putoff 1");
            return;
        }
        if (!ArenaUser.getUser(user.getId()).getItems().get(eqipIndex).isInSlot()) {
            Messages.sendMessage(absSender, chat.getId(), "Вы пытаетесь снять ненадетую вещь. Пример использования: /putoff 1");
            return;
        }
        ArenaUser.getUser(user.getId()).putOff(eqipIndex);
        Messages.sendMessage(absSender, chat.getId(), "Вы сняли "
                + ArenaUser.getUser(user.getId()).getItems().get(eqipIndex).getName());
    }
}
