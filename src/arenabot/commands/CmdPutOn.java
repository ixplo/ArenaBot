package arenabot.commands;

import arenabot.messages.Messages;
import arenabot.user.ArenaUser;
import arenabot.validate.Validation;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdPutOn extends BotCommand {
    public static final String LOGTAG = "DOCOMMAND";

    public CmdPutOn() {
        super("putOn", "&lt&gt &ltномер&gt &lt%&gt - надеть вещь с указанным номером в инвентаре");
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
            Messages.sendMessage(absSender, chat.getId(), "После команды введите один номер. Пример использования: /puton 1");
            return;
        }
        if (!Validation.isNumeric(strings[0])) {
            Messages.sendMessage(absSender, chat.getId(), "Вводите номер вещи. Пример использования: /puton 1");
            return;
        }
        eqipIndex = Integer.parseInt(strings[0]);
        if (eqipIndex + 1 > ArenaUser.getUser(user.getId()).getEqipAmount()) {
            Messages.sendMessage(absSender, chat.getId(), "Неверный номер. Пример использования: /puton 1");
            return;
        }
        if (ArenaUser.getUser(user.getId()).getItems().get(eqipIndex).isInSlot()) {
            Messages.sendMessage(absSender, chat.getId(), "Вы пытаетесь надеть уже надетую вещь. Пример использования: /puton 1");
            return;
        }
        ArenaUser.getUser(user.getId()).putOn(eqipIndex);
        Messages.sendMessage(absSender, chat.getId(), "Вы надели "
                + ArenaUser.getUser(user.getId()).getItems().get(eqipIndex).getName());
    }
}
