package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import config.Config;

class VideoThread extends Thread {

	DatagramSocket socket;
	public static ArrayList<IpAddress> clients;

	byte[] outbuff = new byte[Server.BYTES_LENGTH];
	

	public VideoThread() {

		try {
			clients = new ArrayList<IpAddress>();
			this.socket = new DatagramSocket(Config.portUDPVideo, InetAddress.getByName("localhost"));
			this.start();
		} catch (SocketException | UnknownHostException e) {
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
				this.socket.receive(reP);
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
			if (client.address.getHostAddress().equals(packet.getAddress().getHostAddress())) {
				found = true;
			}
		}
		// Add to list if it doesn't exist
		if (!found) {
			System.out.println("New video connected" + packet.getAddress() + " Port: " + packet.getPort());
			clients.add(new IpAddress(packet.getAddress(), packet.getPort()));
		}

	}

	// todo: send to all other client the packetData
	public void sendToAllClients(byte[] packetData, String sentFromAddress, int sentFromPort) {
		for (IpAddress client : clients) {
			DatagramPacket address;
			DatagramPacket message;
			try {
				address = new DatagramPacket(sentFromAddress.getBytes(), sentFromAddress.length(), client.address,
						client.port);
				this.socket.send(address);

				message = new DatagramPacket(packetData, packetData.length, client.address, client.port);
//				if (!client.address.getHostAddress().equals(sentFromAddress) && client.port != sentFromPort)
				this.socket.send(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
			}

		}
	}

}
