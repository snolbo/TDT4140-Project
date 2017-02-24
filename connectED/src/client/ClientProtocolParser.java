package client;


// this class should handle incoming messages and execute functions based on the protocoll keyword in the start of the message
public class ClientProtocolParser{
	
	
	private ClientController chatTabController;

	public ClientProtocolParser(ClientController chatTabController){
		this.chatTabController = chatTabController;
	}

	public void handleMessageProtocoll(String protocolMessage) {
		String[] splittedString = protocolMessage.split("-");
		String protocol = splittedString[0];
		String message = splittedString[1];
		
		switch (protocol) {
		case "CHAT":
			// do stuff
			this.chatTabController.viewMessage(message, false);
			break;
		case "END":
			// do stuff
			break;
		case "EDITCODE":
			// do stuff
			break;
			
			/// etc....
				
		default:
			// not recognized protocoll, 
			System.out.println("Unknown protocoll");
			break;
		}
		
		// TODO Auto-generated method stub
		
	}
	
	
}