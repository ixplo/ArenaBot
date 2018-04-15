package ml.ixplo.arenabot;

import ml.ixplo.arenabot.battle.Registration;
import ml.ixplo.arenabot.battle.Team;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.callbacks.Callbacks;
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
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
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
    private Registration registration = new Registration();

    public Bot() {
        this(DatabaseManager.getInstance());
    }

    /**
     * Construct new bot with selected database
     * @param db - database to use
     */
    public Bot(DatabaseManager db) {

        registerCommands();
        setDb(db);
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
            getRegisteredCommand("help").execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    private void registerCommands() {
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
        register(new CmdHelp(this));
    }

    /**
     * Set database to bot
     * @param db - database to use
     */
    public void setDb(DatabaseManager db) {
        Registration.setDb(db);
        ArenaUser.setDb(db);
        Item.setDb(db);
        Action.setDb(db);
        Team.setDb(db);
    }

    public Registration getRegistration() {
        return registration;
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
            //no-op
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
        try {
            Callbacks.handleCallbackCommand(callbackQuery, this);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
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
