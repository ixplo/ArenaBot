package ml.ixplo.arenabot.user;

import ml.ixplo.arenabot.battle.Battle;
import ml.ixplo.arenabot.battle.Member;
import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.classes.Archer;
import ml.ixplo.arenabot.user.classes.Mage;
import ml.ixplo.arenabot.user.classes.Priest;
import ml.ixplo.arenabot.user.classes.UserClass;
import ml.ixplo.arenabot.user.classes.Warrior;
import ml.ixplo.arenabot.user.items.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ml.ixplo.arenabot.utils.Utils.roundDouble;

/**
 * ixplo
 * 24.04.2017.
 */
public abstract class ArenaUser extends Member {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ArenaUser.class);
    public static final String LOGTAG = "ARENAUSER";

    private String userTitle;
    private String userPostTitle;
    private String race;
    private String userClass;
    private String descr;
    private int sex;
    private int userGames;
    private int userWins;
    private int nativeStr;
    private int nativeDex;
    private int nativeWis;
    private int nativeInt;
    private int nativeCon;
    private int freePoints;
    private double maxHitPoints;
    private int money;
    private int experience;
    private int level;
    private int curStr;
    private int curDex;
    private int curWis;
    private int curInt;
    private int curCon;
    private int curWeapon;
    private double minHit;
    private double maxHit;
    private BigDecimal attack;
    private double protect;
    private double heal;
    private double magicProtect;
    private double curHitPoints;
    private int curExp;
    private long lastGame;
    protected List<String> actionsName = Arrays.asList("Атака", "Защита", "Лечение");

    // instance
    private static ArenaUser arenaUser;

    /****** constructor ******
     * use ArenaUser.create
     *************************/
    public ArenaUser() {
    }

    // **************************************************
    // ******** abstract ********************************
    // **************************************************

    public abstract void setClassFeatures();

    public abstract void getClassFeatures();

    public abstract void appendClassXstatMsg(StringBuilder out);

    public abstract void putOnClassFeatures(Item item);

    public abstract void putOffClassFeatures(Item item);

    public abstract void addHarkClassFeatures(String harkToUpId, int numberOfPoints);

    public abstract void doAction(String[] command);

    public abstract String doCast(ArenaUser target, int percent, String castId);

    public abstract void endBattleClassFeatures();

    public abstract String getClassActionId(String actionId);

    public abstract List<String> getCastsName();

    public abstract List<String> getCastsId();

    public abstract void learn(int level);

    // **************************************************
    // ******** STATIC **********************************
    // **************************************************
    //todo сделать класс Race
    public static ArenaUser create(int userId, String name, UserClass userClass, String race) {
        if (doesUserExists(userId)) {
            throw new IllegalArgumentException("Such user with userId " + userId + " already exists");
        }
        db.addUser(userId, name);
        arenaUser = create(userClass, race);
        arenaUser.userId = userId;
        arenaUser.name = name;
        generateHarksForRaceAndClass();
        generateCommonHarks();
        arenaUser.setClassFeatures();
        arenaUser.addItem("waa");
        arenaUser.setCurWeapon(-1);
        arenaUser.putOn(0);
        db.updateUser(arenaUser);
        return arenaUser;
    }

    private static void generateHarksForRaceAndClass() {
        arenaUser.nativeStr = db.getIntFrom(Config.CLASSES, arenaUser.userClass, "str_start") +
                db.getIntFrom(Config.RACES, arenaUser.race, "str_bonus");
        arenaUser.nativeDex = db.getIntFrom(Config.CLASSES, arenaUser.userClass, "dex_start") +
                db.getIntFrom(Config.RACES, arenaUser.race, "dex_bonus");
        arenaUser.nativeWis = db.getIntFrom(Config.CLASSES, arenaUser.userClass, "wis_start") +
                db.getIntFrom(Config.RACES, arenaUser.race, "wis_bonus");
        arenaUser.nativeInt = db.getIntFrom(Config.CLASSES, arenaUser.userClass, "int_start") +
                db.getIntFrom(Config.RACES, arenaUser.race, "int_bonus");
        arenaUser.nativeCon = db.getIntFrom(Config.CLASSES, arenaUser.userClass, "con_start") +
                db.getIntFrom(Config.RACES, arenaUser.race, "con_bonus");
        arenaUser.freePoints = db.getIntFrom(Config.CLASSES, arenaUser.userClass, "free_bonus");
        arenaUser.maxHitPoints = roundDouble(1.3333333 * arenaUser.curCon
                + db.getIntFrom(Config.CLASSES, arenaUser.userClass, "hp_bonus"));
        arenaUser.money = db.getIntFrom(Config.CLASSES, arenaUser.userClass, "money_start");
    }

    private static void generateCommonHarks() {
        arenaUser.curStr = arenaUser.nativeStr;
        arenaUser.curDex = arenaUser.nativeDex;
        arenaUser.curWis = arenaUser.nativeWis;
        arenaUser.curInt = arenaUser.nativeInt;
        arenaUser.curCon = arenaUser.nativeCon;
        arenaUser.curHitPoints = arenaUser.maxHitPoints;
        arenaUser.level = 1;
        arenaUser.minHit = (double) (arenaUser.curStr - 3) / 4;
        arenaUser.maxHit = (double) (arenaUser.curStr - 3) / 4;
        arenaUser.attack = BigDecimal.valueOf(0.91 * arenaUser.curDex + 0.39 * arenaUser.curStr);
        arenaUser.protect = roundDouble(0.4 * arenaUser.curDex + 0.6 * arenaUser.curCon);
        arenaUser.heal = roundDouble(0.06 * arenaUser.curWis + 0.04 * arenaUser.curInt);
        arenaUser.magicProtect = roundDouble(0.6 * arenaUser.curWis + 0.4 * arenaUser.curInt);
    }

    public static ArenaUser create(UserClass userClassId, String race) {
        ArenaUser user = create(userClassId);
        user.race = race;
        user.userClass = userClassId.toString();
        return user;
    }

    public static ArenaUser create(UserClass userClassId) {
        if (userClassId == null) {
            throw new IllegalArgumentException("userClassId is invalid");
        }
        ArenaUser hero;
        switch (userClassId) {
            case WARRIOR:
                hero = new Warrior();
                break;
            case ARCHER:
                hero = new Archer();
                break;
            case MAGE:
                hero = new Mage();
                break;
            case PRIEST:
                hero = new Priest();
                break;
            default:
                throw new ArenaUserException("Unknown userClass: " + userClassId);
        }
        return hero;
    }

    public static void dropUser(int userId) {
        if (!db.doesUserExists(userId)) {
            throw new ArenaUserException("No such user in database: " + userId);
        }
        db.dropItems(userId);
        db.dropSpells(userId);
        db.dropUser(userId);
    }

    public static void dropUser(ArenaUser user) {
        dropUser(user.userId);
    }

    public static ArenaUser getUser(Integer userId) {
        if (!db.doesUserExists(userId)) {
            throw new IllegalArgumentException("No such user in database: " + userId);
        }
        ArenaUser arenaUser = db.getUser(userId);
        arenaUser.getClassFeatures();
        return arenaUser;
    }

    public static void doHandler(AbsSender absSender, int userId, long chatId, String[] strings) {

        int target;
        int percent;
        String spellId;

        if (strings.length < 4) {
            spellId = "";
        } else {
            spellId = strings[3];
        }
        if (strings.length < 3) {
            percent = 100;
        } else {
            percent = Integer.parseInt(strings[2]);
        }
        if (strings.length < 2) {
            target = Round.getCurrent().getIndex(userId);
        } else {
            target = Integer.parseInt(strings[1]) - 1;
        }
        if (percent > 100) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.enableHtml(true);
            msg.setText("Больше 100% быть не может. Инфа 146%!");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            return;
        }
        if (strings.length == 0) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.enableHtml(true);
            msg.setText("Формат: <i>/do a 1 100</i> - атаковать цель под номером 1 на 100%");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            return;
        }
        if (target > Round.getCurrent().getCurMembersId().size() - 1) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.enableHtml(true);
            msg.setText("Цель под номером " + Integer.parseInt(strings[1]) +
                    " не найдена. Всего есть целей: " + Round.getCurrent().getCurMembersId().size());
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            return;
        }

        Messages.sendDoMsg(absSender, chatId, strings[0], target, percent); //todo перенести в takeAction
        Battle.getBattle().interrupt();
        Round.getCurrent().takeAction(userId, strings[0], Round.getCurrent().getMembers().get(target).getUserId(), percent, spellId);
        getUser(userId).doAction(strings);
    }

    //todo выпилить
    public static List<ArenaUser> getUsers(List<Integer> usersId) {
        List<ArenaUser> users = new ArrayList<>();
        int count = usersId.size();
        for (int i = 0; i < count; i++) {
            users.add(getUser(usersId.get(i)));
        }
        return users;
    }

    public static String getClassName(String classId) {
        return db.getStringFrom(Config.CLASSES, classId, "name");
    }

    public static String getClassName(Integer userId) {
        return db.getStringFrom(Config.CLASSES, ArenaUser.getUser(userId).userClass, "name");
    }

    public static String getRaceName(String raceId) {
        return db.getStringFrom(Config.RACES, raceId, "name");
    }

    public static String getRaceName(Integer userId) {
        return db.getStringFrom(Config.RACES, ArenaUser.getUser(userId).race, "name");
    }

    public static List<String> getRacesName() {
        return db.getColumn(Config.RACES, "name");
    }

    public static List<String> getRacesId() {
        return db.getColumn(Config.RACES, "id");
    }

    public static List<String> getClassesName() {
        return db.getColumn(Config.CLASSES, "name");
    }

    public static List<String> getClassesId() {
        return db.getColumn(Config.CLASSES, "id");
    }

    public static List<String> getRacesDescr() {
        return db.getColumn(Config.RACES, "descr");
    }

    public static List<String> getClassesDescr() {
        return db.getColumn(Config.CLASSES, "descr");
    }

