package arenabot.battle;

import arenabot.Messages;

import java.util.TimerTask;

public class RemindAboutEndRound extends TimerTask {
    Round round;
    RemindAboutEndRound(Round round) {
        this.round = round;
    }

    @Override
    public void run() {
        Messages.sendToAll(round.getMembers(), "<b>Осталось 15 секунд до конца раунда!</b>");
    }
}
