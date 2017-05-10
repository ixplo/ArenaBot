package arenabot.users.Classes;

import arenabot.Config;
import arenabot.users.ArenaUser;
import arenabot.users.Inventory.Item;
import arenabot.users.Spells.Spell;

import java.util.ArrayList;
import java.util.Random;

import static arenabot.Messages.fillWithSpaces;
import static arenabot.users.Spells.Spell.getSpell;

/**
 * ixplo
 * 28.04.2017.
 */
public class Mage extends ArenaUser implements SpellCaster {
    ArrayList<Spell> spells;
    private double maxMana;
    private double curMana;
    private int spellPoints;
    private double magicAttack;

    public Mage(Integer userId) {
        super(userId);
        setUserClass("m");

    }

    @Override
    public void setClassFeatures() {
        setMaxMana(1.5 * getCurWis());
        setCurMana(maxMana);
        setMagicAttack(roundDouble(0.6 * getCurWis() + 0.4 * getCurInt()));
        setSpell("1ma", 1);
    }

    @Override
    public void getClassFeatures() {
        maxMana = db.getDoubleFrom(Config.USERS, getUserId(), "mana");
        curMana = db.getDoubleFrom(Config.USERS, getUserId(), "cur_mana");
        spellPoints = db.getIntFrom(Config.USERS, getUserId(), "s_points");
        magicAttack = db.getDoubleFrom(Config.USERS, getUserId(), "m_attack");
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        out.append(fillWithSpaces("\n<code>Мана:", getCurMana() + "</code>", Config.WIDTH));
        out.append(fillWithSpaces("\n<code>Очки магии:", getSpellPoints() + "</code>\n", Config.WIDTH));
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
    public String doCast(ArenaUser target, int percent, String castId) {
        String message = "";
        Spell spell = getSpell(castId);
        Random rnd = new Random();
        int chance = rnd.nextInt(100);
        if(chance>(int)(spell.getProbability() * (Math.log(getMagicAttack()/target.getMagicProtect() + 4.6)/7) * percent / 100)){
            return message = "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + "] на " +
                    target.getName() + ", но заклинание провалилось.</code>";
        }
        if(spell.getEffect().equals("a")){//todo change to normal word
            if(spell.getManaCost() > curMana){
                return message = "<code>" + getName() + " пытался создать заклинание [" + spell.getName() + "] на " +
                        target.getName() + ", но у него не хватило маны.</code>";
            }
            target.addCurHitPoints(-spell.getDamage());
            addCurMana(-spell.getManaCost());
            addCurExp(spell.getDamage()*spell.getExpBonus());
            message = "<pre>" + getName() + " запустил заклинанием [" + spell.getName() + "] в " +
                    target.getName() + " и ранил его на " + spell.getDamage() +
                    "\n(жизни:-" + spell.getDamage() + "/" + target.getCurHitPoints() +
                    " \\\\ опыт:+" + spell.getDamage()*spell.getExpBonus() + "/" + getCurExp() + ")</pre>";
        }
        return message;
    }

    private void setSpell(String spellId, int spellGrade) {
        db.addSpell(getUserId(), spellId, spellGrade);
    }

    static int countReceivedSpellPoints(int curExp, int exp) {
        return (exp + curExp) / 120 - exp / 120;//not equals curExp/120 because int cuts fraction
    }

    @Override
    public void castSpell(String spellId) {

    }

    @Override
    public void learnSpell(int spellLevel) {

    }

    @Override
    public void manaRegen() {

    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
        db.setDoubleTo(Config.USERS, getUserId(), "mana", maxMana);
    }

    public void setSpellPoints(int spellPoints) {
        this.spellPoints = spellPoints;
        db.setIntTo(Config.USERS, getUserId(), "s_points", spellPoints);
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
        return maxMana;
    }

    public int getSpellPoints() {
        return spellPoints;
    }

    public double getMagicAttack() {
        return magicAttack;
    }

    public double getCurMana() {
        return curMana;
    }


}
