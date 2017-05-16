package arenabot;

import arenabot.battle.Battle;
import arenabot.battle.Round;
import arenabot.battle.actions.Action;
import arenabot.commands.*;
import arenabot.battle.Registration;
import arenabot.database.DatabaseManager;
import arenabot.users.ArenaUser;
import arenabot.users.Inventory.Item;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * ixplo
 * 24.04.2017.
 */
public class ArenaBot extends TelegramLongPollingCommandBot {

    private static final String LOGTAG = "ARENABOT";
    public static Registration registration;

    ArenaBot() {

        register(new CmdStart());
        register(new CmdReg());
        register(new CmdUnreg());
        register(new CmdDrop());
        register(new CmdNick());
        register(new CmdStat());
        register(new CmdXstat());
        register(new CmdHark());
        register(new CmdEq());
        register(new CmdEx());
        register(new CmdDo());
        register(new CmdList());
        register(new CmdDropStatus());
        CmdHelp cmdHelp = new CmdHelp(this);
        register(cmdHelp);

        initBot();

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

    private void initBot() {
        setDb(DatabaseManager.getInstance());
        registration = new Registration();
    }

    private void setDb(DatabaseManager db) {
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
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackCommand = callbackQuery.getData().substring(0, callbackQuery.getData().indexOf("_"));
        String callbackEntry = callbackQuery.getData().substring(callbackQuery.getData().indexOf("_") + 1);
        switch (callbackCommand) {
            case "newClassIs":
                Messages.sendAskRace(callbackQuery, callbackEntry);
                break;
            case "newRaceIs":
                Integer userId = callbackQuery.getFrom().getId();
                String userName = callbackQuery.getFrom().getFirstName() == null ?
                        callbackQuery.getFrom().getLastName() :
                        callbackQuery.getFrom().getFirstName();
                String userRace = callbackEntry.substring(0, 1);
                String userClass = callbackEntry.substring(1);
                ArenaUser.setNewUser(userId, userName, ArenaUser.UserClass.valueOf(userClass), userRace);

                userClass = ArenaUser.getClassName(userClass);
                userRace = ArenaUser.getRaceName(userRace);
                Messages.sendCreateUser(callbackQuery, userClass, userRace);
                break;
            case "del":
                if (callbackEntry.equals("Cancel")) {
                    Messages.sendCancelDelete(callbackQuery);
                    break;
                } else if (callbackEntry.equals("Delete")) {
                    ArenaUser.dropUser(callbackQuery.getFrom().getId());
                    Messages.sendAfterDelete(callbackQuery);
                    break;
                }
            case "reg":
                registration.regMember(callbackQuery.getFrom().getId());
                Messages.sendEmptyAnswerQuery(callbackQuery);
                Messages.sendRegMsg(callbackQuery.getFrom().getId());
                break;
            case "target":
                Action.addAction(callbackQuery.getFrom().getId());
                Action.setTargetId(callbackQuery.getFrom().getId(), Integer.parseInt(callbackEntry));
                Messages.sendAskActionId(callbackQuery, Integer.parseInt(callbackEntry));
                break;
            case "spell":
                if(callbackEntry.equals("spell")){
                    Messages.sendAskSpell(callbackQuery);
                    break;
                }
                Action.setCastId(callbackQuery.getFrom().getId(), callbackEntry);
                Action.setActionId(callbackQuery.getFrom().getId(),"Магия");
                Messages.sendAskPercent(callbackQuery, "Магия");
                break;
            case "action":
                Action.setActionId(callbackQuery.getFrom().getId(),callbackEntry);
                Messages.sendAskPercent(callbackQuery, callbackEntry);
                break;
            case "percent":
                Action.setPercent(callbackQuery.getFrom().getId(),Integer.parseInt(callbackEntry));
                Battle.battle.interrupt();
                Round.round.takeAction(callbackQuery.getFrom().getId(),
                        Action.getActionId(callbackQuery.getFrom().getId(),1),
                        Action.getTargetId(callbackQuery.getFrom().getId(),1),
                        Action.getPercent(callbackQuery.getFrom().getId(),1),
                        Action.getSpellId(callbackQuery.getFrom().getId(),1));
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
