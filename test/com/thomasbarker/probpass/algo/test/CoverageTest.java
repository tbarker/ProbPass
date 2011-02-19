package com.thomasbarker.probpass.algo.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.thomasbarker.probpass.algo.Population;

public class CoverageTest {

	final static double strictness = 1E-9;

	List<String>	rockyou5per	= new ArrayList<String>();
	List<String>	good		= new ArrayList<String>();
	List<String>	twitter		= new ArrayList<String>();
	Population		pop			= null;

	@Before
	public void populateLists() {
		rockyou5per.add( "123456" );
		rockyou5per.add( "12345" );
		rockyou5per.add( "123456789" );
		rockyou5per.add( "password" );
		rockyou5per.add( "iloveyou" );
		rockyou5per.add( "princess" );
		rockyou5per.add( "1234567" );
		rockyou5per.add( "12345678" );
		rockyou5per.add( "abc123" );
		rockyou5per.add( "nicole" );
		rockyou5per.add( "daniel" );
		rockyou5per.add( "babygirl" );
		rockyou5per.add( "monkey" );

		good.add( "monkeyseemonkeydooratleastIdo" );
		good.add( "sdcnioh22sdf" );
		good.add( "goodnightsam" );
		good.add( "littlevoice" );
		good.add( "crablastdog" );
		good.add( "grahfooZ1&" );
		good.add( "lactosfi1sh£&" );
		good.add( "greekmillionsagolongsandwichItellsu" );
	}

	@Before
	public void readTwitterBanned() {
		File file = new File( "test/twitter-banned.txt" );
		try {
			FileReader reader = new FileReader( file );
			BufferedReader in = new BufferedReader( reader );
			String line = null;
			while ( ( line = in.readLine() ) != null ) {
				twitter.add( line );
			}
			in.close();
		} catch ( FileNotFoundException e ) {
			throw new RuntimeException( e );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	@Before
	public void trainOnPhpBbLeakedWordlist() {

		Map<String, Integer> lines = new HashMap<String, Integer>();
		File file = new File( "test/phpbb.wordcount" );
		try {
			FileReader reader = new FileReader( file );
			BufferedReader in = new BufferedReader( reader );
			String line = null;
			while ( ( line = in.readLine() ) != null ) {
				String[] splitLine = line.split( "\\s+" );
				lines.put( splitLine[2], Integer.parseInt( splitLine[1] ) );
			}
			in.close();

			this.pop = new Population( lines );
			this.pop.truncate( 800 );

		} catch ( FileNotFoundException e ) {
			throw new RuntimeException( e );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}

	}

	@Test
	public void testCoverageBad() {

		int rejected = 0;
		for( String bad : rockyou5per ) {
			if( pop.likelyhood( bad ) > strictness ) {
				rejected++;
			}
		}

		assertTrue( rejected > 10 );
	}

	@Test
	public void moreThan95PercentTwitterBad() {
		int rejected = 0;
		for( String bad : twitter ) {
			if( pop.likelyhood( bad ) > strictness ) {
				rejected++;
			}
		}

		assertTrue( rejected > 0.95 * twitter.size() );
	}

	@Test
	public void testFalsePositive() {

		for( String g : good ) {
			if( pop.likelyhood( g ) > strictness ) {
				assertTrue( false );
			}
		}
	}
}
