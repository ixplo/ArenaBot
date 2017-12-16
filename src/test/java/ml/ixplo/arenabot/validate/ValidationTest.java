package ml.ixplo.arenabot.validate;

import org.junit.Assert;
import org.junit.Test;


public class ValidationTest {

    @Test
    public void isInteger() throws Exception {
        String intNumber = "5";
        String negativeNumber = "-5";
        String doubleNumber = "5.5";
        String longNumber = "55555555555555";
        String nonNumeric = "5g";
        Assert.assertTrue(Validation.isInteger(intNumber));
        Assert.assertTrue(Validation.isInteger(negativeNumber));
        Assert.assertFalse(Validation.isInteger(doubleNumber));
        Assert.assertFalse(Validation.isInteger(longNumber));
        Assert.assertFalse(Validation.isInteger(nonNumeric));
    }

}