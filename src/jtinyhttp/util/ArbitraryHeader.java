package jtinyhttp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import jtinyhttp.core.HTTP;

public class ArbitraryHeader implements HTTP.Header {
	private final byte[] bytes;
	private final int keyStart;
	private final int keyLength;
	private final int valueStart;
	private final int valueLength;

	public ArbitraryHeader(String key, String value) {
		this(key.getBytes(StandardCharsets.US_ASCII),value.getBytes(StandardCharsets.US_ASCII));
	}

	public ArbitraryHeader(String key, byte[] value) {
		this(key.getBytes(StandardCharsets.US_ASCII),value);
	}

	public ArbitraryHeader(byte[] key, byte[] value) {
		this.bytes = new byte[key.length + 2 + value.length];
		this.keyStart = 0;
		this.keyLength = key.length;
		this.valueStart = key.length + 2;
		this.valueLength = value.length;
		System.arraycopy(key, 0, bytes, 0, key.length);
		System.arraycopy(value, 0, bytes, key.length + 2, value.length);
		bytes[key.length] = ':';
		bytes[key.length+1] = ' ';
	}

	public ArbitraryHeader(byte[] bytes, int start, int end) {
		this.bytes = bytes;
		for (int i = start; i < end; ++i) {
			if (bytes[i] == ':') {
				this.keyStart = start;
				this.keyLength = i - start;
				// SKIP WHITESPACE
				while (bytes[i] == ' ') {
					i = i + 1;
				}
				// DONE
				this.valueStart = i;
				this.valueLength = end - valueStart;
				return;
			}
		}
		throw new IllegalArgumentException("invalid header");
	}

	public ArbitraryHeader(byte[] bytes, int keyStart, int keyLength, int valueStart, int valueLength) {
		this.bytes = bytes;
		this.keyStart = keyStart;
		this.keyLength = keyLength;
		this.valueStart = valueStart;
		this.valueLength = valueLength;
	}

	@Override
	public String getKey() {
		return new String(bytes, keyStart, keyLength, StandardCharsets.US_ASCII);
	}

	@Override
	public String getValue() {
		return new String(bytes, valueStart, valueLength, StandardCharsets.US_ASCII);
	}

	@Override
	public void writeHeaderLine(OutputStream output) throws IOException {
		int length = (valueStart - keyStart) + valueLength;
		output.write(bytes, keyStart, length);
		output.write(13); // CR
		output.write(10); // LF
	}

	@Override
	public String toString() {
		return new String(bytes, keyStart, (valueStart - keyStart) + valueLength, StandardCharsets.US_ASCII);
	}
}
