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
    private int grade;
    private double gradeOneBonus;
    private double gradeTwoBonus;
    private double gradeThreeBonus;
    private int ownerId;

    public static Spell getSpell(int ownerId, String spellId) {
        Spell spell = getSpell(spellId);
        spell.ownerId = ownerId;
        spell.grade = getSpellGrade(ownerId, spellId);
        return spell;
    }

    public static Spell getSpell(String id) {
        Spell spell = new Spell();
        spell.id = id;
        spell.name = ArenaUser.getDb().getStringFrom(Config.SPELLS, id, "name");
        spell.effect = ArenaUser.getDb().getStringFrom(Config.SPELLS, id, "effect");
        spell.manaCost = ArenaUser.getDb().getIntFrom(Config.SPELLS, id, "mana_cost");
        spell.level = ArenaUser.getDb().getIntFrom(Config.SPELLS, id, "level");
        spell.duration = ArenaUser.getDb().getIntFrom(Config.SPELLS, id, "duration");
        spell.damage = ArenaUser.getDb().getIntFrom(Config.SPELLS, id, "hit");
        spell.armor = ArenaUser.getDb().getIntFrom(Config.SPELLS, id, "armor");
        spell.expBonus = ArenaUser.getDb().getIntFrom(Config.SPELLS, id, "exp_bonus");
        spell.probability = ArenaUser.getDb().getIntFrom(Config.SPELLS, id, "probability");
        spell.gradeOneBonus = ArenaUser.getDb().getDoubleFrom(Config.SPELLS, id, "bonus_g1");
        spell.gradeTwoBonus = ArenaUser.getDb().getDoubleFrom(Config.SPELLS, id, "bonus_g2");
        spell.gradeThreeBonus = ArenaUser.getDb().getDoubleFrom(Config.SPELLS, id, "bonus_g3");
        spell.grade = 1;
        return spell;
    }

    public static int getSpellGrade(int userId, String spellId){
        return ArenaUser.getDb().getIntByBy(Config.AVAILABLE_SPELLS,
                "spell_grade",
                "id", spellId,
                "user_id", userId);
    }

    public int getGrade() {
        return grade;
    }

    public  double getBonus() {
        return getSpellBonus(ownerId);
    }

    public double getSpellBonus(int userId) {
        double bonus = 0;
        if (Spell.getSpellGrade(userId, id) == 1) {
            bonus = getGradeOneBonus();
        } else if (Spell.getSpellGrade(userId, id) == 2) {
            bonus = getGradeTwoBonus();
        } else if (Spell.getSpellGrade(userId, id) == 3){
            bonus = getGradeThreeBonus();
        }
        return bonus;
    }

    public int getProbability() {
        return probability;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getLevel() {
        return level;
    }

    public int getDuration() {
        return duration;
    }

    public String getEffect() {
        return effect;
    }

    public int getDamage() {
        return damage;
    }

    public int getExpBonus() {
        return expBonus;
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

    public void setGrade(int grade) {
        this.grade = grade;
        ArenaUser.getDb().setIntTo(Config.AVAILABLE_SPELLS, id, Config.SPELL_GRADE, grade);
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spell spell = (Spell) o;

        if (level != spell.level) return false;
        if (grade != spell.grade) return false;
        return id.equals(spell.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + level;
        result = 31 * result + grade;
        return result;
    }
}
