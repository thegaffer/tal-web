package org.tpspencer.tal.mvc.spring.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.tpspencer.tal.mvc.Window;

/**
 * This bean
 * 
 * @author Tom Spencer
 */
public class MVCFactoryBean implements ApplicationContextAware {

	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		// TODO Auto-generated method stub
	}
	
	private Window createWindow() {
		return null;
	}
}
