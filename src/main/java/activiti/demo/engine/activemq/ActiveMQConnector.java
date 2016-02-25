package activiti.demo.engine.activemq;

import activiti.demo.engine.WorkflowLauncher;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Created by nos on 25/02/16.
 */
public class ActiveMQConnector {

    public interface Command {
        void perform(Session session) throws JMSException;
    }

    public static class ActiveMQSession implements AutoCloseable {

        private final Connection connection;
        private final Session session;

        public ActiveMQSession(final Connection connection, final Session session) {

            this.connection = connection;

            this.session = session;

        }

        public <X extends Command> X run(final X command) {

            try {

                command.perform(this.session);

                return command;

            } catch (final JMSException e) {
                throw new RuntimeException(e);
            }

        }

        public void close() {

            // close session quietly
            if (session != null) try {
                session.close();
            } catch (JMSException e) {
                log.error("Supressing exception.", e);
            }

            // close connection quietly
            if (connection != null) try {
                connection.close();
            } catch (JMSException e) {
                log.error("Supressing exception.", e);
            }

        }

    }

    private static final Logger log = LoggerFactory.getLogger(WorkflowLauncher.class);

    private final ActiveMQConnectionFactory connectionFactory;

    public ActiveMQConnector(final String uri) {

        // init the connection factory to it
        this.connectionFactory = new ActiveMQConnectionFactory(uri);

    }

    public ActiveMQSession newSession() {

        try {

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // return the session
            return new ActiveMQSession(connection, session);

        } catch (Exception e) {

            if (e instanceof RuntimeException) throw (RuntimeException) e;

            throw new RuntimeException(e);

        }

    }

}
