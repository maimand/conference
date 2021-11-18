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
		} catch (Exception e) {
			// TODO: handle exception
		}
        start();
    }
    
    public void send(String message) {
    	try {
			outputStream.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void run() {
        String message;
        try {
            while (true) {
            	message = inputStream.readUTF();
            	ClientUI.receive(message);
            }
        } catch (Exception e) {
        }
    }
}