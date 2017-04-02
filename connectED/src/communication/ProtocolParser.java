package communication;

import javafx.application.Platform;

// this class should handle incoming messages and execute functions based on the protocoll keyword in the start of the message
public class ProtocolParser{
	
	
	private ChatController chatController;

	public ProtocolParser(ChatController chatController){
		this.chatController = chatController;
	}

	public void handleMessageProtocol(String protocolMessage) {
		int protocolIndexEnd = protocolMessage.indexOf("-");
		String protocol = protocolMessage.substring(0,protocolIndexEnd);
		String message = protocolMessage.substring(protocolIndexEnd+1);
		System.out.println("receive message");
		System.out.println(message);
		
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
				System.out.println("url set");
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				chatController.reloadCodeEditor();
				System.out.println("page reloaded");


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