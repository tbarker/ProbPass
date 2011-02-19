package com.thomasbarker.probpass.integration.spring;

import java.util.List;

import com.thomasbarker.probpass.bean.impl.PasswordCheckerImpl;

public final class PasswordCheckerSpring extends PasswordCheckerImpl {

	private boolean l33t = true;

	public boolean check( String password, List<String> oneOffList ) {

		if( isL33t() ) {
			// Reverse some easily guessed mutations
			password = password.replace( '1', 'l');
			password = password.replace( '0', 'o');
			password = password.replace( '4', 'a');
			password = password.replace( '3', 'e');
			password = password.replace( '5', 's');
			password = password.toLowerCase();
			password = password.replaceAll( "^(.*)1$", "\1" );
		}

		return super.check( password, oneOffList );
	}

	public boolean isL33t() { return l33t; }
	public void setL33t( boolean l33t ) {
		this.l33t = l33t;
	}

}
