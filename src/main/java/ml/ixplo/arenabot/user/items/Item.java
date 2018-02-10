package ml.ixplo.arenabot.user.items;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.user.ArenaUser;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.math.IntMath.pow;
import static ml.ixplo.arenabot.messages.Messages.fillWithSpaces;

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

    // поля, относящиеся к владельцу вещи, заполняются в arenaUser.getItems
    private Integer ownerId;
    private int eqipIndex;
    private String inSlot;

    public Item() {
    }

    public Item(String itemId) {
        this.itemId = itemId;
    }

    // items count in eq
    public static int getEqipAmount(Integer userId) {
        return db.getCount(Config.EQIP, Config.USER_ID, userId);
    }

    private static List<String> getItemsId(int userId) {
        return db.getStrings(Config.EQIP, Config.USER_ID, userId, "id");
    }

    public static List<Item> getItems(int userId) {
        List<Item> items = new LinkedList<>();
        for (String id : getItemsId(userId)) {
            items.add(db.getItem(id));
        }
        return items;
    }

    public static int getEqipIndex(Integer userId, String itemId) {
        return db.getIntByBy(Config.EQIP, "counter", "id", itemId, Config.USER_ID, userId) - 1;
    }

    public static boolean isItemInSlot(int userId, int eqipIndex) {
        return db.getSlot(userId, eqipIndex) != null;
    }

    public static String getItemId(int userId, int eqipIndex) {
        return db.getStringByBy(Config.EQIP, "id", "counter", eqipIndex + 1, Config.USER_ID, userId);
    }

    public static String getItemInSlot(int userId, int eqipIndex) {
        return db.getStringsBy(Config.INVENTORY, Config.IN_SLOT, "user_Id", userId, "counter", eqipIndex + 1).get(0);
    }

    public static void add(int userId, String itemId) {
        db.addItem(userId, itemId);
    }

    public static void drop(int userId, int eqipIndex) {
        db.dropItem(userId, getItemId(userId, eqipIndex));
    }

    public static void putOn(ArenaUser arenaUser, int eqipIndex) {

        //*** проверка на наличие в инвентаре
        if (eqipIndex + 1 > getEqipAmount(arenaUser.getUserId())) {
            throw new IllegalArgumentException("Invalid eqip index: " + eqipIndex);
        }
        //*** проверка, а не надета ли уже она
        if (isItemInSlot(arenaUser.getUserId(), eqipIndex)) {
            throw new IllegalArgumentException("Attempt to puton item that is already in slot");
        }
        Item item = getItem(getItemId(arenaUser.getUserId(), eqipIndex));
        //*** проверка, а не надета ли в этот слот другая вещь
        List<Item> itemsList = arenaUser.getItems();
        // проверкa, пустой ли слот и освобождение его
        for (Item oneItem : itemsList) {
            if (oneItem.getSlot().equals(item.getSlot()) && oneItem.isInSlot()) {
                // сделать putOff вещи, которая тут раньше была надета
                Item.putOff(arenaUser, oneItem.getEqipIndex());
            }
        }
        //todo проверка на соответствие требованиям (другие вещи тоже надо проверить, на случай если харки уменьшатся)
        //*** изменение характеристик перса
        arenaUser.setCurStr(arenaUser.getCurStr() + item.getStrBonus());
        arenaUser.setCurDex(arenaUser.getCurDex() + item.getDexBonus());
        arenaUser.setCurWis(arenaUser.getCurWis() + item.getWisBonus());
        arenaUser.setCurInt(arenaUser.getCurInt() + item.getIntBonus());
        arenaUser.setCurCon(arenaUser.getCurCon() + item.getConBonus());
        arenaUser.setMinHit(arenaUser.getMinHit() + item.getMinHit() + (double) item.getStrBonus() / 4);
        arenaUser.setMaxHit(arenaUser.getMaxHit() + item.getMaxHit() + (double) item.getStrBonus() / 4);
        arenaUser.setAttack(arenaUser.getAttack().add(BigDecimal.valueOf(item.getAttack() + roundDouble(0.91 * item.getDexBonus() + 0.39 * item.getStrBonus()))));
        arenaUser.setProtect(arenaUser.getProtect() + item.getProtect() + roundDouble(0.4 * item.getDexBonus() + 0.6 * item.getConBonus()));
        arenaUser.setMaxHitPoints(arenaUser.getMaxHitPoints() + roundDouble(1.3333333 * item.getConBonus()));//todo переделать, иначе выскочит нецелое число
        if (arenaUser.getStatus() != Config.IN_BATTLE) {
            arenaUser.setCurHitPoints(arenaUser.getMaxHitPoints()); // not in battle
        }
        arenaUser.setMagicProtect(arenaUser.getMagicProtect() + roundDouble(0.6 * item.getWisBonus() + 0.4 * item.getIntBonus()));
        arenaUser.setHeal(arenaUser.getHeal() + roundDouble(0.06 * item.getWisBonus() + 0.04 * item.getIntBonus()));
        //*** заносим in_slot
        item.markAsPuttedOn(arenaUser.getUserId(), eqipIndex);
        arenaUser.putOnClassFeatures(item);
        if (item.isWeapon()) {
            arenaUser.setCurWeapon(eqipIndex);
        }
        db.updateUser(arenaUser);
        //todo снова изменить характеристики из-за предыдущего пункта
    }


    public static void putOff(ArenaUser arenaUser, int eqipIndex) {

        //*** проверка на наличие в инвентаре
        if (eqipIndex + 1 > getEqipAmount(arenaUser.getUserId())) {
            throw new IllegalArgumentException("Invalid eqip index: " + eqipIndex);
        }
        //*** проверка, а надета ли она
        if (!isItemInSlot(arenaUser.getUserId(), eqipIndex)) {
            throw new IllegalArgumentException("Attempt to putoff item that is not in slot");
        }
        //todo проверка на соответствие требованиям (другие вещи тоже надо проверить, на случай если харки уменьшатся)

        Item item = getItem(getItemId(arenaUser.getUserId(), eqipIndex));

        //*** изменение характеристик перса
        arenaUser.setCurStr(arenaUser.getCurStr() - item.getStrBonus());
        arenaUser.setCurDex(arenaUser.getCurDex() - item.getDexBonus());
        arenaUser.setCurWis(arenaUser.getCurWis() - item.getWisBonus());
        arenaUser.setCurInt(arenaUser.getCurInt() - item.getIntBonus());
        arenaUser.setCurCon(arenaUser.getCurCon() - item.getConBonus());
        arenaUser.setMinHit(arenaUser.getMinHit() - item.getMinHit() - (double) item.getStrBonus() / 4);
        arenaUser.setMaxHit(arenaUser.getMaxHit() - item.getMaxHit() - (double) item.getStrBonus() / 4);
        arenaUser.setAttack(arenaUser.getAttack().subtract(BigDecimal.valueOf(item.getAttack() - roundDouble(0.91 * item.getDexBonus() + 0.39 * item.getStrBonus()))));
        arenaUser.setProtect(arenaUser.getProtect() - item.getProtect() - roundDouble(0.4 * item.getDexBonus() + 0.6 * item.getConBonus()));
        arenaUser.setMaxHitPoints(arenaUser.getMaxHitPoints() - roundDouble(1.3333333 * item.getConBonus()));//todo переделать на BigDecimal, иначе выскочит нецелое число
        if (arenaUser.getStatus() != Config.IN_BATTLE) {
            arenaUser.setCurHitPoints(arenaUser.getMaxHitPoints());
        }
        arenaUser.setMagicProtect(arenaUser.getMagicProtect() - roundDouble(0.6 * item.getWisBonus() + 0.4 * item.getIntBonus()));
        arenaUser.setHeal(arenaUser.getHeal() - roundDouble(0.06 * item.getWisBonus() + 0.04 * item.getIntBonus()));
        //*** заносим in_slot
        item.markAsPuttedOff(arenaUser.getUserId(), eqipIndex);
        arenaUser.putOffClassFeatures(item);
        // если вещь - оружие, и не Ладошка, то надеваем Ладошку
        if (item.isWeapon()) {
            if (item.eqipIndex != 0) {
                putOn(arenaUser, 0);
            } else {
                arenaUser.setCurWeapon(-1);
            }
        }
        db.updateUser(arenaUser);
        //todo снова изменить характеристики из-за предыдущего пункта
    }


    public static String getItemName(Integer userId, Integer eqipIndex) {
        return db.getItem(Item.getItemId(userId, eqipIndex)).name;
    }

    public static Item getItem(Integer userId, Integer eqipIndex) {
        Item item = db.getItem(Item.getItemId(userId, eqipIndex));
        item.setOwnerId(userId);
        item.setInSlot(getItemInSlot(userId, eqipIndex));
        item.setEqipIndex(eqipIndex);
        return item;
    }

    public static Item getItem(String itemId) {
        return db.getItem(itemId);
    }

    public static String getItemInfo(Integer userId, int eqipIndex) {
        Item item = Item.getItem(Item.getItemId(userId, eqipIndex));
        StringBuilder out = new StringBuilder();
        out.append("Вещь: <b>").append(item.getName()).append("</b> \nЦена [").append(item.getPrice());
        out.append("]\n\n").append(item.getDescr()).append("\n\n");
        if (item.isWeapon()) {
            out.append(fillWithSpaces("<code>Урон:", item.getMinHit() + "-" + item.getMaxHit() + "</code>\n", Config.WIDTH));
        }
        out.append(fillWithSpaces("<code>Атака:", item.getAttack() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Защита:", item.getProtect() + "</code>\n", Config.WIDTH));
        if (item.getStrBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Силе:", item.getStrBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getDexBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Ловкости:", item.getDexBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getWisBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Мудрости:", item.getWisBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getIntBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Интеллекту:", item.getIntBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getConBonus() != 0) {
            out.append(fillWithSpaces("<code>Бонус к Телосложению:", item.getConBonus() + "</code>\n", Config.WIDTH));
        }
        if (item.getStrNeeded() != 0
                || item.getDexNeeded() != 0
                || item.getWisNeeded() != 0
                || item.getIntNeeded() != 0
                || item.getConNeeded() != 0) {
            out.append("\n\nТребования к характеристикам:\n");
        }
        if (item.getStrNeeded() != 0) {
            out.append(fillWithSpaces("<code>Сила:", item.getStrNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getDexNeeded() != 0) {
            out.append(fillWithSpaces("<code>Ловкость:", item.getDexNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getWisNeeded() != 0) {
            out.append(fillWithSpaces("<code>Мудрость:", item.getWisNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getIntNeeded() != 0) {
            out.append(fillWithSpaces("<code>Интеллект:", item.getIntNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getConNeeded() != 0) {
            out.append(fillWithSpaces("<code>Телосложение:", item.getConNeeded() + "</code>\n", Config.WIDTH));
        }
        if (item.getRace() != null) {
            out.append(fillWithSpaces("<code>Только для расы:", item.getRace() + "</code>\n", Config.WIDTH));
        }
        out.append(fillWithSpaces("<code>Слот:", Item.getSlotName(item.getSlot()) + "</code>\n", Config.WIDTH));
        String isInSlot = Item.isItemInSlot(eqipIndex, userId) ? "Надето" : "Не надето";
        out.append(fillWithSpaces("<code>Состояние:",
                isInSlot + "</code>\n", Config.WIDTH));
        return out.toString();
    }

    public static String getSlotName(String slot) {
        return db.getStringFrom("SLOTS", slot, "name");
    }

    /***************************************************
     ********* Instance ********************************
     ***************************************************/

    public void drop() {
        drop(ownerId, eqipIndex);
    }

    public void putOn() {
        Item.putOn(db.getUser(ownerId), eqipIndex);
    }

    public void putOff() {
        Item.putOff(db.getUser(ownerId), eqipIndex);
    }

    public int getEqipIndex() {
        return eqipIndex;
    }

    public void setEqipIndex(int eqipIndex) {
        this.eqipIndex = eqipIndex;
    }

    public boolean isInSlot() {
        return inSlot != null;
    }

    public String getInfo() {
        return getItemInfo(ownerId, eqipIndex);
    }

    private boolean markAsPuttedOn(Integer userId, int eqipIndex) {
        return db.setStringTo(Config.EQIP, userId, getItemId(userId, eqipIndex), "in_slot", slot);
    }

    private boolean markAsPuttedOff(Integer userId, int eqipIndex) {
        return db.setStringTo(Config.EQIP, userId, getItemId(userId, eqipIndex), "in_slot", null);
    }

    private static double roundDouble(double d) {
        return roundDouble(d, 2);
    }

    private static double roundDouble(double d, int precise) {
        int prec = pow(10, precise);
        double di = d * prec;
        int i = (int) Math.round(di);
        return (double) i / prec;
    }

    public ArenaUser getOwner() {
        if (ownerId == null) {
            throw new ArenaUserException("Wrong item owner");
        }
        return db.getUser(ownerId);
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setInSlot(String inSlot) {
        this.inSlot = inSlot;
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

    public String getInSlot() {
        return inSlot;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", eqipIndex='" + eqipIndex + '\'' +
                ", price=" + price +
                ", slot='" + slot + '\'' +
                ", inSlot='" + inSlot + '\'' +
                '}';
    }
}
