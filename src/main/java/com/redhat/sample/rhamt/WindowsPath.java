package com.redhat.sample.rhamt;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class WindowsPath implements InitializingBean {

    private static final String MY_WINDOWS_PATH = "C:\temp";

	@Override
	public void afterPropertiesSet() throws Exception {
        System.out.println(MY_WINDOWS_PATH);
	}
}