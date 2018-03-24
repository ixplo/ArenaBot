package ml.ixplo.arenabot.user.classes.spellcaster;

import ml.ixplo.arenabot.battle.Round;
import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.battle.actions.Attack;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.database.DatabaseManager;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.user.params.Hark;
import ml.ixplo.arenabot.user.spells.Spell;
import ml.ixplo.arenabot.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ml.ixplo.arenabot.messages.Messages.fillWithSpaces;

public abstract class SpellCaster extends ArenaUser implements ISpellCaster {
    private static final String NA = "] на ";
    public static final double MANA_FACTOR = 1.5;
    private double maxMana;
    private double curMana;
    private int spellPoints;
    private double magicAttack;

    @Override
    protected void setClassFeatures() {
        actionsName = Arrays.asList("Атака","Защита","Лечение", "Магия");
        setMaxMana(MANA_FACTOR * getCurWis());
        setCurMana(maxMana);
        setMagicAttack(Utils.roundDouble(0.6 * getCurWis() + 0.4 * getCurInt()));
    }

    public void setSpell(String spellId) {
        getDb().addSpell(getUserId(), spellId, Spell.getSpellGrade(getUserId(), spellId) + 1);
    }

    @Override
    protected void getClassFeatures() {
        actionsName = Arrays.asList("Атака", "Защита", "Лечение", "Магия");
        maxMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "mana");
        curMana = getDb().getDoubleFrom(Config.USERS, getUserId(), DatabaseManager.CUR_MANA);
        spellPoints = getDb().getIntFrom(Config.USERS, getUserId(), DatabaseManager.S_POINTS);
        magicAttack = getDb().getDoubleFrom(Config.USERS, getUserId(), "m_attack");
    }

    @Override
    protected void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {
        if (harkToUpId.equals(Hark.NATIVE_WIS)) {
            setMaxMana(getMaxMana() + numberOfPoints * MANA_FACTOR);
            if (getStatus() != Config.IN_BATTLE_STATUS) {
                setCurMana(getMaxMana());
            }
            setMagicAttack(getMagicAttack() + Utils.roundDouble(0.6 * numberOfPoints));
        }
        if (harkToUpId.equals(Hark.NATIVE_INT)) {
            setMagicAttack(getMagicAttack() + Utils.roundDouble(0.4 * numberOfPoints));
        }
    }

    @Override
    public void putOnClassFeatures(Item item) {
        setMaxMana(getMaxMana() + item.getWisBonus() * MANA_FACTOR);
        if (getStatus() != Config.IN_BATTLE_STATUS) {
            setCurMana(getMaxMana());
        }
        setMagicAttack(getMagicAttack() + Utils.roundDouble(0.6 * item.getWisBonus() + 0.4 * item.getIntBonus()));
    }

    @Override
    public void putOffClassFeatures(Item item) {
        setMaxMana(getMaxMana() - item.getWisBonus() * MANA_FACTOR);
        if (getStatus() != Config.IN_BATTLE_STATUS) {
            setCurMana(getMaxMana());
        }
        setMagicAttack(getMagicAttack() - Utils.roundDouble(0.6 * item.getWisBonus() + 0.4 * item.getIntBonus()));
    }

    // пока не используется
    public List<Spell> getSpells() {
        List<Spell> spellList = new ArrayList<>();
        for (String spellId : getSpellsId()) {
            spellList.add(Spell.getSpell(userId, spellId));
        }
        return spellList;
    }

    private List<String> getSpellsId() {
        return getDb().getStrings(Config.AVAILABLE_SPELLS, "user_id", getUserId(), "id");
    }

    @Override
    public List<String> getCastsIdForCallbacks() {
        List<String> castsId = new ArrayList<>();
        for (String spellId : getSpellsId()) {
            castsId.add("spell_" + spellId);
        }
        return castsId;
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        //todo переделать нормально
        out.append(fillWithSpaces("\n<code>Мана:", getCurMana() + "</code>", Config.WIDTH));
        out.append(fillWithSpaces("\n<code>Магич. бонусы:", getSpellPoints() + "</code>\n", Config.WIDTH));
    }

    private List<String> getSpellsName() {
        List<String> spellsName = new ArrayList<>();
        List<String> spellsId = getSpellsId();
        for (String spell : spellsId) {
            spellsName.add(getDb().getStringFrom(Config.SPELLS, spell, "name"));
        }
        return spellsName;
    }

    @Override
    public void castSpell(String spellId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    private String getSpellToLearn(int spellLevel) {
        List<String> spellsId = getDb().getSpellsIdToLearn(getUserId(), spellLevel);
        if (spellsId.isEmpty()) {
            return null;
        }
        Random rnd = new Random();
        return spellsId.get(rnd.nextInt(spellsId.size()));
    }

    @Override
    //todo сделать классы для каждого заклинания и делать обработку внутри, а тут только вызывать
    // у потомков Spell метод cast
    public String doCast(ArenaUser target, int percent, String spellId) {
        Spell spell = getSpell(spellId);
        String errorMessage = checkIfSpellCanBeCasted(target, percent, spell);
        if (errorMessage != null) {
            return errorMessage;
        }
        if (spell.getEffect().equals("DAMAGE")) {
            return handleDamageSpellEffect(target, spell);
        } else if (spell.getEffect().equals("HEAL")) {
            return handleHealSpellEffect(target, spell);
        } else if (spell.getEffect().equals("ARMOR")) {
            return handleArmorSpellEffect(target, spell);
        }
        return "";
    }

    private String handleHealSpellEffect(ArenaUser target, Spell spell) {
        double damage = Utils.roundDouble(spell.getDamage() + spell.getSpellBonus(getUserId()));
        if (target.getMaxHitPoints() - target.getCurHitPoints() < damage) {
            damage = Utils.roundDouble(target.getMaxHitPoints() - target.getCurHitPoints());
        }
        target.addCurHitPoints(damage);
        addCurMana(-spell.getManaCost());
        addCurExp((int) (damage * spell.getExpBonus()));
        return Config.PRE_TAG + getName() + " Магией [" + spell.getName() + "] поднял здоровье у " +
                target.getName() + " на " + damage +
                "\n(жизни:" + damage + "/" + target.getCurHitPoints() +
                " \\\\ опыт:+" + damage * spell.getExpBonus() + "/" + getCurExp() + ")" + Config.CLOSE_PRE_TAG;
    }

    private String handleDamageSpellEffect(ArenaUser target, Spell spell) {
        double damage = Utils.roundDouble(spell.getDamage() + spell.getSpellBonus(getUserId()));
        target.addCurHitPoints(-damage);
        addCurMana(-spell.getManaCost());
        addCurExp((int) (damage * spell.getExpBonus()));
        return Config.PRE_TAG + getName() + " запустил заклинанием [" + spell.getName() + "] в " +
                target.getName() + " и ранил его на " + damage +
                "\n(жизни:-" + damage + "/" + target.getCurHitPoints() +
                " \\\\ опыт:+" + damage * spell.getExpBonus() + "/" + getCurExp() + ")" + Config.CLOSE_PRE_TAG;
    }

    private String checkIfSpellCanBeCasted(ArenaUser target, int percent, Spell spell) {
        if (!isHappened(spell.getProbability() * percent / 100)) { //(Math.log(getMagicAttack()/target.getMagicProtect() + 4.6)/7)
            return "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + NA
                    + target.getName() + ", но заклинание провалилось.</code>";
        }
        if (spell.getManaCost() > curMana) {
            return "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + NA
                    + target.getName() + ", но у него не хватило маны.</code>";
        }
        return null;
    }

    private String handleArmorSpellEffect(ArenaUser target, Spell spell) {
        double armor = Utils.roundDouble(spell.getArmor() + spell.getSpellBonus(getUserId()));
        List<Action> attackOnTargetList = Round.getCurrent().getAttackOnTargetList(target.getUserId());
        if (attackOnTargetList.isEmpty()) {
            return Config.PRE_TAG + getName() + " использовал заклинание [" + spell.getName() + NA +
                    target.getName() + " и поднял защиту на " + armor +
                    "\n(опыт:+" + spell.getExpBonus() + "/" + getCurExp() + ")" + Config.CLOSE_PRE_TAG;
        }
        for (Action attackAction : attackOnTargetList) {
            BigDecimal attack = attackAction.getUser().getAttack().multiply(new BigDecimal(attackAction.getPercent() / 100));
            if (attack.doubleValue() > armor) {
                break;
            }
            int experience = (int) (4 * ((Attack) attackAction).getHit());
            addCurExp(experience);
            attackAction.unDo();
            attackAction.setMessage(Config.PRE_TAG + attackAction.getUser().getName() + " пытался ударить " + target.getName() +
                    " оружием [" + Item.getItemName(attackAction.getUser().getUserId(), attackAction.getUser().getCurWeaponIndex()) + "], но ему не удалось " +
                    "\n(" + getName() + "[опыт:+" + experience + "/" + getCurExp() + ")" + Config.CLOSE_PRE_TAG);
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
    protected String getClassActionId(String actionId) {
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
        String spellId = getSpellToLearn(spellLevel);
        if (spellId == null) {
            Messages.sendMessage((long) getUserId(), "У вас уже есть все заклинания "
                    + spellLevel + " уровня");
            return;
        }
        int probability = (getLevel() - spellLevel + 2) * 25;
        if (isHappened(probability)) {
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
    public void manaRegen() {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    public Spell getSpell(String spellId) {
        return Spell.getSpell(userId, spellId);
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
        getDb().setDoubleTo(Config.USERS, getUserId(), "mana", maxMana);
    }

    public void addSpellPoints(int spellPoints) {

        getDb().setIntTo(Config.USERS, getUserId(), DatabaseManager.S_POINTS, getSpellPoints() + spellPoints);
    }

    public void setMagicAttack(double magicAttack) {
        this.magicAttack = magicAttack;
        getDb().setDoubleTo(Config.USERS, getUserId(), "m_attack", magicAttack);
    }

    public void setCurMana(double curMana) {
        this.curMana = curMana;
        getDb().setDoubleTo(Config.USERS, getUserId(), DatabaseManager.CUR_MANA, curMana);
    }

    public void addCurMana(double manaChange) {
        this.curMana += manaChange;
        getDb().setDoubleTo(Config.USERS, getUserId(), DatabaseManager.CUR_MANA, curMana);
    }

    public double getMaxMana() {
        maxMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "mana");
        return maxMana;
    }

    public int getSpellPoints() {
        spellPoints = getDb().getIntFrom(Config.USERS, getUserId(), DatabaseManager.S_POINTS);
        return spellPoints;
    }

    public double getMagicAttack() {
        return magicAttack;
    }

    public double getCurMana() {
        return curMana;
    }
}
