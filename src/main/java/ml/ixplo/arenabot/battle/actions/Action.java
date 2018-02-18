package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.user.ArenaUser;

/**
 * ixplo
 * 01.05.2017.
 */
public abstract class Action implements Comparable<Action>{
    public static final int FIRST = 1;
    public static final int SECOND = 2;
    public static final int THIRD = 3;
    public static final int FOURTH = 4;
    public static final String ATTACK = "a";
    public static final String PROTECT = "p";
    public static final String HEAL = "h";
    public static final String MAGIC = "m";
    ArenaUser user;
    ArenaUser target;
    String actionId;
    private int percent;    //from 1 to 100
    int experience;
    String message;
    private int priority;
    private static DatabaseManager db;

    /******* constructor **********
     * use Action create
     ******************************/
    Action() {}

    public static Action create(int userId, String actionId, int targetId, int percent) {
        return create(userId, actionId, targetId, percent, null);
    }

    public static Action create(int userId, String actionId, int targetId, int percent, String spellId) {
        Action action;
        switch (actionId) {
            case ATTACK:
                action = new Attack();
                break;
            case PROTECT:
                action = new Protect();
                break;
            case HEAL:
                action = new Heal();
                break;
            case MAGIC:
                action = new CastSpell(spellId);
                break;
            default:
                throw new ArenaUserException("Unknown action actionId: " + actionId);
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

    public static void setActionId(int userId, String actionType) {
        String action;
        switch (actionType) {
            case "Атака":
                action = ATTACK;
                break;
            case "Защита":
                action = PROTECT;
                break;
            case "Лечение":
                action = HEAL;
                break;
            case "Магия":
                action = MAGIC;
                break;
            default:
                throw new ArenaUserException("Unknown action actionId: " + actionType);
        }
        db.setStringTo(Config.ROUND_ACTIONS, userId, "action_type", action);
    }

    public static void setCastId(int userId, String castId) {
        db.setStringTo(Config.ROUND_ACTIONS, userId, "cast_id", castId);
    }

    public static String getActionType(int userId, int counter) {
        return db.getStringByBy(Config.ROUND_ACTIONS,
                "action_type",
                "id", userId,
                Config.COUNTER, counter);
    }

    public static int getTargetId(int userId, int counter) {
        return db.getIntByBy(Config.ROUND_ACTIONS,
                "target_id",
                "id", userId,
                Config.COUNTER, counter);
    }

    public static int getPercent(int userId, int counter) {
        return db.getIntByBy(Config.ROUND_ACTIONS,
                "percent",
                "id", userId,
                Config.COUNTER, counter);
    }

    public static String getSpellId(int userId, int counter) {
        return db.getStringByBy(Config.ROUND_ACTIONS,
                "cast_id",
                "id", userId,
                Config.COUNTER, counter);
    }

    public String getActionType() {
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

    public ArenaUser getUser() {
        return user;
    }

    public abstract void doAction();


    public static void clearActions(int userId) {
        db.dropActions(userId);
    }

    @Override
    public int compareTo(Action o) {
        return priority - o.priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
