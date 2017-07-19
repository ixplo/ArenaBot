package arenabot.users.Inventory;

import arenabot.Config;
import arenabot.database.DatabaseManager;
import arenabot.users.ArenaUser;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.math.IntMath.pow;

/**
 * ixplo
 * 28.04.2017.
 */
public class Item {
    private String itemId;
    private String name;
    private int price;
    private int minHit;
    private int maxHit;
    private int attack;
    private int protect;
    private int strBonus;
    private int dexBonus;
    private int wisBonus;
    private int intBonus;
    private int conBonus;
    private int strNeeded;
    private int dexNeeded;
    private int wisNeeded;
    private int intNeeded;
    private int conNeeded;
    private boolean isWeapon;
    private String slot;
    private String shop;
    private String race;
    private String descr;
    private String itemsSet;
    private static DatabaseManager db;

    public static void setDb(DatabaseManager manager) {
        db = manager;
    }

    //todo
    //int userId;
    //in_slot;
    //eqipIndex;

    public Item(String itemId) {
        this.itemId = itemId;
    }

    public static int getEqipAmount(Integer userId) {
        return db.getCount(Config.EQIP, Config.USER_ID, userId); //items count in eq
    }

    private static List<String> getItemsId(int userId) {
        return db.getStrings(Config.EQIP, Config.USER_ID, userId, "id");
    }

    public static List<Item> getItems(int userId) {
        List<Item> items = new LinkedList<>();
        for (String id :
                getItemsId(userId)) {
            items.add(db.getItem(id));
        }
        return items;
    }

    public static int getEqipIndex(Integer userId, String itemId) {
        return db.getIntByBy(Config.EQIP, "counter", "id", itemId, Config.USER_ID, userId);
    }

    public static boolean isItemInSlot(int userId, int eqipIndex) {
        return db.getSlot(userId, eqipIndex) != null;
    }

    public static String getItemId(int userId, Integer eqipIndex) {
        return db.getStringByBy(Config.EQIP, "id", "counter", eqipIndex, Config.USER_ID, userId);
    }

