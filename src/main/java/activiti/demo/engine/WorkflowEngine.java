package activiti.demo.engine;

import activiti.demo.engine.activemq.ActiveMQCommandFactory;
import activiti.demo.engine.activemq.ActiveMQConnector;
import activiti.demo.engine.activiti.ActivitiCommandFactory;
import activiti.demo.engine.activiti.ActivitiConnector;
import activiti.demo.engine.environment.Environment;
import activiti.demo.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static activiti.demo.util.ThreadUtil.spawn;

/**
 * Created by nos on 25/02/16.
 */
public class WorkflowEngine {

    private final Logger log = LoggerFactory.getLogger(WorkflowEngine.class);

    private static final ActivitiCommandFactory activitiFactory = new ActivitiCommandFactory();
    private static final ActiveMQCommandFactory activemqFactory = new ActiveMQCommandFactory();

    private final Environment environment;
    private final ActivitiConnector activiti;
    private final ActiveMQConnector activemq;

    public WorkflowEngine(Environment environment) {

        this.environment = environment;
        this.activiti = this.environment.workflowConnector();
        this.activemq = this.environment.queueConnector();

    }

    public void runDemo() {

        // create a bridge user
        try (final ActivitiConnector.ActivitiSession workflow = activiti.newSession()) {
            workflow.run(activitiFactory.newUser("bridge"));
        }

        // for the triggers
        spawn(this.revceiveTriggersFromActiveMQProcess());

        // loop for the activiti processing
        spawn(this.processActivitiProcess());

        // send a stream of new tasks
        spawn(this.fireFakeActiveMQTriggersProcess());

    }

    private Runnable revceiveTriggersFromActiveMQProcess() {
        return () -> {

            try (
                    final ActivitiConnector.ActivitiSession workflow = activiti.newSession();
                    final ActiveMQConnector.ActiveMQSession queue = activemq.newSession()) {

                while (true) {

                    // start a process to trigger on sends
                    queue.run(activemqFactory.startWorkflowCommand(
                            "QUEUE1",
                            "process1",
                            workflow,
                            activitiFactory));

                    // sleep
                    ThreadUtil.sleep(log);
                }

            }

        };
    }

    private Runnable processActivitiProcess() {
        return () -> {

            try (
                    final ActivitiConnector.ActivitiSession workflow = activiti.newSession()) {

                while (true) {

                    // start a process to assign workflows
                    workflow.run(activitiFactory.newTaskAcceptCommand("bridge"));

                    // start a process to assign workflows
                    workflow.run(activitiFactory.newTaskCompleteCommand("bridge"));

                    // sleep
                    ThreadUtil.sleep(log);

                }

            }

        };
    }

    private Runnable fireFakeActiveMQTriggersProcess() {
        return () -> {

            try (
                    final ActiveMQConnector.ActiveMQSession queue = activemq.newSession()) {

                while (true) {

                    // start a process to trigger on sends
                    queue.run(activemqFactory.triggerWorkflowCommand("QUEUE1", "Joe Blogs"));

                    // sleep
                    ThreadUtil.sleep(log);

                }

            }
        };
    }

}
