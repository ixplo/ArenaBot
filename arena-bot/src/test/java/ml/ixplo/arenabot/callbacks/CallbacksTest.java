package ml.ixplo.arenabot.callbacks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ml.ixplo.arenabot.callbacks.Callbacks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CallbacksTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = InvocationTargetException.class)
    public void UnsupportedOperationExceptionTest() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor<Callbacks> constructor = Callbacks.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void handleCallbackCommand() throws Exception {
    }

}