package ml.ixplo.arenabot.config;

import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.helper.Presets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class PropertiesLoaderTest {

    @Test
    public void getProperties() throws Exception {
        Map<String, String> settings = new PropertiesLoader(Presets.TEST_PROPERTIES).getProperties();
        Assert.assertTrue(settings.containsKey("warrior.id"));
        Assert.assertEquals(Presets.WARRIOR_NAME, settings.get("warrior.name"));
    }

    @Test
    public void getPropertiesDefault() throws Exception {
        Map<String, String> settings = new PropertiesLoader().getProperties();
        Assert.assertTrue(settings.containsKey("channel.id"));
    }

    @Test(expected = ArenaUserException.class)
    public void badPropertyTest() {
        PropertiesLoader.getInstance().getLong("bad.property");
    }

    @Test(expected = ArenaUserException.class)
    public void badPropertyFileTest() {
        new PropertiesLoader("badProperty.file").getProperties();
    }

    @Test
    public void getChannelIdPropertyTest() {
        Assert.assertEquals(Presets.CHANNEL_ID, PropertiesLoader.getInstance().getChannelId());
    }
}