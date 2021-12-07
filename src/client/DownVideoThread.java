package client;

import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class DownVideoThread extends Thread {
	DatagramSocket datagramSocket;
	public static ArrayList<String> userAddresses = new ArrayList<>();
	byte[] receiveBytes = new byte[62000];
	DatagramPacket dataReceive = new DatagramPacket(receiveBytes, receiveBytes.length);
	BufferedImage bf;

	public DownVideoThread(DatagramSocket datagramSocket) throws Exception {
		this.datagramSocket = datagramSocket;
	}

	public static void removeUser(String address) {
		userAddresses.remove(address);
		ClientUI.setBlack(4 - userAddresses.size());
	}

	@Override
	public void run() {
		try {
			int index = -1;
			while (true) {
				datagramSocket.receive(dataReceive);
				String address = new String(dataReceive.getData()).substring(0, dataReceive.getLength());
				if (userAddresses.size() <= 3 && !userAddresses.contains(address))
					userAddresses.add(address);
				index = userAddresses.indexOf(address);
				// receive image
				datagramSocket.receive(dataReceive);
				if (index != -1) {
					ClientUI.receiveVideo(receiveBytes, index);
				}

			}
		} catch (Exception e) {
			datagramSocket.close();
			System.out.println("couldnt do it");
		}
	}
}