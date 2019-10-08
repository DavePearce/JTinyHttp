package jtinyhttp.core;

import jtinyhttp.core.HTTP.Entity;
import jtinyhttp.core.HTTP.Method;
import jtinyhttp.util.AbstractRequest;

public class Messages {
	/**
	 * Represents a simple GET request which can be constructed and then sent to the
	 * server.
	 *
	 * @author David J. Pearce
	 *
	 */
	public static HTTP.Request GET(String uri, String version) {
		return new AbstractRequest(Method.GET, uri, version, null) {
		};
	}

	/**
	 * Represents a simple GET request which can be constructed and then sent to the
	 * server.
	 *
	 * @author David J. Pearce
	 *
	 */
	public static HTTP.Request GET(String uri, String version, Entity body) {
		return new AbstractRequest(Method.GET, uri, version, body) {
		};
	}

	/**
	 * Represents a simple POST request which can be constructed and then sent to
	 * the server.
	 *
	 * @author David J. Pearce
	 *
	 */
	public static HTTP.Request POST(String uri, String version, Entity body) {
		return new AbstractRequest(Method.GET, uri, version, body) {
		};
	}
}
