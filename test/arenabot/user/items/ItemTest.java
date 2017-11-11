package arenabot.user.items;

import arenabot.database.DatabaseManager;
import arenabot.test.TestHelper;
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

    @Before
    public void setUp() throws Exception {
        testHelper.init();
    }

    @Test
    public void putOn() throws Exception {
        LOGGER.info("Инвентарь: {}", testHelper.WARRIOR.getItems());
        LOGGER.info("Добавляем вещь: {}", Item.getItem("wba"));
        testHelper.db.addItem(testHelper.WARRIOR.getUserId(), "wba");
        int eqipIndex = -1;
        LOGGER.info("Инвентарь:");
        for (Item item : testHelper.WARRIOR.getItems()) {
            LOGGER.info("{} Item: {}", item.getName(), item.getEqipIndex());
            if (item.getItemId().equals("wba")) {
                eqipIndex = item.getEqipIndex();
            }
        }
        LOGGER.info("Тестовый персонаж: {}", testHelper.WARRIOR);
        Map<String, Object> startParams = testHelper.WARRIOR.getParams();
        LOGGER.info("Надеваем вещь {} {}",
                testHelper.WARRIOR.getItems().get(eqipIndex - 1).getEqipIndex(),
                testHelper.WARRIOR.getItems().get(eqipIndex - 1).getName());
        testHelper.WARRIOR.putOn(eqipIndex);
        Map<String, Object> params1 = testHelper.WARRIOR.getParams();
        LOGGER.info("Тестовый персонаж: {}", testHelper.WARRIOR);

        LOGGER.info("Надеваем вещь: {} {}",
                testHelper.WARRIOR.getItems().get(0).getEqipIndex(),
                testHelper.WARRIOR.getItems().get(0).getName());
        testHelper.WARRIOR.putOn(testHelper.WARRIOR.getItems().get(0).getEqipIndex());
        Map<String, Object> params2 = testHelper.WARRIOR.getParams();
        LOGGER.info("Тестовый персонаж: {}", testHelper.WARRIOR);
        LOGGER.info("Инвентарь: {}", testHelper.WARRIOR.getItems());
        for (Item item : testHelper.WARRIOR.getItems()) {
            LOGGER.info("{} Item: {}", item.getName(), item.getEqipIndex());
        }
        Assert.assertNotEquals("Характеристики не изменились ", startParams, params1);
        Assert.assertEquals("Характеристики не совпадают ", startParams, params2);
        //todo доделать сравнение характеристик
    }

    @After
    public void tearDown() throws Exception {
        testHelper.close();
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
    public void putOff() throws Exception {
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