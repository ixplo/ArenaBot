package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ixplo
 * 03.05.2017.
 */
public class RegTimerTask extends TimerTask {
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
        if (passedTime < 1000) {
            messageId = Messages.sendChannelMsgReturnId(Config.CHANNEL_ID, registration.getList() +
                    "\nДо начала боя осталось: " + leftToReg + " сек");
            return;
        }
        Messages.editChannelMsg(Config.CHANNEL_ID, messageId, registration.getList() +
                "\nДо начала боя осталось: " + (leftToReg - passedTime / 1000) + " сек");
        if (leftToReg * 1000 - passedTime <= 0) {
            regTimer.cancel();
            registration.startBattle();
            Messages.editChannelMsg(Config.CHANNEL_ID, messageId, "Битва началась!");
        }
    }

    public void setLeftToReg(int leftToReg) {
        this.leftToReg = leftToReg;
    }

    public int getMessageId() {
        return messageId;
    }
}
