package activiti.demo.engine.activemq;

import activiti.demo.engine.activemq.ActiveMQConnector.Command;
import activiti.demo.engine.activiti.ActivitiCommandFactory;
import activiti.demo.engine.activiti.ActivitiConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nos on 25/02/16.
 */
public class ActiveMQCommandFactory {

    private static final Logger log = LoggerFactory.getLogger(ActiveMQCommandFactory.class);

    public Command startWorkflowCommand(final String queueName,
                                        final String processName,
                                        final ActivitiConnector.ActivitiSession activiti,
                                        final ActivitiCommandFactory activitiFactory) {

        return session -> {

            log.info("Registering workflow start handler");

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(queueName);

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            log.info("Waiting for trigger message");

            // Wait for a message
            MapMessage message = (MapMessage) consumer.receive();

            // pull values
            Map<String, Object> values = new HashMap<>();
            values.put("user", message.getString("user"));

            log.info("Starting workflow");

            // start workflow
            activiti.run(activitiFactory.startProcess(processName, values));

            // close consumer
            consumer.close();
        };

    }

    public Command triggerWorkflowCommand(final String queueName, final String user) {

        return session -> {

            log.info("Triggering workflow with message send");

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(queueName);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            final MapMessage message = session.createMapMessage();
            message.setString("user", user);

            // Tell the producer to send the message
            log.info("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);

        };

    }

}
