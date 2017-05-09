package arenabot.battle;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * ixplo
 * 03.05.2017.
 */
public class RegTimer extends TimerTask {
    private int delay;
    Registration registration;

    public RegTimer(Registration registration, int delay) {
        this.registration = registration;
        this.delay = delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            registration.startBattle();
        }
    }
}