// **************************************************
    // ******** Instance ********************************
    // **************************************************

    public Map<String, Object> getParams() {
        try {
            return getFields();
        } catch (IllegalAccessException e) {
            BotLogger.error(LOGTAG, e);
            throw new ArenaUserException("Не удалось прочесть поля класса ArenaUser", e);
        }
    }

    private Map<String, Object> getFields() throws IllegalAccessException {
        Map<String, Object> params = new HashMap<>();
        Class<?> aClass = this.getClass();
        while (aClass.getSuperclass() != null) {
            for (Field field : aClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    params.put(field.getName(), field.get(this));
                }
            }
            aClass = aClass.getSuperclass();
        }
        return params;
    }

    public void dropUser() {
        dropUser(this);
    }

    public int getEqipAmount() {
        return Item.getEqipAmount(userId);
    }

    public void putOn(int eqipIndex) {
        Item.putOn(this, eqipIndex);
    }

    public void putOff(int eqipIndex) {
        Item.putOff(this, eqipIndex);
    }

    public void addItem(String itemId) {
        Item.add(userId, itemId);
    }

    public void dropItem(int eqipIndex) {
        Item.drop(userId, eqipIndex);
    }

    // **************************************************
    // ******** adders **********************************
    // **************************************************

    public void addCurHitPoints(double hitPointsChange) {
        this.curHitPoints = roundDouble(this.curHitPoints + hitPointsChange);
    }

    public void addHark(String harkId, int numberOfPoints) {
        if (harkId.equals("nativeStr")) {
            nativeStr += numberOfPoints;
            curStr += numberOfPoints;
            minHit += roundDouble((double) numberOfPoints / 4);
            maxHit += roundDouble((double) numberOfPoints / 4);
            attack = attack.add(BigDecimal.valueOf(0.39 * numberOfPoints));
        }
        if (harkId.equals("nativeDex")) {
            nativeDex += numberOfPoints;
            curDex += numberOfPoints;
            attack = attack.add(BigDecimal.valueOf(0.91 * numberOfPoints));
            protect += roundDouble(0.4 * numberOfPoints);
        }
        if (harkId.equals("nativeWis")) {
            nativeWis += numberOfPoints;
            curWis += numberOfPoints;
            heal += roundDouble(0.06 * numberOfPoints);
            magicProtect += roundDouble(0.6 * numberOfPoints);
        }
        if (harkId.equals("nativeInt")) {
            nativeInt += numberOfPoints;
            curInt += numberOfPoints;
            heal += roundDouble(0.04 * numberOfPoints);
            magicProtect += roundDouble(0.4 * numberOfPoints);
        }
        if (harkId.equals("nativeCon")) {
            nativeCon += numberOfPoints;
            curCon += numberOfPoints;
            maxHitPoints += roundDouble(1.3333333 * numberOfPoints);
            protect += roundDouble(0.6 * numberOfPoints);
        }
        addHarkClassFeatures(harkId, numberOfPoints);
        setFreePoints(getFreePoints() - numberOfPoints);
        db.updateUser(this);
    }

    public void addCurExp(int exp) {
        this.curExp += exp;
    }

    public void addExperience(int experience) {
        this.experience += experience;
        db.setIntTo(Config.USERS, userId, "exp", this.experience);
    }

    public void addUserGames() {
        db.setIntTo(Config.USERS, userId, "games", ++this.userGames);
    }

    public void addUserWins() {
        db.setIntTo(Config.USERS, userId, "wins", ++this.userWins);
    }

    public void addMoney(int money) {
        this.money += money;
        db.setIntTo(Config.USERS, userId, "money", this.money);
    }

    // **************************************************
    // ******** setters *********************************
    // **************************************************

    public void setLastGame() {
        db.setLongTo(Config.USERS, userId, "last_game", System.currentTimeMillis());
    }

    public void setCurHitPoints(double curHitPoints) {
        this.curHitPoints = curHitPoints;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public void setUserPostTitle(String userPostTitle) {
        this.userPostTitle = userPostTitle;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setUserGames(int userGames) {
        this.userGames = userGames;
    }

    public void setUserWins(int userWins) {
        this.userWins = userWins;
    }

    public void setNativeStr(int nativeStr) {
        this.nativeStr = nativeStr;
    }

    public void setNativeDex(int nativeDex) {
        this.nativeDex = nativeDex;
    }

    public void setNativeWis(int nativeWis) {
        this.nativeWis = nativeWis;
    }

    public void setNativeInt(int nativeInt) {
        this.nativeInt = nativeInt;
    }

    public void setNativeCon(int nativeCon) {
        this.nativeCon = nativeCon;
    }

    public void setFreePoints(int freePoints) {
        this.freePoints = freePoints;
    }

    public void setMaxHitPoints(double maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCurStr(int curStr) {
        this.curStr = curStr;
    }

    public void setCurDex(int curDex) {
        this.curDex = curDex;
    }

    public void setCurWis(int curWis) {
        this.curWis = curWis;
    }

    public void setCurInt(int curInt) {
        this.curInt = curInt;
    }

    public void setCurCon(int curCon) {
        this.curCon = curCon;
    }

    public void setCurWeapon(int curWeapon) {
        this.curWeapon = curWeapon;
    }

    public void setMinHit(double minHit) {
        this.minHit = minHit;
    }

    public void setMaxHit(double maxHit) {
        this.maxHit = maxHit;
    }

    public void setAttack(BigDecimal attack) {
        this.attack = attack;
    }

    public void setProtect(double protect) {
        this.protect = protect;
    }

    public void setHeal(double heal) {
        this.heal = heal;
    }

    public void setMagicProtect(double magicProtect) {
        this.magicProtect = magicProtect;
    }

    public void setCurExp(int curExp) {
        this.curExp = curExp;
    }

    public void setLastGame(long lastGame) {
        this.lastGame = lastGame;
    }

    // **************************************************
    // ******** getters *********************************
    // **************************************************

    public List<Item> getItems() {
        return db.getItems(userId);
    }

    public Item getItem(int eqipIndex) {
        return Item.getItem(userId, eqipIndex);
    }

    public String getUserTitle() {
        return userTitle;
    }

    public String getUserPostTitle() {
        return userPostTitle;
    }

    public String getRace() {
        return race;
    }

    public String getUserClass() {
        return userClass;
    }

    public String getDescr() {
        return descr;
    }

    public int getSex() {
        return sex;
    }

    public int getUserGames() {
        return userGames;
    }

    public int getUserWins() {
        return userWins;
    }

    public int getNativeStr() {
        return nativeStr;
    }

    public int getNativeDex() {
        return nativeDex;
    }

    public int getNativeWis() {
        return nativeWis;
    }

    public int getNativeInt() {
        return nativeInt;
    }

    public int getNativeCon() {
        return nativeCon;
    }

    public int getFreePoints() {
        return freePoints;
    }

    public double getMaxHitPoints() {
        return maxHitPoints;
    }

    public int getMoney() {
        return money;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getCurStr() {
        return curStr;
    }

    public int getCurDex() {
        return curDex;
    }

    public int getCurWis() {
        return curWis;
    }

    public int getCurInt() {
        return curInt;
    }

    public int getCurCon() {
        return curCon;
    }

    public int getCurWeaponIndex() {
        return curWeapon;
    }

    public double getMinHit() {
        return minHit;
    }

    public double getMaxHit() {
        return maxHit;
    }

    public BigDecimal getAttack() {
        return attack.setScale(Config.SCALE, Config.ROUNDED);
    }

    public double getProtect() {
        return protect;
    }

    public double getHeal() {
        return heal;
    }

    public double getMagicProtect() {
        return magicProtect;
    }

    public double getCurHitPoints() {
        return curHitPoints;
    }

    public int getCurExp() {
        return curExp;
    }

    public long getLastGame() {
        return db.getLongFrom(Config.USERS, userId, "last_game");
    }

    public boolean doesUserExists() {
        return db.doesUserExists(userId);
    }

    public String getClassName() {
        return db.getStringFrom(Config.CLASSES, userClass, "name");
    }

    public String getRaceName() {
        return db.getStringFrom(Config.RACES, race, "name");
    }

    public List<String> getActionsName() {
        return actionsName;
    }

    public List<String> getActionsId() {
        List<String> actionsId = new ArrayList<>();
        for (String actionId : actionsName) {
            actionsId.add(getClassActionId("action_" + actionId));
        }
        return actionsId;
    }

    @Override
    public String toString() {
        return "ArenaUser{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", race='" + race + '\'' +
                ", userClass='" + userClass + '\'' +
                ", nativeStr=" + nativeStr +
                ", nativeDex=" + nativeDex +
                ", nativeWis=" + nativeWis +
                ", nativeInt=" + nativeInt +
                ", nativeCon=" + nativeCon +
                ", freePoints=" + freePoints +
                ", maxHitPoints=" + maxHitPoints +
                ", money=" + money +
                ", experience=" + experience +
                ", level=" + level +
                ", curStr=" + curStr +
                ", curDex=" + curDex +
                ", curWis=" + curWis +
                ", curInt=" + curInt +
                ", curCon=" + curCon +
                ", curWeapon=" + curWeapon +
                ", minHit=" + minHit +
                ", maxHit=" + maxHit +
                ", attack=" + attack +
                ", protect=" + protect +
                ", heal=" + heal +
                ", magicProtect=" + magicProtect +
                ", curHitPoints=" + curHitPoints +
                ", curExp=" + curExp +
                ", lastGame=" + lastGame +
                ", status=" + getStatus() +
                '}';
    }

}
