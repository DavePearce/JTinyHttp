package jtinyhttp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import jtinyhttp.core.HTTP;
import jtinyhttp.core.HTTP.Method;

public abstract class AbstractRequest extends AbstractMessage implements HTTP.Request {

	public AbstractRequest(Method method, String uri, String version, HTTP.Entity body) {
		super(toRequestLine(method,uri,version), body);
	}

	@Override
	public String getVersion() {
		throw new UnsupportedOperationException("implement me");
	}

	@Override
	public void write(OutputStream out) throws IOException {
		out.write(header, 0, numBytes);
		out.write(13); // CR
		out.write(10); // LF
		if(body != null) {
			body.write(out);
		}
	}

	private static byte[] toRequestLine(Method method, String uri, String version) {
		byte[] methodBytes = method.toString().getBytes(StandardCharsets.US_ASCII);
		byte[] uriBytes = uri.getBytes(StandardCharsets.US_ASCII);
		byte[] versionBytes = version.getBytes(StandardCharsets.US_ASCII);
		//
		byte[] result = new byte[methodBytes.length + uriBytes.length + versionBytes.length + 4];
		int index = methodBytes.length;
		System.arraycopy(methodBytes, 0, result, 0, methodBytes.length);
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
