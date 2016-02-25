package activiti.demo.engine.environment;

import activiti.demo.engine.activemq.ActiveMQBroker;
import activiti.demo.engine.activemq.ActiveMQConnector;
import activiti.demo.engine.activiti.ActivitiConnector;
import activiti.demo.engine.activiti.ActivitiEngine;

/**
 * Created by nos on 25/02/16.
 */
public class EnvironmentFactory {

    public static Environment newLocalEnvironment(final String brokerUrl) {

        return new Environment() {

            @Override
            public ActiveMQConnector queueConnector() {

                // init the broker itself in embedded mode
                //new ActiveMQBroker(brokerUrl);

                // return a connector
                return new ActiveMQConnector(brokerUrl);

            }

            @Override
            public ActivitiConnector workflowConnector() {

                // init the workflow engine
                final ActivitiEngine workflow = new ActivitiEngine()
                        .deploy("process1.bpmn20.xml");

                // return a connector
                return workflow.connector();

            }

        };

    }

}
