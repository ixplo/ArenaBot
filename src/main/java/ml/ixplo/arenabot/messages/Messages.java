package ml.ixplo.arenabot.messages;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.IUser;
import ml.ixplo.arenabot.user.items.Item;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
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

import static ml.ixplo.arenabot.config.Config.CLOSE_TAG;

/**
 * ixplo
 * 29.04.2017.
 */
public final class Messages {

    public static final String LOGTAG = "MESSAGES";
    public static final String END_OF_ROUND_REMINDER = "<b>Осталось 15 секунд до конца раунда!</b>";
    private static final String SENDING_MESSAGE_ERROR = "Sending message error";

    /***** no getInstance for this class *****/
    private Messages() {
        throw new UnsupportedOperationException();
    }

    /***** uses for send messages ******/
    private static Bot bot;

    /***** DI *****/
    public static void setBot(Bot bot) {
        Messages.bot = bot;
    }

    public static AnswerInlineQuery getAnswerForInlineQuery(InlineQuery inlineQuery) {

        InlineQueryResultArticle article = new InlineQueryResultArticle()
                .setId(inlineQuery.getId())
                .setUrl(Config.BOT_PRIVATE)
                .setInputMessageContent(new InputTextMessageContent());
        return new AnswerInlineQuery().setInlineQueryId(inlineQuery.getId()).setResults(article);
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

    public static void sendChannelMsg(Long chatId, String msgText) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            bot.sendMessage(msg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR);
        }
    }

    public static int sendChannelMsgReturnId(Long chatId, String msgText) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        msg.setText(msgText);
        try {
            return bot.sendMessage(msg).getMessageId();
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR);
        }
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
            throw new ArenaUserException(SENDING_MESSAGE_ERROR);
        }
    }


    public static void sendToAll(List<? extends IUser> members, String msgText) {

        SendMessage msg = getSendToAllMessage(msgText);
        try {
            for (IUser user : members) {
                msg.setChatId((long) user.getUserId());
                bot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR);
        }
    }

    public static SendMessage getSendToAllMessage(String msgText) {
        SendMessage msg = new SendMessage();
        msg.enableHtml(true);
        msg.setText(msgText);
        return msg;
    }

    public static void sendToAll(List<? extends IUser> members, SendMessage msg) {
        if (msg == null) {
            throw new IllegalArgumentException("SendMessage cant be null");
        }
        try {
            for (IUser user : members) {
                msg.setChatId((long) user.getUserId());
                bot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
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
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
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
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
        }
    }

    public static void sendMessage(SendMessage message) {

        try {
            bot.sendMessage(message);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
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
            if (Item.isItemInSlot(userId, i)) {
                out.append(".<b>").append(items.get(i).getName()).append("</b>, ");
            } else {
                out.append(".").append(items.get(i).getName()).append(", ");
            }
        }
        out.append("\nОружие: <b>");
        out.append(Item.getItem(Item.getItemId(arenaUser.getUserId(), arenaUser.getCurWeaponIndex())).getName());
        out.append("</b>(");
        out.append(Item.getItem(Item.getItemId(arenaUser.getUserId(), arenaUser.getCurWeaponIndex())).getPrice());
        out.append(")");

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
        out.append(fillWithSpaces("<code>Опыт:", arenaUser.getExperience() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Жизни:", arenaUser.getCurHitPoints() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Золото:", arenaUser.getMoney() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Сила:", arenaUser.getCurStr() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Ловкость:", arenaUser.getCurDex() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Мудрость:", arenaUser.getCurWis() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Интеллект:", arenaUser.getCurInt() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Телосложение:", arenaUser.getCurCon() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Свободные очки:", arenaUser.getFreePoints() + CLOSE_TAG, Config.WIDTH));
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
        out.append(fillWithSpaces("<code>Урон:", arenaUser.getMinHit() + "-" + arenaUser.getMaxHit() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Атака:", arenaUser.getAttack() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Защита:", arenaUser.getProtect() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Лечение:", arenaUser.getHeal() + CLOSE_TAG, Config.WIDTH));
        out.append(fillWithSpaces("<code>Защ. от магии:", arenaUser.getMagicProtect() + "</code>", Config.WIDTH));
        arenaUser.appendClassXstatMsg(out);
        SendMessage msg = new SendMessage();
        msg.setChatId((long) userId);
        msg.enableHtml(true);
        msg.setText(out.toString());
        return msg;
    }

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

    public static SendMessage getRegistrationListMsg(Long chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        Registration registration = bot.getRegistration();
        if (!registration.isOn()) {
            msg.setText("Бой уже идет");
            return msg;
        }
        if (registration.getMembersCount() == 0) {
            msg.setText("Еще никто не зарегистрировался");
            return msg;
        }
        msg.setText(registration.getListOfMembersToString());
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
        String messageText = "Добро пожаловать, " + userName + "!\n" +
                "<b>#arena</b> - ролевая чат игра, где вы можете сразиться с другими игроками.\n" +
                "Нажмите кнопку для начала.";
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
        answer.setText(messageText);
        return answer;
    }

    public static SendMessage getAskActionMsg(int userId) {
        return getInlineKeyboardMsg((long) userId, "Выберите действие:",
                ArenaUser.getUser(userId).getActionsName(), ArenaUser.getUser(userId).getActionsIdForInlineKeyboard());
    }


    /*todo переделать все под один универсальный метод, а текст сообщения хранить в xml
    конкретный результат определять по коду
    getAnswerCallbackQuery(String code, Map<String, Object> inParams)
    вынести callbackQuery в отдельный класс*/
    public static AnswerCallbackQuery getAnswerCallbackQuery(String queryId, String queryText) {
        if (queryText == null) {
            return new AnswerCallbackQuery().setCallbackQueryId(queryId);
        }
        return new AnswerCallbackQuery().setCallbackQueryId(queryId).setText(queryText);
    }

    public static AnswerCallbackQuery getEmptyQuery(String queryId) {
        return getAnswerCallbackQuery(queryId, null);
    }

    public static AnswerCallbackQuery getSelectTargetQuery(String queryId, int targetId) {
        return new AnswerCallbackQuery().setCallbackQueryId(queryId)
                .setText("Вы выбрали цель: " + ArenaUser.getUserName(targetId));
    }

    public static AnswerCallbackQuery selectedUserClassQuery(String queryId, String userClass) {
        return new AnswerCallbackQuery().setCallbackQueryId(queryId)
                .setText("Вы выбрали класс: " + ArenaUser.getClassName(userClass));
    }

    public static AnswerCallbackQuery getCreateUserQuery(String queryId, String userClass, String userRace) {
        return new AnswerCallbackQuery().setCallbackQueryId(queryId)
                .setText("Ваш персонаж " + userClass + "/" + userRace + " создан!");
    }

    public static AnswerCallbackQuery getCancelDeleteQuery(String queryId) {
        return new AnswerCallbackQuery().setText("Вы отменили удаление").setCallbackQueryId(queryId);
    }

    public static AnswerCallbackQuery getAfterDeleteQuery(String queryId) {
        return new AnswerCallbackQuery().setText("Персонаж удален").setCallbackQueryId(queryId);
    }

    public static EditMessageReplyMarkup getEditMessageReplyMarkup(int userId, Integer messageId) {
        return new EditMessageReplyMarkup().setChatId((long) userId).setMessageId(messageId);
    }

    public static EditMessageText getAfterDeleteMessageText(int userId, Integer messageId) {
        return new EditMessageText().setChatId((long) userId).setMessageId(messageId).setText("Персонаж удален");
    }

    public static EditMessageText getActionTakenEditMsg(int userId, Integer messageId) {
        return new EditMessageText().setChatId((long) userId).setMessageId(messageId).setText("Заказ принят:");
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
            for (IUser user : team.getMembers()) {
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
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
        }
    }

    public static SendMessage getRoundTeamsList(Long chatId, List<Team> teams) {

        SendMessage message = new SendMessage();
        StringBuilder msgText = new StringBuilder();
        msgText.append("Список: ");
        int count = 0;
        for (Team team : teams) {
            msgText.append("[");
            for (IUser user : team.getMembers()) {
                msgText.append(++count).append(" ");
                msgText.append(user.getName()).append(" ");
            }
            msgText.append("]");
        }
        message.enableHtml(true);
        message.setText(msgText.toString());
        message.setChatId(chatId);
        return message;
    }

    public static void sendResultToAll(List<Team> teams, List<ArenaUser> members, List<Integer> membersLive) {
        SendMessage msg = getBattleResultMessage(teams, members, membersLive);
        try {
            for (ArenaUser arenaUser : members) {
                msg.setChatId((long) arenaUser.getUserId());
                bot.sendMessage(msg);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static SendMessage getBattleResultMessage(List<Team> teams, List<ArenaUser> members, List<Integer> membersLive) {
        SendMessage msg = new SendMessage();
        StringBuilder msgText = new StringBuilder();
        for (Team team : teams) {
            msgText.append("Команда ").append(team.getName()).append(": ");
            for (ArenaUser arenaUser : members) {
                if (team.getBattleMembersId().contains(arenaUser.getUserId())) {
                    msgText.append(arenaUser.getName()).append("(опыт:+").append(arenaUser.getCurExp());
                    msgText.append("/").append(arenaUser.getCurExp() + arenaUser.getExperience());
                    msgText.append(", золото:+");
                    if (membersLive.contains(arenaUser.getUserId())) {
                        msgText.append(members.size() * Config.GOLD_FOR_MEMBER - Config.GOLD_FOR_MEMBER);
                        msgText.append("/").append(arenaUser.getMoney()
                                + members.size() * Config.GOLD_FOR_MEMBER - Config.GOLD_FOR_MEMBER).append(")");
                    } else {
                        msgText.append("0");
                        msgText.append("/").append(arenaUser.getMoney()).append(")");
                    }
                }
            }
            msgText.append("\n");
        }
        msg.enableHtml(true);
        msg.setText(msgText.toString());
        return msg;
    }

    public static void sendDoMsg(AbsSender absSender, Long chatId, String action, int target, int percent) {

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);
        StringBuilder out = new StringBuilder();
        switch (action) { //todo from db
            case Action.ATTACK:
                out.append("Атаковать игрока ");
                break;
            case Action.PROTECT:
                out.append("Защищать игрока ");
                break;
            case Action.HEAL:
                out.append("Лечить игрока ");
                break;
            case Action.MAGIC:
                out.append("Вы пробуете творить заклинание на игрока ");
                break;
            default:
                out.append("Нет пока такого действия. Пожалуйтесь разработчикам!");
                msg.setText(out.toString());
        }
        if (msg.getText() == null) {
            out.append("<b>").append(ArenaUser.getUserName(Round.getCurrent().getCurMembersId().get(target)))
                    .append("</b> на ").append(percent).append(" процентов");
            msg.setText(out.toString());
        }
        try {
            absSender.sendMessage(msg);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
        }
    }

    public static SendMessage getRegMemberMsg(int userId, String teamName) {
        return new SendMessage().setChatId((long) userId).enableHtml(true).setText("</b> ("
                + ArenaUser.getUserName(userId) + "/"
                + ArenaUser.getUser(userId).getRaceName()
                + " уровень:" + ArenaUser.getUser(userId).getLevel() + ")"
                + " вошел в команду " + teamName);
    }

    public static void sendAskPercent(String queryId, long chatId, int messageId, String actionName) {
        try {
            bot.editMessageText(getEditMessageText(chatId, messageId, "Очки действия из 100:"));
            bot.editMessageReplyMarkup(getEditMessageReplyMarkup(chatId, messageId));
            bot.answerCallbackQuery(getAnswerCallbackQuery(queryId, "Вы выбрали: " + actionName));
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
        }
    }

    private static EditMessageText getEditMessageText(long chatId, int messageId, String text) {
        EditMessageText editText = new EditMessageText();
        editText.setChatId(chatId)
                .setMessageId(messageId)
                .setText(text);
        return editText;
    }

    private static EditMessageReplyMarkup getEditMessageReplyMarkup(long chatId, int messageId) {
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
        markup.setChatId(chatId)
                .setMessageId(messageId)
                .setReplyMarkup(getInlineKeyboardMarkup(buttonText, buttonData));
        return markup;
    }

    public static void sendAskSpell(String queryId, int userId, long chatId, int messageId) {
        try {
            bot.editMessageText(getEditMessageText(chatId, messageId, "Выберите заклинание:"));
            bot.editMessageReplyMarkup(getEditMessageReplyMarkup(userId, chatId, messageId));
            bot.answerCallbackQuery(getAnswerCallbackQuery(queryId, "Вы выбрали магию"));
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException(SENDING_MESSAGE_ERROR, e);
        }
    }

    private static EditMessageReplyMarkup getEditMessageReplyMarkup(int userId, Long chatId, Integer messageId) {
        EditMessageReplyMarkup markup = new EditMessageReplyMarkup();
        markup.setChatId(chatId);
        markup.setMessageId(messageId);
        markup.setReplyMarkup(getInlineKeyboardMarkup(ArenaUser.getUser(userId).getCastsName(), ArenaUser.getUser(userId).getCastsIdForCallbacks()));
        return markup;
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
