package jtinyhttp.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import jtinyhttp.util.AbstractRequest;

public class HTTP {

	public interface Header {
		public String getKey();

		public String getValue();

		public void writeHeaderLine(OutputStream out) throws IOException;
	}

	public interface Entity {
		public void write(OutputStream out) throws IOException;
	}

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
		 * Get the (optional) entity associated with this message.
		 *
		 * @return
		 */
		public Entity getBody();

		/**
		 * Write this message to a given output stream.
		 *
		 * @param out
		 */
		public void write(OutputStream stream) throws IOException;

		/**
		 * Write the message request line to a given output stream. This gives
		 * finer-grained control over the process.
		 *
		 * @param out
		 */
		public void writeRequestLine(OutputStream stream) throws IOException;
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

	public static final class Get extends AbstractRequest {

		public Get(String uri) {
			super(uri, V1_1, null);
		}

		public Get(String uri, String version, Entity body) {
			super(uri, version, body);
		}

		@Override
		public Method getMethod() {
			return Method.GET;
		}
	}

	public static final class Post extends AbstractRequest {

		public Post(String uri, String version, Entity body) {
			super(uri, version, body);
		}

		@Override
		public Method getMethod() {
			return Method.POST;
		}
	}

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
	 *
	 */
	public static final int CONTINUE = 100;

	public static final int SWITCHING_PROTOCOLS = 101;

	public static final int PROCESSING = 102;

	public static final int EARLY_HINTS = 103;

	// ===============================================================
	// 2XX success
	// ===============================================================

	public static final int OK = 200;

	public static final int CREATED = 201;

	public static final int ACCEPTED = 202;

	public static final int NON_AUTHORATIVE_INFORMATION = 203;

	public static final int NO_CONTENT = 204;

	public static final int RESET_CONTENT = 205;

	public static final int PARTIAL_CONTENT = 206;

	public static final int MULTI_STATUS = 207;

	public static final int ALREADY_REPORTED = 208;

	public static final int IM_USED = 209;

	// ===============================================================
	// 3XX Redirection
	// ===============================================================
	public static final int MULTIPLE_CHOICES = 300;

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
