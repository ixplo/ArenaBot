package ml.ixplo.arenabot.commands;

import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.validate.CheckResult;
import ml.ixplo.arenabot.validate.Validate;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class CmdPutOn extends BotCommand {
    public static final String LOGTAG = "PUTONCOMMAND";
    public static final String COMMAND_IDENTIFIER = "puton";

    public CmdPutOn() {
        super(COMMAND_IDENTIFIER, "&ltномер&gt - надеть вещь с указанным номером в инвентаре");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        int eqipIndex;

        if (!Validate.check(user, chat).isGood()) {
            return;
        }
        CheckResult result = Validate.check(strings, COMMAND_IDENTIFIER);
        if (!result.isGood()) {
            Messages.sendMessage(absSender, chat.getId(), result.getErrorMessage());
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
