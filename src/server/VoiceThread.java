package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import config.Config;

public class VoiceThread extends Thread {

	DatagramSocket socket;
	public static ArrayList<IpAddress> clients;

	public VoiceThread() {
		try {
			clients = new ArrayList<IpAddress>();
			this.socket = new DatagramSocket(Config.portUDPAudio);
			this.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] outbuff = new byte[Server.BYTES_LENGTH];
				DatagramPacket reP = new DatagramPacket(outbuff, outbuff.length);
				socket.receive(reP);
				addNewClient(reP);
				sendToAllClients(reP.getData(), reP.getAddress().getHostAddress(), reP.getPort());

				Thread.sleep(15);
			} catch (Exception e) {

			}
		}
	}

	public void addNewClient(DatagramPacket packet) {
		if (clients.size() >= 4)
			return;
		// Look through client list
		boolean found = false;
		for (int i = 0; i < clients.size(); i++) {
			IpAddress client = clients.get(i);
			if (client.address.getHostAddress().equals(packet.getAddress().getHostAddress())
					&& client.port == packet.getPort()) {
				found = true;
			}
		}
		// Add to list if it doesn't exist
		if (!found) {
			System.out.println("New voice client connected" + packet.getAddress() + " Port: " + packet.getPort());
			clients.add(new IpAddress(packet.getAddress(), packet.getPort()));
		}
	}

	public void sendToAllClients(byte[] packetData, String sentFromAddress, int sentFromPort) {
		for (IpAddress client : clients) {
//			if (!client.address.getHostAddress().equals(sentFromAddress) && client.port != sentFromPort) {
				DatagramPacket dp = new DatagramPacket(packetData, packetData.length, client.address, client.port);
				try {
					this.socket.send(dp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//			}

		}
	}
}
