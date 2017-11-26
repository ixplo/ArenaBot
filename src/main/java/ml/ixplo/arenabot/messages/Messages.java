package ml.ixplo.arenabot.messages;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Member;
import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ixplo
 * 29.04.2017.
 */
public final class Messages {

    public static final String LOGTAG = "MESSAGES";

    /***** no instance for this class *****/
    private Messages() {
        throw new UnsupportedOperationException();
    }

    /***** uses for send messages ******/
    private static AbsSender bot;

    /***** DI *****/
    public static void setBot(AbsSender bot) {
        Messages.bot = bot;
    }

    public static AnswerInlineQuery getAnswerForInlineQuery(InlineQuery inlineQuery) {

        InlineQueryResultArticle article = new InlineQueryResultArticle()
                .setId(inlineQuery.getId())
                .setUrl(Config.BOT_PRIVATE)
                .setInputMessageContent(new InputTextMessageContent());
        return new AnswerInlineQuery().setInlineQueryId(inlineQuery.getId()).setResults(article);
    }

    //todo наверное, надо вынести в отдельный класс
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

    public static void sendChannelMsg(Long chatId, String msgText) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            bot.sendMessage(msg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static int sendChannelMsgReturnId(Long chatId, String msgText) {

        int id = 0;
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            id = bot.sendMessage(msg).getMessageId();
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
        return id;
    }

    public static void editChannelMsg(long chatId, int msgId, String msgText) {

        EditMessageText editText = new EditMessageText();
        editText.setChatId(chatId);
        editText.setMessageId(msgId);
        editText.setText(msgText);
        try {
            bot.editMessageText(editText);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }


    public static void sendToAll(List<ArenaUser> members, String msgText) {

        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            for (ArenaUser user : members) {
                msg.setChatId((long) user.getUserId());
                bot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendToAll(List<ArenaUser> members, SendMessage msg) {

        try {
            for (ArenaUser user : members) {
                msg.setChatId((long) user.getUserId());
                bot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendToAllMembers(List<Member> members, String msgText) {

        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            for (Member user : members) {
                msg.setChatId((long) user.getUserId());
                bot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
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
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendMessage(Long chatId, String messageText) {

        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(messageText);
        msg.setChatId(chatId);
        try {
            bot.sendMessage(msg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendMessage(SendMessage message) {

        try {
            bot.sendMessage(message);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendEmptyAnswerQuery(CallbackQuery callbackQuery) {

        String queryId = callbackQuery.getId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setCallbackQueryId(queryId);
        try {
            bot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static void deleteMessage(CallbackQuery callbackQuery) {

        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setChatId((long) callbackQuery.getFrom().getId());
        edit.setMessageId(callbackQuery.getMessage().getMessageId());
        EditMessageText editText = new EditMessageText();
        editText.setChatId((long) callbackQuery.getFrom().getId());
        editText.setMessageId(callbackQuery.getMessage().getMessageId());
        editText.setText("");
        try {
            bot.editMessageReplyMarkup(edit);
            bot.editMessageText(editText);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static SendMessage getGreetingsMsg(int userId) {

        String msgText = "Добро пожаловать, " + ArenaUser.getUserName(userId) + "\n"
                + "Список доступных команд: /help\n"
                + getUserStatMsg(userId).getText()
                + getUserXStatMsg(userId).getText()
                + getEqipMsg(userId).getText();
        return new SendMessage().enableHtml(true).setText(msgText).setChatId((long) userId);
    }

    public static SendMessage getEqipMsg(final int userId) {

        ArenaUser arenaUser = ArenaUser.getUser(userId);
        List<Item> items = Item.getItems(userId);
        StringBuilder out = new StringBuilder();
        out.append("Ваш инвентарь: \n");
        int size = items.size();
        for (int i = 0; i < size; i++) {
            out.append(i);
            // bold font for items in slot
            if (Item.isItemInSlot(i, userId)) {
                out.append(".<b>").append(items.get(i).getName()).append("</b>, ");
            } else {
                out.append(".").append(items.get(i).getName()).append(", ");
            }
        }
        out.append("\nОружие: <b>");
        if (arenaUser.getCurWeapon() < 0) {
            out.append("без оружия</b>");
        } else {
            out.append(Item.getItem(Item.getItemId(arenaUser.getUserId(), arenaUser.getCurWeapon())).getName());
            out.append("</b>(");
            out.append(Item.getItem(Item.getItemId(arenaUser.getUserId(), arenaUser.getCurWeapon())).getPrice());
            out.append(")");
        }
        SendMessage msg = new SendMessage();
        msg.setChatId((long) userId);
        msg.enableHtml(true);
        msg.setText(out.toString());
        return msg;
    }

    public static SendMessage getUserStatMsg(int userId) {

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
        out.append("Был в бою: ").append(stringDate).append("\n");
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
        msg.setChatId((long) userId);
        msg.enableHtml(true);
        msg.setText(out.toString());
        return msg;
    }

    public static SendMessage getUserXStatMsg(int userId) {

        ArenaUser arenaUser = ArenaUser.getUser(userId);
        StringBuilder out = new StringBuilder();
        out.append("Ваши расширенные характеристики:\n");
        out.append(fillWithSpaces("<code>Урон:", arenaUser.getMinHit() + "-" + arenaUser.getMaxHit() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Атака:", arenaUser.getAttack() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Защита:", arenaUser.getProtect() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Лечение:", arenaUser.getHeal() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Защ. от магии:", arenaUser.getMagicProtect() + "</code>", Config.WIDTH));
        arenaUser.appendClassXstatMsg(out);
        SendMessage msg = new SendMessage();
        msg.setChatId((long) userId);
        msg.enableHtml(true);
        msg.setText(out.toString());
        return msg;
    }

    //todo переделать и написать тест
    public static SendMessage getChooseRaceMsg(Long chatId, String userClass) {

        StringBuilder msgText = new StringBuilder();
        msgText.append("<b>Доступные расы:</b>\n\n");
        List<String> descr = ArenaUser.getRacesDescr();
        int count = descr.size();
        for (int i = 0; i < count; i++) {
            msgText.append(descr.get(i)).append("\n\n");
        }
        msgText.append("<b>Выберите расу персонажа:</b>");
        List<String> callbackData = new ArrayList<>();
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
        int membersCount = Bot.registration.getMembersCount();
        if (!Registration.isOn) {
            msg.setText("Бой уже идет");
            return msg;
        }
        if (membersCount == 0) {
            msg.setText("Еще никто не зарегистрировался");
            return msg;
        }
        msg.setText(Bot.registration.getList());
        return msg;
    }

    public static SendMessage getInlineKeyboardMsg(Long chatId, String messageText, List<String> buttonsText, List<String> buttonsCallbackData) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(messageText);
        msg.setReplyMarkup(getInlineKeyboardMarkup(buttonsText, buttonsCallbackData));
        msg.enableHtml(true);
        return msg;
    }

    public static SendMessage getDropMsg(int userId) {
        return getInlineKeyboardMsg(
                (long) userId,
                "<b>Удалить</b> персонажа без возможности восстановления?",
                Arrays.asList("Удалить", "Отмена"),
                Arrays.asList("del_Delete", "del_Cancel"));
    }

    public static SendMessage getCreateUserMsg(int userId) {
        String msgText = Messages.getUserStatMsg(userId).getText()
                + Messages.getUserXStatMsg(userId).getText()
                + Messages.getEqipMsg(userId);
        return new SendMessage().setChatId((long) userId).setText(msgText);
    }

    public static AnswerCallbackQuery getCreateUserQuery(String queryId, String userClass, String userRace) {
        return new AnswerCallbackQuery().setCallbackQueryId(queryId)
                .setText("Ваш персонаж " + userClass + "/" + userRace + " создан!");
    }

    public static SendMessage getExMsg(int userId, int eqipIndex) {
        return new SendMessage().enableHtml(true).setChatId((long) userId).setText(Item.getItemInfo(userId, eqipIndex));
    }

    public static SendMessage getChooseClassMsg(Long chatId) {

        StringBuilder msgText = new StringBuilder();
        msgText.append("<b>Доступные классы:</b>\n\n");
        List<String> descr = ArenaUser.getClassesDescr();
        List<String> callbackData = new ArrayList<>();
        List<String> classesId = ArenaUser.getClassesId();
        int count = descr.size();
        for (int i = 0; i < count; i++) {
            msgText.append(descr.get(i)).append("\n\n");
            String callbackText = "newClassIs_" + classesId.get(i);
            callbackData.add(callbackText);
        }
        msgText.append("<b>Выберите класс персонажа:</b>\n\n");
        return getInlineKeyboardMsg(chatId, msgText.toString(), ArenaUser.getClassesName(), callbackData);
    }

    public static SendMessage getOpenPrivateWithBotMsg(int userId, String userName) {

        SendMessage answer = new SendMessage();
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Добро пожаловать, ").append(userName).append("!\n");
        messageBuilder.append("<b>#arena</b> - ролевая чат игра, где вы можете сразиться с другими игроками.\n");
        messageBuilder.append("Нажмите кнопку для начала.");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Начать игру");
        startButton.setUrl(Config.BOT_LINK);
        row.add(startButton);
        rows.add(row);
        markup.setKeyboard(rows);
        answer.enableHtml(true);
        answer.setReplyMarkup(markup);
        answer.setChatId((long) userId);
        answer.setText(messageBuilder.toString());
        return answer;
    }

    public static AnswerCallbackQuery selectedUserClassQuery(String queryId, String userClass) {
        return new AnswerCallbackQuery().setCallbackQueryId(queryId)
                .setText("Вы выбрали класс: " + ArenaUser.getClassName(userClass));
    }

    public static void sendCancelDelete(CallbackQuery callbackQuery) {

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
            bot.editMessageReplyMarkup(edit);
            bot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendAfterDelete(CallbackQuery callbackQuery) {

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
            bot.editMessageReplyMarkup(edit);
            bot.editMessageText(editText);
            bot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendListToAll(List<Team> teams) {

        SendMessage msg = new SendMessage();
        StringBuilder msgText = new StringBuilder();
        List<Integer> membersId = new ArrayList<>();
        List<String> buttonsName = new ArrayList<>();
        List<String> callbacksData = new ArrayList<>();
        msgText.append("Список: ");
        int count = 0;
        for (Team team : teams) {
            msgText.append("[");
            for (Member user : team.getMembers()) {
                msgText.append(++count).append(" ");
                msgText.append(user.getName()).append(" ");
                membersId.add(user.getUserId());
                buttonsName.add(user.getName());
                callbacksData.add("target_" + user.getUserId());
            }
            msgText.append("]");
        }
        msg.enableHtml(true);
        msg.setText(msgText.toString());
        try {
            for (Integer id : membersId) {
                msg.setChatId((long) id);
                bot.sendMessage(msg);
                bot.sendMessage(getInlineKeyboardMsg((long) id, "Выберите цель:", buttonsName, callbacksData));
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
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
            bot.sendMessage(msg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
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
                        msgText.append(members.size() * Config.GOLD_FOR_MEMBER - Config.GOLD_FOR_MEMBER);
                        msgText.append("/").append(user.getMoney()
                                + members.size() * Config.GOLD_FOR_MEMBER - Config.GOLD_FOR_MEMBER).append(")");
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
                bot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendDoMsg(AbsSender absSender, Long chatId, String action, int target, int percent) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        StringBuilder out = new StringBuilder();
        switch (action) { //todo from db
            case "a": //todo msg from class
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
                    BotLogger.error(LOGTAG, e);
                }
                return;
        }
        out.append("<b>").append(ArenaUser.getUserName(Round.getCurMembersId().get(target)))
                .append("</b> на ").append(percent).append(" процентов");
        msg.setText(out.toString());
        try {
            absSender.sendMessage(msg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendRegMsg(int userId) {

        StringBuilder messageText = new StringBuilder();
        messageText.append("<b>").append(ArenaUser.getUserName(userId)).append("</b> (")
                .append(ArenaUser.getUser(userId).getClassName()).append("/")
                .append(ArenaUser.getUser(userId).getRaceName()).append(" уровень:")
                .append(ArenaUser.getUser(userId).getLevel()).append(")")
                .append(" вошел в команду ").append(Bot.registration.getMemberTeam(userId));
        sendToAllMembers(Bot.registration.getMembers(), messageText.toString());

    }

    public static void sendAskActionId(CallbackQuery callbackQuery, int targetId) {

        String queryId = callbackQuery.getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        int userId = callbackQuery.getFrom().getId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Вы выбрали цель: " + ArenaUser.getUserName(targetId));
        query.setCallbackQueryId(queryId);
        try {
            bot.sendMessage(Messages.getInlineKeyboardMsg(chatId, "Выберите действие:",
                    ArenaUser.getUser(userId).getActionsName(), ArenaUser.getUser(userId).getActionsId()));
            bot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendAskPercent(CallbackQuery callbackQuery, String actionName) {

        String queryId = callbackQuery.getId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setText("Вы выбрали: " + actionName)
                .setCallbackQueryId(queryId);
        EditMessageText editText = new EditMessageText();
        editText.setChatId(callbackQuery.getMessage().getChatId())
                .setMessageId(callbackQuery.getMessage().getMessageId())
                .setText("Очки действия из 100:");
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
        EditMessageReplyMarkup markup = new EditMessageReplyMarkup();
        markup.setChatId(callbackQuery.getMessage().getChatId())
                .setMessageId(callbackQuery.getMessage().getMessageId())
                .setReplyMarkup(getInlineKeyboardMarkup(buttonText, buttonData));
        try {
            bot.editMessageText(editText);
            bot.editMessageReplyMarkup(markup);
            bot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendActionTaken(CallbackQuery callbackQuery) {

        String queryId = callbackQuery.getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        AnswerCallbackQuery query = new AnswerCallbackQuery();
        query.setCallbackQueryId(queryId);
        EditMessageText editText = new EditMessageText();
        editText.setChatId(chatId);
        editText.setMessageId(callbackQuery.getMessage().getMessageId());
        editText.setText("Заказ принят:");
        try {
            bot.editMessageText(editText);
            bot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static void sendAskSpell(CallbackQuery callbackQuery) {

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
        markup.setReplyMarkup(getInlineKeyboardMarkup(ArenaUser.getUser(userId).getCastsName(), ArenaUser.getUser(userId).getCastsId()));
        try {
            bot.editMessageText(editText);
            bot.editMessageReplyMarkup(markup);
            bot.answerCallbackQuery(query);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup(List<String> buttonsText, List<String> buttonsCallbackData) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int buttonsAmount = buttonsText.size();
        int rowsAmount = buttonsAmount / 4 + (buttonsAmount % 4 == 0 ? 0 : 1);
        int buttonsCounter = 0;
        for (int i = 0; i < rowsAmount; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
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

}
