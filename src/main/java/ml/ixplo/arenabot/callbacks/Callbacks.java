package ml.ixplo.arenabot.callbacks;

import ml.ixplo.arenabot.Bot;
import ml.ixplo.arenabot.battle.Battle;
import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.classes.UserClass;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Callbacks {
    private static Bot bot;
    private static CallbackQuery query;
    private static String entry;
    private static Integer userId;
    private static Integer messageId;
    private static String queryId;
    private static String callbackCommand;

    /***** no getInstance for this class *****/
    private Callbacks() {
        throw new UnsupportedOperationException();
    }

    public static void handleCallbackCommand(CallbackQuery callbackQuery, Bot bot) throws TelegramApiException {
        initStaticVariables(callbackQuery, bot);
        switch (callbackCommand) {
            case "newClassIs":
                handleNewClassIs();
                break;
            case "newRaceIs":
                handleNewRaceIs();
                break;
            case "del":
                handleDel();
                break;
            case "reg":
                handleReg();
                break;
            case "learnSpell":
                handleLearnSpell();
                break;
            case "target":
                handleTarget();
                break;
            case "spell":
                handleSpell();
                break;
            case "action":
                handleAction();
                break;
            case "percent":
                handlePercent();
                break;
            default:
                throw new ArenaUserException("Unknown query: " + query.getData());
        }
    }

    private static void handlePercent() throws TelegramApiException {
        Action.setPercent(userId, Integer.parseInt(entry));
        Battle.getBattle().interrupt();
        Action action = Action.create(userId,
                Action.getActionType(userId, 1),
                Action.getTargetId(userId, 1),
                Action.getPercent(userId, 1),
                Action.getSpellId(userId, 1));
        Round.getCurrent().takeAction(action);
        bot.answerCallbackQuery(Messages.getEmptyQuery(queryId));
        bot.editMessageText(Messages.getActionTakenEditMsg(userId, messageId));
        Action.clearActions(userId);
    }

    private static void handleAction() {
        Action.setActionId(userId, entry);
        Messages.sendAskPercent(query, entry);
    }

    private static void handleSpell() {
        if (entry.equals("spell")) {
            Messages.sendAskSpell(query);
            return;
        }
        Action.setCastId(userId, entry);
        Action.setActionId(userId, "Магия");
        Messages.sendAskPercent(query, "Магия");
    }

    private static void handleTarget() throws TelegramApiException {
        Action.addAction(userId);
        Action.setTargetId(userId, Integer.parseInt(entry));
        bot.answerCallbackQuery(Messages.getSelectTargetQuery(
                queryId, Integer.parseInt(entry)));
        bot.sendMessage(Messages.getAskActionMsg(userId));
    }

    private static void handleLearnSpell() throws TelegramApiException {
        bot.answerCallbackQuery(Messages.getEmptyQuery(queryId));
        ArenaUser.getUser(userId).learn(Integer.parseInt(entry));
    }

    private static void handleReg() throws TelegramApiException {
        bot.getRegistration().regMember(userId);
        bot.answerCallbackQuery(Messages.getEmptyQuery(queryId));
        Messages.sendToAll(
                bot.getRegistration().getMembers(),
                Messages.getRegMemberMsg(userId, Team.getTeamId(userId))
        );
    }

    private static void handleDel() throws TelegramApiException {
        if (entry.equals("Cancel")) {
            bot.answerCallbackQuery(Messages.getCancelDeleteQuery(queryId));
            bot.editMessageReplyMarkup(Messages.getEditMessageReplyMarkup(userId, messageId));
        } else if (entry.equals("Delete")) {
            ArenaUser.dropUser(userId);
            bot.editMessageReplyMarkup(Messages.getEditMessageReplyMarkup(userId, messageId));
            bot.editMessageText(Messages.getAfterDeleteMessageText(userId, messageId));
            bot.answerCallbackQuery(Messages.getAfterDeleteQuery(queryId));
        }
    }

    private static void handleNewRaceIs() throws TelegramApiException {
        String userName = query.getFrom().getFirstName() == null ?
                query.getFrom().getLastName() :
                query.getFrom().getFirstName();
        String userRace = entry.substring(0, 1);
        String userClass = entry.substring(1);
        ArenaUser.create(userId, userName, UserClass.valueOf(userClass), userRace);
        bot.answerCallbackQuery(Messages.getCreateUserQuery(
                queryId,
                ArenaUser.getClassName(userClass),
                ArenaUser.getRaceName(userRace)));
        bot.sendMessage(Messages.getCreateUserMsg(userId));
    }

    private static void handleNewClassIs() throws TelegramApiException {
        bot.answerCallbackQuery(Messages.selectedUserClassQuery(queryId, entry));
        bot.sendMessage(Messages.getChooseRaceMsg(query.getMessage().getChatId(), entry));
    }

    private static void initStaticVariables(CallbackQuery callbackQuery, Bot bot) {
        Callbacks.bot = bot;
        query = callbackQuery;
        callbackCommand = query.getData().substring(0, query.getData().indexOf('_'));
        entry = query.getData().substring(query.getData().indexOf('_') + 1);
        userId = query.getFrom().getId();
        messageId = query.getMessage().getMessageId();
        queryId = query.getId();
    }
}
