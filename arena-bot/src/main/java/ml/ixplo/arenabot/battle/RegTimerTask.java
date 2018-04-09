package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.messages.Messages;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Reminder of the time before the battle began
 * Every one second from delayToRegistration edit message like this:
 * "15 seconds left to start the battle"
 */
public class RegTimerTask extends TimerTask {
    private static final int MILLIS_IN_SECOND = 1000;
    private static final long CHANNEL_ID = PropertiesLoader.getInstance().getChannelId();
    private Registration registration;
    private Timer regTimer;
    private int delayToRegistration;
    private long startTime;
    private long passedTimeInMillis;
    private long timeToBattleBegin;
    private int messageId;
    private String msgText;

    /**
     * Constructor set variables
     * @param registration - current Registration
     * @param regTimer     - current timer
     * @param delay        - count time to battle begin
     */
    public RegTimerTask(Registration registration, Timer regTimer, int delay) {
        this.registration = registration;
        this.regTimer = regTimer;
        this.delayToRegistration = delay;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        calculateTime();
        prepareMessageText();
        if (thisIsTheFirstTime()) {
            messageId = Messages.sendChannelMsgReturnId(CHANNEL_ID, msgText);
            return;
        }
        if (timeIsOver()) {
            startBattle();
        }
        Messages.editChannelMsg(CHANNEL_ID, messageId, msgText);
    }

    private void calculateTime() {
        passedTimeInMillis = System.currentTimeMillis() - startTime;
        timeToBattleBegin = delayToRegistration - passedTimeInMillis / MILLIS_IN_SECOND;
    }

    private void prepareMessageText() {
        msgText = registration.getListOfMembersToString()
                + "\nДо начала боя осталось: " + timeToBattleBegin + " сек";
    }

    private boolean thisIsTheFirstTime() {
        return passedTimeInMillis < MILLIS_IN_SECOND;
    }

    private boolean timeIsOver() {
        return delayToRegistration * MILLIS_IN_SECOND - passedTimeInMillis <= 0;
    }

    private void startBattle() {
        regTimer.cancel();
        registration.startBattle();
        msgText = "Битва началась!";
    }

    void setDelayToRegistration(int delayToRegistration) {
        this.delayToRegistration = delayToRegistration;
    }

    int getMessageId() {
        return messageId;
    }
}
