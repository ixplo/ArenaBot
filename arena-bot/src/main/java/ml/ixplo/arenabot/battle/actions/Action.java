package ml.ixplo.arenabot.battle.actions;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.user.ArenaUser;

/**
 * Parent class for actions: Attack, Heal and so on
 */
public abstract class Action implements Comparable<Action> {
    static final int FIRST = 1;
    static final int SECOND = 2;
    static final int THIRD = 3;
    static final int FOURTH = 4;
    public static final String ATTACK = "a";
    public static final String PROTECT = "p";
    public static final String HEAL = "h";
    public static final String MAGIC = "m";
    ArenaUser user;
    ArenaUser target;
    String message;
    int experience;
    private String actionId;
    private int percent;    //from 1 to 100
    private int priority;
    private static DatabaseManager db;
    private String castId;

    /******* constructor **********
     * use Action create
     ******************************/
    Action() {
    }

    public static Action create(int userId, String actionId, int targetId, int percent) {
        return create(userId, actionId, targetId, percent, null);
    }

    /**
     * Create Action instance instead constructor
     * @param userId    - action owner id
     * @param actionId  - action type
     * @param targetId  - action target userId
     * @param percent   - summary may be 100% in a round
     * @param spellId   - may be absent if Action is not spell
     * @return created Action
     */
    public static Action create(int userId, String actionId, int targetId, int percent, String spellId) {
        if (actionId == null) {
            throw new IllegalArgumentException("actionId cant be null");
        }
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
                throw new IllegalArgumentException("Unknown action actionId: " + actionId);
        }
        action.user = ArenaUser.getUser(userId);
        action.target = ArenaUser.getUser(targetId);
        action.percent = percent;
        return action;
    }

    public static void setDb(DatabaseManager databaseManager) {
        db = databaseManager;
    }

    public static void save(Action action) {
        db.saveAction(action);
    }

    public static void addAction(int userId) {
        db.addAction(userId);
    }

    /**
     * Set target userId to database
     * @param userId    - action owner id
     * @param targetId  - action target userId
     */
    public static void setTargetId(int userId, int targetId) {
        db.setIntTo(Config.ROUND_ACTIONS, userId, "target_id", targetId);
    }

    public static void setPercent(int userId, int percent) {
        db.setIntTo(Config.ROUND_ACTIONS, userId, "percent", percent);
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * Set Action code to database. Identify by Callback message
     * @param userId        - owner id
     * @param actionType    - Callback message - type of Action
     */
    public static void setActionIdFromCallback(int userId, String actionType) {
        if (actionType == null) {
            throw new IllegalArgumentException("actionId cant be null");
        }
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
                throw new IllegalArgumentException("Unknown action actionId: " + actionType);
        }
        db.setStringTo(Config.ROUND_ACTIONS, userId, "action_type", action);
    }

    public static void setCastId(int userId, String castId) {
        db.setStringTo(Config.ROUND_ACTIONS, userId, "cast_id", castId);
    }

    public void setCastId(String castId) {
        this.castId = castId;
    }

    /**
     * Get code of Action
     * @param userId    - owner id
     * @param counter   - number in order
     * @return String code of Action from database
     */
    public static String getActionType(int userId, int counter) {
        return db.getStringByBy(Config.ROUND_ACTIONS,
                "action_type",
                "id", userId,
                Config.COUNTER, counter);
    }

    /**
     * Get userId for target of Action
     * @param userId    - owner id
     * @param counter   - number in order
     * @return target userId from database
     */
    public static int getTargetId(int userId, int counter) {
        return db.getIntByBy(Config.ROUND_ACTIONS,
                "target_id",
                "id", userId,
                Config.COUNTER, counter);
    }

    /**
     * Get percent points spent for this action
     * @param userId    - owner id
     * @param counter   - number in order
     * @return number from 0 to 100 from database
     */
    public static int getPercent(int userId, int counter) {
        return db.getIntByBy(Config.ROUND_ACTIONS,
                "percent",
                "id", userId,
                Config.COUNTER, counter);
    }

    public int getPercent() {
        return percent;
    }

    /**
     * Get Action castId like "1ma" for magic arrow spell
     * @param userId    - owner id
     * @param counter   - number in order
     * @return String code of cast from database
     */
    public static String getCastId(int userId, int counter) {
        return db.getStringByBy(Config.ROUND_ACTIONS,
                "cast_id",
                "id", userId,
                Config.COUNTER, counter);
    }

    public String getCastId() {
        return castId;
    }

    public String getName() {
        return db.getStringBy(Config.ACTIONS, "id", actionId, "name");
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

    public ArenaUser getUser() {
        return user;
    }

    public abstract void doAction();

    public abstract void unDo();

    public static void clearActions(int userId) {
        db.dropActions(userId);
    }

    protected void setPriority(int priority) {
        this.priority = priority;
    }

    public void setUser(ArenaUser user) {
        this.user = user;
    }

    public void setTarget(ArenaUser target) {
        this.target = target;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    @SuppressWarnings("squid:S1210")
    public int compareTo(Action that) {
        return priority - that.priority;
    }

    @Override
    public String toString() {
        return "Action{" + percent + "% "
                + getName()
                + ", users:[" + user.getUserId()
                + "->" + target.getUserId()
                + "]}";
    }
}
