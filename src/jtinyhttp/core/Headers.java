package jtinyhttp.core;

import jtinyhttp.core.HTTP.Header;
import jtinyhttp.util.ArbitraryHeader;

public class Headers {


	public static Header Host(String value) {
		return new ArbitraryHeader("Host",value.getBytes());
	}


	public static Header Cookie(String value) {
		return new ArbitraryHeader("cookie",value.getBytes());
	}

	// ====================================================
	// Entity Headers
	// ====================================================

	/**
	 * Indicate the size of the entity body in decimal number of OCTETs.
	 *
	 * @param value
	 * @return
	 */
	public static Header ContentLength(int value) {
		String octets = Integer.toString(value);
		return new ArbitraryHeader("Content-Length", octets);
	}
}
