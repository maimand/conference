package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class ChatThread extends Thread {

	Socket soc;
	DataInputStream dis;
	DataOutputStream dos;

	public ChatThread(Socket soc) {
		this.soc = soc;
		try {
			this.dis = new DataInputStream(soc.getInputStream());
			this.dos = new DataOutputStream(soc.getOutputStream());
			System.out.println(soc.getInetAddress());
		} catch (Exception e) {

		}
		this.start();
	}

	public void run() {
		while (true) {
			try {
				System.out.println("here");
				String msg = dis.readUTF();
				for(ServerConnection connection : Server.serverConnections) {
					try {
						connection.chatThreadSocket.dos.writeUTF("" + '@' + soc.getInetAddress() + '>' + msg);
					} catch (Exception e1) {
						//ChatServer.clients.remove(c);???
					}
				}
			} catch (Exception e) {
					
			}
		}
	}
}