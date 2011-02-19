package com.thomasbarker.probpass.run;

import java.io.IOException;

import com.thomasbarker.probpass.algo.Population;
import com.thomasbarker.probpass.reference.Populations;

public  class Main {

	/**
	 * Simple command-line program for using ProbPass
	 * 
	 * Strong password results in no output
	 * A weak password quits with exit code 2 and echos to stdout.
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {

		char LINE_SEPERATOR = System.getProperty( "line.separator" ).charAt( 0 );

		// Read strength
		double strength;
		if ( 0 != args.length ) {
			strength = Math.pow( 10, -Double.parseDouble( args[0] ) );
		} else {
			strength = 1E-9;
		}

		// Set-up checker
		Population population = Populations.getDefault();

		try {
			// Read line
			int character;
			String password = new String();
			while ( ( character = System.in.read() ) != LINE_SEPERATOR ) {
				password += (char) character;
			}

			// If good - silence is golden
			// If bad - exit on error 2 as pwck does
			if ( population.likelyhood( password ) > strength ) {
				System.err.println( "FAIL" );
				System.out.println( password );
				System.exit( 2 );
			}
		} catch ( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

}
