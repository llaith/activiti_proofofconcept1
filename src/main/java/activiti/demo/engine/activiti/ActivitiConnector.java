package activiti.demo.engine.activiti;

import activiti.demo.engine.WorkflowLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nos on 25/02/16.
 */
public class ActivitiConnector {

    public interface Command {
        void perform(ActivitiEngine activiti);
    }

    public static class ActivitiSession implements AutoCloseable {

        private final ActivitiEngine activiti;

        public ActivitiSession(ActivitiEngine activiti) {
            this.activiti = activiti;
        }

        public <X extends Command> X run(final X command) {

            command.perform(this.activiti);

            return command;

        }

        @Override
        public void close() {
            // nothing to do at the moment
        }

    }

    private static final Logger log = LoggerFactory.getLogger(WorkflowLauncher.class);

    private final ActivitiEngine engine;

    public ActivitiConnector(final ActivitiEngine engine) {

        this.engine = engine;

    }

    public ActivitiSession newSession() {

        return new ActivitiSession(this.engine);

    }

}
