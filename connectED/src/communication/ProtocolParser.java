package communication;

import javafx.application.Platform;

/**
 * @author snorr
 * Handles all incoming messages from ReceiveAndSend, Based on the kayword in front, it handles the payload of the message
 */
public class ProtocolParser{
	
	
	private ChatController chatController;
	private String protocol;
	private String message;

	public ProtocolParser(ChatController chatController){
		this.chatController = chatController;
	}

	/**
	 * @param protocolMessage
	 * Receives a message sent from other party and handles the message based on the protocolword and payload
	 */
	public void handleMessageProtocol(String protocolMessage) {
		if (protocolMessage != null){
			splitUpString(protocolMessage);
			switch (protocol) {
			case "CHAT":
				this.chatController.viewMessage(this.message, false);
				break;
//			case "END":
//				break;
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
	
	/**
	 * @param protocolMessage
	 * splits incoming message into protocol keyword and message payload
	 */
	public void splitUpString(String protocolMessage){
		int protocolIndexEnd = protocolMessage.indexOf("-");
		protocol = protocolMessage.substring(0,protocolIndexEnd);
		message = protocolMessage.substring(protocolIndexEnd+1);
		System.out.println("receive protocolMessage: " + protocolMessage);
	}
	
	public String getProtocol(){
		return protocol;
	}
	
	public String getMessage(){
		return message;
	}
	
}