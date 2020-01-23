package jtinyhttp.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import jtinyhttp.util.AbstractRequest;

ublic class HTTP {

	/**
	 * Represents an arbitrary connection between a client and server which is
	 * location agnostic. In other words, this could be either a client connection
	 * or a server connection.
	 *
	 * @author David J. Pearce
	 *
	 * @param <S>
	 * @param <T>
	 */
	public interface Connection {
		/**
		 * Close this connection.
		 */
		public void close() throws IOException;
	}

	/**
	 * Represents a connection from a client (which makes requests) to a server
	 * (which will respond to them).
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface ClientConnection extends Connection {
		/**
		 * Send a request to the server behind this connection and receive a given
		 * reply.
		 *
		 * @param message
		 * @return
		 */
		public Response send(Request message) throws IOException;
	}

	/**
	 * Represents a connection from the server (which responds to requests) to a
	 * client.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface ServerConnection extends Connection {
		/**
		 * Send a response to the client behind this connection.
		 *
		 * @param message
		 * @return
		 */
		public void send(Response response) throws IOException;
	}

	/**
	 * Represents a generic HTTP header.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Header {
		/**
		 * Get the key associated with this header.
		 *
		 * @return
		 */
		public String getKey();

		/**
		 * Get the value associated with this header.
		 * @return
		 */
		public String getValue();

	}

	/**
	 * Represents an abstract payload which may be transfered by a request or
	 * response method.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Entity {
		/**
		 * Write this entity to the output stream.
		 *
		 * @param out
		 * @throws IOException
		 */
		public void write(OutputStream out) throws IOException;
	}

	/**
	 * Represents a generic HTTP message which is either a <code>Request</code> or
	 * <code>Response</code>.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Message {

		/**
		 * Get the HTTP version used in this message.
		 *
		 * @return
		 */
		public String getVersion();

		/**
		 * Return the number of headers contained in this message.
		 *
		 * @return
		 */
		public int size();

		/**
		 * Get a specific header contained in this message.
		 *
		 * @param i
		 * @return
		 */
		public Header getHeader(int i);

		/**
		 * Add a new header to this message.
		 *
		 * @param key
		 * @param value
		 * @return
		 */
		public Message addHeader(byte[] key, String value);

		/**
		 * Get the (optional) entity associated with this message.
		 *
		 * @return
		 */
		public Entity getBody();

		/**
		 * Write the message start line and headers to a given output stream, whilst
		 * ignoring the trailing empty line.
		 *
		 * @param out
		 */
		public void write(OutputStream stream) throws IOException;

		/**
		 * Write the message start line and headers to a given output stream, whilst
		 * including the trailing empty line.
		 *
		 * @param out
		 */
		public void writeln(OutputStream stream) throws IOException;
	}

	public enum Method {
		GET, POST
	}

	/**
	 * Represents a request message, such as GET or POST.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Request extends Message {
		Method getMethod();
	}

	/**
	 * Represents an arbitrary response message which can be returned from the
	 * server.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Response extends Message {
		/**
		 * Get the status code associated with this response.
		 *
		 * @return
		 */
		int getStatusCode();
	}

	// ===============================================================
	// HTTP Version Strings
	// ===============================================================

	public static final String V1_0 = "HTTP/1.0";

	public static final String V1_1 = "HTTP/1.1";

	// ===============================================================
	// 1XX informational
	// ===============================================================

	/**
	 * Informed client that initial part of request has been received and not yet
	 * rejected. The client should continue by sending remainder of the request or,
	 * if already completed, ignore this response.
	 */
	public static final int CONTINUE = 100;

	/**
	 * Server is willing to comply with client's request for a change in protocol.
	 */
	public static final int SWITCHING_PROTOCOLS = 101;

	public static final int PROCESSING = 102;

	public static final int EARLY_HINTS = 103;

	// ===============================================================
	// 2XX success
	// ===============================================================

	/**
	 * Request has succeeded.
	 */
	public static final int OK = 200;

	/**
	 * Request has been completed and a new resource was created.
	 */
	public static final int CREATED = 201;

	/**
	 * Request was accepted for processing, but this has not yet completed.
	 */
	public static final int ACCEPTED = 202;

	public static final int NON_AUTHORATIVE_INFORMATION = 203;

	/**
	 * Server request completed but does not need to return any content.
	 */
	public static final int NO_CONTENT = 204;

	/**
	 * Server requested completed and client should reset its view. This allows for
	 * clearing forms after user input.
	 */
	public static final int RESET_CONTENT = 205;

	public static final int PARTIAL_CONTENT = 206;

	public static final int MULTI_STATUS = 207;

	public static final int ALREADY_REPORTED = 208;

	public static final int IM_USED = 209;

	// ===============================================================
	// 3XX Redirection
	// ===============================================================
	/**
	 * Request resources corresponds to more than one option. Further negotiation
	 * required to disambiguate.
	 */
	public static final int MULTIPLE_CHOICES = 300;

	/**
	 * Requested resource has a new URI which is included in the
	 * <code>Location</code> field.
	 */
	public static final int MOVED_PERMANTENTLY = 301;

	public static final int FOUND = 302;

	public static final int SEE_OTHER = 303;

	public static final int NOT_MODIFIED = 304;

	public static final int USE_PROXY = 305;

	public static final int SWITCH_PROXY = 306;

	public static final int TEMPORARY_REDIRECT = 307;

	public static final int PERMANTENT_REDIRECT = 308;

	// ===============================================================
	// 4XX Redirection
	// ===============================================================

	public static final int BAD_REQUEST = 400;

	public static final int UNAUTHORISED = 401;

	public static final int PAYMENT_REQUIRED = 402;

	public static final int FORBIDDEN = 403;

	public static final int NOT_FOUND = 404;

	public static final int METHOD_NOT_ALLOWED = 405;

	public static final int NOT_ACCEPTABLE = 406;

	public static final int PROXY_AUTHENTICATION_REQUIRED = 407;

	public static final int REQUEST_TIMEOUT = 408;

	public static final int CONFLICT = 409;

	public static final int GONE = 410;

	public static final int LENGTH_REQUIRED = 411;

	public static final int PRECONDITION_FAILED = 412;

	public static final int PAYLOAD_TOO_LARGE = 413;

	public static final int URI_TOO_LONG = 414;

	public static final int UNSUPPORTED_MEDIA_TYPE = 415;

	public static final int RANGE_NOT_SATISFIABLE = 416;

	public static final int EXPECTATION_FAILED = 417;

	public static final int IM_A_TEAPOT = 418;

	public static final int MISDIRECTED_REQUEST = 421;

	public static final int UNPROCESSABLE_ENTITY = 422;

	public static final int LOCKED = 423;

	public static final int FAILED_DEPENDENCY = 424;

	public static final int TOO_EARLY = 425;

	public static final int UPGRADE_REQUIRED = 426;

	public static final int PRECONDITION_REQUIRED = 428;

	public static final int TOO_MANY_REQUESTS = 429;

	public static final int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;

	public static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451;

	// ===============================================================
	// 5XX Server Errors
	// ===============================================================

	public static final int INTERNAL_SERVER_ERROR = 500;

	public static final int NOT_IMPLEMENTED = 501;
	public static final int BAD_GATEWAY = 502;
	public static final int SERVICE_UNAVAILABLE = 503;
	public static final int GATEWAY_TIMEOUT = 504;
	public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
	public static final int VARIANT_ALSO_NEGOTIATES = 506;
	public static final int INSUFFICIENT_STORAGE = 507;
	public static final int LOOP_DETECTED = 508;
	public static final int NOT_EXTENDED = 510;
	public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;
}
