package arenabot.users;

import arenabot.Config;
import arenabot.Messages;
import arenabot.battle.Battle;
import arenabot.battle.Round;
import arenabot.database.DatabaseManager;
import arenabot.users.Classes.Archer;
import arenabot.users.Classes.Mage;
import arenabot.users.Classes.Priest;
import arenabot.users.Classes.Warrior;
import arenabot.users.Inventory.Item;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.math.IntMath.pow;

/**
 * ixplo
 * 24.04.2017.
 */
public abstract class ArenaUser {

    public static DatabaseManager db;

    public enum UserClass { ARCHER, MAGE, PRIEST, WARRIOR }

    public List<String> actionsName = Arrays.asList("Атака", "Защита", "Лечение");
    private int userId;
    private String name;
    private String userTitle;
    private String userPostTitle;
    private String team;
    private String teamRank;
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
    private double attack;
    private double protect;
    private double heal;
    private double magicProtect;
    private double curHitPoints;
    private int curExp;
    private long lastGame;
    private int status;


    /****** constructor ******
     * use ArenaUser.create
     *************************/
    public ArenaUser(){}

    /****** abstract ******/
    public abstract void setClassFeatures();

    public abstract void getClassFeatures();

    public abstract void appendClassXstatMsg(StringBuilder out);

    public abstract void putOnClassFeatures(Item item);

    public abstract void addHarkClassFeatures(String harkToUpId, int numberOfPoints);

    public abstract void doAction(String[] command);

    public abstract String doCast(ArenaUser target, int percent, String castId);

    public abstract void endBattleClassFeatures();

    public abstract String getClassActionId(String actionId);

    public abstract List<String> getCastsName();

    public abstract List<String> getCastsId();

    public abstract void learn(int level);

