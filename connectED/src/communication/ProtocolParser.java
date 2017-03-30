package communication;

import javafx.application.Platform;

// this class should handle incoming messages and execute functions based on the protocoll keyword in the start of the message
public class ProtocolParser{
	
	
	private ChatController chatController;

	public ProtocolParser(ChatController chatController){
		this.chatController = chatController;
	}

	public void handleMessageProtocoll(String protocolMessage) {
		if(protocolMessage != null){
			int protocolIndexEnd = protocolMessage.indexOf("-");
			String protocol = protocolMessage.substring(0,protocolIndexEnd);
			String message = protocolMessage.substring(protocolIndexEnd+1);
			System.out.println("receive protocolMessage: " + protocolMessage);
			
			switch (protocol) {
			case "CHAT":
				// do stuff
				this.chatController.viewMessage(message, false);
				break;
			case "END":
				// do stuff
				break;
			case "CODEURL":
				Platform.runLater(() ->{
					chatController.setCodeUrl(message);
					System.out.println("URL received and set");
	
	
				});
	//			Platform.runLater(() ->{
	//				chatController.reloadCodeEditor();
	//			});
				
				
				break;
				
				/// etc.... this is a framework for adding more utillities
					
			default:
				// not recognized protocoll, 
				System.out.println("Unknown protocoll");
				break;
			}
		}
		
	
		
	}
	
	
}