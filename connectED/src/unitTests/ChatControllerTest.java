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
	
	public void testOnClosed(){
		setUp();
		String tag = null;
		chatController.onClosed(tag);
		//need to set ReceiveAndSend and already set tags
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
	
	public void testAbleToType() {
		setUp();
		//need to simulate user text
		chatController.ableToType(true);
		assertTrue(chatController.getTextArea().isEditable());
	}
	
	public void testSetChatTab(){
		setUp();
		Tab chatTab = new Tab();
		chatController.setChatTab(chatTab);
		assertEquals(chatTab, chatController.getChatTab());
	}
	
	public void testInitializeInteractionArea() throws IOException{
		setUp();
		chatController.setAssistantHost(true);
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("/mainWindow/InteractionTabManager.fxml"));
		Node InteractionArea  = loader.load();
		chatController.initializeInteractionArea();
		assertEquals(InteractionArea, chatController.getInteractionArea());
		//assertEquals for default URL
		
		chatController.setHelperHost(false);
		chatController.setAssistantHost(true);
		chatController.initializeInteractionArea();
		assertEquals(InteractionArea, chatController.getInteractionArea());
		//assertEquals for URL: "http://connected-1e044.firebaseapp.com"
		
	}
	
	public void testSetDefaultURL(){
		setUp();
		chatController.setDefaultURL();
		assertEquals("http://www.lutanho.net/play/tetris.html", chatController.getCodeURL());
	}
	
	public void testSetCodeURL(){
		setUp();
		chatController.setCodeUrl("http://connected-1e044.firebaseapp.com");
		assertEquals("http://connected-1e044.firebaseapp.com", chatController.getCodeURL());
	}
	
	public void testSendCodeURLWhenLoaded(){
		setUp();
		chatController.sendCodeURLWhenLoaded();
		//TODO: find test objects for this method
	}
	
	public void testSendCodeURL(){
		setUp();
		chatController.setCodeUrl("http://connected-1e044.firebaseapp.com");
		chatController.sendCodeURL();
		//TODO: find test objects for this method
	}
	
}
