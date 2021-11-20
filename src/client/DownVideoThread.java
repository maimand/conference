package client;

import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;


public class DownVideoThread extends Thread {
	 byte[] receiveBytes = new byte[62000];
	 DatagramSocket datagramSocket;
	 
	 ArrayList<String> userAddresses = new ArrayList<>();

	 DatagramPacket dataReceive = new DatagramPacket(receiveBytes, receiveBytes.length);
	 BufferedImage bf;
	    
	    
	 public DownVideoThread() throws Exception {
		 datagramSocket = new DatagramSocket();
	 }

	 @Override
	 public void run() {
	    try {
	    		int index = -1;
	            while(true) {
	            	datagramSocket.receive(dataReceive);
					String restr = new String(dataReceive.getData()).substring(0, dataReceive.getLength());
					if(userAddresses.size() <= 3 && !userAddresses.contains(restr)) userAddresses.add(restr);
					index = userAddresses.indexOf(restr);
					if(index != -1) {
						//receive image
						datagramSocket.receive(dataReceive);
		                ClientUI.receiveVideo(receiveBytes, index);
					}
	            }
	        } catch (Exception e) {
	        	datagramSocket.close();
	            System.out.println("couldnt do it");
	        }
	    }
}