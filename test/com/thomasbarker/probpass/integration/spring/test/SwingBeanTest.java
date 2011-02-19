package com.thomasbarker.probpass.integration.spring.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.thomasbarker.probpass.bean.PasswordChecker;
import com.thomasbarker.probpass.bean.test.BeanImplTest;
import com.thomasbarker.probpass.integration.spring.PasswordCheckerSpring;

public class SwingBeanTest extends BeanImplTest {

	protected PasswordChecker checker = new PasswordCheckerSpring();

	@Test
	public void l33tify() {
		assertTrue(	checker.check( "rabbitrabbit" ) );

		// Who doesn't like h4xors?
		List<String> phreekList = new ArrayList<String>();
		phreekList.add( "leet" );
		phreekList.add( "hackers" );

		assertTrue( checker.check( "lipsolordinum", phreekList ) );
		assertFalse( checker.check( "h4ck3r5z7pas", phreekList ) );

		( (PasswordCheckerSpring) checker ).setL33t( false );

		assertTrue( checker.check( "lipsolordinum", phreekList ) );
		assertTrue( checker.check( "h4ck3r5z7pas", phreekList ) );
	}

}
