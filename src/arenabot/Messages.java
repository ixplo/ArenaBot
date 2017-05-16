package arenabot;

import arenabot.battle.*;
import arenabot.users.ArenaUser;
import arenabot.users.Inventory.Item;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.logging.BotLogger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ixplo
 * 29.04.2017.
 */
public class Messages {
    public static final String LOGTAG = "MESSAGES";

    /***** no instance for this class *****/
    private Messages() {
        throw new AssertionError();
    }

    private static ArenaBot arenaBot;
    static {
        arenaBot = new ArenaBot();
    }

    public static void sendToRegisteredUserMsg(AbsSender absSender, Long chatId, Integer userId) {

        SendMessage greetings = new SendMessage();
        greetings.enableHtml(true);
        greetings.setChatId(chatId);
        String msgText = "Добро пожаловать, " + ArenaUser.getUserName(userId) + "\n" +
                "Список доступных команд: /help";
        SendMessage statMsg = Messages.getUserStatMsg(chatId, userId);
        SendMessage xStatMsg = Messages.getUserXStatMsg(chatId, userId);
        SendMessage eqMsg = Messages.getEqipMsg(chatId, userId);
        greetings.setText(msgText);
        try {
            absSender.sendMessage(greetings);
            absSender.sendMessage(statMsg);
            absSender.sendMessage(xStatMsg);
            absSender.sendMessage(eqMsg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static SendMessage getEqipMsg(Long chatId, Integer userId) {

        ArenaUser arenaUser = ArenaUser.getUser(userId);
        List<Item> items = Item.getItems(userId);
        StringBuilder out = new StringBuilder();
        out.append("Ваш инвентарь: \n");
        int size = items.size();
        for (int i = 0; i < size; i++) {
            int count = i + 1;
            out.append(count);
            if (Item.isItemInSlot(count, userId)) {
                out.append(".<b>").append(items.get(i).getName()).append("</b>, ");
            } else {
                out.append(".").append(items.get(i).getName()).append(", ");
            }
        }
        out.append("\nОружие: <b>");
        if (arenaUser.getCurWeapon() == 0) {
            out.append("без оружия</b>");
        } else {
            out.append(Item.getItem(Item.getItemId(arenaUser.getUserId(), arenaUser.getCurWeapon())).getName());
            out.append("</b>(");
            out.append(Item.getItem(Item.getItemId(arenaUser.getUserId(), arenaUser.getCurWeapon())).getPrice());
            out.append(")");
        }
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(out.toString());
        return msg;
    }

    public static SendMessage getUserStatMsg(Long chatId, Integer userId) {

        ArenaUser arenaUser = ArenaUser.getUser(userId);
        StringBuilder out = new StringBuilder();
        out.append("<b>").append(arenaUser.getName()).append("</b> \n");
        out.append("Ваши характеристики: \n");
        out.append(arenaUser.getClassName()).append("/");
        out.append(arenaUser.getRaceName()).append("\n");
        out.append("Победы: ").append(arenaUser.getUserWins()).append(" Игры: ").append(arenaUser.getUserGames()).append("\n");
        String stringDate;
        if (arenaUser.getLastGame() == 0) {
            stringDate = "еще нет";
        } else {
            stringDate = new SimpleDateFormat().format(new Date(arenaUser.getLastGame()));
        }
        out.append("Был в бою: ").append(stringDate).append("\n\n");
        out.append(fillWithSpaces("<code>Опыт:", arenaUser.getExperience() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Жизни:", arenaUser.getCurHitPoints() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Золото:", arenaUser.getMoney() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Сила:", arenaUser.getCurStr() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Ловкость:", arenaUser.getCurDex() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Мудрость:", arenaUser.getCurWis() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Интеллект:", arenaUser.getCurInt() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Телосложение:", arenaUser.getCurCon() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Свободные очки:", arenaUser.getFreePoints() + "</code>\n", Config.WIDTH));
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(out.toString());
        return msg;
    }

    public static SendMessage getUserXStatMsg(Long chatId, Integer userId) {

        ArenaUser arenaUser = ArenaUser.getUser(userId);
        StringBuilder out = new StringBuilder();
        out.append("Ваши расширенные характеристики:\n\n");
        out.append(fillWithSpaces("<code>Урон:", arenaUser.getMinHit() + "-" + arenaUser.getMaxHit() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Атака:", arenaUser.getAttack() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Защита:", arenaUser.getProtect() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Лечение:", arenaUser.getHeal() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Защ. от магии:", arenaUser.getMagicProtect() + "</code>", Config.WIDTH));
        arenaUser.appendClassXstatMsg(out);
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(out.toString());
        return msg;
    }

    private static SendMessage getChooseRaceMsg(Long chatId, String userClass) {

        StringBuilder msgText = new StringBuilder();
        msgText.append("<b>Доступные расы:</b>\n\n");
        List<String> descr = ArenaUser.getRacesDescr();
        int count = descr.size();
        for (int i = 0; i < count; i++) {
            msgText.append(descr.get(i)).append("\n\n");
        }
        msgText.append("<b>Выберите расу персонажа:</b>");
        List<String> callbackData = new LinkedList<>();
        List<String> racesId = ArenaUser.getRacesId();
        int size = racesId.size();
        for (int i = 0; i < size; i++) {
            String buf = "newRaceIs_" + racesId.get(i) + userClass;
            callbackData.add(buf);
        }
        return getInlineKeyboardMsg(chatId, msgText.toString(), ArenaUser.getRacesName(), callbackData);
    }

    public static SendMessage getListMsg(Long chatId) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        int membersCount = ArenaBot.registration.getMembersCount();
        if (!Registration.isOn) {
            msg.setText("Бой уже идет");
            return msg;
        }
        if (membersCount == 0) {
            msg.setText("Еще никто не зарегистрировался");
            return msg;
        }
        msg.setText(ArenaBot.registration.getList());
        return msg;
    }

    public static SendMessage getInlineKeyboardMsg(Long chatId, String messageText, List<String> buttonsText, List<String> buttonsCallbackData) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new LinkedList<>();
        int buttonsAmount = buttonsText.size();
        int rowsAmount = buttonsAmount / 4 + (buttonsAmount % 4 == 0 ? 0 : 1);
        int buttonsCounter = 0;
        for (int i = 0; i < rowsAmount; i++) {
            List<InlineKeyboardButton> row = new LinkedList<>();
            for (int j = 0; j < 4; j++) {
                if (buttonsCounter >= buttonsAmount) break;
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonsText.get(buttonsCounter));
                button.setCallbackData(buttonsCallbackData.get(buttonsCounter));
                row.add(button);
                buttonsCounter++;
            }
            rows.add(row);
        }
        markup.setKeyboard(rows);
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(messageText);
        msg.setReplyMarkup(markup);
        msg.enableHtml(true);
        return msg;
    }

