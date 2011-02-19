package com.thomasbarker.probpass.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thomasbarker.probpass.algo.Population;

public final class LoadPopulation {

	public static Population fromFile( File file )
	throws
		IOException
	{
		return LoadPopulation.fromFile( file, 800 );
	}

	public static Population fromFile( File file, int size )
	throws
		IOException
	{
		if ( !file.canRead() ) {
			throw new IOException( "Cannot read " + file.getName() );
		}

		String extension = file.getName().replaceFirst( "[^\\.]+", "" );
		if ( extension.equals( ".wordlist" ) ) {
			return LoadPopulation.fromWordList( file, size );
		}
		else if ( extension.equals( ".wordcount" ) ) {
			return LoadPopulation.fromWordCountList( file, size );
		}
		else if ( extension.equals( ".ser" ) ) {
			InputStream in = file.toURI().toURL().openStream();
			return LoadPopulation.fromSerialized( in, size );
		}
		else {
			throw new RuntimeException( "Cannot read file with extension: " + extension );
		}
	}

	public static Population fromSerialized( InputStream in, int size ) {
		Population population = LoadPopulation.fromSerialized( in );
		population.truncate( size );

		return population;
	}

	public static Population fromSerialized( InputStream in ) {

		ObjectInputStream oin;
		Population population;

		try {
			oin = new ObjectInputStream( in );
			population = (Population) oin.readObject();
			oin.close();
		} catch ( IOException ioe ) {
			throw new RuntimeException( ioe );
		} catch ( ClassNotFoundException cnfe ) {
			throw new RuntimeException( cnfe );
		}

		return population;
	}

	public static Population fromWordList( File file, int size ) {
		Population population = null;

		List<String> lines = new ArrayList<String>();
		try {
			FileReader reader = new FileReader( file );
			BufferedReader in = new BufferedReader( reader );
			String line = null;
			while ( ( line = in.readLine() ) != null ) {
				lines.add( line );
			}
			in.close();

			population = new Population( lines );
			population.truncate( size );
		} catch ( FileNotFoundException e ) {
			throw new RuntimeException( e );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}

		return population;

	}

	public static Population fromWordCountList( File file, int size ) {

		Population population = null;

		Map<String, Integer> lines = new HashMap<String, Integer>();
		try {
			FileReader reader = new FileReader( file );
			BufferedReader in = new BufferedReader( reader );
			String line = null;
			while ( ( line = in.readLine() ) != null ) {
				String[] splitLine = line.split( "\\s+" );
				lines.put( splitLine[2], Integer.parseInt( splitLine[1] ) );
			}
			in.close();

			population = new Population( lines );
			population.truncate( size );
		} catch ( FileNotFoundException e ) {
			throw new RuntimeException( e );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}

		return population;
	}

}
