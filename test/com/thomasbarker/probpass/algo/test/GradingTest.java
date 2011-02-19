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

public class GradingTest {

	List<String>	bad			= new ArrayList<String>();
	List<String>	ok			= new ArrayList<String>();
	List<String>	good		= new ArrayList<String>();
	List<String>	impossible	= new ArrayList<String>();

	Population		pop			= null;

	@Before
	public void populateLists() {
		bad.add( "password1" );
		bad.add( "fuckyou" );
		bad.add( "letmein" );

		ok.add( "greenlight" );
		ok.add( "verona" );
		ok.add( "phrasepass" );

		good.add( "monkeyseemonkeydooratleastIdo" );
		good.add( "lactosfi1sh£&" );
		good.add( "longsandwichItellsu" );

		impossible.add( "h4flspodiE@d93ed!£" );
		impossible.add( "34fXIf$fdfJ£4asd!£" );
		impossible.add( "8o73Zd%&(!fh%z3Q" );
	}

	@Before
	public void trainOnTuscl() {
		Map<String, Integer> lines = new HashMap<String, Integer>();
		File file = new File( "test/tuscl.wordcount" );
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
	public void testGradeOrder() {
		assertTrue( avgLikelyhood( bad ) > avgLikelyhood( ok ) );
		assertTrue( avgLikelyhood( ok ) > avgLikelyhood( good ) );
		assertTrue( avgLikelyhood( good ) > avgLikelyhood( impossible ) );
	}

	private double avgLikelyhood( List<String> sample ) {
		double totalLikelyhood = 0;
		for( String text : sample ) {
			totalLikelyhood += pop.likelyhood( text );
		}
		return totalLikelyhood / sample.size();
	}
}
