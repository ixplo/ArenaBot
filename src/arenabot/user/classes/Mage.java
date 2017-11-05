package arenabot.user.classes;

import arenabot.Config;
import arenabot.messages.Messages;
import arenabot.battle.Round;
import arenabot.battle.actions.Action;
import arenabot.battle.actions.Attack;
import arenabot.user.ArenaUser;
import arenabot.user.items.Item;
import arenabot.user.spells.Spell;

import java.util.*;

import static arenabot.messages.Messages.fillWithSpaces;
import static arenabot.user.spells.Spell.getSpell;

/**
 * ixplo
 * 28.04.2017.
 */
public class Mage extends ArenaUser implements SpellCaster {

    List<Spell> spells;
    private double maxMana;
    private double curMana;
    private int spellPoints;
    private double magicAttack;

    public Mage() {
        setUserClass("MAGE");
    }

    public static List<String> getAllSpellsName() {
        return db.getStrings(Config.SPELLS, "class", UserClass.MAGE.toString(), "name");
    }

    @Override
    public void setClassFeatures() {
        actionsName = Arrays.asList("Атака", "Защита", "Лечение", "Магия");
        setMaxMana(1.5 * getCurWis());
        setCurMana(maxMana);
        setMagicAttack(roundDouble(0.6 * getCurWis() + 0.4 * getCurInt()));
        setSpell("1am");
    }

    @Override
    public void getClassFeatures() {
        actionsName = Arrays.asList("Атака", "Защита", "Лечение", "Магия");
        maxMana = db.getDoubleFrom(Config.USERS, getUserId(), "mana");
        curMana = db.getDoubleFrom(Config.USERS, getUserId(), "cur_mana");
        spellPoints = db.getIntFrom(Config.USERS, getUserId(), "s_points");
        magicAttack = db.getDoubleFrom(Config.USERS, getUserId(), "m_attack");
        spells = getSpells();
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        out.append(fillWithSpaces("\n<code>Мана:", getCurMana() + "</code>", Config.WIDTH));
        out.append(fillWithSpaces("\n<code>Магич. бонусы:", getSpellPoints() + "</code>\n", Config.WIDTH));
    }

    @Override
    public void putOnClassFeatures(Item item) {
        setMaxMana(getMaxMana() + item.getWisBonus() * 1.5);
        if (getStatus() != 2) { // not in battle
            setCurMana(getMaxMana());
        }
        setMagicAttack(getMagicAttack() + roundDouble(0.6 * item.getWisBonus() + 0.4 * item.getIntBonus()));
    }

    @Override
    public void putOffClassFeatures(Item item) {
        //todo
    }

    @Override
    public void addHarkClassFeatures(String harkToUpId, int numberOfPoints) {
        if (harkToUpId.equals("nativeWis")) {
            setMaxMana(getMaxMana() + numberOfPoints * 1.5);
            if (getStatus() != 2) { // not in battle
                setCurMana(getMaxMana());
            }
            setMagicAttack(getMagicAttack() + roundDouble(0.6 * numberOfPoints));
        }
        if (harkToUpId.equals("nativeInt")) {
            setMagicAttack(getMagicAttack() + roundDouble(0.4 * numberOfPoints));
        }
    }

    @Override
    public void doAction(String[] command) {

    }

