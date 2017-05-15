package arenabot.commands;

/**
 * ixplo
 * 25.04.2017.
 */

import arenabot.Messages;
import arenabot.users.ArenaUser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class CmdHark extends BotCommand {

    public static final String LOGTAG = "HARKCOMMAND";

    public CmdHark() {
        super("hark", "Повысить характеристику");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String harkToUpId;
        String harkName;
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if (!chat.isUserChat()) {
            return;
        }
        if (strings.length < 2 || !isNumeric(strings[1])) {
            Messages.sendMessage(absSender, chat.getId(), "Формат: /hark названиеХарактеристики количествоОчков " +
                    "\nнапример, /hark сила 2 или /hark s 2 \nПовышаются только основные характеристики: сила, ловкость, мудрость, интеллект, телосложение" +
                    "\nРасширенные характеристики зависят от основных, а также могут быть улучшены с помощью надетых вещей.");
            return;
        }
        if (Integer.parseInt(strings[1]) > ArenaUser.getUser(user.getId()).getFreePoints()) {
            Messages.sendMessage(absSender, chat.getId(), "Недостаточно свободных очков");
            return;
        }
        if (Integer.parseInt(strings[1]) < 0) {
            Messages.sendMessage(absSender, chat.getId(), "Что, правда уменьшить хотите?");
            return;
        }
        harkToUpId = getHarkId(strings[0]);
        if(harkToUpId.equals("")){
            Messages.sendMessage(absSender, chat.getId(), "Формат: /hark названиеХарактеристики количествоОчков " +
                    "\nнапример, /hark сила 2 \nПовышаются только основные характеристики: сила, ловкость, мудрость, интеллект, телосложение" +
                    "\nРасширенные характеристики зависят от основных, а также могут быть улучшены с помощью надетых вещей.");
            return;
        }
        harkName = getHarkName(harkToUpId);
        ArenaUser.getUser(user.getId()).addHark(harkToUpId, Integer.parseInt(strings[1]));
        Messages.sendMessage(absSender, chat.getId(), "Вы подняли " + harkName + " на: <b>" + strings[1] + "</b>");
        try {
            absSender.sendMessage(Messages.getUserStatMsg(chat.getId(), user.getId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getHarkName(String harkToUpId) {
        switch (harkToUpId) {
            case "nativeStr":
                return "Силу";
            case "nativeDex":
                return "Ловкость";
            case "nativeWis":
                return "Мудрость";
            case "nativeInt":
                return "Интеллект";
            case "nativeCon":
                return "Телосложение";
            default:
                throw new RuntimeException("Invalid harkToUpId: " + harkToUpId);
        }
    }

    private String getHarkId(String hark) {
        switch (hark) {
            case "Cила":
            case "сила":
            case "с":
            case "str":
            case "s":
                return "nativeStr";
            case "Ловкость":
            case "ловкость":
            case "лов":
            case "л":
            case "l":
            case "dex":
            case "d":
                return "nativeDex";
            case "Мудрость":
            case "мудрость":
            case "муд":
            case "м":
            case "m":
            case "wis":
            case "w":
                return "nativeWis";
            case "Интеллект":
            case "интеллект":
            case "инт":
            case "и":
            case "int":
            case "i":
                return "nativeInt";
            case "Телосложение":
            case "телосложение":
            case "тело":
            case "т":
            case "t":
            case "con":
            case "c":
                return "nativeCon";
            default:
                return "";
        }
    }

    private static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

