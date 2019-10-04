package jtinyhttp.core;

import jtinyhttp.core.HTTP.Header;
import jtinyhttp.util.ArbitraryHeader;

public class Headers {

	public static Header Cookie(String value) {
		return new ArbitraryHeader("cookie",value.getBytes());
	}

	public static Header ContentLength(int value) {
		String octets = Integer.toOctalString(value);
		return new ArbitraryHeader("Content-Length", octets);
	}
}
