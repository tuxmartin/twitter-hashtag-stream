package eu.vancl.martin.twitter.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class UDPClient {
	static final Logger log = Logger.getLogger(UDPClient.class); 	
	
	private String server = "localhost";
	private int port = 1234;
	
	public UDPClient() {
	}
		
	public UDPClient(String server, int port) {
		super();
		this.server = server;
		this.port = port;
	}


	public void send(String message) {
		DatagramSocket s;
		byte[] sendBuffer = new byte[1024];
		DatagramPacket sendPacket;
		InetAddress ADDRESS;
		final int PORT = port;
		
		try {
			ADDRESS = InetAddress.getByName(server);
			
			s = new DatagramSocket();
			log.debug("Odesilam data na server...");

			sendBuffer = message.getBytes();
			sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,
					ADDRESS, PORT);
			s.send(sendPacket);

		} catch (UnknownHostException e) {
			log.error("UnknownHostException " + server);
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	

}