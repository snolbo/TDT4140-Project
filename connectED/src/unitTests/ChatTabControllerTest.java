package unitTests;

import java.net.Socket;

import communication.ChatTabController;
import junit.framework.*;
import mainWindow.MainFrameController;


//Error: alle testSetMode-methods fail (JavaFX Loader fail)
//TODO: find out the general form of junit tests, finish testNewChatTab, testStartChatSession and testOnCloseRequest

public class ChatTabControllerTest extends TestCase{
	
	public ChatTabController chatTabController;
	
	public void setUp(){
		this.chatTabController = new ChatTabController();
	}
	
	public void testDecrementPotentialConnections(){
		ChatTabController.getPotentialConnections();
		ChatTabController.decrementPotentialConnections();
		assertEquals(-1, ChatTabController.getPotentialConnections());
	}
	
	public void testGetPotentialConnections(){
		int pc = ChatTabController.getPotentialConnections();
		assertEquals(0, pc);
	}
	
	public void testPassMainFrameController(){
		MainFrameController mainFrameController = new MainFrameController();
		setUp();
		chatTabController.passMainFrameController(mainFrameController);
		assertEquals(mainFrameController, chatTabController.getMainFrameController());
	}
	
	public void testSetStudentHelperMode() throws Exception{
		setUp();
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testSetStudentAssistantMode() throws Exception{
		setUp();
		chatTabController.setAssistantMode();
		assertTrue(chatTabController.getConnector().isAssistantHost());
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testSetStudentMode() throws Exception{
		setUp();
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testInitializePopUpSubject() throws Exception{
		setUp();
		chatTabController.initializePopUpSubject();
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testClosePopUp() throws Exception{
		setUp();
		chatTabController.initializePopUpSubject();
		chatTabController.closePopUp();
		assertFalse(chatTabController.getStage().isShowing());
	}
	
	public void testMergeTags(){
		setUp();
		chatTabController.getConnector().setAssistantHost();
		chatTabController.mergeTags("Java");
		assertEquals("StudentAssistantJava", chatTabController.getTag());
		chatTabController.getConnector().setHelperHost();
		chatTabController.mergeTags("Java");
		assertEquals("StudentHelperJava", chatTabController.getTag());
		chatTabController.getConnector().setClient();;
		chatTabController.mergeTags("Java");
		assertEquals("StudentJava", chatTabController.getTag());
	}
	
	public void testNewChatTab(){
		setUp();
		chatTabController.newChatTab();
		//TODO: find test objects for this method
	}
	
	public void testStartChatSession(){
		setUp();
		Socket socket = new Socket();
		chatTabController.startChatSession(socket);
		//TODO: find test objects for this method
	}
	
	public void testOnCloseRequest(){
		setUp();
		chatTabController.onCloseRequest();
		//TODO: find test objects for this method
	}
	
}
