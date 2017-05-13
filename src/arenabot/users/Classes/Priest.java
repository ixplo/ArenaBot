package arenabot.users.Classes;

import arenabot.Config;
import arenabot.Messages;
import arenabot.users.ArenaUser;
import arenabot.users.Inventory.Item;
import arenabot.users.Spells.Spell;

import java.util.ArrayList;

import static arenabot.Messages.fillWithSpaces;

/**
 * ixplo
 * 28.04.2017.
 */
public class Priest extends ArenaUser implements SpellCaster {
    ArrayList<Spell> spells;

    private double maxMana;
    private int spellPoints;
    private double magicAttack;
    private double curMana;

    public Priest(Integer userId) {
        super(userId);
        setUserClass("p");
    }

    @Override
    public void setClassFeatures() {
        maxMana = 1.5 * getCurWis();
        curMana = maxMana;
        magicAttack = roundDouble(0.6 * getCurWis() + 0.4 * getCurInt());
        //todo learn first spell
    }

    @Override
    public void getClassFeatures() {
        maxMana = db.getDoubleFrom(Config.USERS, getUserId(), "mana");
        spellPoints = db.getIntFrom(Config.USERS, getUserId(), "s_points");
        magicAttack = db.getDoubleFrom(Config.USERS, getUserId(), "m_attack");
        curMana = db.getDoubleFrom(Config.USERS, getUserId(), "cur_mana");
    }

    @Override
    public void appendClassXstatMsg(StringBuilder out) {
        out.append(fillWithSpaces("<code>Мана:", getCurMana() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Очки магии:", getSpellPoints() + "</code>\n", Config.WIDTH));
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
        return null;
    }

    @Override
    public void endBattle() {
        setCurMana(getMaxMana());
        int newSpellPoints = countReceivedSpellPoints(getCurExp(),getExperience());
        if (newSpellPoints>0){
            addSpellPoints(newSpellPoints);
            Messages.sendMessage((long)getUserId(),"Вы получили магические бонусы: " + newSpellPoints);
        }
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

    static int countReceivedSpellPoints(int curExp, int exp) {
        return (exp + curExp) / 120 - exp / 120;//not equals curExp/120 because int cuts fraction
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
    }

    public void addSpellPoints(int spellPoints) {
        this.spellPoints += spellPoints;
        db.setIntTo(Config.USERS, getUserId(), "s_points", spellPoints);
    }

    public void setMagicAttack(double magicAttack) {
        this.magicAttack = magicAttack;
        this.magicAttack = magicAttack;
        db.setDoubleTo(Config.USERS, getUserId(), "m_attack", magicAttack);
    }

    public void setCurMana(double curMana) {
        this.curMana = curMana;
        db.setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }
    public void addCurMana(double curMana) {
        this.curMana += curMana;
        db.setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }

    public double getMaxMana() {
        maxMana = db.getDoubleFrom(Config.USERS, getUserId(), "max_mana");
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
