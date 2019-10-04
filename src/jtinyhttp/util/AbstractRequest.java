package jtinyhttp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import jtinyhttp.core.HTTP;

public abstract class AbstractRequest extends AbstractMessage implements HTTP.Request {
	private final String uri;

	public AbstractRequest(String uri, String version, HTTP.Entity body) {
		super(version, body);
		this.uri = uri;
	}

	@Override
	public void write(OutputStream out) throws IOException {
		writeRequestLine(out);
		for(int i=0;i!=headers.size();++i) {
			headers.get(i).writeHeaderLine(out);
		}
		out.write(13); // CR
		out.write(10); // LF
		if(body != null) {
			body.write(out);
		}
	}

	@Override
	public void writeRequestLine(OutputStream out) throws IOException {
		// FIXME: this could be made slightly more efficient
		out.write(getMethod().toString().getBytes(StandardCharsets.US_ASCII));
		out.write(' ');
		out.write(uri.getBytes(StandardCharsets.US_ASCII));
		out.write(' ');
		out.write(version.getBytes(StandardCharsets.US_ASCII));
		out.write(13); // CR
		out.write(10); // LF
	}
}
