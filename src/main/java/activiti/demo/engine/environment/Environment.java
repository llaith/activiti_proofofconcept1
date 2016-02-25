package activiti.demo.engine.environment;

import activiti.demo.engine.activemq.ActiveMQConnector;
import activiti.demo.engine.activiti.ActivitiConnector;

/**
 * Created by nos on 25/02/16.
 */
public interface Environment {

    ActiveMQConnector queueConnector();

    ActivitiConnector workflowConnector();

}


