package com.thomasbarker.probpass.algo.test.serialization;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thomasbarker.probpass.algo.Population;

public class WriteToReadFromDiskTest {

	Population population = null;

	@Before
	public void setUp() {
		List<String> passwords = new ArrayList<String>();
		passwords.add( "password" );
		passwords.add( "seesaidhowfuunyIcouldbee" );
		passwords.add( "ilikeyou" );
		passwords.add( "£%$@)($%*&*HFHDS" );

		population = new Population( passwords );
	}

	@Test
	public void writeToFrom() throws IOException, ClassNotFoundException {
		File tmp = File.createTempFile( "foo", "ser" );
		FileOutputStream fos = new FileOutputStream( tmp );
		ObjectOutputStream out = new ObjectOutputStream( fos );
		out.writeObject( population );
		out.close();

		FileInputStream	fis = new FileInputStream( tmp );
		ObjectInputStream oin = new ObjectInputStream(fis);
		Population readPopulation = (Population) oin.readObject();
		oin.close();

		// Note, SortedSet stops equals from working properly
		assertEquals( population.toString(), readPopulation.toString() );
	}
}
