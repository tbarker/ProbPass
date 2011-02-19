package com.thomasbarker.probpass.integration.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class PasswordCheckerNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
        registerBeanDefinitionParser( "checker", new CheckerBeanDefinitionParser() );        
	}

}
