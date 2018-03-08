package ml.ixplo.arenabot.user.classes.spellcaster;

import ml.ixplo.arenabot.BaseTest;
import ml.ixplo.arenabot.battle.BattleState;
import ml.ixplo.arenabot.battle.Order;
import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.helper.Presets;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.classes.Mage;
import ml.ixplo.arenabot.user.params.Hark;
import ml.ixplo.arenabot.user.spells.Spell;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * ixplo
 * 09.05.2017.
 */
public class SpellCasterTest extends BaseTest {

    private Mage testMage = (Mage) mage;

    @Test
    public void countReceivedSpellPoints() throws Exception {
        assertEquals(0, SpellCaster.countReceivedSpellPoints(1, 98));
        assertEquals(1, SpellCaster.countReceivedSpellPoints(2, 118));
        assertEquals(2, SpellCaster.countReceivedSpellPoints(142, 99));
        assertEquals(2, SpellCaster.countReceivedSpellPoints(300, 0));
    }

    @Test
    public void addWisAndcurManaDontChangesInBattleTest() throws Exception {

        double before = testMage.getCurMana();

        testMage.addHark(Hark.NATIVE_WIS, 1);
        Assert.assertNotEquals(before, testMage.getCurMana(), Presets.DELTA);

        testMage.setStatus(Config.IN_BATTLE_STATUS);

        double expected = testMage.getCurMana();
        testMage.addHark(Hark.NATIVE_WIS, 1);
        Assert.assertEquals(expected, testMage.getCurMana(), Presets.DELTA);
    }

    @Test
    public void addWisTest() {
        double before = testMage.getCurMana();
        testMage.addHark(Hark.NATIVE_WIS, 1);
        double after = testMage.getCurMana();
        double difference = after - before;
        Assert.assertEquals(SpellCaster.MANA_FACTOR, difference, Presets.DELTA);
    }

    @Test
    public void putOnPutOffClassFeaturesNotInBattleTest() throws Exception {
        double before = testMage.getCurMana();
        testMage.addItem(Presets.RAINBOW_BRACELET);

        testMage.putOn(Presets.NEW_ITEM_INDEX);
        double afterPutOnNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, before, Presets.DELTA);
        Assert.assertEquals(12, afterPutOnNotInBattle, Presets.DELTA);

