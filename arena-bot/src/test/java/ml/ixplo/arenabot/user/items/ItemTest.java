package ml.ixplo.arenabot.user.items;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.helper.TestHelper;
import ml.ixplo.arenabot.user.ArenaUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ml.ixplo.arenabot.user.items.Item;

import java.util.Map;

public class ItemTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);

    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = testHelper.WARRIOR;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }

    @Test
    public void getInfo() {
        Assert.assertTrue(warrior.getItems().get(Presets.ITEM_INDEX).getInfo().contains(Presets.ITEM_NAME));
    }

    @Test
    public void putOn() throws Exception {
        LOGGER.info("Инвентарь: {}", warrior.getItems());
        LOGGER.info("Добавляем вещь: {}", Item.getItem(Presets.FLAMBERG));
        warrior.addItem(Presets.FLAMBERG);
        int eqipIndex = -1;
        LOGGER.info("Инвентарь:");
        for (Item item : warrior.getItems()) {
            LOGGER.info("{} Item: {}", item.getName(), item.getEqipIndex());
            if (item.getItemId().equals(Presets.FLAMBERG)) {
                eqipIndex = item.getEqipIndex();
            }
        }
        LOGGER.info("Тестовый персонаж: {}", warrior);
        Map<String, Object> startParams = warrior.getParams();
        LOGGER.info("Надеваем вещь {} {}", Presets.NEW_ITEM_INDEX, Presets.FLAMBERG_NAME);
        warrior.putOn(eqipIndex);
        Map<String, Object> params1 = warrior.getParams();
        LOGGER.info("Тестовый персонаж: {}", warrior);
        Assert.assertNotEquals("Характеристики не изменились ", startParams, params1);

        LOGGER.info("Надеваем вещь: {} {}", Presets.ITEM_INDEX, Presets.ITEM_NAME);
        warrior.putOn(Presets.ITEM_INDEX);
        Map<String, Object> params2 = warrior.getParams();
        LOGGER.info("Тестовый персонаж: {}", warrior);
        LOGGER.info("Инвентарь: {}", warrior.getItems());
        for (Item item : warrior.getItems()) {
            LOGGER.info("{} Item: {}", item.getName(), item.getEqipIndex());
        }
        Assert.assertEquals("Характеристики не совпадают ", startParams, params2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putOff_wrongEqipIndex() throws Exception {
        warrior.putOff(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putOff_twice() throws Exception {
        warrior.addItem(Presets.FLAMBERG);
        warrior.putOff(1);
        warrior.putOff(1);
    }

    @Test
    public void putOff_allGood() throws Exception {
        warrior.putOff(0);
    }

    @Test
    public void getEqipAmount() throws Exception {
        Assert.assertEquals(1, warrior.getEqipAmount());
        warrior.addItem(Presets.FLAMBERG);
        Assert.assertEquals(2, warrior.getEqipAmount());
    }

    @Test
    public void getItems() throws Exception {
        Assert.assertEquals(0, warrior.getItems().get(0).getEqipIndex());
        warrior.addItem(Presets.FLAMBERG);
        Assert.assertEquals(1, warrior.getItems().get(1).getEqipIndex());
    }

    @Test
    public void getEqipIndex() throws Exception {
        Assert.assertEquals(0, warrior.getItems().get(0).getEqipIndex());
    }

    @Test
    public void getEqipIndexStatic() {
        Assert.assertEquals(0, Item.getEqipIndex(Presets.WARRIOR_ID, Presets.ITEM_ID));
    }

    @Test
    public void isInSlot() throws Exception {
        warrior.addItem(Presets.FLAMBERG);
        Assert.assertTrue(warrior.getItems().get(0).isInSlot());
        Assert.assertFalse(warrior.getItems().get(1).isInSlot());
        warrior.putOn(1);
        Assert.assertTrue(warrior.getItems().get(1).isInSlot());
        warrior.putOff(1);
        Assert.assertFalse(warrior.getItems().get(1).isInSlot());
    }

    @Test
    public void getItemId_AllGood() throws Exception {
        Assert.assertEquals("waa", warrior.getItems().get(0).getItemId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemId_WrongParam() throws Exception {
        Item.getItemId(warrior.getUserId(), Presets.WRONG_ITEM_INDEX);
    }

    @Test
    public void getItemName_allGood() throws Exception {
        Assert.assertEquals(Presets.ITEM_NAME, Item.getItemName(warrior.getUserId(), Presets.ITEM_INDEX));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemName_wrongParam() throws Exception {
        Item.getItemName(warrior.getUserId(), Presets.WRONG_ITEM_INDEX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemName_wrongUserId() throws Exception {
        Item.getItemName(Presets.NON_EXIST_USER_ID, Presets.ITEM_INDEX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItem_WrongParam() throws Exception {
        Item.getItem(Presets.WRONG_ITEM_ID);
    }

    @Test
    public void add_allGood() {
        Item.add(warrior.getUserId(), Presets.FLAMBERG);
        Assert.assertEquals(2, warrior.getItems().size());
        Assert.assertEquals(Presets.FLAMBERG, warrior.getItems().get(1).getItemId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_wrongItemId() {
        Item.add(warrior.getUserId(), Presets.WRONG_ITEM_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_wrongItemId() {
        Item.drop(warrior.getUserId(), Presets.WRONG_ITEM_INDEX);
    }

    @Test
    public void getItemStatic() {
        Assert.assertEquals(Presets.ITEM_ID, Item.getItem(Presets.WARRIOR_ID, 0).getItemId());
    }

    @Test
    public void drop() {
        Assert.assertEquals(1, warrior.getItems().size());
        warrior.addItem(Presets.FLAMBERG);
        warrior.getItems().get(1).drop();
        Assert.assertEquals(1, warrior.getItems().size());
    }

    @Test
    public void putOff() {
        warrior.addItem(Presets.FLAMBERG);
        Assert.assertFalse(warrior.getItems().get(1).isInSlot());
        warrior.getItems().get(1).putOn();
        Assert.assertTrue(warrior.getItems().get(1).isInSlot());
        warrior.getItems().get(1).putOff();
        Assert.assertFalse(warrior.getItems().get(1).isInSlot());
    }

    @Test
    public void getItem() {
        Item item = warrior.getItem(Presets.ITEM_INDEX);
        Assert.assertTrue(Presets.WARRIOR_ID == item.getOwner().getUserId());
        Assert.assertEquals(Presets.ITEM_INDEX, item.getEqipIndex());
        Assert.assertEquals(Presets.ITEM_SLOT, item.getInSlot());
    }

    @Test(expected = ArenaUserException.class)
    public void getOwnerNull() {
        Item item = new Item();
        item.getOwner();
    }

    @Test
    public void getShop() {
        Assert.assertEquals(Presets.ITEM_SHOP, warrior.getItem(0).getShop());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putOnWrongIndex() {
        Item.putOn(warrior, 999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putOnAlreadyInSlot() {
        warrior.addItem(Presets.FLAMBERG);
        warrior.putOn(Presets.NEW_ITEM_INDEX);
        warrior.putOn(Presets.NEW_ITEM_INDEX);
    }

    @Test
    public void putOnWithPutOff() {
        warrior.addItem(Presets.FLAMBERG);
        Assert.assertTrue(warrior.getItem(0).isInSlot());
        Item.putOn(warrior, 1);
        Assert.assertFalse(warrior.getItem(0).isInSlot());
        Assert.assertTrue(warrior.getItem(1).isInSlot());
    }

    @Test
    public void putOnNotWeapon() {
        warrior.addItem(Presets.ITEM_SHIRT);
        warrior.putOn(Presets.NEW_ITEM_INDEX);
        Assert.assertTrue(warrior.getItem(Presets.ITEM_INDEX).isInSlot());
        Assert.assertTrue(warrior.getItem(1).isInSlot());
        Assert.assertTrue(warrior.getItem(Presets.ITEM_INDEX).isWeapon());
        Assert.assertFalse(warrior.getItem(1).isWeapon());
        Assert.assertTrue(warrior.getCurWeaponIndex() == Presets.ITEM_INDEX);
        warrior.putOff(Presets.NEW_ITEM_INDEX);
        Assert.assertTrue(warrior.getCurWeaponIndex() == Presets.ITEM_INDEX);
    }

    @Test
    public void putOnDefaultWeapon() {
        warrior.addItem(Presets.FLAMBERG);
        Item.putOn(warrior, 1);
        Item.putOff(warrior, 1);
        Assert.assertFalse(warrior.getItem(1).isInSlot());
        Assert.assertTrue(warrior.getItem(0).isInSlot());
    }

    @Test
    public void getItemSet() {
        Assert.assertNull(warrior.getItem(0).getItemsSet());
    }

    @Test
    public void putOnInBattle() {
        warrior.setCurHitPoints(warrior.getMaxHitPoints() - 1);
        warrior.setStatus(Config.IN_BATTLE_STATUS);
        warrior.addItem(Presets.FLAMBERG);
        warrior.putOn(Presets.NEW_ITEM_INDEX);
        Assert.assertNotEquals(warrior.getCurHitPoints(), warrior.getMaxHitPoints());
    }

    @Test
    public void getItemInfo() {
        warrior.addItem("bad");
        String itemInfo = warrior.getItem(Presets.NEW_ITEM_INDEX).getInfo();
        Assert.assertTrue(itemInfo.contains("Радужный браслет"));
        Assert.assertTrue(itemInfo.contains("Бонус к Телосложению:    2"));
    }

    @Test
    public void getItemInfoRace() {
        warrior.addItem("zac");
        String itemInfo = warrior.getItem(Presets.NEW_ITEM_INDEX).getInfo();
        Assert.assertTrue(itemInfo.contains("Ятаган черного пламени"));
        Assert.assertTrue(itemInfo.contains("Только для расы:       Орк"));
    }

    @Test
    public void changeHarksPutOnWeaponToDefault() {
        warrior.addItem(Presets.FLAMBERG);
        warrior.putOn(Presets.NEW_ITEM_INDEX);
        warrior.putOn(Presets.ITEM_INDEX);
        Assert.assertTrue(warrior.getMaxHit() == 2.5);
    }
}