    /****** static ******/
    public static ArenaUser create(UserClass userClassId) {
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
                throw new RuntimeException("Unknown userClass: " + userClassId);
        }
        return hero;
    }

    public static ArenaUser create(int userId, String name, UserClass userClass, String race) {
        ArenaUser arenaUser = ArenaUser.create(userClass);
        arenaUser.userId = userId;
        arenaUser.name = name;
        arenaUser.userClass = userClass.toString();
        db.addUser(userId, name, arenaUser.getUserClass());
        arenaUser.race = race;
        arenaUser.nativeStr = db.getIntFrom(Config.CLASSES, userClass.toString(), "str_start") +
                db.getIntFrom(Config.RACES, race, "str_bonus");
        arenaUser.nativeDex = db.getIntFrom(Config.CLASSES, userClass.toString(), "dex_start") +
                db.getIntFrom(Config.RACES, race, "dex_bonus");
        arenaUser.nativeWis = db.getIntFrom(Config.CLASSES, userClass.toString(), "wis_start") +
                db.getIntFrom(Config.RACES, race, "wis_bonus");
        arenaUser.nativeInt = db.getIntFrom(Config.CLASSES, userClass.toString(), "int_start") +
                db.getIntFrom(Config.RACES, race, "int_bonus");
        arenaUser.nativeCon = db.getIntFrom(Config.CLASSES, userClass.toString(), "con_start") +
                db.getIntFrom(Config.RACES, race, "con_bonus");
        arenaUser.freePoints = db.getIntFrom(Config.CLASSES, userClass.toString(), "free_bonus");
        arenaUser.curStr = arenaUser.nativeStr;
        arenaUser.curDex = arenaUser.nativeDex;
        arenaUser.curWis = arenaUser.nativeWis;
        arenaUser.curInt = arenaUser.nativeInt;
        arenaUser.curCon = arenaUser.nativeCon;
        arenaUser.maxHitPoints = roundDouble(1.3333333 * arenaUser.curCon
                + db.getIntFrom(Config.CLASSES, userClass.toString(), "hp_bonus"));
        arenaUser.curHitPoints = arenaUser.maxHitPoints;
        arenaUser.money = db.getIntFrom(Config.CLASSES, userClass.toString(), "money_start");
        arenaUser.level = 1;
        arenaUser.minHit = (double) (arenaUser.curStr - 3) / 4;
        arenaUser.maxHit = (double) (arenaUser.curStr - 3) / 4;
        arenaUser.attack = roundDouble(0.91 * arenaUser.curDex + 0.39 * arenaUser.curStr);
        arenaUser.protect = roundDouble(0.4 * arenaUser.curDex + 0.6 * arenaUser.curCon);
        arenaUser.heal = roundDouble(0.06 * arenaUser.curWis + 0.04 * arenaUser.curInt);
        arenaUser.magicProtect = roundDouble(0.6 * arenaUser.curWis + 0.4 * arenaUser.curInt);
        arenaUser.setClassFeatures();
        db.addItem(userId, "waa");
        Item.putOn(arenaUser, 1);
        db.setUser(arenaUser);
        return arenaUser;
    }

    //todo save user to db public static void setUser(Integer userId){common + abstract}

    public static void setDb(DatabaseManager dbManager) {
        db = dbManager;
    }

    public static boolean doesUserExists(Integer userId) {
        return db.doesUserExists(userId);
    }

    public static int getStatus(int userId) {
        return db.getIntFrom(Config.USERS, userId, "status");
    }

    public static void dropUser(int userId) {
        if (!db.doesUserExists(userId)) {
            throw new RuntimeException("No such user in database: " + userId);
        }
        db.dropItems(userId);
        db.dropSpells(userId);
        db.dropUser(userId);
    }

    public static double roundDouble(double d) {
        return roundDouble(d, 2);
    }

    public static double roundDouble(double d, int precise) {
        precise = pow(10, precise);
        d *= precise;
        int i = (int) Math.round(d);
        return (double) i / precise;
    }

    public static ArenaUser getUser(Integer userId) {
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
            target = Round.round.getIndex(userId);
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
                e.printStackTrace();
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
                e.printStackTrace();
            }
            return;
        }
        if (target > Round.getCurMembersId().size() - 1) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId);
            msg.enableHtml(true);
            msg.setText("Цель по номером " + Integer.parseInt(strings[1]) +
                    " не найдена. Всего есть целей: " + Round.getCurMembersId().size());
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        Messages.sendDoMsg(absSender, chatId, strings[0], target, percent); //todo перенести в takeAction
        Battle.battle.interrupt();
        Round.round.takeAction(userId, strings[0], Round.round.getMembers().get(target).userId, percent, spellId);
        getUser(userId).doAction(strings);
    }

    public static List<ArenaUser> getUsers(List<Integer> usersId) {
        List<ArenaUser> users = new ArrayList<>();
        int count = usersId.size();
        for (int i = 0; i < count; i++) {
            users.add(getUser(usersId.get(i)));
        }
        return users;
    }

    public static String getUserName(Integer userId) {
        return db.getStringFrom(Config.USERS, userId, "name");
    }

    public static String getUserTeam(Integer userId) {
        return db.getStringFrom(Config.USERS, userId, "team");
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

    /****** Add ******/

    public void addCurHitPoints(double hitPointsChange) {
        this.curHitPoints = ArenaUser.roundDouble(this.curHitPoints + hitPointsChange);
    }

    public void addHark(String harkId, int numberOfPoints) {
        if (harkId.equals("nativeStr")) {
            nativeStr += numberOfPoints;
            curStr += numberOfPoints;
            minHit += roundDouble((double) numberOfPoints / 4);
            maxHit += roundDouble((double) numberOfPoints / 4);
            attack += roundDouble(0.39 * numberOfPoints);
        }
        if (harkId.equals("nativeDex")) {
            nativeDex += numberOfPoints;
            curDex += numberOfPoints;
            attack += roundDouble(0.91 * numberOfPoints);
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
        db.setUser(this);
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

    /****** set ******/
    public void setLastGame() {
        db.setLongTo(Config.USERS, userId, "last_game", System.currentTimeMillis());
    }

    public void setCurHitPoints(double curHitPoints) {
        this.curHitPoints = curHitPoints;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
        db.setStringTo(Config.USERS, userId, "name", name);
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public void setUserPostTitle(String userPostTitle) {
        this.userPostTitle = userPostTitle;
    }

    public void setTeam(String team) {
        this.team = team;
        db.setStringTo(Config.USERS, userId, "team", team);
    }

    public void setTeamRank(String teamRank) {
        this.teamRank = teamRank;
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

    public void setAttack(double attack) {
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


    /****** get ******/
    public String getUserTitle() {
        return userTitle;
    }

    public String getUserPostTitle() {
        return userPostTitle;
    }

    public String getTeamName() {
        return team;
    }

    public String getTeamRank() {
        return teamRank;
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

    public int getCurWeapon() {
        return curWeapon;
    }

    public double getMinHit() {
        return minHit;
    }

    public double getMaxHit() {
        return maxHit;
    }

    public double getAttack() {
        return attack;
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
        return lastGame;
    }

    public int getUserId() {
        return userId;
    }

    public boolean doesUserExists() {
        return db.doesUserExists(userId);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        db.setIntTo(Config.USERS, userId, "status", status);
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return db.getStringFrom(Config.CLASSES, userClass, "name");
    }

    public String getRaceName() {
        return db.getStringFrom(Config.RACES, race, "name");
    }

    public String getTeam() {
        return team;
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


}
