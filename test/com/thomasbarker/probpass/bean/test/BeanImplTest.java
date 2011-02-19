package com.thomasbarker.probpass.bean.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.thomasbarker.probpass.algo.Population;
import com.thomasbarker.probpass.bean.PasswordChecker;
import com.thomasbarker.probpass.bean.impl.PasswordCheckerImpl;
import com.thomasbarker.probpass.io.LoadPopulation;

import static org.junit.Assert.*;

public class BeanImplTest {

	protected PasswordChecker checker = new PasswordCheckerImpl();

	@Before
	public void setUp() throws IOException {
		Map<Population, Double> basePop = new HashMap<Population, Double>();
		basePop.put( LoadPopulation.fromFile( new File( "test/tuscl.wordcount" ) ), 0.85 );
		checker.setBasePopulations( basePop );
		checker.setStrictness( 9.0 );
	}

	@Test
	public void baseBehaviour() {
		assertTrue(	checker.check( "890q4yncw08g5ch" ) );
		assertFalse( checker.check( "password1" ) );
	}

	@Test
	public void oneOffBehaviour() {
		assertTrue(	checker.check( "rabbitrabbit" ) );

		// Who doesn't like rabbits?
		List<String> rabbitList = new ArrayList<String>();
		rabbitList.add( "rabbit" );
		rabbitList.add( "rabbits" );
		rabbitList.add( "jackrabbit" );

		assertFalse( checker.check( "rabbitrabbit", rabbitList ) );
		assertFalse( checker.check( "rabbitfoo", rabbitList ) );
	}

}
