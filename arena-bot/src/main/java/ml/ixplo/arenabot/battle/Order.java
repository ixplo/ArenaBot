package ml.ixplo.arenabot.battle;

import java.util.ArrayList;

import java.util.List;
import ml.ixplo.arenabot.battle.actions.Action;

/**
 * ixplo
 * 04.05.2017.
 */
public class Order {
    public static final int FULL_PERCENT = 100;
    Integer userId;
    private List<Action> actions;
    private Round round;
    private volatile int commonPercent;

    int getCommonPercent() {
        return commonPercent;
    }

    public void setZeroCommonPercent() {
        this.commonPercent = 0;
    }

    Order(Integer userId, Round round) {
        this.userId = userId;
        actions = new ArrayList<>();
        this.round = round;
        commonPercent = FULL_PERCENT;
    }

    void addAction(Action action) {
        if (commonPercent == 0) {
            return;
        }
        if (commonPercent - action.getPercent() < 0) {
            action.setPercent(commonPercent);
        }
        actions.add(action);
        commonPercent -= action.getPercent();
    }

    public List<Action> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("user=").append(userId).append("round=").append(round);
        txt.append(", commonPercent=").append(commonPercent);
        for (Action action : actions) {
            txt.append(" [").append(action.getActionId()).append(action.getPercent()).append("]");
        }
        return txt.toString();
    }
}
