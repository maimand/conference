package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ChatThread extends Thread {
	
	Socket socket;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    public ChatThread(Socket socket) {
    	this.socket = socket;
    	try {
        	inputStream = new DataInputStream(socket.getInputStream());
    		outputStream = new DataOutputStream(socket.getOutputStream());
    		this.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
        start();
    }
    
    public void send(String username, String message) {
    	try {
    		outputStream.writeUTF(username);
			outputStream.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    @Override
    public synchronized void start() {
        String username;
        String message;
        try {
            while (true) {
            	username = inputStream.readUTF();
            	message = inputStream.readUTF();
            	ClientUI.receive(username, message);
            }
        } catch (Exception e) {
        }
    }

}