package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.github.sarxos.webcam.Webcam;

public class UpVideoThread extends Thread {
	 byte[] receiveBytes = new byte[62000];
	 byte[] sendBytes = new byte[62000];
	 Webcam webcam;
	 ArrayList<String> userAddresses = new ArrayList<>();

	    
	 DatagramPacket dataReceive = new DatagramPacket(receiveBytes, receiveBytes.length);
	 BufferedImage bf;
	    
	    
	    
	 public UpVideoThread() throws Exception {
	    webcam = Webcam.getDefault();
	    webcam.open(true);
	 }

	 @Override
	 public void run() {
	    try {
	    		 int index = -1;
	        	 DatagramSocket ds = new DatagramSocket();
	        	 
				
	            while(true) {
	            	System.out.println("here");
	            	String str = InetAddress.getByName("localhost").getHostAddress();
		        	 DatagramPacket seP = new DatagramPacket(str.getBytes(), str.length(),
								InetAddress.getByName("localhost"), 9879);
					ds.send(seP);
	            	
	            	// send image
	            	BufferedImage image = webcam.getImage();
	            	ClientUI.addSelfVideo(image);
	            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					ImageIO.write(image, "jpg", baos);
					
					sendBytes = baos.toByteArray();
					DatagramPacket imageBytes = new DatagramPacket(sendBytes, sendBytes.length,
							InetAddress.getByName("localhost"), 9879);
					ds.send(imageBytes);
					baos.flush();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
//					
//					// receive address
//					ds.receive(dataReceive);
//					String restr = new String(dataReceive.getData()).substring(0, dataReceive.getLength());
//					if(userAddresses.size() < 5 && !userAddresses.contains(restr)) userAddresses.add(restr);
//					index = userAddresses.indexOf(restr);
//					if(index != -1) {
//						//receive image
//						ds.receive(dataReceive);
//		                ClientUI.receiveVideo(receiveBytes, index);
//					}
					
	            }
	        } catch (Exception e) {
	        	System.out.println(e.toString());
	            System.out.println("couldnt do it");
	        }
	    }
}



