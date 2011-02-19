package com.thomasbarker.probpass.algo;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.thomasbarker.probpass.com.google.common.base.Splitter;

public class Population implements Externalizable {

	private static int K = 9;

	private SortedSet<Fragment> population = Population.createEntropySortedSet();

	// Exists solely for serialization
	public Population() { ; }

	public Population( List<String> samples ) {

		Map<String, Integer> countedSample = new HashMap<String, Integer>();

		for( String sample : samples ) {
			if( countedSample.containsKey( sample ) ) {
				int c = countedSample.get( sample );
				countedSample.put( sample, c + 1 );
			} else {
				countedSample.put( sample, 1 );
			}
		}

		train( countedSample );
	}

	public Population( Map<String, Integer> samples ) {
		train( samples );
	}

	protected Population( SortedSet<Fragment> population ) {
		this.population = population;
	}

	private void train( Map<String, Integer> samples ) {

		double size = 0;
		Map<String, Integer> bag = new HashMap<String, Integer>();

		// Count all possible fragments and sum the total character size
		// of the given sample
		for( Entry<String, Integer> sample: samples.entrySet() ) {
			String text = sample.getKey();
			for ( int k = 1; k <= K; k++ ) {
				for ( int i = k; i < text.length(); i++ ) {
					String trans = text.substring( i - k, i + 1);
					if( bag.containsKey( trans ) ) {
						int c = bag.get( trans );
						bag.put( trans, c + sample.getValue() );
					} else {
						bag.put( trans, sample.getValue() );
					}
				}
			}

			size += text.length() * sample.getValue();
		}

		// Calculate the freq/density of the fragments in the sample to get a
		// population that can be used for analysis.
		for ( String key : bag.keySet() ) {
			Fragment fragment = new Fragment();
			fragment.setText(key);
			fragment.setFrequency(bag.get( key ) / ( size / fragment.getText().length() ));
			population.add( fragment );
		}

	}

	public double likelyhood( String text ) {
		double likelyhood = 1.0;
		for( Entry<Fragment, Integer> fc : this.analyse( text ).entrySet()  ) {
			likelyhood *= Math.pow( fc.getKey().getFrequency(), fc.getValue() );
		}
		return likelyhood;
	}

	public Map<Fragment, Integer> analyse( String text ) {
		Map<Fragment, Integer> fragmentBag = new HashMap<Fragment, Integer>();

		// Initialize the algorithm with just the text
		Set<String> passFrags = new HashSet<String>();
		passFrags.add( text );

		// Loop over a set of strings splitting them by the most entropy reducing
		// fragment in our population until we can't find the fragments.
		// When all the fragments are outside our population, just assume a frequency
		// of 1/16 ^ fragment length.  (E.g. 4-bits of uncertainty per character.)
		while ( !passFrags.isEmpty() ) {
			top :
			for ( Iterator<String> i = passFrags.iterator(); i.hasNext(); ) {
				String frag = i.next();
				for ( Fragment candidate : population ) {
					Iterable<String> split = null;
					split = Splitter.on( candidate.getText() ).split( frag );
					if( !split.iterator().next().equals( frag ) ) {
						i.remove();
						int count = 0;
						for ( String s : split ) {
							if( !s.isEmpty() ) {
								passFrags.add( s );
							}
							count++;
						}
						fragmentBag.put( candidate, ( count - 1 ) );
						break top;
					}
				}
				fragmentBag.put( new Fragment( frag, Math.pow( 1.0 / 16.0, frag.length() ) ), 1 );
				i.remove();
			}
		}

		return fragmentBag;
	}

	public Population merge( Population other, double otherWeight ) {
		// Take a re-weighted copy of this population
		SortedSet<Fragment> merged = Population.reweight( this.getPopulation(), 1 - otherWeight );

		// Add the re-weighted frequencies if in both populations
		// Otherwise just merge the two sets
		for( Fragment f : Population.reweight( other.getPopulation(), otherWeight ) ) {
			for( Fragment m : merged ) {
				if( m.getText().equals( f.getText() ) ) {
					m.setFrequency( m.getFrequency() + f.getFrequency() );
					break;
				}
			}
			merged.add( f );
		}

		return new Population( merged );
	}

	public void truncate( int length ) {
		SortedSet<Fragment> truncatedPopulation = Population.createEntropySortedSet();
		int count = 0;
		for( Fragment f : this.getPopulation() ) {
			if( count++ == length ) {
				break;
			}
			truncatedPopulation.add( f );
		}
		this.population = truncatedPopulation;
	}

	private static SortedSet<Fragment> createEntropySortedSet() {
		return new TreeSet<Fragment>( new Comparator<Fragment>() {

			public int compare( Fragment a, Fragment b ) {
				if( a.entropy() == b.entropy() ) {
					return 0;
				} else {
					return ( a.entropy() - b.entropy() > 0 ? -1 : +1 );
				}
			}

		});
	}

	private static SortedSet<Fragment> reweight( SortedSet<Fragment> population, double weight ) {
		SortedSet<Fragment> reweighted = Population.createEntropySortedSet();

		for( Fragment f : population ) {
			Fragment reweighed = new Fragment();
			reweighed.setText(f.getText());
			reweighed.setFrequency(f.getFrequency() * weight);

			reweighted.add( reweighed );
		}

		return reweighted;
	}
	
	protected SortedSet<Fragment> getPopulation() {
		return population;
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		for ( Fragment f : this.getPopulation() ) {
			output.append( String.format( "%s : %s \n" , f.getText(), f.getFrequency() ) );
		}
		return output.toString();
	}

	public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException {
		SortedSet<Fragment> population = Population.createEntropySortedSet();
		int numberToRead = in.readInt();
		for( int i = 0; i < numberToRead; i++ ) {
			Fragment f = new Fragment();
			f.setText( (String) in.readObject() );
			f.setFrequency( in.readDouble() );
			population.add( f );
		}
		this.population = population;
	}

	public void writeExternal( ObjectOutput out ) throws IOException {
		out.writeInt( this.getPopulation().size() );
		for( Fragment f : this.getPopulation() ) {
			out.writeObject( f.getText() );
			out.writeDouble( f.getFrequency() );
		}
	}
}
