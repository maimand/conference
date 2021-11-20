package server;

import java.awt.print.Printable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	//todo: this will hold connections for chat thread, send message to those connections
	
	public final int CHAT_SOCKET_PORT = 9789;
	public final int VIDEO_SOCKET_PORT = 9879;
	public final int VOICE_SOCKET_PORT = 1234;
	public static final int BYTES_LENGTH = 62000;
	
	private ServerSocket chatServerSocket;
	private DatagramSocket videoServerSocket;
	private DatagramSocket voiceServerSocket;
	byte[] outbuff = new byte[BYTES_LENGTH];
	DatagramPacket videoPacket;
	
	public static void main(String[] args) throws Exception
	{
		new Server();
	}

	public Server() {
		try {
			this.chatServerSocket = new ServerSocket(CHAT_SOCKET_PORT);
			this.videoServerSocket = new DatagramSocket(VIDEO_SOCKET_PORT);
			this.voiceServerSocket = new DatagramSocket(VOICE_SOCKET_PORT);
			
			new ChatThread(this.chatServerSocket);
			new VideoThread(this.videoServerSocket);
			new VoiceThread(this.voiceServerSocket);
			
			System.out.println("Server open");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Server is down");
		}
		

	}
	

}


