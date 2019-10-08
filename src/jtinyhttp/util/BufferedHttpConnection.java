package jtinyhttp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import jtinyhttp.core.HTTP;
import jtinyhttp.core.HTTP.Request;
import jtinyhttp.core.HTTP.Response;

public abstract class BufferedHttpConnection implements HTTP.Connection {
	protected final Socket socket;
	protected final byte[] buffer;

	public BufferedHttpConnection(Socket socket, int size) {
		this.socket = socket;
		this.buffer = new byte[size];
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	public class Client extends BufferedHttpConnection implements HTTP.ClientConnection {

		public Client(Socket socket, int size) {
			super(socket, size);
		}

		@Override
		public Response send(Request message) throws IOException {
			// Write request to server
			message.write(socket.getOutputStream());
			// Read response from server
			InputStream in = socket.getInputStream();
			byte[] data = read(buffer,in);
			// Parse data and return response
			return null;
		}
	}

	/**
	 *
	 * @param input
	 * @return
	 * @throws IOException
	 */
	private static byte[] read(byte[] buffer, InputStream input) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		int length;
		while ((length = input.read(buffer)) != -1) {
		    result.write(buffer, 0, length);
		}
		return result.toByteArray();
	}
}
