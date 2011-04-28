package net.quadratum.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class NetworkClient implements Closeable {
	
	/** Socket that this NetworkClient is using. */
	Socket _sock;
	/** Writer that sends messages through sock. */
	BufferedWriter _out;
	/** Reader that gets messages from sock. */
	BufferedReader _in;
	
	public NetworkClient(Socket sock) {
		_sock = sock;
		try {
			_out = new BufferedWriter(new OutputStreamWriter(_sock.getOutputStream()));
			_in = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the given string out of this NetworkClient.
	 * @param s the message to write.
	 */
	protected void write(String s) {
		try {
			_out.write(s);
			_out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes the given string.
	 * @param s the message to process.
	 */
	protected abstract void process(String s);
	
	/**
	 * Finds whether or not this NetworkClient is done reading.
	 * @return true if the client is done, false otherwise.
	 */
	protected abstract boolean doneReading();
	
	@Override
	public void close() {
		try {
			_out.close();
			_in.close();
			_sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class ReadThread extends Thread {
		
		/**
		 * Runs this NetworkClient's read loop.
		 */
		public void run() {
			String line;
			try {
				while (!doneReading() && (line = _in.readLine()) != null) {
					new ProcessThread(line).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			close();
		}
	}
	
	class ProcessThread extends Thread {
		
		/** The string that this thread must process. */
		String _toProcess;
		
		ProcessThread(String toProcess) {
			_toProcess = toProcess;
		}
		
		/**
		 * Runs this NetworkClient's process loop.
		 */
		public void run() {
			process(_toProcess);
		}
	}
}
