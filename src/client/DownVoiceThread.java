package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DownVoiceThread extends Thread {
	private DatagramSocket connection;
	private AudioPlayer audioPlayer;
	byte[] receiveData = new byte[62000];

	public DownVoiceThread(DatagramSocket connection) {
		this.connection = connection;
		this.audioPlayer = new AudioPlayer();
		this.start();
	}
	
	void disconnect() {
		this.connection.close();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				// Packet for receiving data
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

				// Receive data
				connection.receive(receivePacket);

				// Run ProcessReceived worker to handle new data
				audioPlayer.playAudio(receivePacket.getData());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}