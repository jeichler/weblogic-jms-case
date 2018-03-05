package com.redhat.sample.rhamt;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DynamicClassInstantiation implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
        final Class<?> foo = Class.forName("java.lang.String");
        System.out.println(foo.getCanonicalName());
	}
}