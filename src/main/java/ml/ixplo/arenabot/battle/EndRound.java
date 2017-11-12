package ml.ixplo.arenabot.battle;

import java.util.TimerTask;

public class EndRound extends TimerTask {
    Round round;
    EndRound(Round round) {
        this.round = round;
    }

    @Override
    public void run() {
        for (Order order : round.getOrders()) {
            order.setZeroCommonPercent();
        }

    }
}
