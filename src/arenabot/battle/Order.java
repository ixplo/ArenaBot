package arenabot.battle;

import arenabot.battle.actions.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * ixplo
 * 04.05.2017.
 */
public class Order {
    Integer userId;
    private List<Action> actions;
    private Round round;
    private int commonPercent;

    int getCommonPercent() {
        return commonPercent;
    }

    Order(Integer userId, Round round) {
        this.userId = userId;
        actions = new ArrayList<>();
        this.round = round;
        commonPercent = 100;
    }

    void addAction(Action action) {
        if (commonPercent == 0){
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
        txt.append("commonPercent=").append(commonPercent);
        for (Action action:actions){
            //txt.append("[do ").append(action.actionId).append(" ").append(action.targetId);
            txt.append(" ").append(action.getPercent()).append("]");
        }
        return txt.toString();
    }
}
