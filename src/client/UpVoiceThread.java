package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import config.Config;

public class UpVoiceThread extends Thread {

	private TargetDataLine targetDataLine;
	private DatagramSocket connection;
	byte[] data = new byte[62000];
	String serverAddressString;
	boolean isConnecting = true;

	public UpVoiceThread(String address , DatagramSocket connection) throws SocketException {
		this.connection = connection;
		this.serverAddressString = address;
	}
	
	@Override
	public void run() {
		sendAudio();
	}
	
	private void sendAudio() {
		try {

			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(getAudioFormat());
			targetDataLine.start();

			// Create buffer to store received bytes

			while (true) {
				
				targetDataLine.read(data, 0, data.length);
				// Build packet to send to server
				DatagramPacket send_packet = new DatagramPacket(data, data.length, InetAddress.getByName(serverAddressString),
						Config.portUDPAudio);

				// Send to server
				this.connection.send(send_packet);
			}
		} catch (IOException | LineUnavailableException e) {
			System.out.println(e);
		}
	}

	private AudioFormat getAudioFormat() {

		float sampleRate = 44100.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
	}
}
