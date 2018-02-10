package ml.ixplo.arenabot.helper;

import java.util.Arrays;
import java.util.List;

public class Presets {
    public static final String TEST_PROPERTIES = "src/test/resources/test.properties";
    public static final int NON_EXIST_USER_ID = 1;
    public static final int EXIST_USER_ID = -500;
    public static final String EXIST_USER_NAME = "test_notNull";
    public static final String WRONG_ITEM_ID = "wrong";
    public static final String WRONG_ITEM_NAME = "wrong";
    public static final String ITEM_ID = "waa";
    public static final String ITEM_NAME = "Ладошка";
    public static final int ITEM_INDEX = 0;
    public static final String ITEM_SLOT = "a";
    public static final int WRONG_ITEM_INDEX = -1;
    public static final int WARRIOR_ID = -1;
    public static final String WARRIOR_NAME = "test_warrior";
    public static final String WARRIOR_CLASS = "WARRIOR";
    public static final String WARRIOR_CLASS_NAME = "Воин";
    public static final String WARRIOR_RACE = "o";
    public static final String WARRIOR_RACE_NAME = "Хоббит";
    public static final int MAGE_ID = -2;
    public static final String MAGE_NAME = "test_mage";
    public static final String MAGE_RACE = "e";
    public static final String FLAMBERG = "wba";
    public static final CharSequence FLAMBERG_NAME = "Фламберг";
    public static final int TEST_DB_VERSION = 999;
    public static final int TARGET_ID = 1;
    public static final String QUERY_ID = "AnyQueryId";
    public static final Integer MESSAGE_ID = 5353;
    public static final String TEST_TEAM = "test_team";
    public static final String QUERY_TEXT = "query_text";
    public static final String MAGIC_ARROW_SPELL_ID = "1am";
    public static final String PRIEST_SPELL_ID = "1ap";
    public static final int MAGE_SPELL_DAMAGE = 3;
    public static final String MAGE_SPELL_NAME = "Магическая стрела";
    public static final int MAGE_SPELL_MANACOST = 3;
    public static final int MAGE_SPELL_EXP_BONUS = 10;
    public static final String MAGE_SPELL_EFFECT = "DAMAGE";
    public static final int MAGE_SPELL_ARMOR = 0;
    public static final int MAGE_SPELL_DURATION = 1;
    public static final int MAGE_SPELL_FIRST_LEVEL = 1;
    public static final int MAGE_SPELL_SECOND_LEVEL = 2;
    public static final int MAGE_SPELL_PROBABILITY = 95;
    public static final double EMPTY_BONUS = 0.0;
    public static final double MAGE_SPELL_GRADE_ONE_BONUS = 1.0;
    public static final double MAGE_SPELL_GRADE_TWO_BONUS = 1.5;
    public static final double MAGE_SPELL_GRADE_THREE_BONUS = 2.0;
    public static final double DELTA = 0.01;
    public static final int ADD_EXP = 15;
    public static final int MONEY = 50;
    public static final double ADD_HIT_POINTS = 1.5;
    public static final List<String> USER_CLASSES_NAMES = Arrays.asList("Воин", "Маг", "Жрец", "Лучник");
    public static final List<String> USER_RACES_NAMES = Arrays.asList(
            "Эльф",
            "Человек",
            "Гном",
            "Гигант",
            "Назгул",
            "Гоблин",
            "Хоббит",
            "Тролль",
            "Орк",
            "Кобольд");
    public static final List<String> USER_RACES_ID = Arrays.asList(
            "e",
            "h",
            "g",
            "a",
            "n",
            "b",
            "o",
            "i",
            "t",
            "k");
}
