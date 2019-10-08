package jtinyhttp.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import jtinyhttp.core.HTTP;
import jtinyhttp.core.HTTP.Entity;
import jtinyhttp.core.HTTP.Header;

public abstract class AbstractMessage implements HTTP.Message {
	/**
	 * Raw bytes of the header.
	 */
	protected byte[] header;
	/**
	 * Number of bytes in which are in use.
	 */
	protected int numBytes;
	/**
	 * End indices of header lines, where 0 is start line.
	 */
	protected int[] lines;
	/**
	 * Counts the number of active headers
	 */
	protected int numLines;
	/**
	 * The message body (which may be <code>null</code>.
	 */
	protected final Entity body;

	public AbstractMessage(byte[] header, Entity body) {
		this.header = header;
		this.numLines = countLines(header);
		this.lines = new int[numLines];
		//
		int index = 0;
		for(int i=0;i!=lines.length;++i) {
			index = skipLine(index,header);
			lines[i] = index;
		}
		this.numBytes = index;
		this.body = body;
	}

	@Override
	public int size() {
		return numLines - 1;
	}

	@Override
	public Header getHeader(int i) {
		return new ArbitraryHeader(header, lines[i], lines[i + 1] - 2);
	}

	public void addHeader(String key, String value) {
		byte[] keyBytes = key.getBytes(StandardCharsets.US_ASCII);
		byte[] valueBytes = value.getBytes(StandardCharsets.US_ASCII);
		final int keyLen = keyBytes.length;
		final int valueLen = valueBytes.length;
		final int keyStart = numBytes;
		final int valueStart = keyStart + 2 + keyLen;
		final int crlfStart = valueStart + valueLen;
		// Add space for a line of given length
		add(keyLen + valueLen + 4);
		System.arraycopy(keyBytes, 0, header, keyStart, keyBytes.length);
		System.arraycopy(valueBytes, 0, header, valueStart, valueBytes.length);
		//
		header[keyStart + keyLen] = ':';
		header[keyStart + keyLen + 1] = ' ';
		header[crlfStart] = CR;
		header[crlfStart + 1] = LF;
	}

	@Override
	public Entity getBody() {
		return body;
	}

	/**
	 * Add a given line of a given length to the end of this message.
	 *
	 * @param length
	 */
	private void add(int length) {
		resize(length);
		if (numLines == lines.length) {
			lines = Arrays.copyOf(lines, numLines * 2);
		}
		numBytes += length;
		lines[numLines++] = numBytes;
	}

	/**
	 * Resize the byte array underlying this message to have at least a given number
	 * of bytes,
	 *
	 * @param length
	 */
	private void resize(int length) {
		int l = header.length;
		if (l > length) {
			while (l < length) {
				l = l << 1;
			}
			this.header = Arrays.copyOf(header, l);
		}
		this.numBytes = length;
	}

	private static int countLines(byte[] header) {
		int count = 0;
		int index = 0;
		while (index < header.length) {
			final int last = index;
			index = skipLine(index, header);
			// Check for empty line
			if (index == (last + 2)) {
				break;
			}
			count = count + 1;
		}
		return count;
	}

	private static int skipLine(int i, byte[] header) {
		while(i < (header.length+1)) {
			if (header[i] == CR && header[i + 1] == LF) {
				return i + 2;
			}
			i = i + 1;
		}
		throw new IllegalArgumentException("invalid header: missing line end");
	}

	protected static byte CR = 13;
	protected static byte LF = 20;
}
