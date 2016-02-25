package activiti.demo.util;

/**
 * Created by nos on 25/02/16.
 */
public class RunnableUtil {

    public static Runnable combine(final Runnable... runnables) {

        return new Runnable() {
            @Override
            public void run() {

                RuntimeException e = null;

                for (final Runnable runnable : runnables) {

                    try {

                        runnable.run();

                    } catch (RuntimeException re) {

                        if (e == null) e = re;
                        else re.addSuppressed(re);

                    }

                }

                if (e != null) throw e;

            }
        };

    }

}