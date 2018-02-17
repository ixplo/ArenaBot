package ml.ixplo.arenabot.validate;

import ml.ixplo.arenabot.utils.Utils;
import org.junit.Assert;
import org.junit.Test;


public class UtilsTest {

    @Test
    public void isInteger() throws Exception {
        String intNumber = "5";
        String negativeNumber = "-5";
        String doubleNumber = "5.5";
        String longNumber = "55555555555555";
        String nonNumeric = "5g";
        Assert.assertTrue(Utils.isInteger(intNumber));
        Assert.assertTrue(Utils.isInteger(negativeNumber));
        Assert.assertFalse(Utils.isInteger(doubleNumber));
        Assert.assertFalse(Utils.isInteger(longNumber));
        Assert.assertFalse(Utils.isInteger(nonNumeric));
    }

}