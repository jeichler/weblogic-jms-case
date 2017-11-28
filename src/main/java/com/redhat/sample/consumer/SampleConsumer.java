package com.redhat.sample.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class SampleConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			System.err.println("message was: " + ((TextMessage) message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
