package jtinyhttp.util;

import java.util.ArrayList;

import jtinyhttp.core.HTTP;
import jtinyhttp.core.HTTP.Entity;
import jtinyhttp.core.HTTP.Header;

public abstract class AbstractMessage implements HTTP.Message {
	protected final String version;
	protected final ArrayList<HTTP.Header> headers = new ArrayList<>();
	protected final Entity body;

	public AbstractMessage(String version, Entity body) {
		this.version = version;
		this.body = body;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public int size() {
		return headers.size();
	}

	@Override
	public Header getHeader(int i) {
		return headers.get(i);
	}

	public void addHeader(Header header) {
		headers.add(header);
	}

	@Override
	public Entity getBody() {
		return body;
	}
}
