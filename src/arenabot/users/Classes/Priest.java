package arenabot.users.Classes;

import arenabot.Config;
import arenabot.users.ArenaUser;
import arenabot.users.Spells.Spell;

import java.util.ArrayList;

import static arenabot.Messages.fillWithSpaces;

/**
 * ixplo
 * 28.04.2017.
 */
public class Priest extends ArenaUser implements SpellCaster{
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
        maxMana = db.getDoubleFrom(Config.USERS,getUserId(),"mana");
        spellPoints = db.getIntFrom(Config.USERS,getUserId(),"s_points");
        magicAttack = db.getDoubleFrom(Config.USERS,getUserId(),"m_attack");
        curMana = db.getDoubleFrom(Config.USERS,getUserId(),"cur_mana");
    }

    @Override
    public void appendXstatMsg(StringBuilder out) {
        out.append(fillWithSpaces("<code>Мана:", getCurMana() + "</code>\n", Config.WIDTH));
        out.append(fillWithSpaces("<code>Очки магии:", getSpellPoints() + "</code>\n", Config.WIDTH));
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
    }

    public void setSpellPoints(int spellPoints) {
        this.spellPoints = spellPoints;
    }

    public void setMagicAttack(double magicAttack) {
        this.magicAttack = magicAttack;
    }

    public void setCurMana(double curMana) {
        this.curMana = curMana;
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
