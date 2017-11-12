package ml.ixplo.arenabot.user.spells;

import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.user.ArenaUser;

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
    private int armor;
    private int expBonus;
    private int probability;
    private double gradeOneBonus;
    private double gradeTwoBonus;
    private double gradeThreeBonus;

    public static Spell getSpell(String id) {
        Spell spell = new Spell();
        spell.id = id;
        spell.name = ArenaUser.db.getStringFrom(Config.SPELLS, id, "name");
        spell.effect = ArenaUser.db.getStringFrom(Config.SPELLS, id, "effect");
        spell.manaCost = ArenaUser.db.getIntFrom(Config.SPELLS, id, "mana_cost");
        spell.level = ArenaUser.db.getIntFrom(Config.SPELLS, id, "level");
        spell.duration = ArenaUser.db.getIntFrom(Config.SPELLS, id, "duration");
        spell.damage = ArenaUser.db.getIntFrom(Config.SPELLS, id, "hit");
        spell.armor = ArenaUser.db.getIntFrom(Config.SPELLS, id, "armor");
        spell.expBonus = ArenaUser.db.getIntFrom(Config.SPELLS, id, "exp_bonus");
        spell.probability = ArenaUser.db.getIntFrom(Config.SPELLS, id, "probability");
        spell.gradeOneBonus = ArenaUser.db.getDoubleFrom(Config.SPELLS, id, "bonus_g1");
        spell.gradeTwoBonus = ArenaUser.db.getDoubleFrom(Config.SPELLS, id, "bonus_g2");
        spell.gradeThreeBonus = ArenaUser.db.getDoubleFrom(Config.SPELLS, id, "bonus_g3");
        return spell;
    }

    public static int getSpellGrade(int userId, String spellId){
        return ArenaUser.db.getIntByBy(Config.AVAILABLE_SPELLS,
                "spell_grade",
                "id", spellId,
                "user_id", userId);
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

    public double getGradeOneBonus() {
        return gradeOneBonus;
    }

    public double getGradeTwoBonus() {
        return gradeTwoBonus;
    }

    public double getGradeThreeBonus() {
        return gradeThreeBonus;
    }

    public int getArmor() {
        return armor;
    }
}
