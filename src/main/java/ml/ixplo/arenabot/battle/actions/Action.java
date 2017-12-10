package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.battle.Round;

import static com.google.common.math.IntMath.pow;

/**
 * ixplo
 * 01.05.2017.
 */
public abstract class Action {
    ArenaUser user;
    ArenaUser target;
    String actionId;
    private int percent;    //from 1 to 100
    int experience;
    String message;
    private static DatabaseManager db;

    /******* constructor **********
     * use Action create
     ******************************/
    Action() {}

    public static Action create(int userId, String actionId, int targetId, int percent, String spellId) {
        Action action;
        switch (actionId) {
            case "a":
                action = new Attack();
                break;
            case "p":
                action = new Protect();
                break;
            case "h":
                action = new Heal();
                break;
            case "m":
                action = new CastSpell(spellId);
                break;
            default:
                throw new RuntimeException("Unknown action actionId: " + actionId);
        }
        action.user = ArenaUser.getUser(userId);
        action.target = ArenaUser.getUser(targetId);
        action.percent = percent;
        return action;
    }

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
                throw new RuntimeException("Unknown action actionId: " + callbackEntry);
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

    public String getActionId() {
        return actionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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



    static double roundDouble(double d) {
        return roundDouble(d, 2);
    }

    static double roundDouble(double d, int precise) {
        precise = pow(10, precise);
        d *= precise;
        int i = (int) Math.round(d);
        return (double) i / precise;
    }

    public ArenaUser getUser() {
        return user;
    }

    public abstract void doAction();


    public static void clearActions(int userId) {
        db.dropActions(userId);
    }

}
