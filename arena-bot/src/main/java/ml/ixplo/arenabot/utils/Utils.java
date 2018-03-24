package ml.ixplo.arenabot.utils;

import static com.google.common.math.IntMath.pow;

public class Utils {

    private Utils() {
    }

    public static double roundDouble(double d) {
        return roundDouble(d, 2);
    }

    public static double roundDouble(double d, int precise) {
        int prec = pow(10, precise);
        double dWithPrecise = d * prec;
        int i = (int) Math.round(dWithPrecise);
        return (double) i / prec;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
