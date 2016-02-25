package activiti.demo.engine.activemq;

import activiti.demo.engine.WorkflowLauncher;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Created by nos on 25/02/16.
 */
public class ActiveMQBroker {

    public interface Command {
        void perform(Session session) throws JMSException;
    }

    private static final Logger log = LoggerFactory.getLogger(WorkflowLauncher.class);

    private final BrokerService broker;

    public ActiveMQBroker(final String uri) {

        // start embedded broker
        this.broker = broker(uri);

    }

    private BrokerService broker(final String uri) {

        try {
            return BrokerFactory.createBroker(uri);
        } catch (Exception e) {
            throw new RuntimeException("Wrapped", e);
        }

    }

}
