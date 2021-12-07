package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

import config.Config;

class ChatThread extends Thread {
	
	Socket socket;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    public ChatThread(String serverAddress) {
    	
    	try {
    		this.socket = new Socket(serverAddress, Config.portTCPMessage);
        	inputStream = new DataInputStream(socket.getInputStream());
    		outputStream = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
    }

	public void send(String username, String message) {
    	try {
    		outputStream.writeUTF(username);
			outputStream.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void disConnect(String username) {
		try {
			outputStream.writeUTF(username);
			outputStream.writeUTF("disconnect");
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    @Override
    public void run() {
        String username;
        String message;
        try {
            while (true) {
            	username = inputStream.readUTF();
            	message = inputStream.readUTF();
            	ClientUI.receive(username, message);
            	if(message.equals("disconnect")) {
            		String removedAddressString = inputStream.readUTF();
            		DownVideoThread.removeUser(removedAddressString);
            	}
            		
            }
        } catch (Exception e) {
        }
    }

}