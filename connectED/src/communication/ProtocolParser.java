package communication;

import javafx.application.Platform;

// this class should handle incoming messages and execute functions based on the protocoll keyword in the start of the message
public class ProtocolParser{
	
	
	private ChatController chatController;
	private String protocol;
	private String message;

	public ProtocolParser(ChatController chatController){
		this.chatController = chatController;
	}


	public void handleMessageProtocol(String protocolMessage) {
		if (protocolMessage != null){
			splitUpString(protocolMessage);
			
			switch (this.protocol) {
			case "CHAT":
				// do stuff
				this.chatController.viewMessage(this.message, false);
				break;
			case "END":
				// do stuff
				break;
			case "CODEURL":
				Platform.runLater(() ->{
					chatController.setCodeUrl(this.message);
					System.out.println("URL received and set");
				});
				break;
			case "VOICE":
				if(message.equals("request"))
					chatController.acceptVoiceCommunication();
				else if(message.equals("accept"))
					chatController.handleAcceptedVoiceRequest();
				else if(message.equals("cancel")){
					chatController.cancelVoiceCommunication();
				}
				else if(message.equals("deny"))
					chatController.handleDeniedVoiceRequest();
				break;

			default:
				System.out.println("Unknown protocoll");
				break;
			}
		}
	}
	
	public void splitUpString(String protocolMessage){
		int protocolIndexEnd = protocolMessage.indexOf("-");
		this.protocol = protocolMessage.substring(0,protocolIndexEnd);
		this.message = protocolMessage.substring(protocolIndexEnd+1);
		System.out.println("receive protocolMessage: " + protocolMessage);
	}
	
	public String getProtocol(){
		return this.protocol;
	}
	
	public String getMessage(){
		return this.message;
	}
	
}