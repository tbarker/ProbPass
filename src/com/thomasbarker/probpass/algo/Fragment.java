package com.thomasbarker.probpass.algo;

public final class Fragment {

	private String text;
	private double frequency;

	public Fragment() { ; }

	public Fragment( String text, double frequency ) {
		this.text = text;
		this.frequency = frequency;
	}

	// TODO This is clearly not quite right - 16 is just magic number from my estimate
	// of the entropy of an average character.  I guessed this formula :-S
	public double entropy() { return  frequency * Math.pow( 16, text.length() ); }

	public String getText() { return text; }
	public void setText( String text ) {
		this.text = text;
	}

	public double getFrequency() { return frequency; }
	public void setFrequency( double frequency ) {
		this.frequency = frequency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(frequency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		Fragment other = (Fragment) obj;
		if( Double.doubleToLongBits(frequency) != Double.doubleToLongBits(other.frequency) )
			return false;
		if( text == null ) {
			if( other.text != null )
				return false;
		} else if( !text.equals(other.text) )
			return false;
		return true;
	}

}
