package jtinyhttp.core;

import java.nio.charset.StandardCharsets;

import jtinyhttp.core.HTTP.Header;
import jtinyhttp.util.ArbitraryHeader;

public class Headers {

	public static final byte[] HOST = "Host: ".getBytes(StandardCharsets.US_ASCII);
	public static final byte[] COOKIE = "Cookie: ".getBytes(StandardCharsets.US_ASCII);

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
