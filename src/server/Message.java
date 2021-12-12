package server;

public class Message {
	String userNameString;
	String messageString;
	String addressString;
	
	public Message(String userNameString, String messageString, String addressString) {
		this.userNameString = userNameString;
		this.messageString = messageString;
		this.addressString = addressString;
	}
}
