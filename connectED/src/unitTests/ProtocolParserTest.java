package unitTests;

import communication.ChatController;
import communication.ProtocolParser;
import junit.framework.*;

//TODO: find out the general form of junit tests, finish tests for the methods that are commented
public class ProtocolParserTest extends TestCase {
	
	public ProtocolParser protocolParser;
	public ChatController chatController;
	
	public void setUp(){
		this.chatController = new ChatController();
		this.protocolParser = new ProtocolParser(chatController);
	}
	
	public void testHandleMessageProtocol(){
		setUp();
		//need to initiate things
		protocolParser.handleMessageProtocol("CHAT-hei");
		//TODO: find test objects for this method
		
		//need to initiate things
		protocolParser.handleMessageProtocol("CODEURL-http://connected-1e044.firebaseapp.com");
		//TODO: find test objects for this method
	}
}
