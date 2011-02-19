package com.thomasbarker.probpass.algo.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thomasbarker.probpass.algo.Population;

public class PopulationTest {

	private List<String> badPasswords = new ArrayList<String>();
	private Population population = null;

	@Before
	public void setUp() {
		badPasswords.add( "password" );
		badPasswords.add( "password" );
		badPasswords.add( "ilikeyou" );

		population = new Population( badPasswords );
	}

	@Test
	public void testBasicTrain() {
		assertEquals( 1.0/3.0, population.likelyhood( "ilikeyou" ), 0.01 );
		assertEquals( 2.0/3.0, population.likelyhood( "password" ), 0.01 );
		assertEquals( 1.0 / 3.0 , population.likelyhood( "swor" ), 0.01 );

		assertEquals( 2.0 / 3.0 / 3.0, population.likelyhood( "passwordswor" ), 0.01 );
	}

}
