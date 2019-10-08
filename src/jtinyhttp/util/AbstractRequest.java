package jtinyhttp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import jtinyhttp.core.HTTP;
import jtinyhttp.core.HTTP.Entity;
import jtinyhttp.core.HTTP.Header;
import jtinyhttp.core.HTTP.Method;
import jtinyhttp.core.HTTP.Request;

public abstract class AbstractRequest extends AbstractMessage implements HTTP.Request {

	public AbstractRequest(Method method, String uri, String version, HTTP.Entity body) {
		super(toRequestLine(method,uri,version), body);
	}

	@Override
	public Method getMethod() {
		switch (header[0]) {
		case 'G':
			return Method.GET;
		case 'P':
			return Method.POST;
		default:
			throw new IllegalArgumentException("invalid method");
		}
	}

	@Override
	public String getVersion() {
		throw new UnsupportedOperationException("implement me");
	}

	@Override
	public HTTP.Request addHeader(byte[] key, String value) {
		return new HeaderRequest(this,key,value);
	}

	private static final class HeaderRequest extends HeaderMessage<HTTP.Request> implements HTTP.Request {

		public HeaderRequest(Request parent, byte[] key, String value) {
			super(parent, key, value);
		}

		@Override
		public String getVersion() {
			return parent.getVersion();
		}

		@Override
		public int size() {
			return parent.size() + 1;
		}

		@Override
		public Header getHeader(int i) {
			if(i == parent.size()) {
				return new ArbitraryHeader(line, 0, line.length - 2);
			} else {
				return parent.getHeader(i);
			}
		}

		@Override
		public HTTP.Request addHeader(byte[] key, String value) {
			return new HeaderRequest(this,key,value);
		}

		@Override
		public Entity getBody() {
			return parent.getBody();
		}

		@Override
		public void write(OutputStream stream) throws IOException {
			parent.write(stream);
			stream.write(line);
		}

		@Override
		public Method getMethod() {
			return parent.getMethod();
		}
	}

	private static byte[] toRequestLine(Method method, String uri, String version) {
		byte[] methodBytes = method.toString().getBytes(StandardCharsets.US_ASCII);
		byte[] uriBytes = uri.getBytes(StandardCharsets.US_ASCII);
		byte[] versionBytes = version.getBytes(StandardCharsets.US_ASCII);
		//
		byte[] result = new byte[methodBytes.length + uriBytes.length + versionBytes.length + 4];
		System.arraycopy(methodBytes, 0, result, 0, methodBytes.length);
		int index = methodBytes.length;
		result[index++] = ' ';
		System.arraycopy(uriBytes, 0, result, index, uriBytes.length);
		index = index + uriBytes.length;
		result[index++] = ' ';
		System.arraycopy(versionBytes, 0, result, index, versionBytes.length);
		index = index + versionBytes.length;
		result[index++] = CR;
		result[index] = LF;
		return result;
	}
}