    private static InlineKeyboardMarkup gerInlineKeyboardMarkup(List<String> buttonsText, List<String> buttonsCallbackData){

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new LinkedList<>();
        int buttonsAmount = buttonsText.size();
        int rowsAmount = buttonsAmount / 4 + (buttonsAmount % 4 == 0 ? 0 : 1);
        int buttonsCounter = 0;
        for (int i = 0; i < rowsAmount; i++) {
            List<InlineKeyboardButton> row = new LinkedList<>();
            for (int j = 0; j < 4; j++) {
                if (buttonsCounter >= buttonsAmount) break;
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonsText.get(buttonsCounter));
                button.setCallbackData(buttonsCallbackData.get(buttonsCounter));
                row.add(button);
                buttonsCounter++;
            }
            rows.add(row);
        }
        markup.setKeyboard(rows);
        return markup;
    }

    static AnswerInlineQuery getAnswerForInlineQuery(InlineQuery inlineQuery) {

        AnswerInlineQuery query = new AnswerInlineQuery();
        InlineQueryResultArticle article = new InlineQueryResultArticle();
        article.setId(inlineQuery.getId());
        article.setUrl(Config.BOT_PRIVATE);
        InputTextMessageContent input = new InputTextMessageContent();
        article.setInputMessageContent(input);
        query.setResults(article);
        query.setInlineQueryId(inlineQuery.getId());
        return query;
    }

    static void sendCreateUser(CallbackQuery callbackQuery, String userClass, String userRace) {

        Long chatId = callbackQuery.getMessage().getChatId();
        Integer userId = callbackQuery.getFrom().getId();
        String queryId = callbackQuery.getId();
        SendMessage statMsg = Messages.getUserStatMsg(chatId, userId);
        SendMessage xStatMsg = Messages.getUserXStatMsg(chatId, userId);
        SendMessage eqMsg = Messages.getEqipMsg(chatId, userId);
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Ваш персонаж " +
                userClass + "/" +
                userRace + " создан!");
        query.setCallbackQueryId(queryId);
        try {
            arenaBot.sendMessage(statMsg);
            arenaBot.sendMessage(xStatMsg);
            arenaBot.sendMessage(eqMsg);
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    static void sendAskRace(CallbackQuery callbackQuery, String userClass) {

        String queryId = callbackQuery.getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Вы выбрали класс: " + ArenaUser.getClassName(userClass));
        query.setCallbackQueryId(queryId);
        try {
            arenaBot.sendMessage(Messages.getChooseRaceMsg(chatId, userClass));
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static void sendCancelDelete(CallbackQuery callbackQuery) {

        String queryId = callbackQuery.getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer msgId = callbackQuery.getMessage().getMessageId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Вы отменили удаление");
        query.setCallbackQueryId(queryId);
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setChatId(chatId);
        edit.setMessageId(msgId);
        try {
            arenaBot.editMessageReplyMarkup(edit);
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    static void sendAfterDelete(CallbackQuery callbackQuery) {

        String queryId = callbackQuery.getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer msgId = callbackQuery.getMessage().getMessageId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Персонаж удален");
        query.setCallbackQueryId(queryId);
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setChatId(chatId);
        edit.setMessageId(msgId);
        EditMessageText editText = new EditMessageText();
        editText.setChatId(chatId);
        editText.setMessageId(msgId);
        editText.setText("Персонаж удален");
        try {
            arenaBot.editMessageReplyMarkup(edit);
            arenaBot.editMessageText(editText);
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendExMsg(AbsSender absSender, Long chatId, String[] strings, Integer userId) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        if (strings.length == 0 || !isNumeric(strings[0])) {
            msg.setText("Формат: <i>/ex 1</i> - посмотреть предмет в инвентаре под номером 1");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        int eqipIndex = Integer.parseInt(strings[0]);
        if (eqipIndex > Item.getEqipAmount(userId)) {
            msg.setText("Количество вещей у вас: " + Item.getEqipAmount(userId) +
                    ". Предмет под номером " + eqipIndex + " не найден.");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        Item item = Item.getItem(Item.getItemId(userId, eqipIndex));
        StringBuilder out = new StringBuilder();
        out.append("Вещь: <b>").append(item.getName()).append("</b> \nЦена [").append(item.getPrice());
        out.append("]\n\n").append(item.getDescr()).append("\n\n");
        if (item.isWeapon()) {
            out.append(fillWithSpaces("<code>Урон:", item.getMinHit() + "-" + item.getMaxHit() + "</code>\n", Config.WIDTH));
        }
        out.append(fillWithSpaces("<code>Атака:", item.getAttack() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Защита:", item.getProtect() + "</code>\n", Config.WIDTH));
        if (item.getStrBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Силе:", item.getStrBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getDexBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Ловкости:", item.getDexBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getWisBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Мудрости:", item.getWisBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getIntBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Интеллекту:", item.getIntBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getConBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Телосложению:", item.getConBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getStrNeeded() != 0 ||
                item.getDexNeeded() != 0 ||
                item.getWisNeeded() != 0 ||
                item.getIntNeeded() != 0 ||
                item.getConNeeded() != 0) {
            out.append("\n\nТребования к характеристикам:\n");
        }
        if (item.getStrNeeded() != 0) {
            out.append(fillWithSpaces("<code>Сила:", item.getStrNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getDexNeeded() != 0) {
            out.append(fillWithSpaces("<code>Ловкость:", item.getDexNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getWisNeeded() != 0) {
            out.append(fillWithSpaces("<code>Мудрость:", item.getWisNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getIntNeeded() != 0) {
            out.append(fillWithSpaces("<code>Интеллект:", item.getIntNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getConNeeded() != 0) {
            out.append(fillWithSpaces("<code>Телосложение:", item.getConNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getRace() != null) {
            out.append(fillWithSpaces("<code>Только для расы:", item.getRace() + "</code>\n", Config.WIDTH));
        }
        out.append(fillWithSpaces("<code>Слот:", Item.getSlotName(item.getSlot()) + "</code>\n", Config.WIDTH));
        String isInSlot = Item.isItemInSlot(eqipIndex, userId) ? "Надето" : "Не надето";
        out.append(fillWithSpaces("<code>Состояние:",
                isInSlot + "</code>\n", Config.WIDTH));
        msg.setText(out.toString());
        try {
            absSender.sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String fillWithSpaces(String first, String second, int width) {

        String s1 = first.replaceAll("<.*?>", "");
        String s2 = second.replaceAll("<.*?>", "");
        int neededSpaces = width - s1.length() - s2.length();
        StringBuilder spaces = new StringBuilder();
        if (neededSpaces < 0) {
            spaces.append(" ");
        } else {
            for (int i = 0; i < neededSpaces; i++) {
                spaces.append(" ");
            }
        }
        return first + spaces + second;
    }

    private static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void sendDropMsg(AbsSender absSender, Chat chat) {

        try {
            absSender.sendMessage(getInlineKeyboardMsg(chat.getId(),
                    "<b>Удалить</b> персонажа без возможности восстановления?",
                    Arrays.asList("Удалить", "Отмена"),
                    Arrays.asList("del_Delete", "del_Cancel")));
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendChannelMsg(Long chatId, String msgText) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            arenaBot.sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static int sendChannelMsgReturnId(Long chatId, String msgText) {

        int id = 0;
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            id = arenaBot.sendMessage(msg).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void editChannelMsg(long chatId, int msgId, String msgText) {

        EditMessageText editText = new EditMessageText();
        editText.setChatId(chatId);
        editText.setMessageId(msgId);
        editText.setText(msgText);
        try {
            arenaBot.editMessageText(editText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendListToAll(List<Team> teams) {

        SendMessage msg = new SendMessage();
        StringBuilder msgText = new StringBuilder();
        List<Integer> membersId = new ArrayList<>();
        List<String> buttonsName = new ArrayList<>();
        List<String> callbackDatas = new ArrayList<>();
        msgText.append("Список: ");
        int count = 0;
        for (Team team : teams) {
            msgText.append("[");
            for (Member user : team.getMembers()) {
                msgText.append(++count).append(" ");
                msgText.append(user.getName()).append(" ");
                membersId.add(user.getUserId());
                buttonsName.add(user.getName());
                callbackDatas.add("target_" + user.getUserId());
            }
            msgText.append("]");
        }
        msg.enableHtml(true);
        msg.setText(msgText.toString());
        try {
            for (Integer id : membersId) {
                msg.setChatId((long) id);
                arenaBot.sendMessage(msg);
                arenaBot.sendMessage(getInlineKeyboardMsg((long) id, "Выберите цель:", buttonsName, callbackDatas));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendListTo(Long chatId, List<Team> teams) {

        SendMessage msg = new SendMessage();
        StringBuilder msgText = new StringBuilder();
        msgText.append("Список: ");
        int count = 0;
        for (Team team : teams) {
            msgText.append("[");
            for (Member user : team.getMembers()) {
                msgText.append(++count).append(" ");
                msgText.append(user.getName()).append(" ");
            }
            msgText.append("]");
        }
        msg.enableHtml(true);
        msg.setText(msgText.toString());
        try {
            msg.setChatId(chatId);
            arenaBot.sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendResultToAll(List<Team> teams, List<ArenaUser> members, List<Integer> membersLive) {

        SendMessage msg = new SendMessage();
        StringBuilder msgText = new StringBuilder();
        List<Integer> membersId = new ArrayList<>();
        for (Team team : teams) {
            msgText.append("Команда ").append(team.getName()).append(": ");
            for (ArenaUser user : members) {
                if (team.getBattleMembersId().contains(user.getUserId())) {
                    msgText.append(user.getName()).append("(опыт:+").append(user.getCurExp());
                    msgText.append("/").append(user.getCurExp() + user.getExperience());
                    msgText.append(", золото:+");
                    if (membersLive.contains(user.getUserId())) {
                        msgText.append(members.size() * 10 - 10);
                        msgText.append("/").append(user.getMoney() + members.size() * 10 - 10).append(")");
                    } else {
                        msgText.append("0");
                        msgText.append("/").append(user.getMoney()).append(")");
                    }
                    membersId.add(user.getUserId());
                }
            }
            msgText.append("\n");
        }
        msg.enableHtml(true);
        msg.setText(msgText.toString());
        try {
            for (Integer id : membersId) {
                msg.setChatId((long) id);
                arenaBot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendToAll(List<ArenaUser> members, String msgText) {

        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            for (ArenaUser user : members) {
                msg.setChatId((long) user.getUserId());
                arenaBot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendToAll(List<ArenaUser> members, SendMessage msg) {

        try {
            for (ArenaUser user : members) {
                msg.setChatId((long) user.getUserId());
                arenaBot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendToAllMembers(List<Member> members, String msgText) {

        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            for (Member user : members) {
                msg.setChatId((long) user.getUserId());
                arenaBot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendDoMsg(AbsSender absSender, Long chatId, String action, int target, int percent) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        StringBuilder out = new StringBuilder();
        switch (action) {//todo from db
            case "a"://todo msg from class
                out.append("Атаковать игрока ");
                break;
            case "p":
                out.append("Защищать игрока ");
                break;
            case "h":
                out.append("Лечить игрока ");
                break;
            case "m":
                out.append("Вы пробуете творить заклинание на игрока ");
                break;
            default:
                out.append("Нет пока такого действия. Пожалуйтесь разработчикам!");
                msg.setText(out.toString());
                try {
                    absSender.sendMessage(msg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
        }
        out.append("<b>").append(ArenaUser.getUserName(Round.getCurMembersId().get(target)))
                .append("</b> на ").append(percent).append(" процентов");
        msg.setText(out.toString());
        try {
            absSender.sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendRegMsg(int userId) {

        SendMessage msg = new SendMessage();
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
        msg.setReplyMarkup(remove);
        msg.setText("проверка");
        msg.setChatId((long)userId);
        StringBuilder messageText = new StringBuilder();
        messageText.append("<b>").append(ArenaUser.getUserName(userId)).append("</b> (")
                .append(ArenaUser.getUser(userId).getClassName()).append("/")
                .append(ArenaUser.getUser(userId).getRaceName()).append(" уровень:")
                .append(ArenaUser.getUser(userId).getLevel()).append(")")
                .append(" вошел в команду ").append(ArenaBot.registration.getMemberTeam(userId));
        sendToAllMembers(ArenaBot.registration.getMembers(), messageText.toString());
        try {
            arenaBot.sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(AbsSender absSender, Long chatId, String messageText) {

        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(messageText);
        msg.setChatId(chatId);
        try {
            absSender.sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Long chatId, String messageText) {

        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(messageText);
        msg.setChatId(chatId);
        try {
            arenaBot.sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendChooseClassMsg(AbsSender absSender, Long chatId) {

        StringBuilder msgText = new StringBuilder();
        msgText.append("<b>Доступные классы:</b>\n\n");
        List<String> descr = ArenaUser.getClassesDescr();
        List<String> callbackData = new LinkedList<>();
        List<String> classesId = ArenaUser.getClassesId();
        int count = descr.size();
        for (int i = 0; i < count; i++) {
            msgText.append(descr.get(i)).append("\n\n");
            String callbackText = "newClassIs_" + classesId.get(i);
            callbackData.add(callbackText);
        }
        msgText.append("<b>Выберите класс персонажа:</b>\n\n");
        SendMessage chooseClassMsg = getInlineKeyboardMsg(chatId, msgText.toString(), ArenaUser.getClassesName(), callbackData);
        try {
            absSender.sendMessage(chooseClassMsg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static SendMessage toOpenPrivateWithBotMsg(Long chatId, User user) {

        SendMessage answer = new SendMessage();
        StringBuilder messageBuilder = new StringBuilder();
        String userName;
        if (user.getFirstName() != null) {
            if (user.getLastName() != null) {
                userName = user.getFirstName() + " " + user.getLastName();
            } else userName = user.getFirstName();
        } else userName = user.getLastName();
        messageBuilder.append("Добро пожаловать, ").append(userName).append("!\n");
        messageBuilder.append("<b>#arena</b> - ролевая чат игра, где вы можете сразиться с другими игроками.\n");
        messageBuilder.append("Нажмите кнопку для начала.");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new LinkedList<>();
        List<InlineKeyboardButton> row = new LinkedList<>();
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Начать игру");
        startButton.setUrl(Config.BOT_LINK);
        row.add(startButton);
        rows.add(row);
        markup.setKeyboard(rows);
        answer.enableHtml(true);
        answer.setReplyMarkup(markup);
        answer.setChatId(chatId);
        answer.setText(messageBuilder.toString());
        return answer;
    }

    static void sendAskActionId(CallbackQuery callbackQuery, int targetId) {

        String queryId = callbackQuery.getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        int userId = callbackQuery.getFrom().getId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Вы выбрали цель: " + ArenaUser.getUserName(targetId));
        query.setCallbackQueryId(queryId);
        try {
            arenaBot.sendMessage(Messages.getInlineKeyboardMsg(chatId, "Выберите действие:",
                    ArenaUser.getUser(userId).getActionsName(), ArenaUser.getUser(userId).getActionsId()));
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static void sendAskPercent(CallbackQuery callbackQuery, String actionName) {

        String queryId = callbackQuery.getId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Вы выбрали: " + actionName);
        query.setCallbackQueryId(queryId);
        EditMessageText editText = new EditMessageText();
        editText.setChatId(callbackQuery.getMessage().getChatId());
        editText.setMessageId(callbackQuery.getMessage().getMessageId());
        editText.setText("Очки действия из 100:");
        EditMessageReplyMarkup markup = new EditMessageReplyMarkup();
        markup.setChatId(callbackQuery.getMessage().getChatId());
        markup.setMessageId(callbackQuery.getMessage().getMessageId());
        List<String> buttonText = new ArrayList<>();
        List<String> buttonData = new ArrayList<>();
        buttonText.add("100");
        buttonText.add("70");
        buttonText.add("50");
        buttonText.add("30");
        buttonData.add("percent_100");
        buttonData.add("percent_70");
        buttonData.add("percent_50");
        buttonData.add("percent_30");
        markup.setReplyMarkup(gerInlineKeyboardMarkup(buttonText, buttonData));
        try {
            arenaBot.editMessageText(editText);
            arenaBot.editMessageReplyMarkup(markup);
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static void sendActionTaken(CallbackQuery callbackQuery) {

        String queryId = callbackQuery.getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setCallbackQueryId(queryId);
        EditMessageText editText = new EditMessageText();
        editText.setChatId(chatId);
        editText.setMessageId(callbackQuery.getMessage().getMessageId());
        editText.setText("Заказ принят:");
        EditMessageReplyMarkup markup = new EditMessageReplyMarkup();
        markup.setChatId(chatId);
        markup.setMessageId(callbackQuery.getMessage().getMessageId());
        try {
            arenaBot.editMessageText(editText);
            arenaBot.editMessageReplyMarkup(markup);
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static void sendAskSpell(CallbackQuery callbackQuery) {

        String queryId = callbackQuery.getId();
        int userId = callbackQuery.getFrom().getId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Вы выбрали магию");
        query.setCallbackQueryId(queryId);
        EditMessageText editText = new EditMessageText();
        editText.setChatId(callbackQuery.getMessage().getChatId());
        editText.setMessageId(callbackQuery.getMessage().getMessageId());
        editText.setText("Выберите заклинание:");
        EditMessageReplyMarkup markup = new EditMessageReplyMarkup();
        markup.setChatId(callbackQuery.getMessage().getChatId());
        markup.setMessageId(callbackQuery.getMessage().getMessageId());
        markup.setReplyMarkup(gerInlineKeyboardMarkup(ArenaUser.getUser(userId).getCastsName(), ArenaUser.getUser(userId).getCastsId()));
        try {
            arenaBot.editMessageText(editText);
            arenaBot.editMessageReplyMarkup(markup);
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static void sendEmptyAnswerQuery(CallbackQuery callbackQuery) {

        String queryId = callbackQuery.getId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setCallbackQueryId(queryId);
        try {
            arenaBot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static void deleteMessage(CallbackQuery callbackQuery){

        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setChatId((long)callbackQuery.getFrom().getId());
        edit.setMessageId(callbackQuery.getMessage().getMessageId());
        EditMessageText editText = new EditMessageText();
        editText.setChatId((long)callbackQuery.getFrom().getId());
        editText.setMessageId(callbackQuery.getMessage().getMessageId());
        editText.setText("");
        try {
            arenaBot.editMessageReplyMarkup(edit);
            arenaBot.editMessageText(editText);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

}
