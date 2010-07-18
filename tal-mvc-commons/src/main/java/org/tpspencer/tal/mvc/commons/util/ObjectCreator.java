package org.tpspencer.tal.mvc.commons.util;

import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tpspencer.tal.mvc.controller.InterfaceAdaptor;

public class ObjectCreator {
	private final static Log logger = LogFactory.getLog(ObjectCreator.class);

	public static <T> T createObject(Class<T> expected) {
		try {
			if( expected.isInterface() ) {
				return expected.cast(Proxy.newProxyInstance(
						Thread.currentThread().getContextClassLoader(), 
						new Class[]{expected}, 
						new InterfaceAdaptor()));
			}
			else {
				return expected.newInstance();
			}
		}
		catch( IllegalAccessException e ) {
			if( logger.isDebugEnabled() ) logger.debug("!!! Failed to create object due to access error: " + e.getMessage());
			if( logger.isDebugEnabled() ) e.printStackTrace();
		}
		catch( InstantiationException e ) {
			if( logger.isDebugEnabled() ) logger.debug("!!! Failed to create object due to instantiation error: " + e.getMessage());
			if( logger.isDebugEnabled() ) e.printStackTrace();
		}
		
		return null;
	}
}