        testMage.putOff(Presets.NEW_ITEM_INDEX);
        double afterPutOffNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, afterPutOffNotInBattle, Presets.DELTA);
    }

    @Test
    public void putOnPutOffClassFeaturesInBattleTest() throws Exception {
        double before = testMage.getCurMana();
        testMage.addItem(Presets.RAINBOW_BRACELET);
        testMage.setStatus(Config.IN_BATTLE_STATUS);

        testMage.putOn(Presets.NEW_ITEM_INDEX);
        double afterPutOnNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, before, Presets.DELTA);
        Assert.assertEquals(9, afterPutOnNotInBattle, Presets.DELTA);

        testMage.putOff(Presets.NEW_ITEM_INDEX);
        double afterPutOffNotInBattle = testMage.getCurMana();
        Assert.assertEquals(9, afterPutOffNotInBattle, Presets.DELTA);
    }

    @Test
    public void getCastsIdForCallbacksTest() throws Exception {
        List<String> castsId = testMage.getCastsIdForCallbacks();
        Assert.assertTrue(castsId.contains("spell_1am"));
    }

    @Test
    public void getSpellsTest() throws Exception {
        List<Spell> spells = testMage.getSpells();
        Assert.assertTrue(spells.stream().anyMatch(a -> a.getId().equals(Presets.MAGIC_ARROW_SPELL_ID)));
        Assert.assertTrue(spells.stream().anyMatch(a -> a.getOwnerId() == Presets.MAGE_ID));
    }

    @Test
    public void getCastsNameTest() throws Exception {
        List<String> castsName = testMage.getCastsName();
        Assert.assertTrue(castsName.contains(Presets.MAGE_SPELL_NAME));
    }

    @Test
    public void learnSpellTest() {
        StringBuilder log = testHelper.initLogger();

        testMage.addSpellPoints(1);
        testMage.learn(1);

        Assert.assertTrue(0 == testMage.getSpellPoints());
        Assert.assertTrue(log.toString().contains("ничему и не научились")
                || log.toString().contains("Вы выучили заклинание")
                || log.toString().contains("Уровень заклинания") && log.toString().contains("поднялся до"));
    }

    @Test
    public void learnNoSpellPointsTest() {
        StringBuilder log = testHelper.initLogger();

        testMage.learn(1);
        Assert.assertTrue(log.toString().contains("Не хватает магических бонусов"));
    }

    @Test
    public void learnAllSpellsTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        for (int i = 0; i < 30; i++) {
            testMage.addSpellPoints(1);
            testMage.learn(1);
        }

        Assert.assertTrue(log.toString().contains("У вас уже есть все заклинания 1 уровня"));
    }

    @Test
    public void learnSpellInsufficientLevelTest() throws Exception {
        StringBuilder log = testHelper.initLogger();

        testMage.addSpellPoints(2);
        testMage.learn(2);

        Assert.assertTrue(log.toString().contains("Вы не достигли нужного уровня"));
    }

    @Test
    public void endBattleClassFeaturesTest() {
        StringBuilder log = testHelper.initLogger();

        testMage.endBattleClassFeatures();
        Assert.assertFalse(log.toString().contains("Вы получили магические бонусы: 1"));

        testMage.addCurExp(120);
        testMage.endBattleClassFeatures();
        Assert.assertTrue(log.toString().contains("Вы получили магические бонусы: 1"));
    }

    @Test
    public void zeroPercentCastTest() {
        String castMessage = testMage.doCast(warrior, 0, Presets.MAGIC_ARROW_SPELL_ID);
        Assert.assertTrue(castMessage.contains("заклинание провалилось"));
    }

    @Test
    public void zeroManaCastTest() {
        testMage.setCurMana(0);
        StringBuilder log = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            String castMessage = testMage.doCast(warrior, 100, Presets.MAGIC_ARROW_SPELL_ID);
            log.append(castMessage);
        }
        Assert.assertTrue(log.toString().contains("не хватило маны"));
    }

    @Test
    public void healEffectTest() {
        testMage.setSpell(Presets.HEAL_SPELL_ID);
        String castMessage = testMage.doCast(testMage, 100, Presets.HEAL_SPELL_ID);

        Assert.assertTrue(castMessage.contains("поднял здоровье"));
    }

    @Test
    public void armorEffectTest() {
        testHelper.getTestRound();
        testMage.setSpell(Presets.ARMOR_SPELL_ID);
        String castMessage = testMage.doCast(warrior, 100, Presets.ARMOR_SPELL_ID);

        Assert.assertTrue(castMessage.contains("поднял защиту"));
    }

    @Test
    public void armorEffectWithProtectTest() throws InterruptedException {
        Action attack = Action.create(Presets.WARRIOR_ID, Action.ATTACK, Presets.WARRIOR_ID, 1);
        testHelper.getTestRound().takeAction(attack);

        testMage.setSpell(Presets.ARMOR_SPELL_ID);
        testMage.doCast(warrior, 100, Presets.ARMOR_SPELL_ID);

        boolean check = false;
        List<Order> orders = Round.getCurrent().getOrders();
        for (Order order : orders) {
            List<Action> actions = order.getActions();
            for (Action action : actions) {
                if (action.getMessage().contains("пытался ударить")) {
                    check = true;
                    break;
                }
            }
        }
        Assert.assertTrue(check);
    }

    @Test
    public void armorEffectButPenetrationTest() throws InterruptedException {
        warrior.setAttack(BigDecimal.valueOf(12));
        testHelper.getDb().updateUser(warrior);
        Action attack = Action.create(Presets.WARRIOR_ID, Action.ATTACK, Presets.WARRIOR_ID, 100);
        testHelper.getTestRound().takeAction(attack);
        attack.doAction();

        testMage.setSpell(Presets.ARMOR_SPELL_ID);
        testMage.doCast(warrior, 100, Presets.ARMOR_SPELL_ID);

        boolean check = false;
        List<Order> orders = Round.getCurrent().getOrders();
        for (Order order : orders) {
            List<Action> actions = order.getActions();
            for (Action action : actions) {
                if (action.getMessage().contains("напал")) {
                    check = true;
                    break;
                }
            }
        }
        Assert.assertTrue(check);
    }
}