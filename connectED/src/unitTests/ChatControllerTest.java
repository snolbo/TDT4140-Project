package unitTests;

import java.io.IOException;
import java.net.Socket;

import communication.ChatController;
import communication.RecieveAndSend;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import junit.framework.*;

//Error: testAbleToType (nullpointerexception), testInitializeInteractionArea (ExceptionInitializeError), testSetDefaultURL and testSetURL(nullpointer..) fails
//TODO: find out the general form of junit tests, finish tests for the methods that are commented
public class ChatControllerTest extends TestCase{
	
	public ChatController chatController;
	
	public void setUp(){
		this.chatController = new ChatController();
	}
	
	public void testSetHelperHost(){
		setUp();
		chatController.setHelperHost(true);
		assertTrue(chatController.isHelperHost());
	}
	
	public void testSetAssistantHost(){
		setUp();
		chatController.setAssistantHost(true);
		assertTrue(chatController.isAssistantHost());
	}
	
	public void testHandleChatEnterKey(){
		setUp();
		//need to simulate key event
		//chatController.handleChatEnterKey(ENTER);
		//chatController.handleChatEnterKey(I);
		//TODO: find test objects for this method
	}
	
	
	public void testViewMessage(){
		setUp();
		//set up chat?
		//TODO: find test objects for this method
	}
	
	public void testSetRecieveAndSendConnection(){
		setUp();
		Socket socket = new Socket();
		RecieveAndSend recieveAndSend = new RecieveAndSend(socket, chatController);
		chatController.setRecieveAndSendConnection(recieveAndSend);
		assertEquals(recieveAndSend, chatController.getReceiveAndSendConnection());
	}
	
	
	public void testSetChatTab(){
		setUp();
		Tab chatTab = new Tab();
		chatController.setChatTab(chatTab);
		assertEquals(chatTab, chatController.getChatTab());
	}

	
}
