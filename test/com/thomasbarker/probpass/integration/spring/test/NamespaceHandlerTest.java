package com.thomasbarker.probpass.integration.spring.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thomasbarker.probpass.integration.spring.PasswordCheckerSpring;

import static org.junit.Assert.*;

public class NamespaceHandlerTest {

	PasswordCheckerSpring passwordChecker = null;

	@Before
	public void buildBean() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext( "checkTestContext.xml", getClass() );
        passwordChecker = applicationContext.getBean( PasswordCheckerSpring.class );
	}

	@Test
	public void isBuild() {
		assertNotNull( passwordChecker );
	}

	@Test
	public void getsl33t() {
		assertFalse( passwordChecker.isL33t() );
	}

	@Test
	public void functioning() {
		assertFalse( passwordChecker.check( "kitten" ) );
		assertTrue( passwordChecker.check( "efpoc89jm9h" ) );
	}

	@Test
	public void usingFileData() {
		assertFalse( passwordChecker.check( "stripclub" ) );
		assertTrue( passwordChecker.check( "hockeyclub" ) );
	}

}
