package arenabot.battle.actions;

import arenabot.users.ArenaUser;
import arenabot.battle.Round;

import static com.google.common.math.IntMath.pow;

/**
 * ixplo
 * 01.05.2017.
 */
public abstract class Action {
    ArenaUser user;
    ArenaUser target;
    private int percent;    //from 1 to 100
    int experience;
    String message;

    public String getMessage() {
        return message;
    }

    public ArenaUser getTarget() {
        return target;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    Action(int userId, int targetId, int percent) {
        user = Round.round.getMember(userId);
        target = Round.round.getMember(targetId);
        this.percent = percent;
    }

    public static Action create(int userId, String actionId, int targetId, int percent){
        Action action;
        switch (actionId){
            case "a":
                action = new Attack(userId, targetId, percent);
                break;
            case "p":
                action = new Protect(userId, targetId, percent);
                break;
            case "h":
                action = new Heal(userId, targetId, percent);
                break;
            case "m":
                action = new CastSpell(userId, targetId, percent);
                break;
            default:
                throw new RuntimeException("Unknown action type: " + actionId);
        }
        return action;
    }

    public static double roundDouble(double d) {
        return roundDouble(d,2);
    }
    public static double roundDouble(double d, int precise) {
        precise = pow(10,precise);
        d *= precise;
        int i = (int) Math.round(d);
        return (double) i / precise;
    }

    public abstract void doAction();
}
