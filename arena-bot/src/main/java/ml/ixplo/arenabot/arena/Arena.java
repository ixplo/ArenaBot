package ml.ixplo.arenabot.arena;

import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.actions.Action;

public class Arena {
    private static Arena instance = new Arena();

    public static Arena getInstance() {
        return instance;
    }

    public void getAction(Action action) {
        Round.getCurrent().takeAction(action);
    }
}
