package activiti.demo.engine;

import activiti.demo.engine.environment.EnvironmentFactory;

/**
 * Created by nos on 25/02/16.
 */
public class WorkflowLauncher {

    private static final String BROKER_URI = "vm://localhost";

    public static void main(String[] args) {

        new WorkflowEngine(EnvironmentFactory.newLocalEnvironment(BROKER_URI)).runDemo();

    }

}
