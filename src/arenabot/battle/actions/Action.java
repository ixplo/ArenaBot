package arenabot.battle.actions;

import arenabot.Config;
import arenabot.database.DatabaseManager;
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
    private static DatabaseManager db;

    public static void setDb(DatabaseManager databaseManager) {
        db = databaseManager;
    }

    public static void addAction(int userId) {
        db.addAction(userId);
    }

    public static void setTargetId(int userId, int targetId) {
        db.setIntTo(Config.ROUND_ACTIONS, userId, "target_id", targetId);
    }

    public static void setPercent(int userId, int percent) {
        db.setIntTo(Config.ROUND_ACTIONS, userId, "percent", percent);
    }

    public static void setActionId(int userId, String callbackEntry) {
        String action;
        switch (callbackEntry) {
            case "Атака":
                action = "a";
                break;
            case "Защита":
                action = "p";
                break;
            case "Лечение":
                action = "h";
                break;
            case "Магия":
                action = "m";
                break;
            default:
                throw new RuntimeException("Unknown action type: " + callbackEntry);
        }
        db.setStringTo(Config.ROUND_ACTIONS, userId, "action_type", action);
    }

    public static void setCastId(int userId, String castId) {
        db.setStringTo(Config.ROUND_ACTIONS, userId, "cast_id", castId);
    }

    public static String getActionId(int userId, int counter) {
        return db.getStringByBy(Config.ROUND_ACTIONS,
                "action_type",
                "id", userId,
                "counter", counter);
    }

    public static int getTargetId(int userId, int counter) {
        return db.getIntByBy(Config.ROUND_ACTIONS,
                "target_id",
                "id", userId,
                "counter", counter);
    }

    public static int getPercent(int userId, int counter) {
        return db.getIntByBy(Config.ROUND_ACTIONS,
                "percent",
                "id", userId,
                "counter", counter);
    }

    public static String getSpellId(int userId, int counter) {
        return db.getStringByBy(Config.ROUND_ACTIONS,
                "cast_id",
                "id", userId,
                "counter", counter);
    }


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

    public static Action create(int userId, String actionId, int targetId, int percent, String spellId) {
        Action action;
        switch (actionId) {
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
                action = new CastSpell(userId, targetId, percent, spellId);
                break;
            default:
                throw new RuntimeException("Unknown action type: " + actionId);
        }
        return action;
    }

    static double roundDouble(double d) {
        return roundDouble(d, 2);
    }

    static double roundDouble(double d, int precise) {
        precise = pow(10, precise);
        d *= precise;
        int i = (int) Math.round(d);
        return (double) i / precise;
    }


    public abstract void doAction();


    public static void clearActions(int userId) {
        db.dropActions(userId);
    }

}
