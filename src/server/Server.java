package server;

import java.awt.print.Printable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


	public static ArrayList<ServerConnection> serverConnections;
	static final int CHAT_SOCKET_PORT = 9789;
	static final int VIDEO_SOCKET_PORT = 9879;
	static final int VOICE_SOCKET_PORT = 1234;
	static final int BYTES_LENGTH = 62000;

	byte[] outbuff = new byte[Server.BYTES_LENGTH];
	DatagramPacket videoPacket;
	
	public static void main(String[] args) throws Exception
	{
		new Server();
	}

	public Server() throws Exception {
		ServerSocket chatServerSocket = new ServerSocket(CHAT_SOCKET_PORT);
		DatagramSocket videoSerSocket = new DatagramSocket(VIDEO_SOCKET_PORT);
//		DatagramSocket voiceSerSocket = new DatagramSocket(VOICE_SOCKET_PORT);
		
		serverConnections = new ArrayList<ServerConnection>();
		
		byte[] buffer = new byte[BYTES_LENGTH];
		videoPacket = new DatagramPacket(buffer, buffer.length);
		System.out.println("Server open");


		while (true) {
			
			try {
				Socket soc = chatServerSocket.accept();
				System.out.println(soc.getPort());
				ChatThread chatThread = new ChatThread(soc);
				
				videoSerSocket.receive(videoPacket);
				IpAddress ipAddress = new IpAddress(videoPacket.getAddress(), videoPacket.getPort());
				System.out.println(ipAddress.toString());
//				VideoThread videoThread = new VideoThread(videoSerSocket, ipAddress);
				
//				serverConnections.add(new ServerConnection(chatThread, videoThread));
//				DatagramPacket reP = new DatagramPacket(outbuff, outbuff.length);
//				System.out.println(reP.getPort());
//				videoSerSocket.receive(reP);
//				
//				for(ServerConnection connection : Server.serverConnections) {
//					DatagramPacket dp = new DatagramPacket(outbuff, outbuff.length, connection.videoThreadSocket.ipAddress.address,
//							connection.videoThreadSocket.ipAddress.port);
//					videoSerSocket.send(dp);
//				}
				Thread.sleep(15);
			} catch (Exception e) {

			}
			
		}
	}
}


