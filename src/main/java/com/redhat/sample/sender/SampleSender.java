package com.redhat.sample.sender;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SampleSender implements InitializingBean {
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		jmsTemplate.convertAndSend("hello world");
	}
	
	

}
