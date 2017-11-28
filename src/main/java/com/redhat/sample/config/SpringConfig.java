package com.redhat.sample.config;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.redhat.sample.consumer.SampleConsumer;

@EnableWebMvc
@EnableJms
@Configuration
@ComponentScan({ "com.redhat.sample" })
public class SpringConfig extends WebMvcConfigurerAdapter {
	// public class SpringConfig extends WebMvcConfigurerAdapter implements
	// JmsListenerConfigurer {

	private static final Logger LOG = Logger.getLogger(SpringConfig.class.getName());

	private static final String ICF_NAME = "weblogic.jndi.WLInitialContextFactory";
	private static final String CF_NAME = "weblogic.examples.ejb30.QueueConnectionFactory";

	private String jmsBrokerUrl = "t3://localhost:7001";

	private String queueName = "weblogic.examples.ejb30.ExampleQueue";

	private InitialContext ctx;
	private QueueConnectionFactory qcf = null;
	private QueueConnection qc = null;
	private QueueSession session = null;
	private Queue dest = null;

	@Bean
	public QueueConnectionFactory connectionFactory() {
		Hashtable<String, String> props = new Hashtable<>();
		props.put(Context.INITIAL_CONTEXT_FACTORY, ICF_NAME);
		props.put(Context.PROVIDER_URL, jmsBrokerUrl);

		try {
			ctx = new InitialContext(props);
			LOG.severe("Got InitialContext " + ctx.toString());
			qcf = (QueueConnectionFactory) ctx.lookup(CF_NAME);
			LOG.severe("Got QueueConnectionFactory " + qcf);
			dest = (Queue) ctx.lookup(queueName);
			LOG.severe("Queue-Impl_Class: " + dest.getClass().getName());
			qc = qcf.createQueueConnection();
			LOG.severe("Got QueueConnection " + qc);
			session = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			LOG.severe("Got QueueSession " + session);
		} catch (JMSException | NamingException e) {
			LOG.severe("JMS initialization error: " + e.getMessage());
			LOG.severe("JMS initialization error in session" + e);
		}

		return qcf;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory());
		jmsTemplate.setDefaultDestination(defaultDestination());
		return jmsTemplate;
	}

	@Bean
	public MessageListenerContainer msgListenerContainer() {
		DefaultMessageListenerContainer msgListenerContainer = new DefaultMessageListenerContainer();
		msgListenerContainer.setConnectionFactory(connectionFactory());
		msgListenerContainer.setDestination(defaultDestination());
		msgListenerContainer.setMessageListener(new SampleConsumer());
		return msgListenerContainer;
	}

	@Bean
	public Destination defaultDestination() {
		return dest;
	}
}