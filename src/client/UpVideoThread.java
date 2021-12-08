package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import config.Config;

public class UpVideoThread extends Thread {
	Webcam webcam;
	String serverAddressString;
	BufferedImage bf;
	DatagramSocket datagramSocket;

	public UpVideoThread(String serverAddress, DatagramSocket datagramSocket) throws Exception {
		this.serverAddressString = serverAddress;
		this.datagramSocket = datagramSocket;
		webcam = Webcam.getDefault();
		webcam.open(true);
		this.start();
	}
	
	void disconnect() {
		webcam.close();
		this.datagramSocket.close();
	}

	@Override
	public void run() {
		try {
			while (true) {
				// send image
				BufferedImage image = webcam.getImage();
				ClientUI.addSelfVideo(image);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				ImageIO.write(image, "jpg", baos);

				byte[] sendBytes = baos.toByteArray();
				DatagramPacket imageBytes = new DatagramPacket(sendBytes, sendBytes.length,
						InetAddress.getByName(serverAddressString), Config.portUDPVideo);
				this.datagramSocket.send(imageBytes);
				baos.flush();

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println("couldnt do it");
		}
	}
}