    public static void putOn(ArenaUser arenaUser, Integer eqipIndex) { // refactor to instance instead static

        //*** проверка на наличие в инвентаре
        if (eqipIndex > getEqipAmount(arenaUser.getUserId())) {
            throw new RuntimeException("Invalid eqip index: " + eqipIndex);
        }
        //*** проверка, а не надета ли уже она
        if (isItemInSlot(arenaUser.getUserId(), eqipIndex)) {
            return;
        }
        Item item = getItem(getItemId(arenaUser.getUserId(), eqipIndex));
        //todo проверка на соответствие требованиям (другие вещи тоже надо проверить, на случай если харки уменьшатся)
        //*** изменение характеристик перса
        arenaUser.setCurStr(arenaUser.getCurStr() + item.getStrBonus());
        arenaUser.setCurDex(arenaUser.getCurDex() + item.getDexBonus());
        arenaUser.setCurWis(arenaUser.getCurWis() + item.getWisBonus());
        arenaUser.setCurInt(arenaUser.getCurInt() + item.getIntBonus());
        arenaUser.setCurCon(arenaUser.getCurCon() + item.getConBonus());
        arenaUser.setMinHit(arenaUser.getMinHit() + item.getMinHit() + item.getStrBonus() / 4);
        arenaUser.setMaxHit(arenaUser.getMaxHit() + item.getMaxHit() + item.getStrBonus() / 4);
        arenaUser.setAttack(arenaUser.getAttack() + item.getAttack() + roundDouble(0.91 * item.getDexBonus() + 0.39 * item.getStrBonus()));
        arenaUser.setProtect(arenaUser.getProtect() + item.getProtect() + roundDouble(0.4 * item.getDexBonus() + 0.6 * item.getConBonus()));
        arenaUser.setMaxHitPoints(arenaUser.getMaxHitPoints() + roundDouble(1.3333333 * item.getConBonus()));//todo переделать, иначе выскочит нецелое число
        if (arenaUser.getStatus() != 2) {
            arenaUser.setCurHitPoints(arenaUser.getMaxHitPoints()); // not in battle
        }
        arenaUser.setMagicProtect(arenaUser.getMagicProtect() + roundDouble(0.6 * item.getWisBonus() + 0.4 * item.getIntBonus()));
        arenaUser.setHeal(arenaUser.getHeal() + roundDouble(0.06 * item.getWisBonus() + 0.04 * item.getIntBonus()));
        if (item.isWeapon()) arenaUser.setCurWeapon(eqipIndex);
        //*** заносим in_slot
        item.putInSlot(arenaUser.getUserId(), eqipIndex);
        arenaUser.putOnClassFeatures(item);
        db.setUser(arenaUser);
        //todo сделать putOff вещи, которая тут раньше была надета, сделать ли это до надевания вещи?
        //todo снова изменить характеристики из-за предыдущего пункта
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

    public void putOff(String itemId) {

        //todo проверка на наличие в инвентаре
        //todo проверка а надета ли она вообще?
        //todo изменение характеристик перса (если изменяет основные харки, это потянет за собой изменение зависящих от них
        //todo проверка на соответствие требованиям (другие вещи могут перестать соответствовать)
        //todo обнулить in_slot - снова изменить из-за этого характеристики
    }

/*todo
    boolean isInSlot(Integer userId, Integer eqipIndex) {
        return in_slot!=null;
    }
*/

    public static String getItemName(Integer userId, Integer eqipIndex) {
        return db.getItem(Item.getItemId(userId, eqipIndex)).name;
    }

    public static Item getItem(Integer userId, Integer eqipIndex) {
        return db.getItem(Item.getItemId(userId, eqipIndex));
    }

    public static Item getItem(String itemId) {
        return db.getItem(itemId);
    }

    public static String getSlotName(String slot) {
        return db.getStringFrom("SLOTS", slot, "name");
    }

    public boolean putInSlot(Integer userId, Integer eqipIndex) {
        return db.setStringTo(Config.EQIP, itemId, "in_slot", slot);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMinHit() {
        return minHit;
    }

    public void setMinHit(int minHit) {
        this.minHit = minHit;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public void setMaxHit(int maxHit) {
        this.maxHit = maxHit;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getProtect() {
        return protect;
    }

    public void setProtect(int protect) {
        this.protect = protect;
    }

    public int getStrBonus() {
        return strBonus;
    }

    public void setStrBonus(int strBonus) {
        this.strBonus = strBonus;
    }

    public int getDexBonus() {
        return dexBonus;
    }

    public void setDexBonus(int dexBonus) {
        this.dexBonus = dexBonus;
    }

    public int getWisBonus() {
        return wisBonus;
    }

    public void setWisBonus(int wisBonus) {
        this.wisBonus = wisBonus;
    }

    public int getIntBonus() {
        return intBonus;
    }

    public void setIntBonus(int intBonus) {
        this.intBonus = intBonus;
    }

    public int getConBonus() {
        return conBonus;
    }

    public void setConBonus(int conBonus) {
        this.conBonus = conBonus;
    }

    public int getStrNeeded() {
        return strNeeded;
    }

    public void setStrNeeded(int strNeeded) {
        this.strNeeded = strNeeded;
    }

    public int getDexNeeded() {
        return dexNeeded;
    }

    public void setDexNeeded(int dexNeeded) {
        this.dexNeeded = dexNeeded;
    }

    public int getWisNeeded() {
        return wisNeeded;
    }

    public void setWisNeeded(int wisNeeded) {
        this.wisNeeded = wisNeeded;
    }

    public int getIntNeeded() {
        return intNeeded;
    }

    public void setIntNeeded(int intNeeded) {
        this.intNeeded = intNeeded;
    }

    public int getConNeeded() {
        return conNeeded;
    }

    public void setConNeeded(int conNeeded) {
        this.conNeeded = conNeeded;
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    public void setisWeapon(boolean weapon) {
        isWeapon = weapon;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getItemsSet() {
        return itemsSet;
    }

    public void setItemsSet(String itemsSet) {
        this.itemsSet = itemsSet;
    }
}
