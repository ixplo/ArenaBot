package ml.ixplo.arenabot.user.classes;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.items.Item;
import ml.ixplo.arenabot.user.spells.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ml.ixplo.arenabot.messages.Messages.fillWithSpaces;

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

    public Priest() {
        setUserClass("PRIEST");
    }

    @Override
    public void setClassFeatures() {
        actionsName = Arrays.asList("Атака","Защита","Лечение", "Магия");
        maxMana = 1.5 * getCurWis();
        curMana = maxMana;
        magicAttack = roundDouble(0.6 * getCurWis() + 0.4 * getCurInt());
        //todo learn first spell
    }

    @Override
    public void getClassFeatures() {
        maxMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "mana");
        spellPoints = getDb().getIntFrom(Config.USERS, getUserId(), "s_points");
        magicAttack = getDb().getDoubleFrom(Config.USERS, getUserId(), "m_attack");
        curMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "cur_mana");
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
            setMagicAttack(getMagicAttack() + roundDouble(0.6 * numberOfPoints));
        }
        if (harkToUpId.equals("nativeInt")) {
            setMagicAttack(getMagicAttack() + roundDouble(0.4 * numberOfPoints));
        }
    }

    @Override
    public void doAction(String[] command) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public String doCast(ArenaUser target, int percent, String castId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
        return null;
    }

    @Override
    public void endBattleClassFeatures() {
        setCurMana(getMaxMana());
        int newSpellPoints = countReceivedSpellPoints(getCurExp(),getExperience());
        if (newSpellPoints>0){
            addSpellPoints(newSpellPoints);
            Messages.sendMessage((long)getUserId(),"Вы получили магические бонусы: " + newSpellPoints);
        }
    }

    @Override
    public String getClassActionId(String actionId) {
        return actionId;
    }

    @Override
    public List<String> getCastsName() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getCastsId() {
        return new ArrayList<>();
    }

    @Override
    public void learn(int level) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void castSpell(String spellId) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void learnSpell(int spellLevel) {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    @Override
    public void manaRegen() {
        LOGGER.error(Config.NOT_YET_IMPLEMENTED);
    }

    static int countReceivedSpellPoints(int curExp, int exp) {
        return (exp + curExp) / 120 - exp / 120;//not equals curExp/120 because int cuts fraction
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
    }

    public void addSpellPoints(int spellPoints) {
        this.spellPoints += spellPoints;
        getDb().setIntTo(Config.USERS, getUserId(), "s_points", spellPoints);
    }

    public void setMagicAttack(double magicAttack) {
        this.magicAttack = magicAttack;
        getDb().setDoubleTo(Config.USERS, getUserId(), "m_attack", magicAttack);
    }

    public void setCurMana(double curMana) {
        this.curMana = curMana;
        getDb().setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }
    public void addCurMana(double curMana) {
        this.curMana += curMana;
        getDb().setDoubleTo(Config.USERS, getUserId(), "cur_mana", curMana);
    }

    public double getMaxMana() {
        maxMana = getDb().getDoubleFrom(Config.USERS, getUserId(), "max_mana");
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
