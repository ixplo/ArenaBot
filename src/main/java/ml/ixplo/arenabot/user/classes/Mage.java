package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.battle.actions.Attack;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.user.spells.Spell;
import ml.ixplo.arenabot.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ml.ixplo.arenabot.messages.Messages.fillWithSpaces;
import static ml.ixplo.arenabot.user.spells.Spell.getSpell;

/**
 * ixplo
 * 28.04.2017.
 */
public class Mage extends ArenaUser implements SpellCaster {
    private List<Spell> spells;
    private double maxMana;
    private double curMana;
    private int spellPoints;
    private double magicAttack;

    public Mage() {
        setUserClass("MAGE");
    }

    public static List<String> getAllSpellsName() {
        return getDb().getStrings(Config.SPELLS, "class", UserClass.MAGE.toString(), "name");
    }

    @Override
    public void setClassFeatures() {
        actionsName = Arrays.asList("Атака", "Защита", "Лечение", "Магия");
        setMaxMana(1.5 * getCurWis());
        setCurMana(maxMana);
        setMagicAttack(Utils.roundDouble(0.6 * getCurWis() + 0.4 * getCurInt()));
        setSpell("1am");
    }

    @Override
    public void getClassFeatures() {
        actionsName = Arrays.asList("Атака", "Защита", "Лечение", "Магия");
        maxMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "mana");
        curMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "cur_mana");
        spellPoints = getDb().getIntFrom(Config.USERS, getUserId(), "s_points");
        magicAttack = getDb().getDoubleFrom(Config.USERS, getUserId(), "m_attack");
        spells = getSpells();
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        //todo переделать нормально
        out.append(fillWithSpaces("\n<code>Мана:", getCurMana() + "</code>", Config.WIDTH));
        out.append(fillWithSpaces("\n<code>Магич. бонусы:", getSpellPoints() + "</code>\n", Config.WIDTH));
    }

    @Override
    public void putOnClassFeatures(Item item) {
        setMaxMana(getMaxMana() + item.getWisBonus() * 1.5);
        if (getStatus() != 2) { // not in battle
            setCurMana(getMaxMana());
        }
        setMagicAttack(getMagicAttack() + Utils.roundDouble(0.6 * item.getWisBonus() + 0.4 * item.getIntBonus()));
    }

    @Override
    public void putOffClassFeatures(Item item) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {
        if (harkToUpId.equals("nativeWis")) {
            setMaxMana(getMaxMana() + numberOfPoints * 1.5);
            if (getStatus() != 2) { // not in battle
                setCurMana(getMaxMana());
            }
            setMagicAttack(getMagicAttack() + Utils.roundDouble(0.6 * numberOfPoints));
        }
        if (harkToUpId.equals("nativeInt")) {
            setMagicAttack(getMagicAttack() + Utils.roundDouble(0.4 * numberOfPoints));
        }
    }

    @Override
    public void doAction(String[] command) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public String doCast(ArenaUser target, int percent, String spellId) {
        Spell spell = getSpell(spellId);
        if (!isHappened(spell.getProbability() * percent / 100)) { //(Math.log(getMagicAttack()/target.getMagicProtect() + 4.6)/7)
            return "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + "] на "
                    + target.getName() + ", но заклинание провалилось.</code>";
        }
        if (spell.getManaCost() > curMana) {
            return "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + "] на "
                    + target.getName() + ", но у него не хватило маны.</code>";
        }

        double damage = Utils.roundDouble(spell.getDamage() + spell.getSpellBonus(getUserId()));
        if (spell.getEffect().equals("DAMAGE")) {
            target.addCurHitPoints(-damage);
            addCurMana(-spell.getManaCost());
            addCurExp((int) (damage * spell.getExpBonus()));
            return "<pre>" + getName() + " запустил заклинанием [" + spell.getName() + "] в " +
                    target.getName() + " и ранил его на " + damage +
                    "\n(жизни:-" + damage + "/" + target.getCurHitPoints() +
                    " \\\\ опыт:+" + damage * spell.getExpBonus() + "/" + getCurExp() + ")</pre>";
        } else if (spell.getEffect().equals("HEAL")) {
            if (target.getMaxHitPoints() - target.getCurHitPoints() < damage) {
                damage = Utils.roundDouble(target.getMaxHitPoints() - target.getCurHitPoints());
            }
            target.addCurHitPoints(damage);
            addCurMana(-spell.getManaCost());
            addCurExp((int) (damage * spell.getExpBonus()));
            return "<pre>" + getName() + " Магией [" + spell.getName() + "] поднял здоровье у " +
                    target.getName() + " на " + damage +
                    "\n(жизни:" + damage + "/" + target.getCurHitPoints() +
                    " \\\\ опыт:+" + damage * spell.getExpBonus() + "/" + getCurExp() + ")</pre>";
        } else if (spell.getEffect().equals("ARMOR")) {
            double armor = Utils.roundDouble(spell.getArmor() + spell.getSpellBonus(getUserId()));
            List<Action> attackOnTargetList = Round.getCurrent().getAttackOnTargetList(target.getUserId());
            if (attackOnTargetList.isEmpty()) {
                return "<pre>" + getName() + " использовал заклинание [" + spell.getName() + "] на " +
                        target.getName() + " и поднял защиту на " + armor +
                        "\n(опыт:+" + spell.getExpBonus() + "/" + getCurExp() + ")</pre>";
            }
            for (Action attackAction : attackOnTargetList) {
                BigDecimal attack = attackAction.getUser().getAttack().multiply(new BigDecimal(attackAction.getPercent() / 100));
                if (attack.doubleValue() > armor) {
                    break;
                }
                int experience = (int) (4 * ((Attack) attackAction).getHit());
                addCurExp(experience);
                ((Attack) attackAction).unDo();
                attackAction.setMessage("<pre>" + attackAction.getUser().getName() + " пытался ударить " + target.getName() +
                        " оружием [" + Item.getItemName(attackAction.getUser().getUserId(), attackAction.getUser().getCurWeaponIndex()) + "], но ему не удалось " +
                        "\n(" + getName() + "[опыт:+" + experience + "/" + getCurExp() + ")</pre>");
            }
        }
        return "";
    }

    @Override
    public void endBattleClassFeatures() {
        setCurMana(getMaxMana());
        int newSpellPoints = countReceivedSpellPoints(getCurExp(), getExperience());
        if (newSpellPoints > 0) {
            addSpellPoints(newSpellPoints);
            Messages.sendMessage((long) getUserId(), "Вы получили магические бонусы: " + newSpellPoints);
            showLearnButton();
        }
    }

    @Override
    public String getClassActionId(String actionId) {
        if (actionId.equals("action_Магия")) {
            return "spell_spell";
        }
        return actionId;
    }

    @Override
    public List<String> getCastsName() {
        return getSpellsName();
    }

    @Override
    public List<String> getCastsId() {
        List<String> castsId = new ArrayList<>();
        for (String spellId : getSpellsId()) {
            castsId.add("spell_" + spellId);
        }
        return castsId;
    }

    @Override
    public void learn(int spellLevel) {

        if (spellLevel > getLevel()) {
            Messages.sendMessage((long) getUserId(), "Вы не достигли нужного уровня, " +
                    "ваш текущий уровень: " + getLevel());
            return;
        }
        if (spellLevel > getSpellPoints()) {
            Messages.sendMessage((long) getUserId(), "Не хватает магических бонусов, " +
                    "доступно: " + getSpellPoints());
            return;
        }
        int probability = (getLevel() - spellLevel + 2) * 25;
        if (isHappened(probability)) {
            String spellId = getSpellToLearn(spellLevel);
            setSpell(spellId);
            int spellGrade = Spell.getSpellGrade(getUserId(), spellId);
            if (spellGrade == 1) {
                Messages.sendMessage((long) getUserId(), "Вы выучили заклинание: "
                        + Spell.getSpell(spellId).getName());
            } else {
                Messages.sendMessage((long) getUserId(), "Уровень заклинания "
                        + Spell.getSpell(spellId).getName()
                        + " поднялся до " + spellGrade);
            }
        } else {
            Messages.sendMessage((long) getUserId(), "Вы всю ночь потели над книжками, "
                    + "пытаясь выучить новое заклинание, но так ничему и не научились.");
        }
        addSpellPoints(-spellLevel);
        showLearnButton();
    }

    private void setSpell(String spellId) {

        getDb().addSpell(getUserId(), spellId, Spell.getSpellGrade(getUserId(), spellId) + 1);
    }

    static int countReceivedSpellPoints(int curExp, int exp) {
        return (exp + curExp) / 120 - exp / 120; //not equals curExp/120 because int cuts fraction
    }

    private void showLearnButton() {

        Messages.sendMessage(Messages.getInlineKeyboardMsg(
                (long) getUserId(),
                "Всего бонусов: " + getSpellPoints(),
                Collections.singletonList("Выучить заклинание"),
                Collections.singletonList("learnSpell_1"))); //first level spell
    }

    private boolean isHappened(int probability) {
        Random rnd = new Random();
        int chance = rnd.nextInt(99) + 1;
        return chance <= probability;
    }

    @Override
    public void castSpell(String spellId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void learnSpell(int spellLevel) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);

    }

    private String getSpellToLearn(int spellLevel) {
        List<String> spellsId = getDb().getSpellsIdToLearn(getUserId(), spellLevel);
        Random rnd = new Random();
        return spellsId.get(rnd.nextInt(spellsId.size()));
    }

    @Override
    public void manaRegen() {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    public List<Spell> getSpells() {
        List<Spell> spells = new ArrayList<>();
        List<String> spellsId = getSpellsId();
        for (String spellId : spellsId) {
            spells.add(Spell.getSpell(spellId));
        }
        return spells;
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
        getDb().setDoubleTo(Config.USERS, getUserId(), "mana", maxMana);
    }

    public void addSpellPoints(int spellPoints) {

        getDb().setIntTo(Config.USERS, getUserId(), "s_points", getSpellPoints() + spellPoints);
    }

    public void setMagicAttack(double magicAttack) {
        this.magicAttack = magicAttack;
        getDb().setDoubleTo(Config.USERS, getUserId(), "m_attack", magicAttack);
    }

    public void setCurMana(double curMana) {
        this.curMana = curMana;
        getDb().setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }

    public void addCurMana(double manaChange) {
        this.curMana += manaChange;
        getDb().setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }

    public double getMaxMana() {
        maxMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "mana");
        return maxMana;
    }

    public int getSpellPoints() {
        spellPoints = getDb().getIntFrom(Config.USERS, getUserId(), "s_points");
        return spellPoints;
    }

    public double getMagicAttack() {
        return magicAttack;
    }

    public double getCurMana() {
        return curMana;
    }


    public List<String> getSpellsName() {
        List<String> spellsName = new ArrayList<>();
        List<String> spellsId = getSpellsId();
        for (String spell : spellsId) {
            spellsName.add(getDb().getStringFrom(Config.SPELLS, spell, "name"));
        }
        return spellsName;
    }

    public List<String> getSpellsId() {
        return getDb().getStrings(Config.AVAILABLE_SPELLS, "user_id", getUserId(), "id");
    }

}
