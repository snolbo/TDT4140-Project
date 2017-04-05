package unitTests;

import java.net.Socket;

import communication.ChatTabController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import junit.framework.*;
import mainWindow.MainFrameController;


//Error: alle testSetMode-methods fail (JavaFX Loader fail)
//TODO: find out the general form of junit tests, finish testNewChatTab, testStartChatSession and testOnCloseRequest

public class ChatTabControllerTest extends TestCase{
	
	@FXML private Button javaBtn;
	@FXML private Button itgkBtn;
	
	
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
		chatTabController.passMainFrameController(mainFrameController);
		assertEquals(mainFrameController, chatTabController.getMainFrameController());
	}
	
	/*public void testSetStudentHelperMode() throws Exception{
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testSetStudentAssistantMode() throws Exception{
		chatTabController.setAssistantMode();
		assertTrue(chatTabController.getConnector().isAssistantHost());
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testSetStudentMode() throws Exception{
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testInitializePopUpSubject() throws Exception{
		chatTabController.initializePopUpSubject();
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	public void testClosePopUp() throws Exception{
		setUp();
		chatTabController.initializePopUpSubject();
		chatTabController.closePopUp();
		assertFalse(chatTabController.getStage().isShowing());
	}
	*/
	
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
	
	
}
