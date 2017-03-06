package communication;

// this class should handle incoming messages and execute functions based on the protocoll keyword in the start of the message
public class ProtocolParser{
	
	
	private ChatController chatController;

	public ProtocolParser(ChatController chatController){
		this.chatController = chatController;
	}

	public void handleMessageProtocoll(String protocolMessage) {
		int protocolIndexEnd = protocolMessage.indexOf("-");
		String protocol = protocolMessage.substring(0,protocolIndexEnd);
		String message = protocolMessage.substring(protocolIndexEnd+1);
		
		switch (protocol) {
		case "CHAT":
			// do stuff
			this.chatController.viewMessage(message, false);
			break;
		case "END":
			// do stuff
			break;
		case "EDITCODE":
			// do stuff
			break;
			
			/// etc.... this is a framework for adding more utillities
				
		default:
			// not recognized protocoll, 
			System.out.println("Unknown protocoll");
			break;
		}
		
	
		
	}
	
	
}