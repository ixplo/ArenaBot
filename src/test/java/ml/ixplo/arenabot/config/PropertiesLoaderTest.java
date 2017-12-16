package ml.ixplo.arenabot.config;

import ml.ixplo.arenabot.helper.Presets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class PropertiesLoaderTest {

    @Test
    public void getProperties() throws Exception {
        Map<String, String> settings = PropertiesLoader.getProperties(Presets.TEST_PROPERTIES);
        Assert.assertTrue(settings.containsKey("warrior.id"));
        Assert.assertEquals(Presets.WARRIOR_NAME, settings.get("warrior.name"));
    }

}