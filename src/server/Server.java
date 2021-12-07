package server;

import java.awt.print.Printable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import config.Config;

public class Server {

	//todo: this will hold connections for chat thread, send message to those connections
	
	public static final int BYTES_LENGTH = 62000;
	
	private ServerSocket chatServerSocket;
	private DatagramSocket videoServerSocket;
	private DatagramSocket voiceServerSocket;
	byte[] outbuff = new byte[BYTES_LENGTH];
	DatagramPacket videoPacket;
	
	public static ArrayList<IpAddress> clients;
	
	public static void main(String[] args) throws Exception
	{
		new Server();
	}

	public Server() {
		try {
			
			new ChatThread();
			new VideoThread();
			new VoiceThread();
			
			System.out.println("Server open");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Server is down");
		}
		

	}
	

}


