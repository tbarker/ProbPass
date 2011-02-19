package com.thomasbarker.probpass.reference;

import com.thomasbarker.probpass.algo.Population;
import com.thomasbarker.probpass.io.LoadPopulation;

public final class Populations {

	private Populations() { ; }

	private static Population _default = null;

	public static Population getDefault() {
		if ( null == _default ) {
			_default = LoadPopulation.fromSerialized(
					Populations.class.getResourceAsStream( "default.ser" )
			);
		}
		return _default;
	}
}
