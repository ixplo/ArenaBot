package arenabot.users.Spells;

import arenabot.Config;

import static arenabot.users.ArenaUser.db;

/**
 * ixplo
 * 28.04.2017.
 */
public class Spell { //todo add implements Durable в потомка - класс Ледяное прикосновение
    private String id;
    private String name;
    private int manaCost;
    private int level;
    private int duration;
    private String effect;
    private int damage;
    private int expBonus;
    private int probability;

    public static Spell getSpell(String id) {
        Spell spell = new Spell();
        spell.id = id;
        spell.name = db.getStringFrom(Config.SPELLS, id, "name");
        spell.effect = db.getStringFrom(Config.SPELLS, id, "effect");
        spell.manaCost = db.getIntFrom(Config.SPELLS, id, "mana_cost");
        spell.level = db.getIntFrom(Config.SPELLS, id, "level");
        spell.duration = db.getIntFrom(Config.SPELLS, id, "duration");
        spell.damage = db.getIntFrom(Config.SPELLS, id, "hit");
        spell.expBonus = db.getIntFrom(Config.SPELLS, id, "exp_bonus");
        spell.probability = db.getIntFrom(Config.SPELLS, id, "probability");
        return spell;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getExpBonus() {
        return expBonus;
    }

    public void setExpBonus(int expBonus) {
        this.expBonus = expBonus;
    }
}
