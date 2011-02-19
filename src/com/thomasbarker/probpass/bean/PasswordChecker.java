package com.thomasbarker.probpass.bean;

import java.util.List;
import java.util.Map;

import com.thomasbarker.probpass.algo.Population;

public interface PasswordChecker {

	boolean check( String password );

	boolean check( String password, List<String> oneOffList );

	void setStrictness( double strictness );

	void setBasePopulations( Map<Population, Double> weightedBasePopulations );

	void setMaxPopulationSize( int maxPopulationSize );

}
