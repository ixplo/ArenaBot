package ml.ixplo.arenabot.validate;

import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.utils.Utils;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;

public class Validate {
    private Validate() {
    }

    public static CheckResult check(User user, Chat chat) {
        if (!chat.isUserChat()) {
            return new CheckResult(false);
        }
        return new CheckResult(ArenaUser.doesUserExists(user.getId()));
    }

    public static CheckResult check(String[] strings, String commandName) {
        if (strings.length != 1) {
            return new CheckResult("После команды введите один номер. Пример использования: /" + commandName + " 1");
        }
        if (!Utils.isInteger(strings[0])) {
            return new CheckResult("Вводите номер вещи. Пример использования: /" + commandName + " 1");
        }
        return new CheckResult(true);
    }
}