    @Override
    public String doCast(ArenaUser target, int percent, String spellId) {
        String message = "";
        Spell spell = getSpell(spellId);
        if (!isHappened(spell.getProbability() * percent / 100)) { //(Math.log(getMagicAttack()/target.getMagicProtect() + 4.6)/7)
            return message = "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + "] на "
                    + target.getName() + ", но заклинание провалилось.</code>";
        }
        if (spell.getManaCost() > curMana) {
            return message = "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + "] на "
                    + target.getName() + ", но у него не хватило маны.</code>";
        }
        double bonus;
        if (Spell.getSpellGrade(getUserId(), spellId) == 1) {
            bonus = spell.getGradeOneBonus();
        } else if (Spell.getSpellGrade(getUserId(), spellId) == 2) {
            bonus = spell.getGradeTwoBonus();
        } else {
            bonus = spell.getGradeThreeBonus();
        }
        double damage = roundDouble(spell.getDamage() + bonus);
        if (spell.getEffect().equals("DAMAGE")) {
            target.addCurHitPoints(-damage);
            addCurMana(-spell.getManaCost());
            addCurExp((int) (damage * spell.getExpBonus()));
            message = "<pre>" + getName() + " запустил заклинанием [" + spell.getName() + "] в " +
                    target.getName() + " и ранил его на " + damage +
                    "\n(жизни:-" + damage + "/" + target.getCurHitPoints() +
                    " \\\\ опыт:+" + damage * spell.getExpBonus() + "/" + getCurExp() + ")</pre>";
        } else if (spell.getEffect().equals("HEAL")) {
            if (target.getMaxHitPoints() - target.getCurHitPoints() < damage) {
                damage = roundDouble(target.getMaxHitPoints() - target.getCurHitPoints());
            }
            target.addCurHitPoints(damage);
            addCurMana(-spell.getManaCost());
            addCurExp((int) (damage * spell.getExpBonus()));
            message = "<pre>" + getName() + " Магией [" + spell.getName() + "] поднял здоровье у " +
                    target.getName() + " на " + damage +
                    "\n(жизни:" + damage + "/" + target.getCurHitPoints() +
                    " \\\\ опыт:+" + damage * spell.getExpBonus() + "/" + getCurExp() + ")</pre>";
        } else if (spell.getEffect().equals("ARMOR")) {
            double armor = roundDouble(spell.getArmor() + bonus);
            List<Action> attackOnTargetList = Round.round.getAttackOnTargetList(target.getUserId());
            if (attackOnTargetList.size() == 0) {
                return "<pre>" + getName() + " использовал заклинание [" + spell.getName() + "] на " +
                        target.getName() + " и поднял защиту на " + armor +
                        "\n(опыт:+" + spell.getExpBonus() + "/" + getCurExp() + ")</pre>";
            }
            for (Action attackAction : attackOnTargetList) {
                double attack = attackAction.getUser().getAttack() * attackAction.getPercent() / 100;
                if (attack > armor) {
                    break;
                }
                int experience = (int) (4 * ((Attack) attackAction).getHit());
                addCurExp(experience);
                ((Attack) attackAction).unDo();
                attackAction.setMessage("<pre>" + attackAction.getUser().getName() + " пытался ударить " + target.getName() +
                        " оружием [" + Item.getItemName(attackAction.getUser().getUserId(), attackAction.getUser().getCurWeapon()) + "], но ему не удалось " +
                        "\n(" + getName() + "[опыт:+" + experience + "/" + getCurExp() + ")</pre>");
            }
        }
        return message;
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

        db.addSpell(getUserId(), spellId, Spell.getSpellGrade(getUserId(), spellId) + 1);
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

    }

    @Override
    public void learnSpell(int spellLevel) {


    }

    private String getSpellToLearn(int spellLevel) {
        List<String> spellsId = db.getSpellsIdToLearn(getUserId(), spellLevel);
        Random rnd = new Random();
        return spellsId.get(rnd.nextInt(spellsId.size()));
    }

    @Override
    public void manaRegen() {

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
        db.setDoubleTo(Config.USERS, getUserId(), "mana", maxMana);
    }

    public void addSpellPoints(int spellPoints) {

        db.setIntTo(Config.USERS, getUserId(), "s_points", getSpellPoints() + spellPoints);
    }

    public void setMagicAttack(double magicAttack) {
        this.magicAttack = magicAttack;
        db.setDoubleTo(Config.USERS, getUserId(), "m_attack", magicAttack);
    }

    public void setCurMana(double curMana) {
        this.curMana = curMana;
        db.setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }

    public void addCurMana(double manaChange) {
        this.curMana += manaChange;
        db.setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }

    public double getMaxMana() {
        maxMana = db.getDoubleFrom(Config.USERS, getUserId(), "mana");
        return maxMana;
    }

    public int getSpellPoints() {
        spellPoints = db.getIntFrom(Config.USERS, getUserId(), "s_points");
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
            spellsName.add(db.getStringFrom(Config.SPELLS, spell, "name"));
        }
        return spellsName;
    }

    public List<String> getSpellsId() {
        return db.getStrings(Config.AVAILABLE_SPELLS, "user_id", getUserId(), "id");
    }

}
