package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;

class VideoThread extends Thread {

	DatagramSocket socket;
	private ArrayList<IpAddress> clients;

	byte[] outbuff = new byte[Server.BYTES_LENGTH];

	public VideoThread(DatagramSocket socket) throws Exception {
		clients = new ArrayList<IpAddress>();
		this.socket = socket;
		this.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				DatagramPacket reP = new DatagramPacket(outbuff, outbuff.length);
				socket.receive(reP);
				addNewClient(reP);
				sendToAllClients(outbuff, reP.getAddress().getHostAddress(), reP.getPort());

				Thread.sleep(15);
			} catch (Exception e) {

			}
		}
	}

	public void addNewClient(DatagramPacket packet) {
		if(clients.size() >= 4) return;
		// Look through client list
		boolean found = false;
		for (int i = 0; i < this.clients.size(); i++) {
			IpAddress client = this.clients.get(i);
			if (client.address.getHostAddress().equals(packet.getAddress().getHostAddress())
					&& client.port == packet.getPort()) {
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
		for (IpAddress client : this.clients) {
			if (!client.address.getHostAddress().equals(sentFromAddress) && client.port != sentFromPort) {
				DatagramPacket dp = new DatagramPacket(packetData, packetData.length, client.address, client.port);
				try {
					this.socket.send(dp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
