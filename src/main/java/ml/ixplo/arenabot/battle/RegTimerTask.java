package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.messages.Messages;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ixplo
 * 03.05.2017.
 */
public class RegTimerTask extends TimerTask {
    private static final int SECOND = 1000;
    private int leftToReg;
    private long startTime;
    private Registration registration;
    private Timer regTimer;
    private int messageId;

    public RegTimerTask(Registration registration, Timer regTimer, int leftToReg) {
        this.registration = registration;
        this.regTimer = regTimer;
        this.leftToReg = leftToReg;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        long passedTime = System.currentTimeMillis() - startTime;
        if (rememberEveryMilliseconds(passedTime)) {
            return;
        }
        if (leftToReg * SECOND - passedTime <= 0) {
            regTimer.cancel();
            registration.startBattle();
            Messages.editChannelMsg(PropertiesLoader.getInstance().getChannelId(), messageId, "Битва началась!");
        }
    }

    private boolean rememberEveryMilliseconds(long passedTime) {
        if (itIsAFirstTime(passedTime)) {
            messageId = Messages.sendChannelMsgReturnId(PropertiesLoader.getInstance().getChannelId(), registration.getListOfMembersToString() +
                    "\nДо начала боя осталось: " + leftToReg + " сек");
            return true;
        }
        Messages.editChannelMsg(PropertiesLoader.getInstance().getChannelId(), messageId, registration.getListOfMembersToString() +
                "\nДо начала боя осталось: " + (leftToReg - passedTime / SECOND) + " сек");
        return false;
    }

    private boolean itIsAFirstTime(long passedTime) {
        return passedTime < SECOND;
    }

    public void setLeftToReg(int leftToReg) {
        this.leftToReg = leftToReg;
    }

    public int getMessageId() {
        return messageId;
    }
}
