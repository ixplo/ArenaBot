package arenabot.user.items;

import arenabot.database.DatabaseManager;
import arenabot.test.TestHelper;
import arenabot.user.ArenaUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ItemTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(ItemTest.class);

    private DatabaseManager db;
    private TestHelper testHelper = new TestHelper();
    private ArenaUser warrior = TestHelper.WARRIOR;

    @Before
    public void setUp() throws Exception {
        testHelper.init();
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
    }

    @Test
    public void putOn() throws Exception {
        LOGGER.info("Инвентарь: {}", warrior.getItems());
        LOGGER.info("Добавляем вещь: {}", Item.getItem("wba"));
        testHelper.db.addItem(warrior.getUserId(), "wba");
        int eqipIndex = -1;
        LOGGER.info("Инвентарь:");
        for (Item item : warrior.getItems()) {
            LOGGER.info("{} Item: {}", item.getName(), item.getEqipIndex());
            if (item.getItemId().equals("wba")) {
                eqipIndex = item.getEqipIndex();
            }
        }
        LOGGER.info("Тестовый персонаж: {}", warrior);
        Map<String, Object> startParams = warrior.getParams();
        LOGGER.info("Надеваем вещь {} {}",
                warrior.getItems().get(eqipIndex - 1).getEqipIndex(),
                warrior.getItems().get(eqipIndex - 1).getName());
        warrior.putOn(eqipIndex);
        Map<String, Object> params1 = warrior.getParams();
        Assert.assertNotEquals("Характеристики не изменились ", startParams, params1);
        LOGGER.info("Тестовый персонаж: {}", warrior);

        LOGGER.info("Надеваем вещь: {} {}",
                warrior.getItems().get(0).getEqipIndex(),
                warrior.getItems().get(0).getName());
        warrior.putOn(warrior.getItems().get(0).getEqipIndex());
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
        warrior.putOff(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putOff_twice() throws Exception {
        warrior.putOff(1);
        warrior.putOff(1);
    }

    @Test
    public void putOff_allGood() throws Exception {
        warrior.putOff(1);
    }

    @Test
    public void getEqipAmount() throws Exception {
    }

    @Test
    public void getItems() throws Exception {
    }

    @Test
    public void getEqipIndex() throws Exception {
    }

    @Test
    public void isItemInSlot() throws Exception {
    }

    @Test
    public void getItemId() throws Exception {
    }


    @Test
    public void getItemName() throws Exception {
    }

    @Test
    public void getItem() throws Exception {
    }

    @Test
    public void markAsPuttedOn() throws Exception {
    }

    @Test
    public void markAsPuttedOff() throws Exception {
    }

}