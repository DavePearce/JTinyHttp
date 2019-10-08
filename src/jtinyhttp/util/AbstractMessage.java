package jtinyhttp.util;

import java.io.IOException;
import java.io.OutputStream;
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

	@Override
	public abstract HTTP.Message addHeader(byte[] key, String value);

	@Override
	public Entity getBody() {
		return body;
	}

	@Override
	public void write(OutputStream out) throws IOException {
		out.write(header, 0, numBytes);
	}

	@Override
	public void writeln(OutputStream out) throws IOException {
		this.write(out);
		out.write(13); // CR
		out.write(10); // LF
	}

	@Override
	public String toString() {
		return toHttpString(header, numBytes);
	}

	protected static abstract class HeaderMessage<T extends HTTP.Message> implements HTTP.Message {
		protected final T parent;
		protected final byte[] line;

		public HeaderMessage(T parent, byte[] key, String value) {
			this(parent,toHeaderLine(key,value));
		}
		public HeaderMessage(T parent, byte[] bytes) {
			this.parent = parent;
			this.line = bytes;
		}

		@Override
		public void writeln(OutputStream out) throws IOException {
			this.write(out);
			out.write(13); // CR
			out.write(10); // LF
		}

		@Override
		public String toString() {
			String r = parent.toString();
			return r + toHttpString(line, line.length);
		}
	}

	protected static String toHttpString(byte[] bytes, int len) {
		String r = "";
		for(int i=0;i!=len;++i) {
			byte b =  bytes[i];
			switch(b) {
				case CR:
					r += "<CR>";
					break;
				case LF:
					r += "<LF>";
					break;
				default:
					r += Character.toString((char) bytes[i]);
			}

		}
		return r;
	}

	private static byte[] toHeaderLine(byte[] keyBytes, String value) {
		byte[] valueBytes = value.getBytes(StandardCharsets.US_ASCII);
		byte[] line = new byte[keyBytes.length + valueBytes.length + 2];
		int index = 0;
		System.arraycopy(keyBytes, 0, line, index, keyBytes.length);
		index = index + keyBytes.length;
		System.arraycopy(valueBytes, 0, line, index, valueBytes.length);
		index = index + valueBytes.length;
		line[index++] = CR;
		line[index] = LF;
		return line;
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


	protected static final byte CR = 13;
	protected static final byte LF = 10;
}
