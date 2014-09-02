package eu.vancl.martin.twitter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import eu.vancl.martin.twitter.start.Start;

public class TcpClient {
	static final Logger log = Logger.getLogger(TcpClient.class);

	Start instance;

	private String host;
	private int port;

	private InetAddress address;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public TcpClient(Start instance) {
		this.instance = instance;
		host = "localhost";
		port = 7777;
	}

	public TcpClient(String host, int port, Start instance) {
		this.host = host;
		this.port = port;
		this.instance = instance;
	}

	public void connect() {
		try {
			address = Inet4Address.getByName(host);
			this.socket = new Socket(address, port);
			this.in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream());
			(new Thread(new TcpListeningThread())).start();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(String message) {
		synchronized (this) {
			if (!this.socket.isConnected()) {
				log.error("No connection with " + host + ":" + port + "!");
				return;
			}
			this.out.println(message);
			this.out.flush();
			try {
				Thread.sleep(10); // 10ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class TcpListeningThread implements Runnable {

		public TcpListeningThread() {
		}

		@Override
		public void run() {
			try {
				while (true) {
					String inputLine = in.readLine();
					System.out.println(inputLine);
					instance.tcpReceive(inputLine);
				}
			} catch (IOException e) {
				log.error("Connection problem! Closed socket?");
			}
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Start getInstance() {
		return instance;
	}

	public void setInstance(Start instance) {
		this.instance = instance;
	}

}