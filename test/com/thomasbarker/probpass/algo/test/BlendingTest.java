package com.thomasbarker.probpass.algo.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thomasbarker.probpass.algo.Population;
import com.thomasbarker.probpass.io.LoadPopulation;

public class BlendingTest {

	final static double strictness = 1E-9;

	Population base = null;
	Population kittens = null;

	@Before
	public void setUp() throws IOException {
		// Get a general population
		base = LoadPopulation.fromFile( new File( "test/tuscl.wordcount" ) );

		// Assemble one for kittens.com
		List<String> kittensList = new ArrayList<String>();
		kittensList.add( "kittens.com" );
		kittensList.add( "kittens" );
		kittensList.add( "cats" );
		kittensList.add( "feline" );
		kittens = new Population( kittensList );
	}

	@Test
	public void blendBeforeAfter() {
		assertTrue( base.likelyhood( "password1" ) > strictness );
		assertTrue( base.likelyhood( "kittenlive" ) < strictness );

		// Skewing the population to 5% kitten related passwords
		Population pop = base.merge( kittens, 0.05 );

		assertTrue( pop.likelyhood( "password1" ) > strictness );
		assertTrue( pop.likelyhood( "kittenlive" ) > strictness ); // Want to shift

	}

}
