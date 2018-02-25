package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.messages.Messages;

import java.util.TimerTask;

public class RemindAboutEndOfRound extends TimerTask {
    private Round round;
    RemindAboutEndOfRound(Round round) {
        this.round = round;
    }

    @Override
    public void run() {
        Messages.sendToAll(round.getMembers(), Messages.END_OF_ROUND_REMINDER);
    }
}
