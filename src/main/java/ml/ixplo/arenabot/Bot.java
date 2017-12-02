package ml.ixplo.arenabot;

import ml.ixplo.arenabot.battle.Battle;
import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.commands.CmdDo;
import ml.ixplo.arenabot.commands.CmdDrop;
import ml.ixplo.arenabot.commands.CmdDropStatus;
import ml.ixplo.arenabot.commands.CmdEq;
import ml.ixplo.arenabot.commands.CmdEx;
import ml.ixplo.arenabot.commands.CmdHark;
import ml.ixplo.arenabot.commands.CmdHelp;
import ml.ixplo.arenabot.commands.CmdList;
import ml.ixplo.arenabot.commands.CmdNick;
import ml.ixplo.arenabot.commands.CmdPutOff;
import ml.ixplo.arenabot.commands.CmdPutOn;
import ml.ixplo.arenabot.commands.CmdReg;
import ml.ixplo.arenabot.commands.CmdStart;
import ml.ixplo.arenabot.commands.CmdStat;
import ml.ixplo.arenabot.commands.CmdUnreg;
import ml.ixplo.arenabot.commands.CmdXstat;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * ixplo
 * 24.04.2017.
 */
public class Bot extends TelegramLongPollingCommandBot {

    private static final String LOGTAG = "ARENABOT";
    public static Registration registration;

    public Bot() {
        this(DatabaseManager.getInstance());
    }

    public Bot(DatabaseManager db) {

        register(new CmdStart());
        register(new CmdReg());
        register(new CmdUnreg());
        register(new CmdDrop());
        register(new CmdNick());
        register(new CmdStat());
        register(new CmdXstat());
        register(new CmdHark());
        register(new CmdEq());
        register(new CmdPutOn());
        register(new CmdPutOff());
        register(new CmdEx());
        register(new CmdDo());
        register(new CmdList());
        register(new CmdDropStatus());
        CmdHelp cmdHelp = new CmdHelp(this);
        register(cmdHelp);

        setDb(db);
        registration = new Registration();
        Messages.setBot(this);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("Команда '" + message.getText() + "' неизвестна боту.");
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            cmdHelp.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    public void setDb(DatabaseManager db) {
        ArenaUser.setDb(db);
        Item.setDb(db);
        Registration.setDb(db);
        Action.setDb(db);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        } else if (update.hasInlineQuery()) {
            handleInlineQuery(update.getInlineQuery());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleMessage(Message message) {
        if (!ArenaUser.doesUserExists(message.getFrom().getId())) {
            return;
        }
        if (!Config.DO_NOT_COMMAND_UPDATE) {
            return;
        }
        if (message.hasText()) {
            //something useful
        }
    }

    private void handleInlineQuery(InlineQuery inlineQuery) {
        try {
            answerInlineQuery(Messages.getAnswerForInlineQuery(inlineQuery));
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackCommand = callbackQuery.getData().substring(0, callbackQuery.getData().indexOf('_'));
        String callbackEntry = callbackQuery.getData().substring(callbackQuery.getData().indexOf('_') + 1);
        //todo сделать нормально
        switch (callbackCommand) {
            case "newClassIs":
                try {
                    answerCallbackQuery(Messages.selectedUserClassQuery(callbackQuery.getId(), callbackEntry));
                    sendMessage(Messages.getChooseRaceMsg(
                            callbackQuery.getMessage().getChatId(),
                            callbackEntry));
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
                break;
            case "newRaceIs":
                Integer userId = callbackQuery.getFrom().getId();
                String userName = callbackQuery.getFrom().getFirstName() == null ?
                        callbackQuery.getFrom().getLastName() :
                        callbackQuery.getFrom().getFirstName();
                String userRace = callbackEntry.substring(0, 1);
                String userClass = callbackEntry.substring(1);
                ArenaUser.create(userId, userName, ArenaUser.UserClass.valueOf(userClass), userRace);
                try {
                    answerCallbackQuery(Messages.getCreateUserQuery(
                            callbackQuery.getId(),
                            ArenaUser.getClassName(userClass),
                            ArenaUser.getRaceName(userRace)));
                    sendMessage(Messages.getCreateUserMsg(userId));
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
                break;
            case "del":
                if (callbackEntry.equals("Cancel")) {
                    Messages.sendCancelDelete(callbackQuery);
                } else if (callbackEntry.equals("Delete")) {
                    ArenaUser.dropUser(callbackQuery.getFrom().getId());
                    Messages.sendAfterDelete(callbackQuery);
                }
                break;
            case "reg":
                int memberId = callbackQuery.getFrom().getId();
                registration.regMember(memberId);
                try {
                    answerCallbackQuery(Messages.getAnswerCallbackQuery(callbackQuery.getId(), null));
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
                Messages.sendToAllMembers(
                        registration.getMembers(),
                        Messages.getRegMemberMsg(memberId, registration.getMember(memberId).getTeamId())
                );
                break;
            case "learnSpell":
                try {
                    answerCallbackQuery(Messages.getAnswerCallbackQuery(callbackQuery.getId(), null));
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
                ArenaUser.getUser(callbackQuery.getFrom().getId()).learn(Integer.parseInt(callbackEntry));
                break;
            case "target":
                Action.addAction(callbackQuery.getFrom().getId());
                Action.setTargetId(callbackQuery.getFrom().getId(), Integer.parseInt(callbackEntry));
                try {
                    answerCallbackQuery(Messages.getSelectTargetQuery(
                            callbackQuery.getId(), Integer.parseInt(callbackEntry)));
                    sendMessage(Messages.getAskActionMsg(callbackQuery.getFrom().getId()));
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
                break;
            case "spell":
                if (callbackEntry.equals("spell")) {
                    Messages.sendAskSpell(callbackQuery);
                    break;
                }
                Action.setCastId(callbackQuery.getFrom().getId(), callbackEntry);
                Action.setActionId(callbackQuery.getFrom().getId(), "Магия");
                Messages.sendAskPercent(callbackQuery, "Магия");
                break;
            case "action":
                Action.setActionId(callbackQuery.getFrom().getId(), callbackEntry);
                Messages.sendAskPercent(callbackQuery, callbackEntry);
                break;
            case "percent":
                Action.setPercent(callbackQuery.getFrom().getId(), Integer.parseInt(callbackEntry));
                Battle.battle.interrupt();
                Round.round.takeAction(callbackQuery.getFrom().getId(),
                        Action.getActionId(callbackQuery.getFrom().getId(), 1),
                        Action.getTargetId(callbackQuery.getFrom().getId(), 1),
                        Action.getPercent(callbackQuery.getFrom().getId(), 1),
                        Action.getSpellId(callbackQuery.getFrom().getId(), 1));
                Messages.sendActionTaken(callbackQuery);
                Action.clearActions(callbackQuery.getFrom().getId());
                break;
            default: {
                throw new RuntimeException("Unknown callbackQuery: " + callbackQuery.getData());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return Config.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }
}
