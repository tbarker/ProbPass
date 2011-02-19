package com.thomasbarker.probpass.collate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.thomasbarker.probpass.algo.Population;
import com.thomasbarker.probpass.io.LoadPopulation;

public final class GenerateFromWordListWithCount {

	public static void main( String[] args ) {

		File file = new File( args[0] );
		try {
			Population population = LoadPopulation.fromFile( file );

			FileOutputStream fos = new FileOutputStream( args[0] + ".ser" );
			ObjectOutputStream out = new ObjectOutputStream( fos );
			out.writeObject( population );
			out.close();

		} catch ( FileNotFoundException e ) {
			throw new RuntimeException( e );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}

	}

}
