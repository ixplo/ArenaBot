package arenabot.battle;

import arenabot.Messages;

import java.util.TimerTask;

public class EndRoundReminder extends TimerTask {
    Round round;
    EndRoundReminder(Round round) {
        this.round = round;
    }

    @Override
    public void run() {
        Messages.sendToAll(round.getMembers(), "<b>Осталось 15 секунд до конца раунда!</b>");
    }
}
