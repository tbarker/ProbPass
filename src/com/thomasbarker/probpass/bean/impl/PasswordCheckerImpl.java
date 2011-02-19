package com.thomasbarker.probpass.bean.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thomasbarker.probpass.algo.Population;
import com.thomasbarker.probpass.bean.PasswordChecker;

public class PasswordCheckerImpl
	implements PasswordChecker
{
	private Population	basePopulation			= new Population();
	private double		basePopulationWeight	= 0.0;
	private double		strictness				= 7.0;
	private int			maxPopulationSize		= 800;

	public boolean check( String password ) {
		return doCheck( password, basePopulation );
	}

	public boolean check( String password, List<String> oneOffList ) {
		Population oneOffPopulation = new Population( oneOffList );
		return check( password, oneOffPopulation );
	}

	boolean check( String password, Population oneOffPopulation ) {
		Population pop = basePopulation.merge( oneOffPopulation, 1.0 - basePopulationWeight );
		pop.truncate( maxPopulationSize );

		return doCheck( password, pop );
	}
	
	private boolean doCheck( String password, Population oneOffPopulation ) {
		return Math.pow( 10, -strictness ) > oneOffPopulation.likelyhood( password );
	}

	public void setStrictness( double strictness ) {
		this.strictness = strictness;
	}

	public void setBasePopulations( Map<Population, Double> weightedBasePopulations ) {

		for( Entry<Population, Double> pop : weightedBasePopulations.entrySet() ) {
			basePopulationWeight += pop.getValue();
			basePopulation = basePopulation.merge( pop.getKey(), pop.getValue() );
		}

		basePopulation.truncate( this.maxPopulationSize );
	}

	public void setMaxPopulationSize( int maxPopulationSize ) {
		this.maxPopulationSize = maxPopulationSize;
	}

}
