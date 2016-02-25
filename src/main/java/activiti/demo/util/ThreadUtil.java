package activiti.demo.util;

import org.slf4j.Logger;

/**
 * Created by nos on 25/02/16.
 */
public class ThreadUtil {

    public static Thread spawn(Runnable runnable) {

        final Thread thread = new Thread(runnable);
        thread.setDaemon(false);
        thread.start();

        return thread;
    }

    public static void sleep(final Logger log) {

        // sleep
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.warn("Ignoring cancel request", e);
        }

    }

}
